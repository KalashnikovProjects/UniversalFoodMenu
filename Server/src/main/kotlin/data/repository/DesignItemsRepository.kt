package com.kalashnikovprojects.ufmserver.data.repository

import com.kalashnikovprojects.ufmserver.data.tables.Categories
import com.kalashnikovprojects.ufmserver.data.tables.DesignItems
import com.kalashnikovprojects.ufmserver.data.tables.FoodItems
import com.kalashnikovprojects.ufmserver.data.tables.ImageItems
import com.kalashnikovprojects.ufmserver.data.tables.TextItems
import com.kalashnikovprojects.ufmserver.models.Category
import com.kalashnikovprojects.ufmserver.models.DesignItem
import com.kalashnikovprojects.ufmserver.models.Designable
import com.kalashnikovprojects.ufmserver.models.FoodItem
import com.kalashnikovprojects.ufmserver.models.ImageItem
import com.kalashnikovprojects.ufmserver.models.NoIdDesignItem
import com.kalashnikovprojects.ufmserver.models.TextItem
import com.kalashnikovprojects.ufmserver.models.toDesignItemWithScreenId
import com.kalashnikovprojects.ufmserver.models.DesignItemWithScreenId
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

class DesignItemsRepository(val database: R2dbcDatabase) : Repository {
    override suspend fun createSchema() {
        suspendTransaction(database) {
            SchemaUtils.create(DesignItems)
        }
    }

    suspend fun create(userId: Int, screenId: Int, item: NoIdDesignItem): Int = suspendTransaction(database) {
        val newRecord = DesignItems.insert {
            when (item.element) {
                is FoodItem -> it[food_item_id] = item.element.id.toUInt()
                is Category -> it[category_id] = item.element.id.toUInt()
                is TextItem -> it[text_item_id] = item.element.id.toUInt()
                is ImageItem -> it[image_item_id] = item.element.id.toUInt()
                else -> {}
            }
            it[style] = item.style
            it[screen_id] = screenId.toUInt()
            it[user_id] = userId.toUInt()
        }
        newRecord[DesignItems.id].value.toInt()
    }

    suspend fun getAllByUserId(userId: Int): List<DesignItemWithScreenId> = suspendTransaction(database) {
        (DesignItems leftJoin
                FoodItems leftJoin
                Categories leftJoin
                TextItems leftJoin
                ImageItems)
            .selectAll()
            .where {
                DesignItems.user_id eq userId.toUInt()
            }
            .orderBy(DesignItems.id)
            .map { row ->
                rowToDesignItemWithScreenId(row)
            }.toList()
    }

    suspend fun getAllByScreenIdUserId(screenId: Int, userId: Int): List<DesignItem> = suspendTransaction(database) {
        (DesignItems leftJoin
                FoodItems leftJoin
                Categories leftJoin
                TextItems leftJoin
                ImageItems)
            .selectAll()
            .where {
                (DesignItems.user_id eq userId.toUInt()) and (DesignItems.screen_id eq screenId.toUInt())
            }
            .orderBy(DesignItems.id)
            .map { row ->
                rowToDesignItem(row)
            }.toList()
    }

    suspend fun getById(userId: Int, id: Int): DesignItem? = suspendTransaction(database) {
        (DesignItems leftJoin
                FoodItems leftJoin
                Categories leftJoin
                TextItems leftJoin
                ImageItems)
            .selectAll()
            .where { DesignItems.id eq id.toUInt() and (DesignItems.user_id eq userId.toUInt()) }
            .map { row ->
                rowToDesignItem(row)
            }.singleOrNull()
    }

    suspend fun updateById(userId: Int, id: Int, item: NoIdDesignItem): Boolean = suspendTransaction(database) {
        DesignItems.update({ DesignItems.id eq id.toUInt() and (DesignItems.user_id eq userId.toUInt()) }) {
            when (item.element) {
                is FoodItem -> it[food_item_id] = item.element.id.toUInt()
                is Category -> it[category_id] = item.element.id.toUInt()
                is TextItem -> it[text_item_id] = item.element.id.toUInt()
                is ImageItem -> it[image_item_id] = item.element.id.toUInt()
                else -> {}
            }
            it[style] = item.style
        } == 1
    }

    suspend fun deleteById(userId: Int, id: Int): Boolean = suspendTransaction(database) {
        DesignItems.deleteWhere { DesignItems.id eq id.toUInt() and (DesignItems.user_id eq userId.toUInt()) } == 1
    }
}

fun rowToDesignItem(row: ResultRow): DesignItem {

    val element: Designable = when {
        row.getOrNull(DesignItems.food_item_id) != null -> {
            rowToFoodItem(row)
        }
        row.getOrNull(DesignItems.category_id) != null -> {
            rowToCategoryItem(row)
        }
        row.getOrNull(DesignItems.image_item_id) != null -> {
            rowToImageItem(row)
        }
        row.getOrNull(DesignItems.text_item_id) != null -> {
            rowToTextItem(row)
        }
        else -> throw IllegalStateException("DesignItem has no element")
    }
    return DesignItem(
        row[DesignItems.id].value.toInt(),
        element,
        row[DesignItems.style],
    )
}


fun rowToDesignItemWithScreenId(row: ResultRow): DesignItemWithScreenId {
    return rowToDesignItem(row).toDesignItemWithScreenId(row[DesignItems.screen_id].value.toInt())
}