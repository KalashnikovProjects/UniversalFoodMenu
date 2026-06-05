package com.example.ufmcontroller.domain.repository

import com.example.ufmcontroller.domain.entity.Category
import kotlinx.coroutines.flow.Flow

interface CategoryRepository {
    suspend fun addCategory(category: Category): Category
    suspend fun editCategory(id: Int, category: Category)
    suspend fun toggleCategory(id: Int)
    suspend fun deleteCategory(id: Int)
    fun getCategories(): Flow<List<Category>>
    fun getCategory(id: Int): Flow<Category>
    fun getCategoriesByFoodId(foodId: Int): Flow<List<Category>>

    suspend fun setCategoryFoodRelations(categoryId: Int, foodIds: List<Int>)
    suspend fun updateFoodRelationsForCategories(foodId: Int, categoryIds: List<Int>)
}