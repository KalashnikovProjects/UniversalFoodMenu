package com.kalashnikovprojects.ufmtv.data.model

import kotlinx.serialization.Serializable

@Serializable
sealed interface LoginEvent {
    @Serializable
    data class CodeReceived(val code: String) : LoginEvent
    @Serializable
    data class TokenReceived(val token: String) : LoginEvent
    @Serializable
    object Closed : LoginEvent
}