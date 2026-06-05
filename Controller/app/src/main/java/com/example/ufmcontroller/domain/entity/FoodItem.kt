package com.example.ufmcontroller.domain.entity

data class FoodItem(
    val id: Int=0,
    val name: String,
    val price: Float,
    val imageUri: String? = null,
    val inStock: Boolean,
) : Designable


fun defaultFoodItem() =
    FoodItem(0, "", 0F, null, true)