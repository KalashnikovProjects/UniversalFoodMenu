package com.example.ufmcontroller.data.model

import com.example.ufmcontroller.domain.entity.FoodItem
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