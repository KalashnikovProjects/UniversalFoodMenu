package com.kalashnikovprojects.ufmserver.adapters.filestorage

import java.io.File
import java.util.UUID


class FileStorageAdapter(
    val baseUrl: String,
) {
    init {
        listOf<String>(
            "static/images/image_item_image",
            "static/images/food_item_images",
            "static/images/category_item_images",
            ).forEach {
            val dir = File(it)
            if (!dir.exists()) {
                dir.mkdirs()
            }
        }
    }

    private suspend fun saveImage(folder: String, fileBytes: ByteArray, extension: String): String {
        val uniqueId = UUID.randomUUID().toString().take(8)
        val filename = "image$uniqueId.$extension"
        val file = File("static/images/$folder/$filename")

        file.parentFile?.mkdirs()
        file.writeBytes(fileBytes)

        return "$baseUrl/static/images/$folder/$filename"
    }

    private suspend fun deleteImage(imageUrl: String, folder: String) {
        val filename = "/static/images/${folder}/${imageUrl.substringAfterLast("/static/images/${folder}")}"
        val file = File(filename)
        file.delete()
    }

    suspend fun saveImageItemImage(fileBytes: ByteArray, extension: String): String {
        return saveImage("image_item_images", fileBytes, extension)
    }

    suspend fun deleteImageItemImage(imageUrl: String) {
        return deleteImage(imageUrl, "image_item_images")
    }

    suspend fun saveFoodItemImage(fileBytes: ByteArray, extension: String): String {
        return saveImage("food_item_images", fileBytes, extension)
    }

    suspend fun deleteFoodItemImage(imageUrl: String) {
        return deleteImage(imageUrl, "food_item_images")
    }

    suspend fun saveCategoryItemImage(fileBytes: ByteArray, extension: String): String {
        return saveImage("category_item_images", fileBytes, extension)
    }

    suspend fun deleteCategoryItemImage(imageUrl: String) {
        return deleteImage(imageUrl, "category_item_images")
    }
}