package com.example.ufmcontroller.domain.repository

import kotlinx.coroutines.flow.SharedFlow

interface EventsServiceRepository {
    fun getLogoutEvent(): SharedFlow<Unit>
    fun startService()
    fun stopService()
}