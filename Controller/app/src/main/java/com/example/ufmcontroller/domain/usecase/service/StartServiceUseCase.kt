package com.example.ufmcontroller.domain.usecase.service

import com.example.ufmcontroller.domain.repository.EventsServiceRepository
import javax.inject.Inject


class StartServiceUseCase @Inject constructor(
    private val eventsServiceRepository: EventsServiceRepository
) {
    operator fun invoke() {
        eventsServiceRepository.startService()
    }
}