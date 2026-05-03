package com.kalashnikovprojects.ufmserver.dto

import kotlinx.serialization.Serializable

@Serializable
class ResponseDesign(
    val id: Int,
    val elements: List<ResponseDesignItem>,
    val style: String,
)