package com.kalashnikovprojects.ufmtv.domain.model

data class Category(
    val id: Int,
    val name: String,
    val imageUri: String?,
    val price: Float?,
    val inStock: Boolean,
) : Designable