package com.example.ufmcontroller.domain.usecase.food

import com.example.ufmcontroller.domain.entity.FoodItem
import com.example.ufmcontroller.domain.repository.FoodRepository
import javax.inject.Inject

class AddFoodItemUseCase @Inject constructor(
    private val foodRepository: FoodRepository
) {
    suspend operator fun invoke(foodItem: FoodItem): FoodItem =
        foodRepository.addFoodItem(foodItem)
}
