package com.example.ufmcontroller.domain.entity

data class Category(
    val id: Int=0,
    val name: String,
    val imageUri: String? = null,
    val price: Float? = null,
    val inStock: Boolean?,
)


fun defaultCategory() =
    Category(0, "", null, null, true)