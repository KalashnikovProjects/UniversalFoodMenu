package com.kalashnikovprojects.ufmserver.data.repository

import com.kalashnikovprojects.ufmserver.data.tables.Screens
import com.kalashnikovprojects.ufmserver.models.NoIdTVScreen
import com.kalashnikovprojects.ufmserver.models.TVScreen
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

class ScreensRepository(val database: R2dbcDatabase) : Repository {
    override suspend fun createSchema() {
        suspendTransaction(database) {
            SchemaUtils.create(Screens)
        }
    }

    suspend fun create(userId: Int, screen: NoIdTVScreen): Int = suspendTransaction(database) {
        val newRecord = Screens.insert {
            it[name] = screen.name
            it[width] = screen.width
            it[height] = screen.height
            it[style] = screen.style
            it[user_id] = userId.toUInt()
        }
        newRecord[Screens.id].value.toInt()
    }

    suspend fun getAllByUserId(userId: Int): List<TVScreen> = suspendTransaction(database) {
            Screens
                .selectAll()
                .where { Screens.user_id eq userId.toUInt() and (Screens.user_id eq userId.toUInt()) }
                .orderBy(Screens.id)
                .map { row ->
                    rowToTVScreen(row)
                }.toList()
        }

    suspend fun getById(userId: Int, id: Int): TVScreen? = suspendTransaction(database) {
        Screens
            .selectAll()
            .where { Screens.id eq id.toUInt() and (Screens.user_id eq userId.toUInt()) }
            .map { row ->
                rowToTVScreen(row)
            }.singleOrNull()
    }


    suspend fun updateById(userId: Int, id: Int, item: NoIdTVScreen): Boolean = suspendTransaction(database) {
        Screens.update({ Screens.id eq id.toUInt() and (Screens.user_id eq userId.toUInt()) }) {
            it[name] = item.name
            it[style] = item.style
        } == 1
    }

    suspend fun deleteById(userId: Int, id: Int): Boolean = suspendTransaction(database) {
        Screens.deleteWhere { Screens.id eq id.toUInt() and (Screens.user_id eq userId.toUInt()) } == 1
    }
}


fun rowToTVScreen(row: ResultRow): TVScreen {
    return TVScreen(
        row[Screens.id].value.toInt(),
        row[Screens.name],
        row[Screens.width],
        row[Screens.height],
        row[Screens.style],
    )
}