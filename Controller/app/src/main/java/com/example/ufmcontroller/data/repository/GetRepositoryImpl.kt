package com.example.ufmcontroller.data.repository

import com.example.ufmcontroller.data.local.LocalDataSource
import com.example.ufmcontroller.domain.entity.Category
import com.example.ufmcontroller.domain.entity.DesignItem
import com.example.ufmcontroller.domain.entity.FoodItem
import com.example.ufmcontroller.domain.entity.FoodItemsCategorized
import com.example.ufmcontroller.domain.entity.TVScreenWithDesignItems
import com.example.ufmcontroller.domain.repository.GetRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetRepositoryImpl @Inject constructor(
    private val dataSource: LocalDataSource,
) : GetRepository {
    override fun getCategories(): Flow<List<Category>> {
        return dataSource.categories
    }

    override fun getCategory(id: Int): Flow<Category> {
        return dataSource.getCategory(id)
    }

    override fun getCategoriesByFoodId(foodId: Int): Flow<List<Category>> {
        return dataSource.getCategoryByFoodId(foodId)
    }

    override fun getDesignItem(id: Int): Flow<DesignItem> {
        return dataSource.getDesignItem(id)
    }

    override fun getDesignItems(): Flow<List<DesignItem>> {
        return dataSource.designItems
    }

    override fun getFoodItem(id: Int): Flow<FoodItem> {
        return dataSource.getFoodItem(id)
    }

    override fun getFoodItemsByCategoryId(categoryId: Int): Flow<List<FoodItem>> {
        return dataSource.getFoodItemsByCategoryId(categoryId)
    }

    override fun getFoodItems(): Flow<List<FoodItem>> {
        return dataSource.foodItems
    }

    override fun getCategorizedFoodItems(): Flow<FoodItemsCategorized> {
        return dataSource.categorizedFoodItems
    }

    override fun getScreenWithDesignItems(id: Int): Flow<TVScreenWithDesignItems> {
        return dataSource.getScreenWithDesignItems(id)
    }

    override fun getScreensWithDesignItems(): Flow<List<TVScreenWithDesignItems>> {
        return dataSource.screensWithDesignItems
    }

}