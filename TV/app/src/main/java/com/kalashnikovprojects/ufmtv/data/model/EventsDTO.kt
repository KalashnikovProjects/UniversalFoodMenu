package com.kalashnikovprojects.ufmtv.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed interface EventsDTO {
    @SerialName("toggle_food_event")
    @Serializable
    data class ToggleFoodEvent(val id: Int, val inStock: Boolean) : EventsDTO
    @SerialName("add_food_event")
    @Serializable
    data class AddFoodEvent(val element: FoodItemDTO) : EventsDTO
    @SerialName("change_food_event")
    @Serializable
    data class ChangeFoodEvent(val id: Int, val element: FoodItemDTO) : EventsDTO
    @SerialName("delete_food_event")
    @Serializable
    data class DeleteFoodEvent(val id: Int) : EventsDTO


    @SerialName("toggle_category_event")
    @Serializable
    data class ToggleCategoryEvent(val id: Int, val inStock: Boolean) : EventsDTO
    @SerialName("add_category_event")
    @Serializable
    data class AddCategoryEvent(val element: CategoryDTO) : EventsDTO
    @SerialName(value = "change_category_event")
    @Serializable
    data class ChangeCategoryEvent(val id: Int, val element: CategoryDTO) : EventsDTO
    @SerialName("delete_category_event")
    @Serializable
    data class DeleteCategoryEvent(val id: Int) : EventsDTO

    @SerialName("add_image_event")
    @Serializable
    data class AddImageEvent(val element: ImageItemDTO) : EventsDTO
    //    @SerialName(value = "change_image_event")
//    @Serializable
//    data class ChangeImageEvent(val id: Int, val element: ImageItemDTO) : Events
    @SerialName("delete_image_event")
    @Serializable
    data class DeleteImageEvent(val id: Int) : EventsDTO

    @SerialName("add_text_event")
    @Serializable
    data class AddTextEvent(val element: TextItemDTO) : EventsDTO
    @SerialName(value = "change_text_event")
    @Serializable
    data class ChangeTextEvent(val id: Int, val element: TextItemDTO) : EventsDTO
    @SerialName("delete_text_event")
    @Serializable
    data class DeleteTextEvent(val id: Int) : EventsDTO

    @SerialName("add_design_event")
    @Serializable
    data class AddDesignEvent(val element: DesignItemWithScreenIdDTO) : EventsDTO
    @SerialName("change_design_event")
    @Serializable
    data class ChangeDesignEvent(val id: Int, val element: DesignItemWithScreenIdDTO) : EventsDTO
    @SerialName("delete_design_event")
    @Serializable
    data class DeleteDesignEvent(val id: Int) : EventsDTO

    @SerialName("set_category_items_event")
    @Serializable
    data class SetCategoryItems(val categoryId: Int, val foodItemsIds: List<Int>) : EventsDTO

    @SerialName("set_food_categories_event")
    @Serializable
    data class SetFoodCategories(val foodId: Int, val categoriesIds: List<Int>) : EventsDTO

    @SerialName("add_screen_event")
    @Serializable
    data class AddScreenEvent(val element: TVScreenDTO) : EventsDTO

    @SerialName("change_screen_event")
    @Serializable
    data class ChangeScreenEvent(val id: Int, val element: TVScreenDTO) : EventsDTO

    @SerialName("logout_screen_event")
    @Serializable
    data class LogoutScreenEvent(val id: Int) : EventsDTO

    @SerialName("reload_design_items_by_screen_id")
    @Serializable
    data class ReloadDesignItemsByScreenId(val screenId: Int, val items: List<DesignItemDTO>) : EventsDTO

    @SerialName("reload_screen")
    @Serializable
    data class ReloadScreen(val screenId: Int, val screen: TVScreenDTO) : EventsDTO

    @SerialName("reload_design_items_with_screen_id")
    @Serializable
    data class ReloadDesignItemsWithScreenId(val items: List<DesignItemWithScreenIdDTO>) : EventsDTO

    @SerialName("reload_categorized_food_items")
    @Serializable
    data class ReloadCategorizedFoodItems(val c: FoodItemsCategorizedDTO) : EventsDTO

    @SerialName("reload_screens")
    @Serializable
    data class ReloadScreens(val items: List<TVScreenDTO>) : EventsDTO
}