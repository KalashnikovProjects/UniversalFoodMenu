package com.kalashnikovprojects.ufmtv.data.model

import com.kalashnikovprojects.ufmtv.domain.entity.NotInStockStyle
import com.kalashnikovprojects.ufmtv.domain.entity.ScreenStyle
import com.kalashnikovprojects.ufmtv.domain.entity.TVScreen
import kotlinx.serialization.Serializable


@Serializable
data class ScreenStyleDTO(
    val backgroundColorHex: String?,
    val defaultNotInStockStyle: NotInStockStyle?,
    val defaultTextColorHex: String?,
    val defaultShowPrice: Boolean?,
)

fun ScreenStyleDTO.toEntity() = ScreenStyle(
    backgroundColorHex=backgroundColorHex,
    defaultNotInStockStyle=defaultNotInStockStyle,
    defaultTextColorHex=defaultTextColorHex,
    defaultShowPrice=defaultShowPrice,
)