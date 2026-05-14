package com.kalashnikovprojects.ufmtv.data.model

import com.kalashnikovprojects.ufmtv.domain.entity.FoodItem
import com.kalashnikovprojects.ufmtv.domain.entity.ImageItem
import kotlinx.serialization.Serializable

@Serializable
data class ImageItemDTO(
    val id: Int,
    val imageUri: String,
) : DesignableDTO {
    override fun toEntity(): ImageItem {
        return ImageItem(
            id = id,
            imageUri=imageUri,
        )
    }
}
