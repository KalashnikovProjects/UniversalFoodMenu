package com.example.ufmcontroller.data.repository

import com.example.ufmcontroller.data.local.FoodAPIDataSource
import com.example.ufmcontroller.domain.model.FoodItem
import com.example.ufmcontroller.domain.repository.FoodRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow


class FoodRepositoryImpl(private val api: FoodAPIDataSource) : FoodRepository {
    private val _cache = MutableStateFlow<List<FoodItem>>(emptyList())

    override fun getFoodItems(): Flow<List<FoodItem>> = _cache.asStateFlow()

    override suspend fun refreshFoodItems() {
        // TODO: убрать на всех уровнях, если будет websocket, иначе сделать view model и в ui потягивание вверх
//        val remoteData = api.fetchFood()
//        _cache.value = remoteData
    }

    override suspend fun toggleFoodItem(id: Int) {
        TODO("Not yet implemented")
    }

    override suspend fun addFoodItem(foodItem: FoodItem) {
        // TODO: add returning id from local package to add foodItem to cache
    }

    override suspend fun editFoodItem(id: Int, foodItem: FoodItem) {
    }
}