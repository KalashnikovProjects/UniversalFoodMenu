package com.kalashnikovprojects.ufmtv.domain.entity

data class Category(
    val id: Int,
    val name: String,
    val imageUri: String? = null,
    val price: Float? = null,
    val inStock: Boolean?,
)