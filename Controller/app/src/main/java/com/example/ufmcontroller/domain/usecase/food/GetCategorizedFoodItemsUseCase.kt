package com.example.ufmcontroller.domain.usecase.food

import com.example.ufmcontroller.domain.entity.FoodItemsCategorized
import com.example.ufmcontroller.domain.repository.FoodRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCategorizedFoodItemsUseCase @Inject constructor(
    private val foodRepository: FoodRepository
) {
    operator fun invoke(): Flow<FoodItemsCategorized> =
        foodRepository.getCategorizedFoodItems()
}
