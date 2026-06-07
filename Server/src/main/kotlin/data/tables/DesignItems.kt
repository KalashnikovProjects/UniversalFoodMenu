package com.kalashnikovprojects.ufmserver.data.tables

import org.jetbrains.exposed.v1.core.ReferenceOption
import org.jetbrains.exposed.v1.core.dao.id.UIntIdTable

object DesignItems : UIntIdTable("design_items") {
    val food_item_id = optReference("food_item_id", FoodItems.id, onDelete = ReferenceOption.CASCADE)
    val category_id = optReference("category_id", Categories.id, onDelete = ReferenceOption.CASCADE)
    val text_item_id = optReference("text_item_id", TextItems.id, onDelete = ReferenceOption.CASCADE)
    val image_item_id = optReference("image_item_id", ImageItems.id, onDelete = ReferenceOption.CASCADE)

    val style = text("style")

    val screen_id = reference("screen_id", Screens.id, onDelete = ReferenceOption.CASCADE)
    val user_id = reference("user_id", Users.id, onDelete = ReferenceOption.CASCADE)
}