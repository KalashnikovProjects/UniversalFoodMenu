package com.kalashnikovprojects.ufmtv.domain.model

sealed interface LoginEvent {
    data class CodeReceived(val code: String) : LoginEvent
    data class TokenReceived(val token: String) : LoginEvent
    object Closed : LoginEvent
}