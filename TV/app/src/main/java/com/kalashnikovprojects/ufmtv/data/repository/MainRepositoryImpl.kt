package com.kalashnikovprojects.ufmtv.data.repository

import com.kalashnikovprojects.ufmtv.data.local.MainDataSource
import com.kalashnikovprojects.ufmtv.data.local.UserPreferencesDataSource
import com.kalashnikovprojects.ufmtv.data.model.Events
import com.kalashnikovprojects.ufmtv.data.model.toEntity
import com.kalashnikovprojects.ufmtv.data.remote.EventsWebSocketService
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
                    is Events.AddFoodEvent -> {
                        dataSource.putFood(event.element.toEntity())
                    }
                    is Events.ChangeFoodEvent -> {
                        dataSource.putFood(event.element.toEntity())
                    }
                    is Events.ToggleFoodEvent -> {
                        dataSource.foodItems.value.find { it.id == event.id }?.let {
                            dataSource.putFood(it.copy(inStock = event.inStock))
                        }
                    }
                    is Events.DeleteFoodEvent -> {
                        dataSource.deleteFood(event.id)
                    }
                    is Events.SetFoodCategories -> {
                        dataSource.updateFoodRelationsForCategories(event.foodId, event.categories.map { it.id })
                    }
                    is Events.ReloadFoodItems -> {
                        event.items.forEach { dataSource.putFood(it.toEntity()) }
                    }

                    is Events.AddCategoryEvent -> {
                        dataSource.putCategory(event.element.toEntity().category)
                    }
                    is Events.ChangeCategoryEvent -> {
                        dataSource.putCategory(event.element.toEntity().category)
                    }
                    is Events.DeleteCategoryEvent -> {
                        dataSource.deleteCategory(event.id)
                    }
                    is Events.SetCategoryItems -> {
                        dataSource.setCategoryFoodRelations(event.categoryId, event.foodItems.map { it.id })
                        event.foodItems.forEach { dataSource.putFood(it.toEntity()) }
                    }
                    is Events.ReloadCategoryItems -> {
                        event.items.forEach { dataSource.putCategory(it.toEntity().category) }
                    }

                    is Events.AddDesignEvent -> {
                        dataSource.addDesignItemRaw(event.element.toEntity())
                    }
                    is Events.ChangeDesignEvent -> {
                        dataSource.putDesignItemRaw(event.element.toEntity())
                    }
                    is Events.DeleteDesignEvent -> {
                        dataSource.deleteDesignItemRaw(event.id)
                    }
                    is Events.ReloadDesignItemsByScreenId -> {
                        dataSource.updateDesignItemsRaw(event.items.map { it.toEntity() })
                    }

                    is Events.ChangeTextEvent -> {
                        dataSource.updateDesignItemElement(event.id) { event.element.toEntity() }
                    }
                    is Events.DeleteTextEvent -> {
                        dataSource.updateDesignItemElement(event.id) { current ->
                            if (current is TextItem) Unit else current
                        }
                    }
                    is Events.DeleteImageEvent -> {
                        dataSource.updateDesignItemElement(event.id) { current ->
                            if (current is ImageItem) Unit else current
                        }
                    }
                    is Events.ChangeScreenEvent -> {
                        if (event.id == userPreferencesDataSource.screenId.value) {
                            dataSource.updateCurrentScreen(event.element.toEntity())
                        }
                    }
                    is Events.ReloadScreen -> {
                        dataSource.updateCurrentScreen(event.screen.toEntity())
                    }


                    is Events.LogoutScreenEvent -> {
                        if (event.id == userPreferencesDataSource.screenId.value) {
                            userPreferencesDataSource.clearAuthToken()
                            userPreferencesDataSource.clearScreenId()
                            _logoutEvent.emit(Unit)
                            eventsWebSocketService.disconnect()
                        }
                    }

                    is Events.AddTextEvent -> {}
                    is Events.ReloadTextItems -> {}
                    is Events.AddImageEvent -> {}
                    is Events.ReloadImageItems -> {}
                    is Events.AddScreenEvent -> {}
                    is Events.ReloadScreens -> {}
                    is Events.ReloadDesignItemsWithScreenId -> {}
                }
            }
        }
    }

    override suspend fun disconnect() {
        eventsWebSocketService.disconnect()
    }
}