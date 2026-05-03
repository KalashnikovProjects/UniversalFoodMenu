package com.kalashnikovprojects.ufmserver.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("image")
class NoIdImageItem(
    @Transient
    val imageURI: String,
)