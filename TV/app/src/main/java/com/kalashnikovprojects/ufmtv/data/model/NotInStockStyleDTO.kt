package com.kalashnikovprojects.ufmtv.data.model

import kotlinx.serialization.Serializable


@Serializable
enum class NotInStockStyleDTO {
    CROSSED_OUT,
    HIDDEN,
}