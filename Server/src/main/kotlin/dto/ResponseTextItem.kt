package com.kalashnikovprojects.ufmserver.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("text")
class ResponseTextItem(
    val id: Int,
    val text: String,
) : ResponseDesignItem