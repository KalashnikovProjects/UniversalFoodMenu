package com.example.ufmcontroller.domain.repository

import com.example.ufmcontroller.domain.entity.FoodItem
import com.example.ufmcontroller.domain.entity.FoodItemsCategorized
import kotlinx.coroutines.flow.Flow

interface FoodRepository {
    suspend fun addFoodItem(foodItem: FoodItem): FoodItem
    suspend fun toggleFoodItem(id: Int)
    suspend fun editFoodItem(id: Int, foodItem: FoodItem)
    suspend fun deleteFoodItem(id: Int)
    fun getFoodItems(): Flow<List<FoodItem>>
    fun getCategorizedFoodItems(): Flow<FoodItemsCategorized>
}