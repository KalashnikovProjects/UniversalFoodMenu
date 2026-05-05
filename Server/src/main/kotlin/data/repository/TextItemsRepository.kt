package com.kalashnikovprojects.ufmserver.data.repository

import com.kalashnikovprojects.ufmserver.data.tables.FoodItems
import com.kalashnikovprojects.ufmserver.data.tables.FoodItems.image_uri
import com.kalashnikovprojects.ufmserver.data.tables.FoodItems.in_stock
import com.kalashnikovprojects.ufmserver.data.tables.FoodItems.name
import com.kalashnikovprojects.ufmserver.data.tables.FoodItems.price
import com.kalashnikovprojects.ufmserver.data.tables.ImageItems
import com.kalashnikovprojects.ufmserver.data.tables.TextItems
import com.kalashnikovprojects.ufmserver.data.tables.Users
import com.kalashnikovprojects.ufmserver.dto.ImageItem
import com.kalashnikovprojects.ufmserver.dto.NoIdTextItem
import com.kalashnikovprojects.ufmserver.dto.TextItem
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

class TextItemsRepository(val database: R2dbcDatabase) {
    suspend fun createSchema() {
        suspendTransaction(database) {
            SchemaUtils.create(TextItems)
        }
    }

    suspend fun create(userId: Int, item: NoIdTextItem): Int = suspendTransaction(database) {
        val newRecord = TextItems.insert {
            it[text] = item.text
            it[user_id] = userId.toUInt()
        }
        newRecord[TextItems.id].value.toInt()
    }

    suspend fun getAllByUserId(userId: Int): List<TextItem> = suspendTransaction(database) {
        TextItems
            .selectAll()
            .where { TextItems.user_id eq userId.toUInt() }
            .map { row ->
                rowToTextItem(row)
            }.toList()
    }

    suspend fun getById(userId: Int, id: Int): TextItem? = suspendTransaction(database) {
        TextItems
            .selectAll()
            .where { TextItems.id eq id.toUInt() and (TextItems.user_id eq userId.toUInt())}
            .map { row ->
                rowToTextItem(row)
            }.singleOrNull()
    }

    suspend fun updateById(userId: Int, id: Int, item: NoIdTextItem): Boolean = suspendTransaction(database) {
        TextItems.update({ TextItems.id eq id.toUInt() and (TextItems.user_id eq userId.toUInt()) }) {
            it[text] = item.text
        } == 1
    }

    suspend fun deleteById(userId: Int, id: Int): Boolean = suspendTransaction(database) {
        TextItems.deleteWhere { TextItems.id eq id.toUInt() and (TextItems.user_id eq userId.toUInt()) } == 1
    }
}

fun rowToTextItem(row: ResultRow): TextItem {
    return TextItem(
        row[TextItems.id].value.toInt(),
        row[TextItems.text],
    )
}