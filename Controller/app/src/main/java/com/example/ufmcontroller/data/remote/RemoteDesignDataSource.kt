package com.example.ufmcontroller.data.remote

import android.content.Context
import androidx.core.net.toUri
import com.example.ufmcontroller.data.model.DesignItemDTO
import com.example.ufmcontroller.data.model.ImageItemDTO
import com.example.ufmcontroller.data.model.TextItemDTO
import com.example.ufmcontroller.data.model.toEntity
import com.example.ufmcontroller.domain.entity.DesignItem
import com.example.ufmcontroller.domain.entity.DesignItemWithScreenId
import com.example.ufmcontroller.domain.entity.ImageItem
import com.example.ufmcontroller.domain.entity.TextItem
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteDesignDataSource @Inject constructor(
    private val client: HttpClient,
    @ApplicationContext private val context: Context,
    private val externalScope: CoroutineScope // Внедряем ApplicationScope
) {
    suspend fun addDesignItem(designItem: DesignItemWithScreenId): DesignItem {
        val deferred = externalScope.async {
            client.post {
                url { path("/api/screens/${designItem.screenId}/design-items") }
                setBody(designItem.toDTO())
            }.body<DesignItemDTO>().toEntity()
        }
        return deferred.await()
    }

    suspend fun addDesignItemWithImage(designItem: DesignItemWithScreenId): DesignItem {
        val deferred = externalScope.async {
            val imageItem = designItem.element as ImageItem
            var imageBytes: ByteArray? = null
            var mimeType = ""
            val uri = imageItem.imageUri.toUri()
            val contentResolver = context.contentResolver

            imageBytes = withContext(Dispatchers.IO) {
                contentResolver.openInputStream(uri)?.use { inputStream ->
                    inputStream.readBytes()
                } ?: throw Exception("Не удалось прочитать файл")
            }

            mimeType = contentResolver.getType(uri) ?: "image/jpeg"

            val response = client.post {
                url { path("/api/image-items") }
                setBody(
                    MultiPartFormDataContent(
                        formData {
                            append("image", imageBytes, Headers.build {
                                append(HttpHeaders.ContentType, mimeType)
                                append(HttpHeaders.ContentDisposition, "filename=\"image.jpg\"")
                            })
                        }
                    )
                )
            }
            val resp: ImageItemDTO = response.body()

            addDesignItem(designItem.copy(element = resp.toEntity()))
        }
        return deferred.await()
    }

    suspend fun addDesignItemWithText(designItem: DesignItemWithScreenId): DesignItem {
        val deferred = externalScope.async {
            val resp = client.post {
                url { path("/api/text-items") }
                setBody((designItem.element as TextItem).toDTO())
            }.body<TextItemDTO>()

            addDesignItem(designItem.copy(element = resp.toEntity()))
        }
        return deferred.await()
    }

    suspend fun editDesignItem(id: Int, designItem: DesignItemWithScreenId) {
        val deferred = externalScope.async {
            client.put {
                url { path("/api/screens/${designItem.screenId}/design-items/${id}") }
                setBody(designItem.toDTO())
            }
            Unit
        }
        deferred.await()
    }

    suspend fun deleteDesignItem(screenId: Int, id: Int) {
        val deferred = externalScope.async {
            client.delete {
                url { path("/api/screens/${screenId}/design-items/$id") }
            }
            Unit
        }
        deferred.await()
    }

    suspend fun deleteImageItem(id: Int) {
        val deferred = externalScope.async {
            client.delete {
                url { path("/api/image-items/$id") }
            }
            Unit
        }
        deferred.await()
    }


    suspend fun deleteTextItem(id: Int) {
        val deferred = externalScope.async {
            client.delete {
                url { path("/api/text-items/$id") }
            }
            Unit
        }
        deferred.await()
    }
}