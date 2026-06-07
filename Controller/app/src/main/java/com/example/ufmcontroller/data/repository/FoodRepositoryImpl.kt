package com.example.ufmcontroller.data.repository

import com.example.ufmcontroller.data.remote.RemoteCategoryDataSource
import com.example.ufmcontroller.data.remote.RemoteFoodDataSource
import com.example.ufmcontroller.domain.entity.FoodItem
import com.example.ufmcontroller.domain.repository.FoodRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FoodRepositoryImpl @Inject constructor(
    private val remoteFoodDataSource: RemoteFoodDataSource,
    ) : FoodRepository {

    override suspend fun addFoodItem(foodItem: FoodItem): FoodItem {
        return remoteFoodDataSource.addFoodItem(foodItem)
    }

    override suspend fun toggleFoodItem(id: Int, boolean: Boolean) {
        return remoteFoodDataSource.toggleFoodItem(id, boolean)
    }

    override suspend fun editFoodItem(
        id: Int,
        foodItem: FoodItem,
        changedImage: Boolean
    ) {
        return remoteFoodDataSource.editFoodItem(id, foodItem, changedImage)
    }

    override suspend fun deleteFoodItem(id: Int) {
        return remoteFoodDataSource.deleteFoodItem(id)
    }
}