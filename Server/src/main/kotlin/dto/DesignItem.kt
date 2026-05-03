package com.kalashnikovprojects.ufmserver.dto

import kotlinx.serialization.Serializable

@Serializable
data class DesignItem(
    val id: Int,
    val element: Designable,
    val style: String,
)

fun DesignItem.toNoIdDesign() = NoIdDesign(
    element = element,
    style = style,
)