package com.kalashnikovprojects.ufmtv.data.model

import com.kalashnikovprojects.ufmtv.domain.entity.Category
import com.kalashnikovprojects.ufmtv.domain.entity.CategoryWithFoodItems
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("category")
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
                imageUri=imageUri,
                price=price,
                inStock=inStock,
            ),
            foodItems = emptyList(),
        )
    }
}