package com.kalashnikovprojects.ufmserver.data.repository

import com.kalashnikovprojects.ufmserver.data.tables.FoodItems
import com.kalashnikovprojects.ufmserver.data.tables.ImageItems
import com.kalashnikovprojects.ufmserver.data.tables.Users
import com.kalashnikovprojects.ufmserver.dto.FoodItem
import com.kalashnikovprojects.ufmserver.dto.NoIdFoodItem
import io.r2dbc.spi.Row
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.singleOrNull
import kotlinx.coroutines.flow.toList
import org.jetbrains.exposed.v1.core.ResultRow
import org.jetbrains.exposed.v1.core.and
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.r2dbc.R2dbcDatabase
import org.jetbrains.exposed.v1.r2dbc.SchemaUtils
import org.jetbrains.exposed.v1.r2dbc.deleteWhere
import org.jetbrains.exposed.v1.r2dbc.insert
import org.jetbrains.exposed.v1.r2dbc.selectAll
import org.jetbrains.exposed.v1.r2dbc.transactions.suspendTransaction
import org.jetbrains.exposed.v1.r2dbc.update

class FoodItemsRepository(val database: R2dbcDatabase) {
    suspend fun createSchema() {
        suspendTransaction(database) {
            SchemaUtils.create(FoodItems)
        }
    }

    suspend fun create(userId: Int, item: NoIdFoodItem): Int = suspendTransaction(database) {
        val newRecord = FoodItems.insert {
            it[name] = item.name
            it[price] = item.price
            it[image_uri] = item.imageUri
            it[in_stock] = item.inStock
            it[user_id] = userId.toUInt()
        }
        newRecord[FoodItems.id].value.toInt()
    }

    suspend fun getAllByUserId(userId: Int): List<FoodItem> = suspendTransaction(database) {
        FoodItems
            .selectAll()
            .where { FoodItems.user_id eq userId.toUInt() }
            .map { row ->
                rowToFoodItem(row)
            }.toList()
    }

    suspend fun getById(userId: Int, id: Int): FoodItem? = suspendTransaction(database) {
        FoodItems
            .selectAll()
            .where { FoodItems.id eq id.toUInt() and (FoodItems.user_id eq userId.toUInt())}
            .map { row ->
                rowToFoodItem(row)
            }.singleOrNull()
    }

    suspend fun updateById(userId: Int, id: Int, item: NoIdFoodItem): Boolean = suspendTransaction(database) {
        FoodItems.update({ FoodItems.id eq id.toUInt() and (FoodItems.user_id eq userId.toUInt()) }) {
                it[name] = item.name
                it[price] = item.price
                it[image_uri] = item.imageUri
                it[in_stock] = item.inStock
            } == 1
    }

    suspend fun toggleById(userId: Int, id: Int, inStock: Boolean): Boolean = suspendTransaction(database) {
        FoodItems.update({ FoodItems.id eq id.toUInt() and (FoodItems.user_id eq userId.toUInt()) }) {
            it[in_stock] = inStock
        } == 1
    }

    suspend fun deleteById(userId: Int, id: Int): Boolean = suspendTransaction(database) {
        FoodItems.deleteWhere { FoodItems.id eq id.toUInt() and (FoodItems.user_id eq userId.toUInt()) } == 1
    }
}

fun rowToFoodItem(row: ResultRow): FoodItem {
    return FoodItem(
        row[FoodItems.id].value.toInt(),
        row[FoodItems.name],
        row[FoodItems.price],
        row[FoodItems.image_uri],
        row[FoodItems.in_stock],
    )
}