package com.example.ufmcontroller.data.model

import com.example.ufmcontroller.domain.entity.Category
import com.example.ufmcontroller.domain.entity.CategoryWithFoodItems
import kotlinx.serialization.Serializable

@Serializable
data class CategoryDTO(
    val id: Int,
    val name: String,
    val imageUri: String?,
    val price: Float?,
    val inStock: Boolean?,
) : DesignableDTO {
    override fun toEntity(): CategoryWithFoodItems {
        return CategoryWithFoodItems(category = Category(
            id = id,
            name = name,
            imageUri = imageUri,
            price = price,
            inStock = inStock,
        ),
            foodItems = emptyList(),
        )
    }
}