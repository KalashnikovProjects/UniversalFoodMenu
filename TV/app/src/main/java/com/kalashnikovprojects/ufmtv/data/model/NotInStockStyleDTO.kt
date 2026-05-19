package com.kalashnikovprojects.ufmtv.data.model

import com.kalashnikovprojects.ufmtv.domain.entity.NotInStockStyle
import kotlinx.serialization.Serializable


@Serializable
enum class NotInStockStyleDTO {
    CROSSED_OUT,
    OPACITY,
    HIDDEN,
    NO_CHANGES,
}

fun NotInStockStyleDTO.toEntity(): NotInStockStyle = when(this) {
        NotInStockStyleDTO.CROSSED_OUT -> NotInStockStyle.CROSSED_OUT
        NotInStockStyleDTO.OPACITY -> NotInStockStyle.OPACITY
        NotInStockStyleDTO.HIDDEN -> NotInStockStyle.HIDDEN
        NotInStockStyleDTO.NO_CHANGES -> NotInStockStyle.NO_CHANGES
}