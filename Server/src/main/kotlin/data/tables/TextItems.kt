package com.kalashnikovprojects.ufmserver.data.tables

import org.jetbrains.exposed.v1.core.dao.id.UIntIdTable

object TextItems : UIntIdTable("text_items") {
    val text = text("text")

    val user_id = reference("user_id", Users.id)
}