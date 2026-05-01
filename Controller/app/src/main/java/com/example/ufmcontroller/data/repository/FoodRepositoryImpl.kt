package com.example.ufmcontroller.data.repository

import androidx.compose.foundation.text.input.TextFieldState
import com.example.ufmcontroller.data.local.FoodAPIDataSource
import com.example.ufmcontroller.domain.model.FoodItem
import com.example.ufmcontroller.domain.repository.FoodRepository
import com.example.ufmcontroller.presentation.theme.UFMControllerTheme
import com.example.ufmcontroller.presentation.ui.screen.HomeScreen
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow


class FoodRepositoryImpl(private val api: FoodAPIDataSource) : FoodRepository {
    private val _cache = MutableStateFlow<List<FoodItem>>(
        listOf(
                    FoodItem(1, "Американский бургер", imageUri = "https://static.vecteezy.com/system/resources/previews/041/290/624/non_2x/ai-generated-fresh-burger-isolated-on-transparent-background-free-png.png", price = 99.99F, inStock = true),
                    FoodItem(2, "Итальянская пицца", imageUri = "https://static.vecteezy.com/system/resources/previews/041/290/624/non_2x/ai-generated-fresh-burger-isolated-on-transparent-background-free-png.png", price = 99.99F, inStock = false),
                    FoodItem(2, "Китайский вок", imageUri = null, price = 99.99F, inStock = false),
                    FoodItem(1, "Японские роллы", imageUri = null, price = 99.99F, inStock = true)),
    )

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