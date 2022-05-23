package com.example.bankan.data.network

import com.example.bankan.data.models.BoardInfo
import com.example.bankan.data.network.payload.request.BoardCreateRequest
import com.example.bankan.data.network.payload.request.BoardUpdateRequest
import com.example.bankan.data.repository.ProfileRepository
import com.example.bankan.data.store.room.AuthedBoardInfoDao
import kotlinx.coroutines.flow.first
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class BoardInfoInternetProvider : KoinComponent {
    private val profileRepository: ProfileRepository by inject()
    private val authedBoardInfoDao: AuthedBoardInfoDao by inject()

    suspend fun refreshBoards() {
        val userId = profileRepository.userId.first()
        MyHttpClient.WorkspaceApi.getWorkspaceByUserId(userId)?.let { workspaceResponse ->
            authedBoardInfoDao.insertOrUpdate(
                *(workspaceResponse.listOfBoardEntities.map {
                    BoardInfo(
                        it.name ?: "",
                        it.description ?: "",
                        it.isOpen,
                        it.creationData!!,
                        it.id
                    )
                }.toTypedArray())
            )
        }
    }

    suspend fun addBoard(boardInfo: BoardInfo) {
        val userId = profileRepository.userId.first()
        MyHttpClient.WorkspaceApi.getWorkspaceByUserId(userId)?.let { workspaceResponse ->
            MyHttpClient.BoardsApi.create(
                workspaceResponse.id,
                BoardCreateRequest(boardInfo.name, boardInfo.description)
            )
        }
    }

    suspend fun deleteBoard(localId: Int) {
        authedBoardInfoDao.getOne(localId).first().id?.let { MyHttpClient.BoardsApi.delete(it) }
    }

    suspend fun deleteBoard(boardInfo: BoardInfo) {
        boardInfo.id?.let { MyHttpClient.BoardsApi.delete(it) }
    }

    suspend fun update(boardInfo: BoardInfo) {
        boardInfo.id?.let {
            MyHttpClient.BoardsApi.update(
                it,
                BoardUpdateRequest(boardInfo.name, boardInfo.description)
            )
        }
    }
}