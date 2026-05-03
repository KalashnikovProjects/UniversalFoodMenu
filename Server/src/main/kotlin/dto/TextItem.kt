package com.kalashnikovprojects.ufmserver.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("text")
class TextItem(
    val id: Int,
    val text: String,
) : Designable

fun TextItem.toNoIdTextItem() = NoIdTextItem(
    text=text,
)