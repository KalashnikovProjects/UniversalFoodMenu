package com.example.ufmcontroller.domain.repository

import com.example.ufmcontroller.domain.model.FoodItem
import kotlinx.coroutines.flow.Flow


interface FoodRepository {
    fun getFoodItems(): Flow<List<FoodItem>>
    suspend fun toggleFoodItem(id: Int)
    suspend fun addFoodItem(foodItem: FoodItem)
    suspend fun editFoodItem(id: Int, foodItem: FoodItem)
    suspend fun refreshFoodItems()
}