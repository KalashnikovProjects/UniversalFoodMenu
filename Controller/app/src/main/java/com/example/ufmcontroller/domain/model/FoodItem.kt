package com.example.ufmcontroller.domain.model

data class FoodItem(
    val id: Int,
    val name: String,
    val price: Float,
    val imageUri: String?,
    val inStock: Boolean,
)