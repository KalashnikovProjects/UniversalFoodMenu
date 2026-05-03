package com.kalashnikovprojects.ufmserver.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("image")
class InputImageItem(
    @Transient
    val imageURI: String,
) : InputDesignItem