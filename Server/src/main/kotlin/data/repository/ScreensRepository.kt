package com.kalashnikovprojects.ufmserver.data.repository

import com.kalashnikovprojects.ufmserver.data.tables.Screens
import com.kalashnikovprojects.ufmserver.data.tables.Users
import com.kalashnikovprojects.ufmserver.dto.NoIdTVScreen
import com.kalashnikovprojects.ufmserver.dto.NoIdUserHashedPassword
import com.kalashnikovprojects.ufmserver.dto.TVScreen
import com.kalashnikovprojects.ufmserver.dto.UserHashedPassword
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.singleOrNull
import kotlinx.coroutines.flow.toList
import org.jetbrains.exposed.v1.core.ResultRow
import org.jetbrains.exposed.v1.core.and
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.exceptions.ExposedSQLException
import org.jetbrains.exposed.v1.r2dbc.R2dbcDatabase
import org.jetbrains.exposed.v1.r2dbc.SchemaUtils
import org.jetbrains.exposed.v1.r2dbc.insert
import org.jetbrains.exposed.v1.r2dbc.selectAll
import org.jetbrains.exposed.v1.r2dbc.transactions.suspendTransaction

class ScreensRepository(val database: R2dbcDatabase) {
    suspend fun createSchema() {
        suspendTransaction(database) {
            SchemaUtils.create(Screens)
        }
    }

    suspend fun create(userId: Int, screen: NoIdTVScreen): Int = suspendTransaction(database) {
        val newRecord = Screens.insert {
            it[name] = screen.name
            it[width] = screen.width
            it[height] = screen.height
            it[user_id] = userId.toUInt()
        }
        newRecord[Screens.id].value.toInt()
    }

    suspend fun getAllByUserId(userId: Int): List<TVScreen> = suspendTransaction(database) {
            Screens
                .selectAll()
                .where { Screens.user_id eq userId.toUInt() and (Screens.user_id eq userId.toUInt()) }
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
}


fun rowToTVScreen(row: ResultRow): TVScreen {
    val screenId = row[Screens.id]
    val screenName = row[Screens.name]
    val screenWidth = row[Screens.width]
    val screenHeight = row[Screens.height]
    return TVScreen(
        screenId.value.toInt(),
        screenName,
        screenWidth,
        screenHeight,
    )
}