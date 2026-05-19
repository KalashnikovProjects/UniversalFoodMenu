package com.kalashnikovprojects.ufmtv.domain.entity

data class FoodItem(
    val id: Int,
    val name: String,
    val price: Float,
    val imageUri: String? = null,
    val inStock: Boolean,
) : Designable