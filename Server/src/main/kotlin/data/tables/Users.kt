package com.kalashnikovprojects.ufmserver.data.tables

import org.jetbrains.exposed.v1.core.dao.id.UIntIdTable

object Users : UIntIdTable("users") {
    val username = text("username").uniqueIndex()
    val passwordHash = text("password_hash")
}