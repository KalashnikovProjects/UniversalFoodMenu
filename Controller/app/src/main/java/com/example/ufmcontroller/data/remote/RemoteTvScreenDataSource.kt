package com.example.ufmcontroller.data.remote

import android.util.Log
import com.example.ufmcontroller.data.model.TVScreenDTO
import com.example.ufmcontroller.data.model.toEntity
import com.example.ufmcontroller.domain.entity.TVScreen
import com.example.ufmcontroller.domain.entity.toDTO
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.HttpStatusCode
import io.ktor.http.path
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteTvScreenDataSource @Inject constructor(
    private val client: HttpClient,
    private val externalScope: CoroutineScope
) {
    suspend fun inputCodeForTvAuth(code: String): TVScreen {
        val deferred = externalScope.async {
            val response = client.post {
                url { path("/api/input_code_for_tv_auth") }
                setBody(code)
            }

            if (response.status == HttpStatusCode.BadRequest) {
                throw Exception("Неверный код")
            }

            response.body<TVScreenDTO>().toEntity()
        }
        return deferred.await()
    }

    suspend fun editScreen(id: Int, screen: TVScreen) {
        val deferred = externalScope.async {
            Log.d("RemoteTvScreenDataSource", "editScreen: $screen")
            client.put {
                url { path("/api/screens/$id") }
                setBody(screen.toDTO())
            }
            Unit
        }
        deferred.await()
    }

    suspend fun deleteScreen(id: Int) {
        val deferred = externalScope.async {
            client.delete {
                url { path("/api/screens/$id") }
            }
            Unit
        }
        deferred.await()
    }
}