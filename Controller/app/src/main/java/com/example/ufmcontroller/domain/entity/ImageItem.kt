package com.example.ufmcontroller.domain.entity

import com.example.ufmcontroller.data.model.DesignableDTO
import com.example.ufmcontroller.data.model.ImageItemDTO

data class ImageItem(
    val id: Int=0,
    val imageUri: String,
) : Designable {
    override fun toDTO(): DesignableDTO {
        return ImageItemDTO(
            id = id,
            imageUri = imageUri,
        )
    }
}
