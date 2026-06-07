package com.kalashnikovprojects.ufmserver.data.repository

import com.kalashnikovprojects.ufmserver.data.tables.Categories
import com.kalashnikovprojects.ufmserver.models.Category
import com.kalashnikovprojects.ufmserver.models.NoIdCategory
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

class CategoriesRepository(val database: R2dbcDatabase) : Repository {
    override suspend fun createSchema() {
        suspendTransaction(database) {
            SchemaUtils.create(Categories)
        }
    }

    suspend fun create(userId: Int, item: NoIdCategory): Int = suspendTransaction(database) {
        val newRecord = Categories.insert {
            it[name] = item.name
            it[image_uri] = item.imageUri
            it[price] = item.price
            it[in_stock] = item.inStock
            it[user_id] = userId.toUInt()
        }
        newRecord[Categories.id].value.toInt()
    }

    suspend fun getAllByUserId(userId: Int): List<Category> = suspendTransaction(database) {
        Categories
            .selectAll()
            .where { Categories.user_id eq userId.toUInt() }
            .map { row ->
                rowToCategoryItem(row)
            }.toList()
    }

    suspend fun getById(userId: Int, id: Int): Category? = suspendTransaction(database) {
        Categories
            .selectAll()
            .where { Categories.id eq id.toUInt() and (Categories.user_id eq userId.toUInt()) }
            .map { row ->
                rowToCategoryItem(row)
            }.singleOrNull()
    }

    suspend fun updateById(userId: Int, id: Int, item: NoIdCategory): Boolean = suspendTransaction(database) {
        Categories.update({
            Categories.id eq id.toUInt() and (Categories.user_id eq userId.toUInt())
        }) {
            it[name] = item.name
        } == 1
    }

    suspend fun toggleById(userId: Int, id: Int, inStock: Boolean): Boolean = suspendTransaction(database) {
        Categories.update({ Categories.id eq id.toUInt() and (Categories.user_id eq userId.toUInt()) }) {
            it[in_stock] = inStock
        } == 1
    }

    suspend fun deleteById(userId: Int, id: Int): Boolean = suspendTransaction(database) {
        Categories.deleteWhere {
            Categories.id eq id.toUInt() and (Categories.user_id eq userId.toUInt())
        } == 1
    }
}

fun rowToCategoryItem(row: ResultRow): Category {
    return Category(
        row[Categories.id].value.toInt(),
        row[Categories.name],
        row[Categories.image_uri],
        row[Categories.price],
        row[Categories.in_stock]
    )
}