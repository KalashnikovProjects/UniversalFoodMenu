package com.kalashnikovprojects.ufmtv.data.model

import com.kalashnikovprojects.ufmtv.domain.entity.Category
import com.kalashnikovprojects.ufmtv.domain.entity.FoodItem
import kotlinx.serialization.Serializable

@Serializable
data class FoodItemDTO(
    val id: Int,
    val name: String,
    val price: Float,
    val imageUri: String?,
    val inStock: Boolean,
) : DesignableDTO {
    override fun toEntity(): FoodItem {
        return FoodItem(
            id = id,
            name = name,
            price=price,
            imageUri=imageUri,
            inStock=inStock,
        )
    }
}