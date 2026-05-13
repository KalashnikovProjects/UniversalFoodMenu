package com.kalashnikovprojects.ufmtv.data.model

import kotlinx.serialization.Serializable

@Serializable
data class CategoryDTO(
    val id: Int,
    val name: String,
    val imageUri: String?,
    val price: Float?,
    val inStock: Boolean,
) : DesignableDTO