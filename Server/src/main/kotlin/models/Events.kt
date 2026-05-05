package com.kalashnikovprojects.ufmserver.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed class Events {
    @SerialName("toggle_food_event")
    @Serializable
    class ToggleFoodEvent(val id: Int, val inStock: Boolean) : Events()
    @SerialName("add_food_event")
    @Serializable
    class AddFoodEvent(val element: FoodItem) : Events()
    @SerialName("change_food_event")
    @Serializable
    class ChangeFoodEvent(val id: Int, val element: FoodItem) : Events()
    @SerialName("delete_food_event")
    @Serializable
    class DeleteFoodEvent(val id: Int) : Events()

    @SerialName("add_category_event")
    @Serializable
    class AddCategoryEvent(val element: Category) : Events()
    @SerialName(value = "change_category_event")
    @Serializable
    class ChangeCategoryEvent(val id: Int, val element: Category) : Events()
    @SerialName("delete_category_event")
    @Serializable
    class DeleteCategoryEvent(val id: Int) : Events()

    @SerialName("add_image_event")
    @Serializable
    class AddImageEvent(val element: ImageItem) : Events()
    @SerialName(value = "change_image_event")
    @Serializable
    class ChangeImageEvent(val id: Int, val element: ImageItem) : Events()
    @SerialName("delete_image_event")
    @Serializable
    class DeleteImageEvent(val id: Int) : Events()

    @SerialName("add_text_event")
    @Serializable
    class AddTextEvent(val element: TextItem) : Events()
    @SerialName(value = "change_text_event")
    @Serializable
    class ChangeTextEvent(val id: Int, val element: TextItem) : Events()
    @SerialName("delete_text_event")
    @Serializable
    class DeleteTextEvent(val id: Int) : Events()

    @SerialName("add_design_event")
    @Serializable
    class AddDesignEvent(val element: DesignItem) : Events()
    @SerialName("change_design_event")
    @Serializable
    class ChangeDesignEvent(val id: Int, val element: DesignItem) : Events()
    @SerialName("delete_design_event")
    @Serializable
    class DeleteDesignEvent(val id: Int) : Events()

    @SerialName("set_category_items_event")
    @Serializable
    class SetCategoryItems(val categoryId: Int, val foodItems: List<FoodItem>) : Events()

    @SerialName("set_food_categories_event")
    @Serializable
    class SetFoodCategories(val foodId: Int, val category: List<Category>) : Events()

    @SerialName("full_reload_event")
    @Serializable
    class FullReloadEvent() : Events()
}