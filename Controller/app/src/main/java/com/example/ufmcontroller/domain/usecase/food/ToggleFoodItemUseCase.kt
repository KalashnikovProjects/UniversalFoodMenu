package com.example.ufmcontroller.domain.usecase.food

import com.example.ufmcontroller.domain.repository.FoodRepository
import javax.inject.Inject

class ToggleFoodItemUseCase @Inject constructor(
    private val foodRepository: FoodRepository
) {
    suspend operator fun invoke(id: Int, boolean: Boolean) =
        foodRepository.toggleFoodItem(id, boolean)
}
