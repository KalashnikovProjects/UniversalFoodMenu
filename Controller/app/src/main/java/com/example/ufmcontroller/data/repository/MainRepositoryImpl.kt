package com.example.ufmcontroller.data.repository

import com.example.ufmcontroller.data.local.MainDataSource
import com.example.ufmcontroller.data.local.UserPreferencesDataSource
import com.example.ufmcontroller.data.remote.EventsWebSocketService
import com.example.ufmcontroller.domain.entity.Category
import com.example.ufmcontroller.domain.entity.DesignItem
import com.example.ufmcontroller.domain.entity.FoodItem
import com.example.ufmcontroller.domain.entity.FoodItemsCategorized
import com.example.ufmcontroller.domain.entity.ImageItem
import com.example.ufmcontroller.domain.entity.TVScreen
import com.example.ufmcontroller.domain.entity.TVScreenWithDesignItems
import com.example.ufmcontroller.domain.entity.TextItem
import com.example.ufmcontroller.domain.repository.CategoryRepository
import com.example.ufmcontroller.domain.repository.DesignRepository
import com.example.ufmcontroller.domain.repository.FoodRepository
import com.example.ufmcontroller.domain.repository.TvScreenRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MainRepositoryImpl @Inject constructor(
    private val dataSource: MainDataSource,
    private val eventsWebSocketService: EventsWebSocketService,
    private val userPreferencesDataSource: UserPreferencesDataSource,
    private val externalScope: CoroutineScope
) : CategoryRepository, DesignRepository, FoodRepository, TvScreenRepository {
    override suspend fun addCategory(category: Category): Category {
        TODO("Not yet implemented")
    }

    override suspend fun editCategory(
        id: Int,
        category: Category
    ) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteCategory(id: Int) {
        TODO("Not yet implemented")
    }

    override suspend fun getCategories(): Flow<List<Category>> {
        TODO("Not yet implemented")
    }

    override suspend fun setCategoryFoodRelations(
        categoryId: Int,
        foodIds: List<Int>
    ) {
        TODO("Not yet implemented")
    }

    override suspend fun updateFoodRelationsForCategories(
        foodId: Int,
        categoryIds: List<Int>
    ) {
        TODO("Not yet implemented")
    }


    override suspend fun addDesignItem(designItem: DesignItem): DesignItem {
        TODO("Not yet implemented")
    }

    override suspend fun addDesignItemWithImageOrText(designItem: DesignItem): DesignItem {
        TODO("Not yet implemented")
    }

    override suspend fun editDesignItem(
        id: Int,
        designItem: DesignItem
    ) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteDesignItem(id: Int) {
        TODO("Not yet implemented")
    }

    override fun getDesignItems(): Flow<List<DesignItem>> {
        TODO("Not yet implemented")
    }

    override suspend fun toggleFoodItem(id: Int) {
        TODO("Not yet implemented")
    }

    override suspend fun addFoodItem(foodItem: FoodItem): FoodItem {
        TODO("Not yet implemented")
    }

    override suspend fun editFoodItem(
        id: Int,
        foodItem: FoodItem
    ) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteFoodItem(id: Int) {
        TODO("Not yet implemented")
    }

    override fun getFoodItems(): Flow<List<FoodItem>> {
        TODO("Not yet implemented")
    }

    override fun getCategorizedFoodItems(): Flow<FoodItemsCategorized> {
        TODO("Not yet implemented")
    }

    override suspend fun inputCodeForTvAuth(code: String) {
        TODO("Not yet implemented")
    }

    override suspend fun editScreen(
        id: Int,
        screen: TVScreen
    ) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteScreen(id: Int) {
        TODO("Not yet implemented")
    }

    override fun getScreen(id: Int): Flow<TVScreen> {
        TODO("Not yet implemented")
    }

    override fun getScreensWithDesignItems(): Flow<List<TVScreenWithDesignItems>> {
        TODO("Not yet implemented")
    }
}