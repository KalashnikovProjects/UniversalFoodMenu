package com.kalashnikovprojects.ufmserver.data.tables

import org.jetbrains.exposed.v1.core.dao.id.UIntIdTable

object Users : UIntIdTable("users") {
    val username = varchar("username", 50).uniqueIndex()
    val passwordHash = varchar("password_hash", 60)
}