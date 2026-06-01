package com.example.ufmcontroller.presentation.ui.screen

import android.app.UiModeManager
import android.content.Context
import android.content.res.Configuration
import android.os.Build
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ufmcontroller.presentation.theme.UFMControllerTheme
import com.example.ufmcontroller.presentation.ui.component.DefaultAppTop
import com.example.ufmcontroller.presentation.viewmodel.SettingsUiState
import com.example.ufmcontroller.presentation.viewmodel.SettingsViewModel

enum class AppThemeMode {
    LIGHT, DARK, SYSTEM
}

fun changeAppTheme(context: Context, mode: AppThemeMode) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        val uiModeManager = context.getSystemService(Context.UI_MODE_SERVICE) as UiModeManager
        val nightModeFlags = when (mode) {
            AppThemeMode.LIGHT -> UiModeManager.MODE_NIGHT_NO
            AppThemeMode.DARK -> UiModeManager.MODE_NIGHT_YES
            AppThemeMode.SYSTEM -> UiModeManager.MODE_NIGHT_AUTO
        }
        uiModeManager.setApplicationNightMode(nightModeFlags)
    } else {
        val delegateMode = when (mode) {
            AppThemeMode.LIGHT -> AppCompatDelegate.MODE_NIGHT_NO
            AppThemeMode.DARK -> AppCompatDelegate.MODE_NIGHT_YES
            AppThemeMode.SYSTEM -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
        }
        AppCompatDelegate.setDefaultNightMode(delegateMode)
    }
}

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel= hiltViewModel<SettingsViewModel>(),
    onLogout: () -> Unit,
    onToggleDrawer: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    SettingsScreenContent(
        uiState,
        onLogout = {
            viewModel.logoutBlocking()
            onLogout()
        },
        onToggleDrawer,
        toggleDropdown = viewModel::toggleDropdown
    )
}

@Composable
fun SettingsScreenContent(
    uiState: SettingsUiState,
    onLogout: () -> Unit,
    onToggleDrawer: () -> Unit,
    toggleDropdown: () -> Unit,
    ) {
    val context = LocalContext.current

    Column {
        DefaultAppTop("Настройки", onButton = onToggleDrawer)
        Column(
            modifier=Modifier.padding(30.dp)
        ) {
            Text(
                text = "Аккаунт",
                color= colorScheme.onBackground,
                style = MaterialTheme.typography.labelLarge,
                fontSize = 25.sp,
            )
            Column(
                modifier = Modifier.padding(10.dp).padding(bottom = 30.dp)
            ) {
                Button(
                    onClick = onLogout,
                    shape = MaterialTheme.shapes.medium
                ) {
                    Text(
                        text = "Выход из аккаунта",
                    )
                }
            }
            Text(
                text = "Тема приложения",
                color= colorScheme.onBackground,
                style = MaterialTheme.typography.labelLarge,
                fontSize = 24.sp,
            )
            Column(
                modifier = Modifier.padding(10.dp),
                horizontalAlignment = Alignment.End,
            ) {
                Button(
                    onClick = toggleDropdown,
                    shape = MaterialTheme.shapes.medium
                ) {
                    Text(
                        text = "Выбрать тему",
                    )
                }
                DropdownMenu(
                    expanded = uiState.dropdownOpen,
                    onDismissRequest = toggleDropdown,
                ) {
                    DropdownMenuItem(
                        text = { Text("Как в системе") },
                        onClick = {
                            changeAppTheme(context, AppThemeMode.SYSTEM)
                            toggleDropdown()
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Тёмная") },
                        onClick = {
                            changeAppTheme(context, AppThemeMode.DARK)
                            toggleDropdown()
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Светлая") },
                        onClick = {
                            changeAppTheme(context, AppThemeMode.LIGHT)
                            toggleDropdown()
                        }
                    )
                }
            }
        }

    }
}

@Preview(group = "light", showSystemUi = true, showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(group = "dark", showSystemUi = true, showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun SettingsScreenContentPreview() {
    UFMControllerTheme {
        Box(
            modifier = Modifier
                .fillMaxSize().background(colorScheme.background)
        ) {
            var uiState by remember {
                mutableStateOf(
                    SettingsUiState(false)
                )
            }
            SettingsScreenContent(
                uiState,
                onLogout = {},
                onToggleDrawer = { },
                toggleDropdown = {
                    uiState = uiState.copy(
                        dropdownOpen = !uiState.dropdownOpen
                    )
                },
            )
        }
    }
}