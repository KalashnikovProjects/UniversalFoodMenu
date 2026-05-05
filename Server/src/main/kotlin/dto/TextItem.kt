package com.kalashnikovprojects.ufmserver.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("text")
data class TextItem(
    val id: Int,
    val text: String,
) : Designable

fun TextItem.toNoIdTextItem() = NoIdTextItem(
    text=text,
)

@Serializable
data class NoIdTextItem(
    val text: String,
)

fun NoIdTextItem.toTextItem(id: Int) = TextItem(
    id=id,
    text=text,
)