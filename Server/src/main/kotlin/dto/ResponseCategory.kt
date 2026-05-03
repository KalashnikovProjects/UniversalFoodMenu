package com.kalashnikovprojects.ufmserver.dto
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("category")
class ResponseCategory(
    val id: Int,
    val name: Int,
) : InputDesignItem