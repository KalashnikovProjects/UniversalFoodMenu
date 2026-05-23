package com.kalashnikovprojects.ufmtv.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed interface Events {
    @SerialName("toggle_food_event")
    @Serializable
    data class ToggleFoodEvent(val id: Int, val inStock: Boolean) : Events
    @SerialName("add_food_event")
    @Serializable
    data class AddFoodEvent(val element: FoodItemDTO) : Events
    @SerialName("change_food_event")
    @Serializable
    data class ChangeFoodEvent(val id: Int, val element: FoodItemDTO) : Events
    @SerialName("delete_food_event")
    @Serializable
    data class DeleteFoodEvent(val id: Int) : Events

    @SerialName("add_category_event")
    @Serializable
    data class AddCategoryEvent(val element: CategoryDTO) : Events
    @SerialName(value = "change_category_event")
    @Serializable
    data class ChangeCategoryEvent(val id: Int, val element: CategoryDTO) : Events
    @SerialName("delete_category_event")
    @Serializable
    data class DeleteCategoryEvent(val id: Int) : Events

    @SerialName("add_image_event")
    @Serializable
    data class AddImageEvent(val element: ImageItemDTO) : Events
    //    @SerialName(value = "change_image_event")
//    @Serializable
//    data class ChangeImageEvent(val id: Int, val element: ImageItemDTO) : Events
    @SerialName("delete_image_event")
    @Serializable
    data class DeleteImageEvent(val id: Int) : Events

    @SerialName("add_text_event")
    @Serializable
    data class AddTextEvent(val element: TextItemDTO) : Events
    @SerialName(value = "change_text_event")
    @Serializable
    data class ChangeTextEvent(val id: Int, val element: TextItemDTO) : Events
    @SerialName("delete_text_event")
    @Serializable
    data class DeleteTextEvent(val id: Int) : Events

    @SerialName("add_design_event")
    @Serializable
    data class AddDesignEvent(val element: DesignItemDTO) : Events
    @SerialName("change_design_event")
    @Serializable
    data class ChangeDesignEvent(val id: Int, val element: DesignItemDTO) : Events
    @SerialName("delete_design_event")
    @Serializable
    data class DeleteDesignEvent(val id: Int) : Events

    @SerialName("set_category_items_event")
    @Serializable
    data class SetCategoryItems(val categoryId: Int, val foodItems: List<FoodItemDTO>) : Events

    @SerialName("set_food_categories_event")
    @Serializable
    data class SetFoodCategories(val foodId: Int, val categories: List<CategoryDTO>) : Events

    @SerialName("add_screen_event")
    @Serializable
    data class AddScreenEvent(val element: TVScreenDTO) : Events

    @SerialName("change_screen_event")
    @Serializable
    data class ChangeScreenEvent(val id: Int, val element: TVScreenDTO) : Events

    @SerialName("logout_screen_event")
    @Serializable
    data class LogoutScreenEvent(val id: Int) : Events

    @SerialName("reload_design_items_by_screen_id")
    @Serializable
    data class ReloadDesignItemsByScreenId(val screenId: Int, val items: List<DesignItemDTO>) : Events

    @SerialName("reload_screen")
    @Serializable
    data class ReloadScreen(val screenId: Int, val screen: TVScreenDTO) : Events

    @SerialName("reload_design_items_with_screen_id")
    @Serializable
    data class ReloadDesignItemsWithScreenId(val items: List<DesignItemWithScreenIdDTO>) : Events

    @SerialName("reload_food_items")
    @Serializable
    data class ReloadFoodItems(val items: List<FoodItemDTO>) : Events

    @SerialName("reload_CategoryDTO_items")
    @Serializable
    data class ReloadCategoryItems(val items: List<CategoryDTO>) : Events

    @SerialName("reload_text_items")
    @Serializable
    data class ReloadTextItems(val items: List<TextItemDTO>) : Events

    @SerialName("reload_image_items")
    @Serializable
    data class ReloadImageItems(val items: List<ImageItemDTO>) : Events

    @SerialName("reload_screens")
    @Serializable
    data class ReloadScreens(val items: List<TVScreenDTO>) : Events
}