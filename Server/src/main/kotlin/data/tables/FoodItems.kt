package com.kalashnikovprojects.ufmserver.data.tables

import org.jetbrains.exposed.v1.core.ReferenceOption
import org.jetbrains.exposed.v1.core.dao.id.UIntIdTable

object FoodItems : UIntIdTable("food_items") {
    val name = text("name")
    val price = float("price")
    val image_uri = text("image_uri").nullable()
    val in_stock = bool("in_stock")

    val user_id = reference("user_id", Users.id, onDelete = ReferenceOption.CASCADE)
}