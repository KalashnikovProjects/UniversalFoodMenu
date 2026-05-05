package com.kalashnikovprojects.ufmserver.dto

import kotlinx.serialization.Serializable

@Serializable
data class DesignItem(
    val id: Int,
    val element: Designable,
    val style: String,
)

fun DesignItem.toNoIdDesignItem() = NoIdDesignItem(
    element = element,
    style = style,
)

@Serializable
data class NoIdDesignItem(
    val element: Designable,
    val style: String,
)

fun NoIdDesignItem.toDesignItem(id: Int) = DesignItem(
    id=id,
    element = element,
    style = style,
)