package com.kalashnikovprojects.ufmtv.presentation.ui.screen

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices.TV_1080p
import androidx.compose.ui.tooling.preview.Devices.TV_720p
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.tv.material3.MaterialTheme.colorScheme
import androidx.tv.material3.Text
import androidx.tv.material3.surfaceColorAtElevation
import com.kalashnikovprojects.ufmtv.presentation.theme.UFMControllerTheme
import com.kalashnikovprojects.ufmtv.presentation.ui.component.ScreenBase
import com.kalashnikovprojects.ufmtv.presentation.ui.component.ScreenLoadingWithText
import com.kalashnikovprojects.ufmtv.presentation.ui.component.scaled
import com.kalashnikovprojects.ufmtv.presentation.viewmodel.LoginStep
import com.kalashnikovprojects.ufmtv.presentation.viewmodel.LoginUIState
import com.kalashnikovprojects.ufmtv.presentation.viewmodel.LoginViewModel

@Composable
fun LoadingScreen() {
    Box(
        contentAlignment = Alignment.TopCenter,
        modifier = Modifier.fillMaxSize().background(colorScheme.background),
    ) {
        ScreenLoadingWithText("Загрузка данных с сервера...")
    }
}