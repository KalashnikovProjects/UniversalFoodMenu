package com.kalashnikovprojects.ufmtv.domain.entity

sealed interface LoginEvents {
    data class CodeReceived(val code: String) : LoginEvents
    data class TokenReceived(val screenId: Int, val token: String) : LoginEvents
    object Error : LoginEvents
}