package com.example.ufmcontroller.presentation.ui.screen

import android.content.res.Configuration
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.TextObfuscationMode
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.SecureTextField
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ufmcontroller.R
import com.example.ufmcontroller.presentation.theme.UFMControllerTheme
import com.example.ufmcontroller.presentation.viewmodel.LoginStep
import com.example.ufmcontroller.presentation.viewmodel.LoginTab
import com.example.ufmcontroller.presentation.viewmodel.LoginUiState
import com.example.ufmcontroller.presentation.viewmodel.LoginViewModel

@Composable
fun LoginScreen(
    viewModel: LoginViewModel = hiltViewModel<LoginViewModel>(),
    onSuccessfulLogin: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    LaunchedEffect(
        uiState.step
    ) {
        if (uiState.step is LoginStep.Successful) {
            onSuccessfulLogin()
        }
    }
    LoginScreenContent(
        uiState,
        viewModel.loginFieldState,
        viewModel.passwordFieldState,
        { tab: LoginTab -> viewModel.selectTab(tab) },
        { viewModel.togglePasswordVisibility() },
        { viewModel.login(viewModel.loginFieldState.text.toString(), viewModel.passwordFieldState.text.toString()) },
        { viewModel.register(viewModel.loginFieldState.text.toString(), viewModel.passwordFieldState.text.toString()) })
}


// Разделение чтобы preview работало
@Composable
fun LoginScreenContent(
    uiState: LoginUiState,
    loginFieldState: TextFieldState,
    passwordFieldState: TextFieldState,
    selectTab: (LoginTab) -> Unit,
    toggleShowVisibility: () -> Unit,
    login: () -> Unit,
    register: () -> Unit,
) {
    val tabs = LoginTab.entries

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Row(
            modifier = Modifier.padding(top = 15.dp).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ufm_icon_foreground),
                contentDescription = "UFM logo",
                tint = Color.Unspecified,
            )
            Text(
                "Universal Food Menu",
                fontSize = 25.sp,
                color = colorScheme.onBackground,
                modifier = Modifier.padding(end=20.dp)
            )
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Card(
                modifier = Modifier
                    .width(330.dp),
                shape = MaterialTheme.shapes.large,
                colors = androidx.compose.material3.CardDefaults.cardColors(
                    containerColor = colorScheme.surfaceContainerLow
                )
            ) {
                SecondaryTabRow (
                    selectedTabIndex = uiState.currentTab.ordinal,
                    containerColor = colorScheme.surfaceContainerHigh,
                    contentColor = colorScheme.primary,
                ) {
                    tabs.forEach { tab ->
                        Tab(
                            selected = uiState.currentTab == tab,
                            onClick = { selectTab(tab) },
                            text = {
                                val tabText = when (tab) {
                                    LoginTab.LOGIN -> "Войти"
                                    LoginTab.REGISTER -> "Создать аккаунт"
                                }
                                Text(
                                    text = tabText,
                                    style = MaterialTheme.typography.titleLarge,
                                    textAlign = TextAlign.Center,
                                    color=colorScheme.onSurfaceVariant,
                                )
                            }
                        )
                    }
                }

                Crossfade(
                    targetState = uiState.currentTab,
                    label = "AuthScreenTransition"
                ) { screen ->
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier.padding(30.dp)
                    ) {
                        TextField(
                            state=loginFieldState,
                            label = { Text("Логин") },
                            modifier = Modifier.padding(top=20.dp)
                        )
                        SecureTextField(
                            state=passwordFieldState,
                            label = {
                                Text("Пароль")
                            },
                            textObfuscationMode = if (uiState.passwordHidden) TextObfuscationMode.RevealLastTyped else TextObfuscationMode.Visible,
                            trailingIcon = {
                                IconButton(onClick = toggleShowVisibility) {
                                    Icon(
                                        imageVector = if (uiState.passwordHidden) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                                        contentDescription = if (uiState.passwordHidden) "Hide password" else "Show password"
                                    )
                                }
                            },
                            modifier = Modifier.padding(top=20.dp)
                        )
                        if (uiState.step is LoginStep.Error) {
                            Text(
                                text = uiState.step.message,
                                style = MaterialTheme.typography.labelLarge,
                                color = colorScheme.error,
                                modifier = Modifier.padding(vertical = 20.dp)
                            )
                        }
                        Button(
                            onClick = when (uiState.currentTab) {
                                LoginTab.LOGIN -> login
                                LoginTab.REGISTER -> register
                            },
                            enabled = uiState.step != LoginStep.Loading,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    top = if (uiState.step is LoginStep.Error) 0.dp else 30.dp,
                                    bottom = 30.dp
                                ),
                            shape = MaterialTheme.shapes.medium
                        ) {
                            Box(
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = when (screen) {
                                        LoginTab.LOGIN -> "Войти"
                                        LoginTab.REGISTER -> "Зарегистрироваться"
                                    },
                                    style = MaterialTheme.typography.labelLarge,
                                    modifier = Modifier.padding(vertical = 6.dp).align(Alignment.Center)
                                )
                                if (uiState.step == LoginStep.Loading) {
                                    CircularProgressIndicator(
                                        strokeWidth = 2.dp,
                                        modifier = Modifier
                                            .size(25.dp)
                                            .align(Alignment.CenterEnd)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(group = "light", showSystemUi = true, showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(group = "dark", showSystemUi = true, showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun LoginScreenContentPreview() {
    UFMControllerTheme {
        var loginUiState by remember { mutableStateOf(
            LoginUiState(step = LoginStep.Normal))
        }
        LoginScreenContent(
            uiState = loginUiState,
            selectTab = {
                tab ->
                loginUiState = loginUiState.copy(currentTab = tab)
            },
            loginFieldState = rememberTextFieldState("Логин123"),
            passwordFieldState = rememberTextFieldState("пароль123"),
            toggleShowVisibility = {
                loginUiState = loginUiState.copy(
                    passwordHidden = !loginUiState.passwordHidden
                )
            },
            login = {
                loginUiState = loginUiState.copy(step = LoginStep.Loading)
            },
            register={
                loginUiState = loginUiState.copy(step = LoginStep.Error("Пароль неверный"))
            },
        )
    }
}