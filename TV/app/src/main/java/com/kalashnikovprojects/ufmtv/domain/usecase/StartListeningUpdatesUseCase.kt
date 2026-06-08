package com.kalashnikovprojects.ufmtv.domain.usecase

import com.kalashnikovprojects.ufmtv.domain.repository.EventsServiceRepository
import javax.inject.Inject

class StartListeningUpdatesUseCase @Inject constructor(
    private val eventsServiceRepository: EventsServiceRepository
) {
    operator fun invoke() {
        eventsServiceRepository.startService()
    }
}