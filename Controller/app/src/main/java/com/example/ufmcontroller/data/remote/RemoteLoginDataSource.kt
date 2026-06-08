package com.example.ufmcontroller.data.remote

import com.example.ufmcontroller.data.model.UserRawPasswordDTO
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.HttpStatusCode
import io.ktor.http.path
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteLoginDataSource @Inject constructor(
    private val client: HttpClient,
    private val externalScope: CoroutineScope
) {
    suspend fun sendLogin(username: String, password: String): String {
        val deferred = externalScope.async {
            val resp = client.post {
                url { path("/api/login") }
                setBody(UserRawPasswordDTO(username, password))
            }

            if (resp.status == HttpStatusCode.Unauthorized) {
                throw Exception("Неверный логин или пароль")
            }

            resp.body<String>()
        }
        return deferred.await()
    }

    suspend fun sendRegister(username: String, password: String): String {
        val deferred = externalScope.async {
            val resp = client.post {
                url { path("/api/register") }
                setBody(UserRawPasswordDTO(username, password))
            }

            if (resp.status == HttpStatusCode.Conflict) {
                throw Exception("Имя пользователя уже занято")
            }

            resp.body<String>()
        }
        return deferred.await()
    }
}