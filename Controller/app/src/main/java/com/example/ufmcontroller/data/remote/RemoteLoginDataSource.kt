package com.example.ufmcontroller.data.remote

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.path
import javax.inject.Inject
import javax.inject.Singleton
import com.example.ufmcontroller.data.model.UserRawPasswordDTO
import io.ktor.http.HttpStatusCode

@Singleton
class RemoteLoginDataSource @Inject constructor(
    private val client: HttpClient,
) {
    suspend fun sendLogin(username: String, password: String): String {
        val resp = client.post {
            url {
                path("/api/login")
            }
            setBody(UserRawPasswordDTO(username, password))
        }
        if (resp.status == HttpStatusCode.Unauthorized) {
            throw Exception("Неверный логин или пароль")
        }
        return resp.body()
    }

    suspend fun sendRegister(username: String, password: String): String {
        val resp = client.post {
            url {
                path("/api/register")
            }
            setBody(UserRawPasswordDTO(username, password))
        }
        if (resp.status == HttpStatusCode.Conflict) {
            throw Exception("Имя пользователя уже занято")
        }
        return resp.body()
    }
}