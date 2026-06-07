package com.kalashnikovprojects.ufmtv.domain.usecase

import com.kalashnikovprojects.ufmtv.domain.repository.EventsServiceRepository
import javax.inject.Inject

class FinishListeningUpdatesUseCase @Inject constructor(
    private val eventsServiceRepository: EventsServiceRepository
) {
    suspend operator fun invoke() {
        eventsServiceRepository.stopService()
    }
}