package com.example.ufmcontroller.domain.usecase.category

import com.example.ufmcontroller.domain.entity.Category
import com.example.ufmcontroller.domain.entity.FoodItem
import com.example.ufmcontroller.domain.repository.CategoryRepository
import javax.inject.Inject

class UploadCategoryUseCase @Inject constructor(
    private val categoryRepository: CategoryRepository
) {
    suspend operator fun invoke(
        category: Category,
        foodItems: List<FoodItem> = emptyList(),
    ): Category {
        val category = categoryRepository.addCategory(category)
        categoryRepository.setCategoryFoodRelations(category.id, foodItems.map { it.id })
        return category
    }
}
