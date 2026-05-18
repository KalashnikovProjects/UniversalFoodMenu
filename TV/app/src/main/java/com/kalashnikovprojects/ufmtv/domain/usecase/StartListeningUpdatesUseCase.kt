package com.kalashnikovprojects.ufmtv.domain.usecase

import com.kalashnikovprojects.ufmtv.domain.repository.EventsServiceController
import com.kalashnikovprojects.ufmtv.domain.repository.MainRepository
import javax.inject.Inject

class StartListeningUpdatesUseCase @Inject constructor(
    private val mainRepository: MainRepository,
    private val eventsServiceController: EventsServiceController
) {
    operator fun invoke() {
        eventsServiceController.startService()
        mainRepository.observeEvents()
    }
}