package com.kalashnikovprojects.ufmtv.data.model

import com.kalashnikovprojects.ufmtv.domain.model.NotInStockStyle
import kotlinx.serialization.Serializable

@Serializable
data class StyleDTO(
    val x: Int,
    val y: Int,
    val scale: Float,
    val notInStockStyle: NotInStockStyle?,
    val textColorHex: String?,
    val showImage: Boolean?,
    val showPrice: Boolean?,
)
