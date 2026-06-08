package com.example.ufmcontroller.data.repository

import com.example.ufmcontroller.data.remote.RemoteCategoryDataSource
import com.example.ufmcontroller.domain.entity.Category
import com.example.ufmcontroller.domain.repository.CategoryRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CategoryRepositoryImpl @Inject constructor(
    private val remoteCategoryDataSource: RemoteCategoryDataSource,
    ) : CategoryRepository {
    override suspend fun addCategory(category: Category): Category {
        return remoteCategoryDataSource.addCategory(category)
    }
    override suspend fun editCategory(
        id: Int,
        category: Category,
        changedImage: Boolean
    ) {
        return remoteCategoryDataSource.editCategory(id, category, changedImage)
    }

    override suspend fun toggleCategory(id: Int, boolean: Boolean) {
        return remoteCategoryDataSource.toggleCategory(id, boolean)
    }

    override suspend fun deleteCategory(id: Int) {
        return remoteCategoryDataSource.deleteCategory(id)
    }

    override suspend fun setCategoryFoodRelations(
        categoryId: Int,
        foodIds: List<Int>
    ) {
        return remoteCategoryDataSource.setCategoryFoodRelations(categoryId, foodIds)
    }

    override suspend fun updateFoodRelationsForCategories(
        foodId: Int,
        categoryIds: List<Int>
    ) {
        return remoteCategoryDataSource.updateFoodRelationsForCategories(foodId, categoryIds)
    }
}