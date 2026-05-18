package com.kalashnikovprojects.ufmtv.domain.usecase

import com.kalashnikovprojects.ufmtv.domain.repository.EventsServiceController
import com.kalashnikovprojects.ufmtv.domain.repository.MainRepository
import javax.inject.Inject

class FinishListeningUpdatesUseCase @Inject constructor(
    private val mainRepository: MainRepository,
    private val eventsServiceController: EventsServiceController
) {
    suspend operator fun invoke() {
        eventsServiceController.stopService()
        mainRepository.disconnect()
    }
}