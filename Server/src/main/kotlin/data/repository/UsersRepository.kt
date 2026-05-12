package com.kalashnikovprojects.ufmserver.data.repository

import com.kalashnikovprojects.ufmserver.data.tables.Users
import com.kalashnikovprojects.ufmserver.models.NoIdUserHashedPassword
import com.kalashnikovprojects.ufmserver.models.UserHashedPassword
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.singleOrNull
import org.jetbrains.exposed.v1.core.*
import org.jetbrains.exposed.v1.exceptions.ExposedSQLException
import org.jetbrains.exposed.v1.r2dbc.*
import org.jetbrains.exposed.v1.r2dbc.transactions.suspendTransaction
import org.jetbrains.exposed.v1.r2dbc.R2dbcDatabase


class UsersRepository(val database: R2dbcDatabase) : Repository {
    override suspend fun createSchema() {
        suspendTransaction(database) {
            SchemaUtils.create(Users)
        }
    }

    suspend fun create(user: NoIdUserHashedPassword): Int = suspendTransaction(database) {
        try {
            val newRecord = Users.insert {
                it[username] = user.username
                it[passwordHash] = user.hashedPassword
            }
            newRecord[Users.id].value.toInt()
        } catch (e: ExposedSQLException) {
            if (e.message?.contains("23505") == true) {
                throw IllegalArgumentException("User with username '${user.username}' already exists")
            }
            throw e
        }

    }

    suspend fun getByUsername(username: String): UserHashedPassword? {
        return suspendTransaction(database) {
            Users.selectAll()
                .where { Users.username eq username }
                .map { UserHashedPassword(it[Users.id].value.toInt(), it[Users.username], it[Users.passwordHash]) }
                .singleOrNull()
        }
    }
    suspend fun getById(id: Int): UserHashedPassword? {
        return suspendTransaction(database) {
            Users.selectAll()
                .where { Users.id eq id.toUInt() }
                .map { UserHashedPassword(it[Users.id].value.toInt(), it[Users.username], it[Users.passwordHash]) }
                .singleOrNull()
        }
    }
}
