package com.kalashnikovprojects.ufmtv.data.model

import com.kalashnikovprojects.ufmtv.domain.entity.NotInStockStyle
import kotlinx.serialization.Serializable


@Serializable
enum class NotInStockStyleDTO {
    CROSSED_OUT,
    HIDDEN,
}

fun NotInStockStyleDTO.toEntity(): NotInStockStyle = when(this) {
        NotInStockStyleDTO.CROSSED_OUT -> NotInStockStyle.CROSSED_OUT
        NotInStockStyleDTO.HIDDEN -> NotInStockStyle.HIDDEN
}