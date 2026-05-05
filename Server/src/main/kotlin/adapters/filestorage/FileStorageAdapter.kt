package com.kalashnikovprojects.ufmserver.adapters.filestorage

import java.io.File
import java.util.UUID

class FileStorageAdapter(
    val host: String,
) {
    init {
        listOf<String>(
            "static/images/image_item_image",
            "static/images/food_item_images",
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

        return "$host/static/images/$folder/$filename"
    }

    suspend fun saveImageItemImage(fileBytes: ByteArray, extension: String): String {
        return saveImage("image_item_images", fileBytes, extension)
    }

    suspend fun saveFoodItemImage(fileBytes: ByteArray, extension: String): String {
        return saveImage("food_item_images", fileBytes, extension)
    }
}