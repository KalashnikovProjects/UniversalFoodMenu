package com.kalashnikovprojects.ufmtv.data.model

import com.kalashnikovprojects.ufmtv.domain.entity.FoodItem
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("food")
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