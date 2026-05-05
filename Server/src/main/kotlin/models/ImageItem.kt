package com.kalashnikovprojects.ufmserver.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("image")
class ImageItem(
    val id: Int,
    val imageUri: String,
) : Designable

fun ImageItem.toNoIdImageItem() = NoIdImageItem(
    imageUri = imageUri,
)

@Serializable
data class NoIdImageItem(
    val imageUri: String,
)

fun NoIdImageItem.toImageItem(id: Int) = ImageItem(
    id=id,
    imageUri = imageUri,
)