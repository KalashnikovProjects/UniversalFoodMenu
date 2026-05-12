package com.kalashnikovprojects.ufmserver.adapters.eventbus

import com.kalashnikovprojects.ufmserver.models.Events
import kotlinx.coroutines.flow.MutableSharedFlow
import java.util.concurrent.ConcurrentHashMap


class EventBus() {
    val userFlows = ConcurrentHashMap<Int, MutableSharedFlow<Events>>()

    fun getFlow(userId: Int): MutableSharedFlow<Events> {
        return userFlows.getOrPut(userId) {
            MutableSharedFlow(extraBufferCapacity = 64)
        }
    }
}