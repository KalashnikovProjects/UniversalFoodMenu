package com.example.ufmcontroller.domain.entity

import com.example.ufmcontroller.data.model.CategoryDTO

data class Category(
    val id: Int=0,
    val name: String,
    val imageUri: String? = null,
    val price: Float? = null,
    val inStock: Boolean?=null,
) : Designable {
    override fun toDTO(): CategoryDTO {
        return CategoryDTO(
            id = id,
            name = name,
            imageUri = imageUri,
            price = price,
            inStock = inStock,
        )
    }
}

fun defaultCategory() =
    Category(0, "", null, null, true)