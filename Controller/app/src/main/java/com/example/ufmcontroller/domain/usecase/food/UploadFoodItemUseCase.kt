package com.example.ufmcontroller.domain.usecase.food

import com.example.ufmcontroller.domain.entity.FoodItem
import com.example.ufmcontroller.domain.repository.CategoryRepository
import com.example.ufmcontroller.domain.repository.FoodRepository
import javax.inject.Inject

class UploadFoodItemUseCase @Inject constructor(
    private val foodRepository: FoodRepository,
    private val categoryRepository: CategoryRepository,
) {
    suspend operator fun invoke(
        foodItem: FoodItem,
        categoriesIds: List<Int> = emptyList(),
    ): FoodItem {
        val foodItem = foodRepository.addFoodItem(foodItem)
        categoryRepository.updateFoodRelationsForCategories(foodItem.id, categoriesIds)
        return foodItem
    }
}
