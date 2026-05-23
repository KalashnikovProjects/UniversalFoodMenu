package com.kalashnikovprojects.ufmtv.domain.usecase

import com.kalashnikovprojects.ufmtv.domain.repository.EventsServiceRepository
import com.kalashnikovprojects.ufmtv.domain.repository.MainRepository
import javax.inject.Inject

class StartListeningUpdatesUseCase @Inject constructor(
    private val mainRepository: MainRepository,
    private val eventsServiceRepository: EventsServiceRepository
) {
    operator fun invoke() {
        eventsServiceRepository.startService()
        mainRepository.observeEvents()
    }
}