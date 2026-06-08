package com.example.ufmcontroller.domain.repository

import com.example.ufmcontroller.domain.entity.FoodItem

interface FoodRepository {
    suspend fun addFoodItem(foodItem: FoodItem): FoodItem
    suspend fun toggleFoodItem(id: Int, boolean: Boolean)
    suspend fun editFoodItem(id: Int, foodItem: FoodItem, changedImage: Boolean)
    suspend fun deleteFoodItem(id: Int)
}