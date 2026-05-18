package com.kalashnikovprojects.ufmtv.domain.entity

sealed interface LoginEvents {
    data class CodeReceived(val code: String) : LoginEvents
    data class TokenReceived(val token: String) : LoginEvents
    object ClosedWithError : LoginEvents
}