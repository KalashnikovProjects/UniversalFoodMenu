package com.kalashnikovprojects.ufmserver.adapters.eventbus

import com.kalashnikovprojects.ufmserver.dto.Event
import kotlinx.coroutines.flow.MutableSharedFlow
import java.util.concurrent.ConcurrentHashMap


class EventBus() {
    val userFlows = ConcurrentHashMap<Int, MutableSharedFlow<Event>>()

    fun getFlow(userId: Int): MutableSharedFlow<Event> {
        return userFlows.getOrPut(userId) {
            MutableSharedFlow(extraBufferCapacity = 64)
        }
    }
}