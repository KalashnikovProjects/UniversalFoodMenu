package com.kalashnikovprojects.ufmserver.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed class Events {
    @SerialName("toggle_food_event")
    @Serializable
    data class ToggleFoodEvent(val id: Int, val inStock: Boolean) : Events()
    @SerialName("add_food_event")
    @Serializable
    data class AddFoodEvent(val element: FoodItem) : Events()
    @SerialName("change_food_event")
    @Serializable
    data class ChangeFoodEvent(val id: Int, val element: FoodItem) : Events()
    @SerialName("delete_food_event")
    @Serializable
    data class DeleteFoodEvent(val id: Int) : Events()

    @SerialName("add_category_event")
    @Serializable
    data class AddCategoryEvent(val element: Category) : Events()
    @SerialName(value = "change_category_event")
    @Serializable
    data class ChangeCategoryEvent(val id: Int, val element: Category) : Events()
    @SerialName("delete_category_event")
    @Serializable
    data class DeleteCategoryEvent(val id: Int) : Events()

    @SerialName("add_image_event")
    @Serializable
    data class AddImageEvent(val element: ImageItem) : Events()
//    @SerialName(value = "change_image_event")
//    @Serializable
//    data class ChangeImageEvent(val id: Int, val element: ImageItem) : Events()
    @SerialName("delete_image_event")
    @Serializable
    data class DeleteImageEvent(val id: Int) : Events()

    @SerialName("add_text_event")
    @Serializable
    data class AddTextEvent(val element: TextItem) : Events()
    @SerialName(value = "change_text_event")
    @Serializable
    data class ChangeTextEvent(val id: Int, val element: TextItem) : Events()
    @SerialName("delete_text_event")
    @Serializable
    data class DeleteTextEvent(val id: Int) : Events()

    @SerialName("add_design_event")
    @Serializable
    data class AddDesignEvent(val element: DesignItem) : Events()
    @SerialName("change_design_event")
    @Serializable
    data class ChangeDesignEvent(val id: Int, val element: DesignItem) : Events()
    @SerialName("delete_design_event")
    @Serializable
    data class DeleteDesignEvent(val id: Int) : Events()

    @SerialName("set_category_items_event")
    @Serializable
    data class SetCategoryItems(val categoryId: Int, val foodItems: List<FoodItem>) : Events()

    @SerialName("set_food_categories_event")
    @Serializable
    data class SetFoodCategories(val foodId: Int, val category: List<Category>) : Events()

    @SerialName("add_screen_event")
    @Serializable
    data class AddScreenEvent(val element: TVScreen) : Events()

    @SerialName("change_screen_event")
    @Serializable
    data class ChangeScreenEvent(val id: Int, val element: TVScreen) : Events()

    @SerialName("logout_screen_event")
    @Serializable
    data class DeleteScreenEvent(val id: Int) : Events()

    @SerialName("reload_design_items_by_screen_id")
    @Serializable
    data class ReloadDesignItemsByScreenId(val screenId: Int, val items: List<DesignItem>) : Events()

    @SerialName("reload_design_items_with_screen_id")
    @Serializable
    data class ReloadDesignItemsWithScreenId(val items: List<DesignItemWithScreenId>) : Events()

    @SerialName("reload_food_items")
    @Serializable
    data class ReloadFoodItems(val items: List<FoodItem>) : Events()

    @SerialName("reload_category_items")
    @Serializable
    data class ReloadCategoryItems(val items: List<Category>) : Events()

    @SerialName("reload_text_items")
    @Serializable
    data class ReloadTextItems(val items: List<TextItem>) : Events()

    @SerialName("reload_image_items")
    @Serializable
    data class ReloadImageItems(val items: List<ImageItem>) : Events()

    @SerialName("reload_screen_items")
    @Serializable
    data class ReloadScreenItems(val items: List<TVScreen>) : Events()
}