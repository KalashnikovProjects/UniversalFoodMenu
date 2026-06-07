package com.example.ufmcontroller.domain.usecase.service

import com.example.ufmcontroller.domain.repository.EventsServiceRepository
import com.example.ufmcontroller.domain.repository.GetRepository
import kotlinx.coroutines.flow.SharedFlow
import javax.inject.Inject

class GetLogoutEventUseCase @Inject constructor(
    private val getRepository: GetRepository
) {
    operator fun invoke(): SharedFlow<Unit> = getRepository.getLogoutEvent()
}