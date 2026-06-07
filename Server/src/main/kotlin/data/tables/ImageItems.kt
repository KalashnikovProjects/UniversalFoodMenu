package com.kalashnikovprojects.ufmserver.data.tables

import org.jetbrains.exposed.v1.core.ReferenceOption
import org.jetbrains.exposed.v1.core.dao.id.UIntIdTable

object ImageItems : UIntIdTable("image_items") {
    val image_uri = text("image_uri")

    val user_id = reference("user_id", Users.id, onDelete = ReferenceOption.CASCADE)
}