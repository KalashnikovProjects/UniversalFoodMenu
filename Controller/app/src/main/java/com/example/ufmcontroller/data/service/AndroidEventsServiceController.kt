package com.example.ufmcontroller.data.service

import android.app.UiModeManager
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Build
import android.util.Log
import com.example.ufmcontroller.data.local.LocalDataSource
import com.example.ufmcontroller.data.model.Events
import com.example.ufmcontroller.data.model.toEntity
import com.example.ufmcontroller.data.remote.EventsWebSocketService
import com.example.ufmcontroller.domain.entity.Category
import com.example.ufmcontroller.domain.entity.FoodItem
import com.example.ufmcontroller.domain.entity.TextItem
import com.example.ufmcontroller.domain.repository.EventsServiceRepository
import com.example.ufmcontroller.services.EventsForegroundService
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.update
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

    override fun getLogoutEvent(): SharedFlow<Unit> {
        return eventsWebSocketService.logoutEvent
    }

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
                        is Events.AddFoodEvent -> {
                            dataSource.updateFoodItem(event.element.id, event.element.toEntity())
                        }
                        is Events.ChangeFoodEvent -> {
                            dataSource.updateFoodItem(event.element.id, event.element.toEntity())
                        }
                        is Events.ToggleFoodEvent -> {
                            dataSource.toggleFoodInStock(event.id, event.inStock)
                        }
                        is Events.ToggleCategoryEvent -> {
                            dataSource.toggleCategoryInStock(event.id, event.inStock)
                        }
                        is Events.DeleteFoodEvent -> {
                            dataSource.deleteFoodItem(event.id)
                        }
                        is Events.SetFoodCategories -> {
                            dataSource.setFoodCategories(event.foodId, event.categoriesIds)
                        }
                        is Events.AddCategoryEvent -> {
                            dataSource.updateCategory(event.element.id, event.element.toEntity().category)
                        }
                        is Events.ChangeCategoryEvent -> {
                            dataSource.updateCategory(event.id, event.element.toEntity().category)
                        }
                        is Events.DeleteCategoryEvent -> {
                            dataSource.deleteCategory(event.id)
                        }
                        is Events.SetCategoryItems -> {
                            dataSource.setCategoryItems(event.categoryId, event.foodItemsIds)
                        }
                        is Events.AddDesignEvent -> {
                            dataSource.updateDesignItem(event.element.id, event.element.toEntity())
                        }
                        is Events.ChangeDesignEvent -> {
                            dataSource.updateDesignItem(event.id, event.element.toEntity())
                        }
                        is Events.DeleteDesignEvent -> {
                            dataSource.deleteDesignItem(event.id)
                        }
                        is Events.ChangeTextEvent -> {
                            dataSource.updateTextItemInDesign(event.id, event.element.toEntity())
                        }
                        is Events.ChangeScreenEvent -> {
                            dataSource.updateScreen(event.id, event.element.toEntity())
                        }
                        is Events.AddScreenEvent -> {
                            dataSource.updateScreen(event.element.id, event.element.toEntity())
                        }
                        is Events.LogoutScreenEvent -> {
                            dataSource.deleteScreen(event.id)
                        }
                        is Events.ReloadScreens -> {
                            val screens = event.items.map { it.toEntity() }.associateBy { it.id }
                            dataSource.reloadScreens(screens)
                        }
                        is Events.ReloadDesignItemsWithScreenId -> {
                            val items = event.items.map { it.toEntity() }.associateBy { it.id }
                            dataSource.reloadDesignItems(items)
                        }
                        is Events.ReloadCategorizedFoodItems -> {
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
                        is Events.ReloadDesignItemsByScreenId,
                        is Events.DeleteTextEvent,
                        is Events.DeleteImageEvent,
                        is Events.ReloadScreen,
                        is Events.ReloadScreens,
                        is Events.AddTextEvent,
                        is Events.AddImageEvent -> {}
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