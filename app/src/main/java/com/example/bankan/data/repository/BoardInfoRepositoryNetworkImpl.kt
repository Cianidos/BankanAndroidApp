package com.example.bankan.data.repository

import com.example.bankan.data.models.BoardInfo
import com.example.bankan.data.network.MyHttpClient
import com.example.bankan.data.network.payload.request.BoardCreateRequest
import com.example.bankan.data.network.payload.request.BoardUpdateRequest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.time.LocalDate

class BoardInfoRepositoryNetworkImpl : KoinComponent, BoardInfoRepository {
    private val localBoardInfoRepositoryImpl: LocalBoardInfoRepositoryImpl by inject()
    private val profileRepository: ProfileRepository by inject()

    private fun syncAll() {
        CoroutineScope(Dispatchers.IO).launch {
            MyHttpClient.WorkspaceApi.getWorkspaceByUserId(profileRepository.userId.first())
                ?.let { ws ->
                    val local = localBoardInfoRepositoryImpl.getAll().first()
                    local.filter { it.id == null }.forEach {
                        MyHttpClient.BoardsApi.create(
                            ws.id,
                            BoardCreateRequest(name = it.name, description = it.description)
                        )
                    }

                    MyHttpClient.WorkspaceApi.getWorkspaceByUserId(profileRepository.userId.first())
                        ?.let { res ->
                            res.listOfBoardEntities.forEach { allIt ->
                                val old =
                                    local.find { it.name == allIt.name && it.description == allIt.description }
                                if (old == null) {
                                    add(
                                        BoardInfo(
                                            name = allIt.name ?: "",
                                            description = allIt.description ?: "",
                                            isOpen = allIt.isOpen,
                                            creationDate = allIt.creationData ?: LocalDate.now(),
                                            id = allIt.id
                                        )
                                    )
                                } else {
                                    localBoardInfoRepositoryImpl.update(
                                        old.copy(
                                            id = allIt.id,
                                            name = allIt.name ?: "",
                                            description = allIt.description ?: "",
                                            isOpen = allIt.isOpen,
                                            creationDate = allIt.creationData ?: LocalDate.now(),
                                        )
                                    )
                                }
                            }
                        }
                }
        }
    }

    override fun getOne(): Flow<BoardInfo> {
        syncAll()
        return localBoardInfoRepositoryImpl.getOne()
    }

    override fun getOne(boardId: Int): Flow<BoardInfo> {
        syncAll()
        return localBoardInfoRepositoryImpl.getOne(boardId = boardId)
    }

    override fun getAll(): Flow<List<BoardInfo>> {
        syncAll()
        return localBoardInfoRepositoryImpl.getAll()
    }

    override suspend fun add(boardInfo: BoardInfo) {
        localBoardInfoRepositoryImpl.add(boardInfo = boardInfo)
        syncAll()
    }

    override suspend fun delete(localId: Int) {
        localBoardInfoRepositoryImpl.getOne(boardId = localId).first().id?.let {
            MyHttpClient.BoardsApi.delete(boardId = it)
        }
        localBoardInfoRepositoryImpl.delete(localId = localId)
    }

    override suspend fun delete(boardInfo: BoardInfo) {
        boardInfo.id?.let {
            MyHttpClient.BoardsApi.delete(boardId = it)
        }
        localBoardInfoRepositoryImpl.delete(boardInfo = boardInfo)
    }

    override suspend fun update(boardInfo: BoardInfo) {
        boardInfo.id?.let {
            MyHttpClient.BoardsApi.update(
                boardId = it,
                body = BoardUpdateRequest(boardInfo.name, boardInfo.description)
            )
        }
        localBoardInfoRepositoryImpl.update(boardInfo = boardInfo)
    }
}