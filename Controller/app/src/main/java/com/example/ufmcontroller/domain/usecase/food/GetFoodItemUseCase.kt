package com.example.ufmcontroller.domain.usecase.food

import com.example.ufmcontroller.domain.entity.FoodItem
import com.example.ufmcontroller.domain.repository.GetRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetFoodItemUseCase @Inject constructor(
    private val getRepository: GetRepository
) {
    operator fun invoke(id: Int): Flow<FoodItem> =
        getRepository.getFoodItem(id)
}
