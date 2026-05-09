package com.kalashnikovprojects.ufmserver.data.tables

import org.jetbrains.exposed.v1.core.dao.id.UIntIdTable

object Categories : UIntIdTable("categories") {
    val name = text("name")
    val image_uri = text("image_uri").nullable()
    val price = float("price").nullable()
    val user_id = reference("user_id", Users.id)
}