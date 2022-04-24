package com.example.bankan.data.network

import com.example.bankan.data.network.payload.request.*
import com.example.bankan.data.network.payload.response.*
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.*
import io.ktor.client.features.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.features.observer.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.http.cio.*

object HttpClient {
    private const val baseUrl: String = ""
    private const val dataApiUrl: String = ""

    val client: HttpClient = HttpClient(Android) {
        install(JsonFeature) {
            serializer = KotlinxSerializer(kotlinx.serialization.json.Json {
                prettyPrint = true
            })
            engine { }
        }
        install(ResponseObserver) {
            onResponse { }
        }

        install(DefaultRequest) {
            header(HttpHeaders.ContentType, ContentType.Application.Json)
        }
    }

    object AuthenticationApi {
        private const val apiAuthUrl = "/api/auth"
        suspend fun registerUser(signupRequest: SignupRequest): Boolean {
            val r = client.post<Response>(path = "$baseUrl$apiAuthUrl/signup", body = signupRequest)
            return when (r.status) {
                200 -> true
                else -> false
            }
        }

        suspend fun authenticateUser(loginRequest: LoginRequest): JwtResponse =
            client.post(path = "$baseUrl$apiAuthUrl/login", body = loginRequest)
    }

    object UserApi {
        private const val userApiUrl = "api/user"

        suspend fun getUserInfo(userId: Int): UserInfoResponse =
            client.get(urlString = "$baseUrl$userApiUrl/$userId")

        suspend fun updateUserInfo(userId: Int, request: UserInfoPatchRequest): Unit =
            client.patch(urlString = "$baseUrl$userApiUrl/$userId") {
                body = request
            }

        suspend fun delete(userId: Int): Unit =
            client.delete(urlString = "$baseUrl$userApiUrl/$userId")
    }

    object WorkspaceApi {
        private const val workspaceApiUrl = "/api/workspace"

        suspend fun getWorkspace(workspaceId: Int): WorkspaceResponse =
            client.get(urlString = "$baseUrl$workspaceApiUrl/$workspaceId")

        suspend fun getWorkspaceByUserId(userId: Int): WorkspaceResponse =
            client.get(urlString = "$baseUrl$workspaceApiUrl/user/$userId")
    }

    object SettingsApi {
        private const val settingsApiUrl = "/api/settings"
        suspend fun getSettings(userId: Int): SettingsResponse =
            client.get(urlString = "$baseUrl$settingsApiUrl/$userId")

        suspend fun updateSettings(userId: Int, request: SettingsPatchRequest): Unit =
            client.patch<Unit>(urlString = "$baseUrl$settingsApiUrl/$userId") {
                body = request
            }
    }

    object BoardsApi {
        private const val boardApiUrl = "/api/board/"

        suspend fun create(workspaceId: Int, body: BoardCreateRequest): Unit =
            client.post(urlString = "$baseUrl$boardApiUrl/$workspaceId") { this.body = body }

        suspend fun readInfo(boardId: Int): BoardInfoResponse =
            client.get(urlString = "$baseUrl$boardApiUrl/$boardId")

        suspend fun read(boardId: Int): List<Pair<Int, ListInfoResponse>> =
            client.get(urlString = "$baseUrl$boardApiUrl/$boardId")

        suspend fun update(boardId: Int, body: BoardUpdateRequest): Unit =
            client.patch(urlString = "$baseUrl$boardApiUrl/edit/$boardId") { this.body = body }

        suspend fun delete(boardId: Int): Unit =
            client.delete(urlString = "$baseUrl$boardApiUrl/delete/$boardId")
    }

    object ListApi {
        private const val listApiUrl = "/api/list/"
        suspend fun create(boardId: Int, listRequest: ListRequest): Unit =
            client.post(urlString = "$baseUrl$listApiUrl/$boardId") { body = listRequest }

        suspend fun readInfo(listId: Int): ListInfoResponse =
            client.get(urlString = "$baseUrl$listApiUrl/info/$listId")

        suspend fun readListContent(id: Int): ListContentResponse =
            client.get(urlString = "$baseUrl$listApiUrl/$id")

        suspend fun updateList(id: Int, patch: ListPatchRequest): Unit =
            client.patch(urlString = "$baseUrl$listApiUrl/edit/$id") { body = patch }

        suspend fun deleteList(boardId: Int, listId: Int): Unit =
            client.delete(urlString = "$baseUrl$listApiUrl/$boardId/$listId")

        suspend fun deleteList(id: Int): Unit =
            client.delete(urlString = "$baseUrl$listApiUrl/$id")
    }

    object CardApi {
        private const val cardApiUrl = "/api/card"
        suspend fun getCard(cardId: Int): CardResponse =
            client.get(urlString = "$baseUrl$cardApiUrl/$cardId")

        suspend fun createCard(listId: Int, requestBody: CardCreationRequest): Unit =
            client.post("$baseUrl$cardApiUrl/$listId") { body = requestBody }

        suspend fun editCard(cardId: Int, requestBody: CardPatchRequest): Unit =
            client.patch(urlString = "$baseUrl$cardApiUrl/edit/$cardId") { body = requestBody }

        suspend fun deleteCardFromList(listId: Int, cardId: Int): Unit =
            client.delete(urlString = "$baseUrl$cardApiUrl/$listId/$cardId")

        suspend fun deleteCard(cardId: Int): Unit =
            client.delete(urlString = "$baseUrl$cardApiUrl/$cardId")
    }
}

