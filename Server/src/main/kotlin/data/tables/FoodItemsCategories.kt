package com.kalashnikovprojects.ufmserver.data.tables

import org.jetbrains.exposed.v1.core.ReferenceOption
import org.jetbrains.exposed.v1.core.Table

object FoodItemsCategories : Table("food_items_categories") {
    val category_id = reference("category_id", Categories.id, onDelete = ReferenceOption.CASCADE)
    val food_item_id = reference("food_item_id", FoodItems.id, onDelete = ReferenceOption.CASCADE)

    override val primaryKey = PrimaryKey(category_id, food_item_id)
}