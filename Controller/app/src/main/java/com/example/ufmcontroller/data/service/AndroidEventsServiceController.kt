package com.example.ufmcontroller.data.service

import android.app.UiModeManager
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Build
import android.util.Log
import com.example.ufmcontroller.data.local.LocalDataSource
import com.example.ufmcontroller.data.model.EventsDTO
import com.example.ufmcontroller.data.model.toEntity
import com.example.ufmcontroller.data.remote.EventsWebSocketService
import com.example.ufmcontroller.domain.entity.Category
import com.example.ufmcontroller.domain.entity.FoodItem
import com.example.ufmcontroller.domain.repository.EventsServiceRepository
import com.example.ufmcontroller.services.EventsForegroundService
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class AndroidEventsServiceController @Inject constructor(
    @ApplicationContext private val context: Context,
    private val dataSource: LocalDataSource,
    private val eventsWebSocketService: EventsWebSocketService,
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
                            dataSource.updateFoodItem(event.element.id, event.element.toEntity())
                        }
                        is EventsDTO.ChangeFoodEvent -> {
                            dataSource.updateFoodItem(event.element.id, event.element.toEntity())
                        }
                        is EventsDTO.ToggleFoodEvent -> {
                            dataSource.toggleFoodInStock(event.id, event.inStock)
                        }
                        is EventsDTO.ToggleCategoryEvent -> {
                            dataSource.toggleCategoryInStock(event.id, event.inStock)
                        }
                        is EventsDTO.DeleteFoodEvent -> {
                            dataSource.deleteFoodItem(event.id)
                        }
                        is EventsDTO.SetFoodCategories -> {
                            dataSource.setFoodCategories(event.foodId, event.categoriesIds)
                        }
                        is EventsDTO.AddCategoryEvent -> {
                            dataSource.updateCategory(event.element.id, event.element.toEntity().category)
                        }
                        is EventsDTO.ChangeCategoryEvent -> {
                            dataSource.updateCategory(event.id, event.element.toEntity().category)
                        }
                        is EventsDTO.DeleteCategoryEvent -> {
                            dataSource.deleteCategory(event.id)
                        }
                        is EventsDTO.SetCategoryItems -> {
                            dataSource.setCategoryItems(event.categoryId, event.foodItemsIds)
                        }
                        is EventsDTO.AddDesignEvent -> {
                            dataSource.updateDesignItem(event.element.id, event.element.toEntity())
                        }
                        is EventsDTO.ChangeDesignEvent -> {
                            dataSource.updateDesignItem(event.id, event.element.toEntity())
                        }
                        is EventsDTO.DeleteDesignEvent -> {
                            dataSource.deleteDesignItem(event.id)
                        }
                        is EventsDTO.ChangeTextEvent -> {
                            dataSource.updateTextItemInDesign(event.id, event.element.toEntity())
                        }
                        is EventsDTO.ChangeScreenEvent -> {
                            dataSource.updateScreen(event.id, event.element.toEntity())
                        }
                        is EventsDTO.AddScreenEvent -> {
                            dataSource.updateScreen(event.element.id, event.element.toEntity())
                        }
                        is EventsDTO.LogoutScreenEvent -> {
                            dataSource.deleteScreen(event.id)
                        }
                        is EventsDTO.ReloadScreens -> {
                            val screens = event.items.map { it.toEntity() }.associateBy { it.id }
                            dataSource.reloadScreens(screens)
                        }
                        is EventsDTO.ReloadDesignItemsWithScreenId -> {
                            val items = event.items.map { it.toEntity() }.associateBy { it.id }
                            dataSource.reloadDesignItems(items)
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

                            dataSource.reloadAllCategorizedItems(categories, foodItems, relations)
                        }
                        is EventsDTO.DeleteTextEvent -> {
                            dataSource.deleteTextItem(event.id)
                        }
                        is EventsDTO.DeleteImageEvent -> {
                            dataSource.deleteImageItem(event.id)
                        }
                        is EventsDTO.ReloadDesignItemsByScreenId,
                        is EventsDTO.ReloadScreen,
                        is EventsDTO.AddTextEvent,
                        is EventsDTO.AddImageEvent -> {}
                    }
                }
            }

            launch {
                eventsWebSocketService.logoutEvent.collect {
                    Log.d("UFM", "Logout event")
                    stopService()
                }
            }
        }
    }

    override fun stopService() {
        context.stopService(intent)
        eventsJob?.cancel()
        eventsJob = null
    }
}