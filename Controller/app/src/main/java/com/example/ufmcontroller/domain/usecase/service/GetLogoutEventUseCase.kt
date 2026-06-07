package com.example.ufmcontroller.domain.usecase.service

import com.example.ufmcontroller.domain.repository.EventsServiceRepository
import kotlinx.coroutines.flow.SharedFlow
import javax.inject.Inject

class GetLogoutEventUseCase @Inject constructor(
    private val eventsServiceRepository: EventsServiceRepository
) {
    operator fun invoke(): SharedFlow<Unit> = eventsServiceRepository.getLogoutEvent()
}