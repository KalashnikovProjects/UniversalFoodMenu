package com.kalashnikovprojects.ufmtv.data.service

import android.app.UiModeManager
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Build
import android.util.Log
import com.kalashnikovprojects.ufmtv.data.local.LocalDataSource
import com.kalashnikovprojects.ufmtv.data.local.UserPreferencesDataSource
import com.kalashnikovprojects.ufmtv.data.model.EventsDTO
import com.kalashnikovprojects.ufmtv.data.model.toDesignItemEntity
import com.kalashnikovprojects.ufmtv.data.model.toEntity
import com.kalashnikovprojects.ufmtv.data.remote.EventsWebSocketService
import com.kalashnikovprojects.ufmtv.domain.entity.Category
import com.kalashnikovprojects.ufmtv.domain.entity.FoodItem
import com.kalashnikovprojects.ufmtv.domain.entity.defaultTVScreen
import com.kalashnikovprojects.ufmtv.domain.repository.EventsServiceRepository
import com.kalashnikovprojects.ufmtv.services.EventsForegroundService
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

class AndroidEventsServiceController @Inject constructor(
    @ApplicationContext private val context: Context,
    private val localDataSource: LocalDataSource,
    private val eventsWebSocketService: EventsWebSocketService,
    private val userPreferencesDataSource: UserPreferencesDataSource,
    private val externalScope: CoroutineScope
) : EventsServiceRepository {

    private val intent = Intent(context, EventsForegroundService::class.java)
    private var eventsJob: Job? = null

    override fun startService() {
        Log.d("UFM", "create events service controller")

        val uiModeManager = context.getSystemService(Context.UI_MODE_SERVICE) as UiModeManager
        val isTv = uiModeManager.currentModeType == Configuration.UI_MODE_TYPE_TELEVISION

        if (isTv) {
            context.startService(intent)
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(intent)
            } else {
                context.startService(intent)
            }
        }
        if (eventsJob == null || eventsJob?.isCancelled == true) {
            observeEvents()
        }
    }

    private fun observeEvents() {
        eventsJob = externalScope.launch {
            launch {
                eventsWebSocketService.events.collect { event ->
                    when (event) {
                        is EventsDTO.AddFoodEvent -> {
                            localDataSource.updateFoodItem(event.element.id, event.element.toEntity())
                        }
                        is EventsDTO.ChangeFoodEvent -> {
                            localDataSource.updateFoodItem(event.element.id, event.element.toEntity())
                        }
                        is EventsDTO.ToggleFoodEvent -> {
                            localDataSource.toggleFoodInStock(event.id, event.inStock)
                        }
                        is EventsDTO.ToggleCategoryEvent -> {
                            localDataSource.toggleCategoryInStock(event.id, event.inStock)
                        }
                        is EventsDTO.DeleteFoodEvent -> {
                            localDataSource.deleteFoodItem(event.id)
                        }
                        is EventsDTO.SetFoodCategories -> {
                            localDataSource.setFoodCategories(event.foodId, event.categoriesIds)
                        }
                        is EventsDTO.AddCategoryEvent -> {
                            localDataSource.updateCategory(event.element.id, event.element.toEntity().category)
                        }
                        is EventsDTO.ChangeCategoryEvent -> {
                            localDataSource.updateCategory(event.id, event.element.toEntity().category)
                        }
                        is EventsDTO.DeleteCategoryEvent -> {
                            localDataSource.deleteCategory(event.id)
                        }
                        is EventsDTO.SetCategoryItems -> {
                            localDataSource.setCategoryItems(event.categoryId, event.foodItemsIds)
                        }
                        is EventsDTO.AddDesignEvent -> {
                            if (event.element.screenId == userPreferencesDataSource.screenId.value) {
                                localDataSource.updateDesignItem(event.element.id, event.element.toDesignItemEntity())
                            }
                        }
                        is EventsDTO.ChangeDesignEvent -> {
                            if (event.element.screenId == userPreferencesDataSource.screenId.value) {
                                localDataSource.updateDesignItem(
                                    event.id,
                                    event.element.toDesignItemEntity()
                                )
                            }
                        }
                        is EventsDTO.DeleteDesignEvent -> {
                            localDataSource.deleteDesignItem(event.id)
                        }
                        is EventsDTO.ChangeTextEvent -> {
                            localDataSource.updateTextItemInDesign(event.id, event.element.toEntity())
                        }
                        is EventsDTO.ChangeScreenEvent -> {
                            if (userPreferencesDataSource.screenId.value == event.id) {
                                localDataSource.updateCurrentScreen(event.element.toEntity())
                            }
                        }
                        is EventsDTO.LogoutScreenEvent -> {
                            if (event.id == userPreferencesDataSource.screenId.value) {
                                localDataSource.updateCurrentScreen(defaultTVScreen())
                                userPreferencesDataSource.clearAuthToken()
                                userPreferencesDataSource.clearScreenId()
                                eventsWebSocketService.logout()
                                stopService()
                            }
                        }
                        is EventsDTO.ReloadDesignItemsByScreenId -> {
                            val items = event.items.map { it.toEntity() }.associateBy { it.id }
                            localDataSource.reloadDesignItems(items)
                        }
                        is EventsDTO.ReloadCategorizedFoodItems -> {
                            val foodItems = mutableMapOf<Int, FoodItem>()
                            val categories = mutableMapOf<Int, Category>()
                            val relations = mutableListOf<Pair<Int, Int>>()

                            event.c.categories.forEach { categorized ->
                                categories[categorized.category.id] = categorized.category.toEntity().category
                                categorized.foodItems.forEach { food ->
                                    foodItems[food.id] = food.toEntity()
                                    relations.add(Pair(categorized.category.id, food.id))
                                }
                            }
                            event.c.noCategoryFoodItems.forEach { food ->
                                foodItems[food.id] = food.toEntity()
                            }

                            localDataSource.reloadAllCategorizedItems(categories, foodItems, relations)
                        }
                        is EventsDTO.ReloadScreen -> {
                            localDataSource.updateCurrentScreen(event.screen.toEntity())
                        }
                        is EventsDTO.DeleteTextEvent -> {
                            localDataSource.deleteTextItem(event.id)
                        }
                        is EventsDTO.DeleteImageEvent -> {
                            localDataSource.deleteImageItem(event.id)
                        }
                        is EventsDTO.ReloadScreens,
                        is EventsDTO.AddScreenEvent,
                        is EventsDTO.ReloadDesignItemsWithScreenId,
                        is EventsDTO.AddTextEvent,
                        is EventsDTO.AddImageEvent -> {}
                    }
                }
            }
        }
    }

    override fun stopService() {
        context.stopService(intent)
    }
}