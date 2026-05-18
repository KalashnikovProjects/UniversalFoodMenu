package com.kalashnikovprojects.ufmtv.data.repository

import com.kalashnikovprojects.ufmtv.data.local.MainDataSource
import com.kalashnikovprojects.ufmtv.data.local.UserPreferencesDataSource
import com.kalashnikovprojects.ufmtv.data.model.Events
import com.kalashnikovprojects.ufmtv.data.model.toEntity
import com.kalashnikovprojects.ufmtv.data.remote.EventsWebSocketService
import com.kalashnikovprojects.ufmtv.domain.entity.CategoryWithFoodItems
import com.kalashnikovprojects.ufmtv.domain.entity.DesignItem
import com.kalashnikovprojects.ufmtv.domain.entity.FoodItem
import com.kalashnikovprojects.ufmtv.domain.entity.ImageItem
import com.kalashnikovprojects.ufmtv.domain.entity.ScreenStyle
import com.kalashnikovprojects.ufmtv.domain.entity.TextItem
import com.kalashnikovprojects.ufmtv.domain.repository.MainRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MainRepositoryImpl @Inject constructor(
    private val dataSource: MainDataSource,
    private val eventsWebSocketService: EventsWebSocketService,
    private val userPreferencesDataSource: UserPreferencesDataSource,
    private val externalScope: CoroutineScope
) : MainRepository {
    private val _logoutEvent = eventsWebSocketService.logoutEvent

    override fun getDesignItems(): Flow<List<DesignItem>> {
        return dataSource.designItems
    }

    override fun getScreenStyle(): Flow<ScreenStyle> {
        return dataSource.currentScreen.map { it.style }
    }

    override fun getLogoutEvent(): SharedFlow<Unit> {
        return _logoutEvent.asSharedFlow()
    }

    override fun observeEvents() {
        externalScope.launch {
            eventsWebSocketService.events.collect { event ->
                when (event) {
                    is Events.AddDesignEvent -> {
                        dataSource.updateDesignItemsList(
                            dataSource.designItems.value + listOf(event.element.toEntity())
                        )
                    }
                    is Events.AddFoodEvent -> {
                        dataSource.updateFoodItems(
                            dataSource.foodItems.value + listOf(event.element.toEntity())
                        )
                    }
                    is Events.ChangeCategoryEvent -> {
                        dataSource.updateDesignItemsList(
                            dataSource.designItems.value.map {
                                if (it.element is CategoryWithFoodItems && it.element.category.id == event.id)
                                    it.copy(element = it.element.copy(event.element.toEntity().category))
                                else it
                            }
                        )
                    }
                    is Events.ChangeDesignEvent -> {
                        dataSource.updateDesignItemsList(
                            dataSource.designItems.value.map {
                                if (it.id == event.id) event.element.toEntity() else it
                            }
                        )
                    }
                    is Events.ChangeFoodEvent -> {
                        dataSource.updateDesignItemsList(
                            dataSource.designItems.value.map {
                                if (it.element is FoodItem && it.element.id == event.id)
                                    it.copy(element = event.element.toEntity())
                                else it
                            }
                        )
                        dataSource.updateFoodItems(
                            dataSource.foodItems.value.map {
                                if (it.id == event.id)
                                    event.element.toEntity()
                                else it
                            }
                        )
                    }
                    is Events.ChangeScreenEvent -> {
                        if (event.id == userPreferencesDataSource.screenId.value) {
                            dataSource.updateCurrentScreen(event.element.toEntity())
                        }
                    }
                    is Events.ChangeTextEvent -> {
                        dataSource.updateDesignItemsList(
                            dataSource.designItems.value.map {
                                if (it.element is TextItem && it.element.id == event.id)
                                    it.copy(element = event.element.toEntity())
                                else it
                            }
                        )
                    }
                    is Events.DeleteCategoryEvent -> {
                        dataSource.updateDesignItemsList(
                            dataSource.designItems.value.filter {
                                it.element !is CategoryWithFoodItems || it.element.category.id != event.id
                            }
                        )
                    }
                    is Events.DeleteDesignEvent -> {
                        dataSource.updateDesignItemsList(
                            dataSource.designItems.value.filter { it.id != event.id }
                        )
                    }
                    is Events.DeleteFoodEvent -> {
                        dataSource.updateDesignItemsList(
                            dataSource.designItems.value.filter {
                                it.element !is FoodItem || it.element.id != event.id
                            }
                        )
                        dataSource.updateFoodItems(
                            dataSource.foodItems.value.filter {
                                it.id != event.id
                            }
                        )
                    }
                    is Events.DeleteImageEvent -> {
                        dataSource.updateDesignItemsList(
                            dataSource.designItems.value.filter {
                                it.element !is ImageItem || it.element.id != event.id
                            }
                        )
                    }
                    is Events.DeleteTextEvent -> {
                        dataSource.updateDesignItemsList(
                            dataSource.designItems.value.filter {
                                it.element !is TextItem || it.element.id != event.id
                            }
                        )
                    }
                    is Events.LogoutScreenEvent -> {
                        if (event.id == userPreferencesDataSource.screenId.value) {
                            userPreferencesDataSource.clearAuthToken()
                            userPreferencesDataSource.clearScreenId()
                            _logoutEvent.emit(Unit)
                            eventsWebSocketService.disconnect()
                        }
                    }
                    is Events.SetCategoryItems -> {
                        dataSource.updateDesignItemsList(
                            dataSource.designItems.value.map {
                                if (it.element is CategoryWithFoodItems && it.element.category.id == event.categoryId)
                                    it.copy(element = it.element.copy(foodItems =
                                        event.foodItems.map { foodItem -> foodItem.toEntity() })
                                    )
                                else it
                            }
                        )
                    }
                    is Events.SetFoodCategories -> {
                        dataSource.updateDesignItemsList(
                            dataSource.designItems.value.map {
                                if (it.element is CategoryWithFoodItems) {
                                    if (event.categories.map { category -> category.id }.contains(it.element.category.id)) {
                                        if (it.element.foodItems.map { fd -> fd.id }.contains(event.foodId)) {
                                            it
                                        } else {
                                            it.copy(element = it.element.copy(foodItems =
                                                it.element.foodItems + dataSource.foodItems.value.filter {
                                                    foodItem -> foodItem.id == event.foodId
                                                }
                                            ))
                                        }
                                    } else {
                                        it.copy(element = it.element.copy(foodItems =
                                            it.element.foodItems.filter { item -> item.id != event.foodId }
                                        ))
                                    }
                                } else it
                            }
                        )
                    }
                    is Events.ToggleFoodEvent -> {
                        dataSource.updateDesignItemsList(
                            dataSource.designItems.value.map {
                                if (it.element is FoodItem && it.element.id == event.id)
                                    it.copy(element=it.element.copy(inStock = event.inStock))
                                else it
                            }
                        )
                    }
                    is Events.ReloadScreen -> {
                        dataSource.updateCurrentScreen(event.screen.toEntity())
                    }

                    is Events.ReloadDesignItemsByScreenId -> {
                        dataSource.updateDesignItemsList(event.items.map { it.toEntity() })
                    }

                    // Events not for tv
                    is Events.AddCategoryEvent -> {}
                    is Events.AddImageEvent -> {}
                    is Events.AddScreenEvent -> {}
                    is Events.AddTextEvent -> {}
                    is Events.ReloadCategoryItems -> {}
                    is Events.ReloadDesignItemsWithScreenId -> {}
                    is Events.ReloadFoodItems -> {}
                    is Events.ReloadImageItems -> {}
                    is Events.ReloadScreens -> {}
                    is Events.ReloadTextItem -> {}
                }
            }
        }
    }

    override suspend fun disconnect() {
        eventsWebSocketService.disconnect()
    }
}