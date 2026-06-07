package com.example.ufmcontroller.domain.repository

import com.example.ufmcontroller.domain.entity.Category
import com.example.ufmcontroller.domain.entity.DesignItem
import com.example.ufmcontroller.domain.entity.FoodItem
import com.example.ufmcontroller.domain.entity.FoodItemsCategorized
import com.example.ufmcontroller.domain.entity.TVScreenWithDesignItems
import kotlinx.coroutines.flow.Flow

interface GetRepository {
    fun getCategories(): Flow<List<Category>>
    fun getCategory(id: Int): Flow<Category>
    fun getCategoriesByFoodId(foodId: Int): Flow<List<Category>>

    fun getDesignItem(id: Int): Flow<DesignItem>
    fun getDesignItems(): Flow<List<DesignItem>>

    fun getFoodItem(id: Int): Flow<FoodItem>
    fun getFoodItemsByCategoryId(categoryId: Int): Flow<List<FoodItem>>
    fun getFoodItems(): Flow<List<FoodItem>>
    fun getCategorizedFoodItems(): Flow<FoodItemsCategorized>

    fun getScreenWithDesignItems(id: Int): Flow<TVScreenWithDesignItems>
    fun getScreensWithDesignItems(): Flow<List<TVScreenWithDesignItems>>
}