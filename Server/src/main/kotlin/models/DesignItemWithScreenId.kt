package com.kalashnikovprojects.ufmserver.models

import com.kalashnikovprojects.ufmserver.dto.DesignItem
import com.kalashnikovprojects.ufmserver.dto.Designable
import kotlinx.serialization.Serializable

@Serializable
data class DesignItemWithScreenId(
    val id: Int,
    val element: Designable,
    val style: String,
    val screenId: Int,
)

fun DesignItemWithScreenId.toDesignItem() = DesignItem(
    id=id,
    element = element,
    style = style,
)