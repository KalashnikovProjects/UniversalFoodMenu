package com.example.ufmcontroller.data.model

import com.example.ufmcontroller.domain.model.FoodItem

data class FoodItemDTO(
    val id: Int,
    val name: String,
    val price: Float,
    val imageUri: String,
    val inStock: Boolean,
)

fun FoodItemDTO.toDomain(): FoodItem {
    return FoodItem(id=id, name=name, price=price, imageUri=imageUri, inStock=inStock)
}