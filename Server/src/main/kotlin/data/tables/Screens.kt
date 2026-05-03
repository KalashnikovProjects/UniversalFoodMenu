package com.kalashnikovprojects.ufmserver.data.tables

import org.jetbrains.exposed.v1.core.dao.id.UIntIdTable

object Screens : UIntIdTable("screens") {
    val name = text("name")
    val width = integer("width")
    val height = integer("height")

    val user_id = reference("user_id", Users.id)
}