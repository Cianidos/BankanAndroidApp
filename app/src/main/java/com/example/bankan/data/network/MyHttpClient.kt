package com.example.bankan.data.network

import com.example.bankan.data.network.payload.request.*
import com.example.bankan.data.network.payload.response.*
import com.example.bankan.data.repository.ProfileRepository
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.android.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.auth.*
import io.ktor.client.plugins.auth.providers.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.observer.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

object MyHttpClient : KoinComponent {

    private val pref: ProfileRepository by inject()
    private var token: String = ""

    init {
        CoroutineScope(Dispatchers.IO).launch {
            pref.sessionToken.collect {
                token = it ?: ""
            }
        }
    }

    private const val baseUrl: String = "http://127.0.0.1:8080"

    private val client: HttpClient = HttpClient(Android) {
        install(ContentNegotiation) {
            json(
                Json {
                    prettyPrint = true
                    ignoreUnknownKeys = true
                },
            )
        }
        install(Auth) {
            bearer {
                loadTokens { BearerTokens(token, "") }
            }
        }

        install(ResponseObserver) {
            onResponse { }
        }

        install(DefaultRequest) {
            header(HttpHeaders.ContentType, ContentType.Application.Json)
//            header(HttpHeaders.Authorization, )
        }
    }

    object AuthenticationApi {
        private const val apiAuthUrl = "/api/auth"
        suspend fun registerUser(signupRequest: SignupRequest): Boolean {
            val r = client.post(urlString = "$baseUrl$apiAuthUrl/signup") {
                setBody(signupRequest)
            }
            return when (r.status) {
                HttpStatusCode.OK -> true
                else -> false
            }
        }

        suspend fun authenticateUser(loginRequest: LoginRequest): JwtResponse? {
            val resp: HttpResponse =
                client.post(urlString = "$baseUrl$apiAuthUrl/signin") { setBody(body = loginRequest) }
            return when (resp.status) {
                HttpStatusCode.OK -> resp.body()
                else -> null
            }
        }
    }

    object UserApi {
        private const val userApiUrl = "api/user"

        suspend fun getUserInfo(userId: Int): UserInfoResponse =
            client.get(urlString = "$baseUrl$userApiUrl/$userId").body()

        suspend fun updateUserInfo(userId: Int, request: UserInfoPatchRequest): Unit =
            client.patch(urlString = "$baseUrl$userApiUrl/$userId") { setBody(body = request) }
                .body()

        suspend fun delete(userId: Int): Unit =
            client.delete(urlString = "$baseUrl$userApiUrl/$userId").body()
    }

    object WorkspaceApi {
        private const val workspaceApiUrl = "/api/workspace"

        suspend fun getWorkspace(workspaceId: Int): WorkspaceResponse =
            client.get(urlString = "$baseUrl$workspaceApiUrl/$workspaceId").body()

        suspend fun getWorkspaceByUserId(userId: Int): WorkspaceResponse =
            client.get(urlString = "$baseUrl$workspaceApiUrl/user/$userId").body()
    }

    object SettingsApi {
        private const val settingsApiUrl = "/api/settings"
        suspend fun getSettings(userId: Int): SettingsResponse =
            client.get(urlString = "$baseUrl$settingsApiUrl/$userId").body()

        suspend fun updateSettings(userId: Int, request: SettingsPatchRequest): Unit =
            client.patch(urlString = "$baseUrl$settingsApiUrl/$userId") {
                setBody(body = request)
            }.body()
    }

    object BoardsApi {
        private const val boardApiUrl = "/api/board/"

        suspend fun create(workspaceId: Int, body: BoardCreateRequest): Unit =
            client.post(urlString = "$baseUrl$boardApiUrl/$workspaceId") { setBody(body) }.body()

        suspend fun readInfo(boardId: Int): BoardInfoResponse =
            client.get(urlString = "$baseUrl$boardApiUrl/$boardId").body()

        suspend fun read(boardId: Int): List<Pair<Int, ListInfoResponse>> =
            client.get(urlString = "$baseUrl$boardApiUrl/$boardId").body()

        suspend fun update(boardId: Int, body: BoardUpdateRequest): Unit =
            client.patch(urlString = "$baseUrl$boardApiUrl/edit/$boardId") { setBody(body) }.body()

        suspend fun delete(boardId: Int): Unit =
            client.delete(urlString = "$baseUrl$boardApiUrl/delete/$boardId").body()
    }

    object ListApi {
        private const val listApiUrl = "/api/list/"
        suspend fun create(boardId: Int, listRequest: ListRequest): Unit =
            client.post(urlString = "$baseUrl$listApiUrl/$boardId") { setBody(listRequest) }.body()

        suspend fun readInfo(listId: Int): ListInfoResponse =
            client.get(urlString = "$baseUrl$listApiUrl/info/$listId").body()

        suspend fun readListContent(id: Int): ListContentResponse =
            client.get(urlString = "$baseUrl$listApiUrl/$id").body()

        suspend fun updateList(id: Int, patch: ListPatchRequest): Unit =
            client.patch(urlString = "$baseUrl$listApiUrl/edit/$id") { setBody(patch) }.body()

        suspend fun deleteList(boardId: Int, listId: Int): Unit =
            client.delete(urlString = "$baseUrl$listApiUrl/$boardId/$listId").body()

        suspend fun deleteList(id: Int): Unit =
            client.delete(urlString = "$baseUrl$listApiUrl/$id").body()
    }

    object CardApi {
        private const val cardApiUrl = "/api/card"
        suspend fun getCard(cardId: Int): CardResponse =
            client.get(urlString = "$baseUrl$cardApiUrl/$cardId").body()

        suspend fun createCard(listId: Int, requestBody: CardCreationRequest): Unit =
            client.post("$baseUrl$cardApiUrl/$listId") { setBody(requestBody) }.body()

        suspend fun editCard(cardId: Int, requestBody: CardPatchRequest): Unit =
            client.patch(urlString = "$baseUrl$cardApiUrl/edit/$cardId") { setBody(requestBody) }
                .body()

        suspend fun deleteCardFromList(listId: Int, cardId: Int): Unit =
            client.delete(urlString = "$baseUrl$cardApiUrl/$listId/$cardId").body()

        suspend fun deleteCard(cardId: Int): Unit =
            client.delete(urlString = "$baseUrl$cardApiUrl/$cardId").body()
    }
}

