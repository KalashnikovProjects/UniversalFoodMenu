package com.kalashnikovprojects.ufmserver.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
sealed interface LoginEvents {
    @SerialName("code_received")
    @Serializable
    data class CodeReceived(val code: String) : LoginEvents
    @SerialName("token_received")
    @Serializable
    data class TokenReceived(val screenId: Int, val token: String) : LoginEvents
}