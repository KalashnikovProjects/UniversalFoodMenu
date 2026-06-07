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
import com.example.ufmcontroller.data.model.CategoryDTO
import com.example.ufmcontroller.domain.entity.toDTO
import io.ktor.client.request.forms.MultiPartFormDataContent
import kotlinx.serialization.encodeToString

@Singleton
class RemoteCategoryDataSource @Inject constructor(
    private val client: HttpClient,
    @ApplicationContext private val context: Context,
) {
    suspend fun addCategory(category: Category): Category {
        var imageBytes: ByteArray? = null
        var mimeType: String = ""
        if (category.imageUri != null) {
            val uri = category.imageUri.toUri()
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
                path("/api/categories")
            }
            setBody(
                MultiPartFormDataContent(
                    formData {
                        val jsonString = Json.encodeToString(category.toDTO())
                        append("categoryData", jsonString, Headers.build {
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
        return response.body<CategoryDTO>().toEntity().category
    }

    suspend fun editCategory(
        id: Int,
        category: Category
    ) {
        var imageBytes: ByteArray? = null
        var mimeType: String = ""
        if (category.imageUri != null) {
            val uri = category.imageUri.toUri()
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
                path("/api/categories/$id")
            }
            setBody(
                MultiPartFormDataContent(
                    formData {
                        val jsonString = Json.encodeToString(category.toDTO())
                        append("categoryData", jsonString, Headers.build {
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

    suspend fun toggleCategory(id: Int, boolean: Boolean) {
        client.post {
            url {
                path("/api/categories/$id/toggle")
            }
            setBody(boolean)
        }
    }

    suspend fun deleteCategory(id: Int) {
        client.delete {
            url {
                path("/api/categories/$id")
            }
        }
    }

    suspend fun setCategoryFoodRelations(
        categoryId: Int,
        foodIds: List<Int>
    ) {
        client.put {
            url {
                path("/api/categories/$categoryId/food-items")
            }
            setBody(foodIds)
        }
    }

    suspend fun updateFoodRelationsForCategories(
        foodId: Int,
        categoryIds: List<Int>
    ) {
        client.put {
            url {
                path("/api/food-items/$foodId/categories")
            }
            setBody(categoryIds)
        }
    }
}