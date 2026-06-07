package com.example.ufmcontroller.data.remote

import android.content.Context
import android.net.Uri
import com.example.ufmcontroller.data.local.UserPreferencesDataSource
import com.example.ufmcontroller.domain.entity.Category
import dagger.hilt.android.qualifiers.ApplicationContext
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.forms.formData
import io.ktor.client.request.forms.submitFormWithBinaryData
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.request
import io.ktor.client.request.setBody
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.http.path
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Singleton
import androidx.core.net.toUri
import com.example.ufmcontroller.data.model.UserRawPassword
import io.ktor.client.request.forms.MultiPartFormDataContent
import kotlinx.serialization.encodeToString

@Singleton
class RemoteLoginDataSource @Inject constructor(
    private val client: HttpClient,
) {
    suspend fun sendLogin(username: String, password: String): String {
        return client.post {
            url {
                path("/api/login")
            }
            setBody(UserRawPassword(username, password))
        }.body()
    }

    suspend fun sendRegister(username: String, password: String): String {
        return client.post {
            url {
                path("/api/register")
            }
            setBody(UserRawPassword(username, password))
        }.body()
    }
}