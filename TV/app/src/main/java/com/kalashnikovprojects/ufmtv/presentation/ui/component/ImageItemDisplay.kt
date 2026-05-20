package com.kalashnikovprojects.ufmtv.presentation.ui.component

import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.kalashnikovprojects.ufmtv.domain.entity.ImageItem
import com.kalashnikovprojects.ufmtv.domain.entity.Style

@Composable
fun ImageItemDisplay(
    imageItem: ImageItem,
    style: Style,
) {
    AsyncImage(
        model = imageItem.imageUri,
        contentDescription = null,
        modifier = Modifier.size(50.dp),
    )
}