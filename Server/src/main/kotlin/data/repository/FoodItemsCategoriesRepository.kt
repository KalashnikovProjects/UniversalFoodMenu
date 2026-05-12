package com.kalashnikovprojects.ufmserver.data.repository

import com.kalashnikovprojects.ufmserver.data.tables.Categories
import com.kalashnikovprojects.ufmserver.data.tables.FoodItems
import com.kalashnikovprojects.ufmserver.data.tables.FoodItemsCategories
import com.kalashnikovprojects.ufmserver.models.Category
import com.kalashnikovprojects.ufmserver.models.FoodItem
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import org.jetbrains.exposed.v1.core.and
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.core.inList
import org.jetbrains.exposed.v1.core.isNull
import org.jetbrains.exposed.v1.core.neq
import org.jetbrains.exposed.v1.r2dbc.R2dbcDatabase
import org.jetbrains.exposed.v1.r2dbc.SchemaUtils
import org.jetbrains.exposed.v1.r2dbc.batchInsert
import org.jetbrains.exposed.v1.r2dbc.deleteWhere
import org.jetbrains.exposed.v1.r2dbc.selectAll
import org.jetbrains.exposed.v1.r2dbc.transactions.suspendTransaction

class FoodItemsCategoriesRepository(val database: R2dbcDatabase) : Repository {
    override suspend fun createSchema() {
        suspendTransaction(database) {
            SchemaUtils.create(FoodItemsCategories)
        }
    }

    suspend fun setFoodItemsForCategory(userId: Int, categoryId: Int, foodItemsId: List<Int>) = suspendTransaction(database) {
        val categoryExists = Categories.selectAll()
            .where { (Categories.id eq categoryId.toUInt()) and (Categories.user_id eq userId.toUInt()) }
            .empty().not()

        if (!categoryExists) {
            throw IllegalArgumentException("Категория не найдена или не принадлежит пользователю с ID: $userId")
        }

        val invalidItemsCount = FoodItems.selectAll()
            .where { (FoodItems.id inList foodItemsId.map { it.toUInt() }) and (FoodItems.user_id neq userId.toUInt()) }
            .count()

        if (invalidItemsCount > 0) {
            throw IllegalArgumentException("Один или несколько элементов еды не принадлежат пользователю с ID: $userId")
        }
        FoodItemsCategories.deleteWhere { FoodItemsCategories.category_id eq categoryId.toUInt() }
        FoodItemsCategories.batchInsert(foodItemsId, ignore = true) {
            foodId ->
            this[FoodItemsCategories.category_id] = categoryId.toUInt()
            this[FoodItemsCategories.food_item_id] = foodId.toUInt()
        }
    }

    suspend fun setCategoriesForFoodItem(userId: Int, foodId: Int, categoriesId: List<Int>) = suspendTransaction(database) {
        val foodItemExists = FoodItems.selectAll()
            .where { (FoodItems.id eq foodId.toUInt()) and (FoodItems.user_id eq userId.toUInt()) }
            .empty().not()

        if (!foodItemExists) {
            throw IllegalArgumentException("Элемент еды не найден или не принадлежит пользователю с ID: $userId")
        }

        val invalidCategoriesCount = Categories.selectAll()
            .where { (Categories.id inList categoriesId.map { it.toUInt() }) and (Categories.user_id neq userId.toUInt()) }
            .count()

        if (invalidCategoriesCount > 0) {
            throw IllegalArgumentException("Одна или несколько категорий не принадлежат пользователю с ID: $userId")
        }

        FoodItemsCategories.deleteWhere { FoodItemsCategories.food_item_id eq foodId.toUInt() }
        FoodItemsCategories.batchInsert(categoriesId, ignore = true) { categoryId ->
            this[FoodItemsCategories.category_id] = categoryId.toUInt()
            this[FoodItemsCategories.food_item_id] = foodId.toUInt()
        }
    }

    suspend fun getCategoriesForFoodItem(userId: Int, foodItemId: Int): List<Category> = suspendTransaction(database) {
        (FoodItemsCategories innerJoin Categories).selectAll()
            .where { (FoodItemsCategories.food_item_id eq foodItemId.toUInt()) and (Categories.user_id eq userId.toUInt()) }
            .map { row ->
                rowToCategoryItem(row)
            }.toList()
    }

    suspend fun getFoodItemsForCategory(userId: Int, categoryId: Int): List<FoodItem> = suspendTransaction(database) {
        (FoodItemsCategories innerJoin FoodItems).selectAll()
            .where { (FoodItemsCategories.category_id eq categoryId.toUInt()) and (FoodItems.user_id eq userId.toUInt()) }
            .map { row ->
                rowToFoodItem(row)
            }.toList()
    }

    suspend fun getNoCategoryFoodItems(userId: Int): List<FoodItem> = suspendTransaction(database) {
        (FoodItems leftJoin FoodItemsCategories)
            .selectAll()
            .where { (FoodItems.user_id eq userId.toUInt()) and (FoodItemsCategories.food_item_id.isNull()) }
            .map { row ->
                rowToFoodItem(row)
            }.toList()
    }
}