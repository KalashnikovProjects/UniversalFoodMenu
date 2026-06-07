package com.example.ufmcontroller.data.remote

import android.content.Context
import androidx.core.net.toUri
import com.example.ufmcontroller.data.local.UserPreferencesDataSource
import com.example.ufmcontroller.data.model.TextItemDTO
import com.example.ufmcontroller.domain.entity.Category
import com.example.ufmcontroller.domain.entity.DesignItem
import com.example.ufmcontroller.domain.entity.DesignItemWithScreenId
import com.example.ufmcontroller.domain.entity.FoodItem
import com.example.ufmcontroller.domain.entity.ImageItem
import com.example.ufmcontroller.domain.entity.TextItem
import com.example.ufmcontroller.domain.entity.toDTO
import com.example.ufmcontroller.domain.entity.toDesignItem
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
class RemoteDesignDataSource @Inject constructor(
    private val client: HttpClient,
    @ApplicationContext private val context: Context,
    ) {
    suspend fun addDesignItem(designItem: DesignItemWithScreenId): DesignItem {
        return client.post {
            url {
                path("/api/screens/${designItem.screenId}/design-items")
            }
            setBody(designItem.toDTO())
        }.body()
    }

    suspend fun addDesignItemWithImage(designItem: DesignItemWithScreenId): DesignItem {
        val imageItem = designItem.element as ImageItem
        var imageBytes: ByteArray? = null
        var mimeType: String = ""
        val uri = imageItem.imageUri.toUri()
        val contentResolver = context.contentResolver

        imageBytes = withContext(Dispatchers.IO) {
            contentResolver.openInputStream(uri)?.use { inputStream ->
                inputStream.readBytes()
            } ?: throw Exception("Не удалось прочитать файл")
        }

        mimeType = contentResolver.getType(uri) ?: "image/jpeg"


        val response = client.post {
            url {
                path("/api/image-items")
            }
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
        val resp: ImageItem = response.body()
        return addDesignItem(designItem.copy(element = resp))
    }

    suspend fun addDesignItemWithText(designItem: DesignItemWithScreenId): DesignItem {
        val resp = client.post {
            url {
                path("/api/text-items")
            }
            setBody((designItem.element as TextItem).toDTO())
        }.body<TextItemDTO>()
        return addDesignItem(designItem.copy(element = resp.toEntity()))
    }

    suspend fun editDesignItem(
        id: Int,
        designItem: DesignItemWithScreenId
    ) {
        client.put {
            url {
                path("/api/screens/${designItem.screenId}/design-items/${id}")
            }
            setBody(designItem.toDTO())
        }
    }

    suspend fun deleteDesignItem(id: Int) {
        client.delete {
            url {
                path("/api/design-items/$id")
            }
        }
    }
}