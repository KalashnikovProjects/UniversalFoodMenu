package com.kalashnikovprojects.ufmtv.data.repository

import com.kalashnikovprojects.ufmtv.data.local.LocalDataSource
import com.kalashnikovprojects.ufmtv.data.remote.EventsWebSocketService
import com.kalashnikovprojects.ufmtv.domain.entity.DesignItem
import com.kalashnikovprojects.ufmtv.domain.entity.ScreenStyle
import com.kalashnikovprojects.ufmtv.domain.repository.MainRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MainRepositoryImpl @Inject constructor(
    private val dataSource: LocalDataSource,
    private val eventsWebSocketService: EventsWebSocketService,
) : MainRepository {
    override fun getLogoutEvent(): SharedFlow<Unit> {
        return eventsWebSocketService.logoutEvent
    }

    override fun getDesignItems(): Flow<List<DesignItem>> {
        return dataSource.designItems
    }

    override fun getScreenStyle(): Flow<ScreenStyle> {
        return dataSource.getScreenStyle()
    }
}