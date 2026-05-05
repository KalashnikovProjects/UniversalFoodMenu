package com.kalashnikovprojects.ufmserver.dto

import com.kalashnikovprojects.ufmserver.data.tables.FoodItems
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed class Event {
    @SerialName("toggle_food_event")
    @Serializable
    class ToggleFoodEvent(val id: Int, val inStock: Boolean) : Event()
    @SerialName("add_food_event")
    @Serializable
    class AddFoodEvent(val element: FoodItem) : Event()
    @SerialName("change_food_event")
    @Serializable
    class ChangeFoodEvent(val id: Int, val element: FoodItem) : Event()
    @SerialName("delete_food_event")
    @Serializable
    class DeleteFoodEvent(val id: Int) : Event()

    @SerialName("add_category_event")
    @Serializable
    class AddCategoryEvent(val element: Category) : Event()
    @SerialName(value = "change_category_event")
    @Serializable
    class ChangeCategoryEvent(val id: Int, val element: Category) : Event()
    @SerialName("delete_category_event")
    @Serializable
    class DeleteCategoryEvent(val id: Int) : Event()

    @SerialName("add_image_event")
    @Serializable
    class AddImageEvent(val element: ImageItem) : Event()
    @SerialName(value = "change_image_event")
    @Serializable
    class ChangeImageEvent(val id: Int, val element: ImageItem) : Event()
    @SerialName("delete_image_event")
    @Serializable
    class DeleteImageEvent(val id: Int) : Event()

    @SerialName("add_text_event")
    @Serializable
    class AddTextEvent(val element: TextItem) : Event()
    @SerialName(value = "change_text_event")
    @Serializable
    class ChangeTextEvent(val id: Int, val element: TextItem) : Event()
    @SerialName("delete_text_event")
    @Serializable
    class DeleteTextEvent(val id: Int) : Event()

    @SerialName("add_design_event")
    @Serializable
    class AddDesignEvent(val element: DesignItem) : Event()
    @SerialName("change_design_event")
    @Serializable
    class ChangeDesignEvent(val id: Int, val element: DesignItem) : Event()
    @SerialName("delete_design_event")
    @Serializable
    class DeleteDesignEvent(val id: Int) : Event()

    @SerialName("set_category_items_event")
    @Serializable
    class SetCategoryItems(val categoryId: Int, val foodItems: List<FoodItem>) : Event()

    @SerialName("set_food_categories_event")
    @Serializable
    class SetFoodCategories(val foodId: Int, val category: List<Category>) : Event()

    @SerialName("full_reload_event")
    @Serializable
    class FullReloadEvent() : Event()
}