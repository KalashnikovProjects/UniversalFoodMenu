package com.example.ufmcontroller.data.remote

import android.R
import android.content.Context
import androidx.core.net.toUri
import com.example.ufmcontroller.data.local.UserPreferencesDataSource
import com.example.ufmcontroller.data.model.FoodItemDTO
import com.example.ufmcontroller.domain.entity.Category
import com.example.ufmcontroller.domain.entity.FoodItem
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
class RemoteFoodDataSource @Inject constructor(
    private val client: HttpClient,
    @ApplicationContext private val context: Context,
    ) {
    suspend fun addFoodItem(foodItem: FoodItem): FoodItem {
        var imageBytes: ByteArray? = null
        var mimeType: String = ""
        if (foodItem.imageUri != null) {
            val uri = foodItem.imageUri.toUri()
            val contentResolver = context.contentResolver

            imageBytes = withContext(Dispatchers.IO) {
                contentResolver.openInputStream(uri)?.use { inputStream ->
                    inputStream.readBytes()
                } ?: throw Exception("Не удалось прочитать файл")
            }

            mimeType = contentResolver.getType(uri) ?: "image/jpeg"
        }


        val response = client.post {
            url {
                path("/api/food-items")
            }
            setBody(
                MultiPartFormDataContent(
                    formData {
                        val jsonString = Json.encodeToString(foodItem.toDTO())
                        append("foodItemData", jsonString, Headers.build {
                            append(HttpHeaders.ContentType, "application/json")
                        })

                        if (imageBytes != null) {
                            append("image", imageBytes, Headers.build {
                                append(HttpHeaders.ContentType, mimeType)
                                append(HttpHeaders.ContentDisposition, "filename=\"image.jpg\"")
                            })
                        }
                    }
                )
            )
        }
        return response.body<FoodItemDTO>().toEntity()
    }

    suspend fun toggleFoodItem(id: Int, boolean: Boolean) {
        client.post {
            url {
                path("/api/food-items/$id/toggle")
            }
            setBody(boolean)
        }
    }

    suspend fun editFoodItem(
        id: Int,
        foodItem: FoodItem,
        changedImage: Boolean
    ) {
        var imageBytes: ByteArray? = null
        var mimeType: String = ""
        if (foodItem.imageUri != null && changedImage) {
            val uri = foodItem.imageUri.toUri()
            val contentResolver = context.contentResolver

            imageBytes = withContext(Dispatchers.IO) {
                contentResolver.openInputStream(uri)?.use { inputStream ->
                    inputStream.readBytes()
                } ?: throw Exception("Не удалось прочитать файл")
            }

            mimeType = contentResolver.getType(uri) ?: "image/jpeg"
        }


        client.put {
            url {
                path("/api/food-items/$id")
            }
            setBody(
                MultiPartFormDataContent(
                    formData {
                        val jsonString = Json.encodeToString(foodItem.toDTO())
                        append("foodItemData", jsonString, Headers.build {
                            append(HttpHeaders.ContentType, "application/json")
                        })

                        if (imageBytes != null) {
                            append("image", imageBytes, Headers.build {
                                append(HttpHeaders.ContentType, mimeType)
                                append(HttpHeaders.ContentDisposition, "filename=\"image.jpg\"")
                            })
                        }
                    }
                )
            )
        }
    }

    suspend fun deleteFoodItem(id: Int) {
        client.delete {
            url {
                path("/api/food-items/$id")
            }
        }
    }
}