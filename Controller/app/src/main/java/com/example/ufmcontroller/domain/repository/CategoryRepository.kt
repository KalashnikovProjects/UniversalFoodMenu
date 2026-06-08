package com.example.ufmcontroller.domain.repository

import com.example.ufmcontroller.domain.entity.Category

interface CategoryRepository {
    suspend fun addCategory(category: Category): Category
    suspend fun editCategory(id: Int, category: Category, changedImage: Boolean)
    suspend fun toggleCategory(id: Int, boolean: Boolean)
    suspend fun deleteCategory(id: Int)

    suspend fun setCategoryFoodRelations(categoryId: Int, foodIds: List<Int>)
    suspend fun updateFoodRelationsForCategories(foodId: Int, categoryIds: List<Int>)
}