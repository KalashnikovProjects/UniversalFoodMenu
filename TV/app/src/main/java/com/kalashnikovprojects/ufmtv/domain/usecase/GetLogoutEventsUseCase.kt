package com.kalashnikovprojects.ufmtv.domain.usecase

import com.kalashnikovprojects.ufmtv.domain.repository.MainRepository
import kotlinx.coroutines.flow.SharedFlow
import javax.inject.Inject

class GetLogoutEventsUseCase @Inject constructor(private val repository: MainRepository) {
    operator fun invoke(): SharedFlow<Unit> = repository.getLogoutEvent()
}
