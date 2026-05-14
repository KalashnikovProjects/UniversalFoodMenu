package com.kalashnikovprojects.ufmtv.data.model

import com.kalashnikovprojects.ufmtv.domain.entity.LoginEvents
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed interface LoginEventsDTO {
    fun toEntity(): LoginEvents

    @SerialName("code_received")
    @Serializable
    data class CodeReceivedDTO(val code: String) : LoginEventsDTO {
        override fun toEntity() = LoginEvents.CodeReceived(code)
    }

    @SerialName("token_received")
    @Serializable
    data class TokenReceivedDTO(val token: String) : LoginEventsDTO {
        override fun toEntity() = LoginEvents.TokenReceived(token)
    }
}