package com.example.ufmcontroller.domain.usecase.food

import com.example.ufmcontroller.domain.entity.Category
import com.example.ufmcontroller.domain.entity.FoodItem
import com.example.ufmcontroller.domain.repository.CategoryRepository
import com.example.ufmcontroller.domain.repository.FoodRepository
import javax.inject.Inject

class EditFoodItemUseCase @Inject constructor(
    private val foodRepository: FoodRepository,
    private val categoryRepository: CategoryRepository,
    ) {
    suspend operator fun invoke(id: Int,
                                foodItem: FoodItem,
                                categoriesIds: List<Int> = emptyList(),
                                changedImage: Boolean,
                                ) {
        foodRepository.editFoodItem(id, foodItem, changedImage)
        categoryRepository.updateFoodRelationsForCategories(foodItem.id, categoriesIds)
    }
}
