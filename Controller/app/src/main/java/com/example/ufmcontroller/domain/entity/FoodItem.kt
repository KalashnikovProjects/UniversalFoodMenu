package com.example.ufmcontroller.domain.entity

import com.example.ufmcontroller.data.model.FoodItemDTO

data class FoodItem(
    val id: Int=0,
    val name: String,
    val price: Float,
    val imageUri: String? = null,
    val inStock: Boolean,
) : Designable {
    override fun toDTO(): FoodItemDTO {
        return FoodItemDTO(
            id = id,
            name = name,
            price = price,
            imageUri = imageUri,
            inStock = inStock,
        )
    }
}


fun defaultFoodItem() =
    FoodItem(0, "", 0F, null, true)