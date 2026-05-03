package com.kalashnikovprojects.ufmserver.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("image")
class ImageItem(
    val imageURI: String,
) : Designable

fun ImageItem.toNoIdImageItem() = NoIdImageItem(
    imageURI = imageURI,
)