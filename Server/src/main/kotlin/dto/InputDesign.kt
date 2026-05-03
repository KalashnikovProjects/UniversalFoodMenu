package com.kalashnikovprojects.ufmserver.dto

import kotlinx.serialization.Serializable

@Serializable
class InputDesign(
    val elements: List<InputDesignItem>,
    val style: String,    // json with: x, y, scale, color and other properties
)