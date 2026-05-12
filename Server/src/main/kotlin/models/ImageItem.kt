package com.kalashnikovprojects.ufmserver.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("image")
data class ImageItem(
    val id: Int,
    var imageUri: String,
) : Designable

fun ImageItem.toNoIdImageItem() = NoIdImageItem(
    imageUri = imageUri,
)

@Serializable
data class NoIdImageItem(
    var imageUri: String,
)

fun NoIdImageItem.toImageItem(id: Int) = ImageItem(
    id=id,
    imageUri = imageUri,
)