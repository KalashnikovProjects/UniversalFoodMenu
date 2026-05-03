package com.kalashnikovprojects.ufmserver.adapters.filestorage

import java.io.File
import java.util.UUID

class FileStorageAdapter {
    constructor() {
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

    private suspend fun saveImage(folder: String, uniqueId: Int, fileBytes: ByteArray, extension: String): String {
        val filename = "$uniqueId.${extension}"
        val file = File("static/images/$folder/$filename")

        file.parentFile?.mkdirs()
        file.writeBytes(fileBytes)

        return "/static/images/$folder/$filename"
    }

    suspend fun saveImageItemImage(uniqueId: Int, fileBytes: ByteArray, extension: String): String {
        return saveImage("image_item_images", uniqueId, fileBytes, extension)
    }

    suspend fun saveFoodItemImage(uniqueId: Int, fileBytes: ByteArray, extension: String): String {
        return saveImage("food_item_images", uniqueId, fileBytes, extension)
    }
}