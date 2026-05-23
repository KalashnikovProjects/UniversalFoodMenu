package com.example.ufmcontroller.data.model

import com.example.ufmcontroller.domain.entity.ImageItem
import kotlinx.serialization.Serializable

@Serializable
data class ImageItemDTO(
    val id: Int,
    val imageUri: String,
) : DesignableDTO {
    override fun toEntity(): ImageItem {
        return ImageItem(
            id = id,
            imageUri = imageUri,
        )
    }
}
