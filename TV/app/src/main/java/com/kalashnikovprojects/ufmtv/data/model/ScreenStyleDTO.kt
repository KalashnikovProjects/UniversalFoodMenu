package com.kalashnikovprojects.ufmtv.data.model

import com.kalashnikovprojects.ufmtv.domain.model.NotInStockStyle
import kotlinx.serialization.Serializable


@Serializable
data class ScreenStyleDTO(
    val backgroundColorHex: String?,
    val defaultNotInStockStyle: NotInStockStyle?,
    val defaultTextColorHex: String?,
    val defaultShowPrice: Boolean?,
)
