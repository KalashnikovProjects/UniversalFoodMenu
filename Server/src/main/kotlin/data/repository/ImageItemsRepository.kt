package com.kalashnikovprojects.ufmserver.data.repository

import com.kalashnikovprojects.ufmserver.data.tables.ImageItems
import com.kalashnikovprojects.ufmserver.models.ImageItem
import com.kalashnikovprojects.ufmserver.models.NoIdImageItem
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

class ImageItemsRepository(val database: R2dbcDatabase) : Repository {
    override suspend fun createSchema() {
        suspendTransaction(database) {
            SchemaUtils.create(ImageItems)
        }
    }

    suspend fun create(userId: Int, item: NoIdImageItem): Int = suspendTransaction(database) {
        val newRecord = ImageItems.insert {
            it[image_uri] = item.imageUri
            it[user_id] = userId.toUInt()
        }
        newRecord[ImageItems.id].value.toInt()
    }

    suspend fun getAllByUserId(userId: Int): List<ImageItem> = suspendTransaction(database) {
        ImageItems
            .selectAll()
            .where { ImageItems.user_id eq userId.toUInt() and (ImageItems.user_id eq userId.toUInt()) }
            .map { row ->
                rowToImageItem(row)
            }.toList()
    }

    suspend fun getById(userId: Int, id: Int): ImageItem? = suspendTransaction(database) {
        ImageItems
            .selectAll()
            .where { ImageItems.id eq id.toUInt() and (ImageItems.user_id eq userId.toUInt()) }
            .map { row ->
                rowToImageItem(row)
            }.singleOrNull()
    }

    suspend fun deleteById(userId: Int, id: Int): Boolean = suspendTransaction(database) {
        ImageItems.deleteWhere { ImageItems.id eq id.toUInt() and (ImageItems.user_id eq userId.toUInt()) } == 1
    }
}


fun rowToImageItem(row: ResultRow): ImageItem {
    return ImageItem(
        row[ImageItems.id].value.toInt(),
        row[ImageItems.image_uri],
    )
}