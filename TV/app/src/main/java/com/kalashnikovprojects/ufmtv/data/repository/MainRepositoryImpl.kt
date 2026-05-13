package com.kalashnikovprojects.ufmtv.data.repository

import com.kalashnikovprojects.ufmtv.data.local.MainDataSource
import com.kalashnikovprojects.ufmtv.data.model.Events
import com.kalashnikovprojects.ufmtv.data.remote.EventsWebSocketService
import com.kalashnikovprojects.ufmtv.domain.model.DesignItem
import com.kalashnikovprojects.ufmtv.domain.model.ScreenStyle
import com.kalashnikovprojects.ufmtv.domain.repository.MainRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainRepositoryImpl @Inject constructor(
    private val dataSource: MainDataSource,
    private val eventsWebSocketService: EventsWebSocketService,
    private val externalScope: CoroutineScope
) : MainRepository {

    init {
        observeWebSocketEvents()
    }

    override fun getDesignItems(): Flow<List<DesignItem>> {
        return dataSource.designItems
    }

    override fun getScreenStyle(): Flow<ScreenStyle> {
        return dataSource.screenStyle
    }

    private fun observeWebSocketEvents() {
        externalScope.launch {
            eventsWebSocketService.events.collect { event ->
                when (event) {
                    is Events.AddCategoryEvent -> TODO()
                    is Events.AddDesignEvent -> TODO()
                    is Events.AddFoodEvent -> TODO()
                    is Events.AddImageEvent -> TODO()
                    is Events.AddScreenEvent -> TODO()
                    is Events.AddTextEvent -> TODO()
                    is Events.ChangeCategoryEvent -> TODO()
                    is Events.ChangeDesignEvent -> TODO()
                    is Events.ChangeFoodEvent -> TODO()
                    is Events.ChangeScreenEvent -> TODO()
                    is Events.ChangeTextEvent -> TODO()
                    is Events.DeleteCategoryEvent -> TODO()
                    is Events.DeleteDesignEvent -> TODO()
                    is Events.DeleteFoodEvent -> TODO()
                    is Events.DeleteImageEvent -> TODO()
                    is Events.DeleteScreenEvent -> TODO()
                    is Events.DeleteTextEvent -> TODO()
                    is Events.SetCategoryItems -> TODO()
                    is Events.SetFoodCategories -> TODO()
                    is Events.ToggleFoodEvent -> TODO()

                    is Events.ReloadDesignItemsByScreenId -> {

                    }

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
}