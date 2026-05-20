package com.kalashnikovprojects.ufmtv.presentation.ui.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.tv.material3.Text
import com.kalashnikovprojects.ufmtv.domain.entity.Style
import com.kalashnikovprojects.ufmtv.domain.entity.TextItem

@Composable
fun TextItemDisplay(
    textItem: TextItem,
    style: Style,
) {
    Text(textItem.text,
        fontSize = 14.sp,
        color=Color(style.textColorHex?.toLong(16) ?: 0xFFFFFFFF),
        textAlign = TextAlign.Center,
    )
}