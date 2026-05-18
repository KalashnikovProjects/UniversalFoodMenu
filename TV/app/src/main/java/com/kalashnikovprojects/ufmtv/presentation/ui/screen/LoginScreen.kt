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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices.TV_1080p
import androidx.compose.ui.tooling.preview.Devices.TV_720p
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.tv.material3.MaterialTheme.colorScheme
import androidx.tv.material3.Text
import androidx.tv.material3.surfaceColorAtElevation
import com.kalashnikovprojects.ufmtv.presentation.theme.UFMControllerTheme
import com.kalashnikovprojects.ufmtv.presentation.ui.component.LoginScreenBase
import com.kalashnikovprojects.ufmtv.presentation.ui.component.LoginScreenLoadingWithText
import com.kalashnikovprojects.ufmtv.presentation.viewmodel.LoginStep
import com.kalashnikovprojects.ufmtv.presentation.viewmodel.LoginUIState
import com.kalashnikovprojects.ufmtv.presentation.viewmodel.LoginViewModel


@Composable
fun Dp.scaled(): Dp {
    val configuration = LocalConfiguration.current
    val scaleFactor = configuration.screenWidthDp.toFloat() / 960f
    return (this.value * scaleFactor).dp
}

@Composable
fun TextUnit.scaled(): TextUnit {
    if (!this.isSp) return this
    val configuration = LocalConfiguration.current
    val scaleFactor = configuration.screenWidthDp.toFloat() / 960f
    return (this.value * scaleFactor).sp
}

@Composable
fun LoginScreen(
    onNavigateMainScreen: () -> Unit,
    viewModel: LoginViewModel = hiltViewModel<LoginViewModel>(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    DisposableEffect(Unit) {
        viewModel.startListeningLoginEvents()
        onDispose {
            viewModel.stopListeningLoginEvents()
        }
    }
    LaunchedEffect(Unit) {
        viewModel.navigationEvent.collect {
            onNavigateMainScreen()
        }
    }
    LoginContent(
        uiState=uiState,
    )
}

@Composable
fun LoginContent(
    uiState: LoginUIState,
) {
    when (uiState.loginStep) {
        is LoginStep.Initial -> {Box(
            contentAlignment = Alignment.TopCenter,
            modifier = Modifier.fillMaxSize().background(colorScheme.background),
        ) {
            LoginScreenLoadingWithText("Подключение к серверу..")
        }
        }
        is LoginStep.DisplayCode -> {
            LoginScreenBase {
                Text("Чтобы добавить экран, введите в мобильном приложении во вкладке 'Настроить экраны' следующий код:",
                    fontSize = 18.sp.scaled(),
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.W500,
                    modifier = Modifier.width(550.dp.scaled()),
                    lineHeight = 20.sp.scaled(),
                    color=colorScheme.onBackground,)
                Spacer(modifier = Modifier.padding(vertical = 12.dp.scaled()))
                Box(modifier = Modifier
                    .clip(RoundedCornerShape(20.dp.scaled()))
                    .background(colorScheme.surfaceColorAtElevation(3.dp))
                ) {
                    Text(uiState.loginStep.code,
                        fontSize=60.sp.scaled(),
                        letterSpacing = 20.sp.scaled(),
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(
                            vertical = 15.dp.scaled(),
                            horizontal = 30.dp.scaled(),
                        ),
                        color=colorScheme.onSurface)
                }
                Spacer(modifier = Modifier.padding(vertical = 19.dp.scaled()))
            }
        }
        LoginStep.Completed -> {
            LoginScreenLoadingWithText("Загрузка контента...")
        }
        LoginStep.ReconnectBecauseError -> {
            LoginScreenLoadingWithText("Произошла ошибка при подключении. Повторное подключение к серверу...")
        }
    }
}

@Preview(name="1080pLight", group = "light", showSystemUi = true, showBackground = true, device = TV_1080p, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(name="720pLight", group = "light", showSystemUi = true, showBackground = true, device = TV_720p, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(name="1080pDark", group = "dark", showSystemUi = true, showBackground = true, device = TV_1080p, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(name="720pDark", group = "dark", showSystemUi = true, showBackground = true, device = TV_720p, uiMode =Configuration.UI_MODE_NIGHT_YES)
@Composable
fun LoginContentDisplayCodePreview() {
    UFMControllerTheme() {
        LoginContent(
            uiState = LoginUIState(
                LoginStep.DisplayCode("751853")
            )
        )
    }
}


@Preview(name="1080pLight", group = "light", showSystemUi = true, showBackground = true, device = TV_1080p, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(name="720pLight", group = "light", showSystemUi = true, showBackground = true, device = TV_720p, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(name="1080pDark", group = "dark", showSystemUi = true, showBackground = true, device = TV_1080p, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(name="720pDark", group = "dark", showSystemUi = true, showBackground = true, device = TV_720p, uiMode =Configuration.UI_MODE_NIGHT_YES)
@Composable
fun LoginContentInitialPreview() {
    UFMControllerTheme() {
        LoginContent(
            uiState = LoginUIState(
                LoginStep.Initial
            )
        )
    }
}


@Preview(name="1080pLight", group = "light", showSystemUi = true, showBackground = true, device = TV_1080p, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(name="720pLight", group = "light", showSystemUi = true, showBackground = true, device = TV_720p, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(name="1080pDark", group = "dark", showSystemUi = true, showBackground = true, device = TV_1080p, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(name="720pDark", group = "dark", showSystemUi = true, showBackground = true, device = TV_720p, uiMode =Configuration.UI_MODE_NIGHT_YES)
@Composable
fun LoginContentCompletedPreview() {
    UFMControllerTheme() {
        LoginContent(
            uiState = LoginUIState(
                LoginStep.Completed
            )
        )
    }
}



@Preview(name="1080pLight", group = "light", showSystemUi = true, showBackground = true, device = TV_1080p, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(name="720pLight", group = "light", showSystemUi = true, showBackground = true, device = TV_720p, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(name="1080pDark", group = "dark", showSystemUi = true, showBackground = true, device = TV_1080p, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(name="720pDark", group = "dark", showSystemUi = true, showBackground = true, device = TV_720p, uiMode =Configuration.UI_MODE_NIGHT_YES)
@Composable
fun LoginContentErrorPreview() {
    UFMControllerTheme() {
        LoginContent(
            uiState = LoginUIState(
                LoginStep.ReconnectBecauseError
            )
        )
    }
}