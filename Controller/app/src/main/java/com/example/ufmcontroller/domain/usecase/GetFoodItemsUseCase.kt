package com.example.ufmcontroller.domain.usecase

import com.example.ufmcontroller.domain.model.FoodItem
import com.example.ufmcontroller.domain.repository.FoodRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

class GetFoodItemsUseCase(private val repository: FoodRepository) {
    operator fun invoke(): Flow<List<FoodItem>> = repository.getFoodItems()
}

class ToggleFoodItemUseCase(private val repository: FoodRepository) {
    suspend operator fun invoke(id: Int) = repository.toggleFoodItem(id)
}

class AddFoodItemUseCase(private val repository: FoodRepository) {
    suspend operator fun invoke(foodItem: FoodItem) = repository.addFoodItem(foodItem)
}

class EditFoodItemUseCase(private val repository: FoodRepository) {
    suspend operator fun invoke(id: Int, foodItem: FoodItem) = repository.editFoodItem(id, foodItem)
}

class RefreshFoodItemsUseCase(private val repository: FoodRepository) {
    suspend operator fun invoke() = repository.refreshFoodItems()
}