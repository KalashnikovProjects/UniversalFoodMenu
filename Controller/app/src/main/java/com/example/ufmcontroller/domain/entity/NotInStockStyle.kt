package com.example.ufmcontroller.domain.entity

import com.example.ufmcontroller.data.model.NotInStockStyleDTO

enum class NotInStockStyle {
    CROSSED_OUT,
    OPACITY,
    HIDDEN,
    NO_CHANGES,
}

fun NotInStockStyle.toDTO(): NotInStockStyleDTO = when(this) {
    NotInStockStyle.CROSSED_OUT -> NotInStockStyleDTO.CROSSED_OUT
    NotInStockStyle.OPACITY -> NotInStockStyleDTO.OPACITY
    NotInStockStyle.HIDDEN -> NotInStockStyleDTO.HIDDEN
    NotInStockStyle.NO_CHANGES -> NotInStockStyleDTO.NO_CHANGES
}