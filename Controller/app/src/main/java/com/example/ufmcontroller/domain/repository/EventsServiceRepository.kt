package com.example.ufmcontroller.domain.repository

import kotlinx.coroutines.flow.SharedFlow

interface EventsServiceRepository {
    fun startService()
    fun stopService()
}