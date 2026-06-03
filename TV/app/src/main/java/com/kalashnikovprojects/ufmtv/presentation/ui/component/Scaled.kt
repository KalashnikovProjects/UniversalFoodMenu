package com.kalashnikovprojects.ufmtv.presentation.ui.component

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
fun Dp.scaled(): Dp {
    val configuration = LocalConfiguration.current
    val scaleFactor = configuration.screenWidthDp.toFloat() / 960f
    return (this.value * scaleFactor).dp
}

@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
fun TextUnit.scaled(): TextUnit {
    if (!this.isSp) return this
    val configuration = LocalConfiguration.current
    val scaleFactor = configuration.screenWidthDp.toFloat() / 960f
    return (this.value * scaleFactor).sp
}