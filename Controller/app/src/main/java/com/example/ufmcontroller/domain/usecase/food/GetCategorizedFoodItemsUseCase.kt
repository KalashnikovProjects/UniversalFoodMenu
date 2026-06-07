package com.example.ufmcontroller.domain.usecase.food

import com.example.ufmcontroller.domain.entity.FoodItemsCategorized
import com.example.ufmcontroller.domain.repository.FoodRepository
import com.example.ufmcontroller.domain.repository.GetRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCategorizedFoodItemsUseCase @Inject constructor(
    private val getRepository: GetRepository
) {
    operator fun invoke(): Flow<FoodItemsCategorized> =
        getRepository.getCategorizedFoodItems()
}
