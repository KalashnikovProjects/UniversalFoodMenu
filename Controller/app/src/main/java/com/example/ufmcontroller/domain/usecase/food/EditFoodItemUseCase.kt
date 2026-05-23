package com.example.ufmcontroller.domain.usecase.food

import com.example.ufmcontroller.domain.entity.FoodItem
import com.example.ufmcontroller.domain.repository.FoodRepository
import javax.inject.Inject

class EditFoodItemUseCase @Inject constructor(
    private val foodRepository: FoodRepository
) {
    suspend operator fun invoke(id: Int, foodItem: FoodItem) =
        foodRepository.editFoodItem(id, foodItem)
}
