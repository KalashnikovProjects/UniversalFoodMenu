package com.example.ufmcontroller.data.remote

import android.content.Context
import androidx.core.net.toUri
import com.example.ufmcontroller.data.local.UserPreferencesDataSource
import com.example.ufmcontroller.domain.entity.Category
import com.example.ufmcontroller.domain.entity.FoodItem
import com.example.ufmcontroller.domain.entity.TVScreen
import com.example.ufmcontroller.domain.entity.toDTO
import dagger.hilt.android.qualifiers.ApplicationContext
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.http.path
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteTvScreenDataSource @Inject constructor(
    private val client: HttpClient,
    ) {
    suspend fun inputCodeForTvAuth(code: String): TVScreen {
        val response = client.post {
            url {
                path("/api/input_code_for_tv_auth")
            }
            setBody(code)
        }
        return response.body()
    }

    suspend fun editScreen(
        id: Int,
        screen: TVScreen
    ) {
        client.put {
            url {
                path("/api/screens/$id")
            }
            setBody(screen.toDTO())
        }
    }

    suspend fun deleteScreen(id: Int) {
        client.delete {
            url {
                path("/api/screens/$id")
            }
        }
    }
}