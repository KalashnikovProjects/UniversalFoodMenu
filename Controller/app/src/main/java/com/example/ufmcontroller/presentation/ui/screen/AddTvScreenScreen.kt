package com.example.ufmcontroller.presentation.ui.screen

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.InputTransformation
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.maxLength
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.text.input.then
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.outlined.AddCircle
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ufmcontroller.presentation.theme.UFMControllerTheme
import com.example.ufmcontroller.presentation.ui.component.DefaultAppTop
import com.example.ufmcontroller.presentation.viewmodel.AddTvScreenStep
import com.example.ufmcontroller.presentation.viewmodel.AddTvScreenUiState
import com.example.ufmcontroller.presentation.viewmodel.AddTvScreenViewModel
import com.example.ufmcontroller.presentation.viewmodel.LoginStep
import com.example.ufmcontroller.presentation.viewmodel.LoginTab

@Composable
fun AddTvScreenScreen(
    viewModel: AddTvScreenViewModel= hiltViewModel<AddTvScreenViewModel>(),
    onBack: () -> Unit,
) {
    val uiState: AddTvScreenUiState by viewModel.uiState.collectAsStateWithLifecycle()

    AddTvScreenScreenContent(
        uiState,
        viewModel.codeFieldState, {
        viewModel.postCode()
    }, onBack)
}

@Composable
fun AddTvScreenScreenContent(
    uiState: AddTvScreenUiState,
    codeFieldState: TextFieldState,
    onInputCode: () -> Unit,
    onBack: () -> Unit,
    ) {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        DefaultAppTop("Настройка экранов", onButton = onBack, buttonIsToMenu = false)
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize(),
        ) {
            Text("Введите код, отображаемый на экране",
                fontSize = 22.sp,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.W500,
                modifier = Modifier.width(330.dp),
                lineHeight = 20.sp,
                color=colorScheme.onBackground)
            Spacer(modifier = Modifier.padding(vertical = 9.dp))
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Box(modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(colorScheme.surfaceColorAtElevation(3.dp))
                    .wrapContentWidth(),
                ) {
                    BasicTextField(
                        state = codeFieldState,
                        modifier = Modifier.width(IntrinsicSize.Min),
                        textStyle = TextStyle(
                            fontSize = 25.sp,
                            letterSpacing = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = colorScheme.onSurface,
                            textAlign = TextAlign.Start,
                        ),
                        inputTransformation = InputTransformation.maxLength(6).then {
                            if (!asCharSequence().all { it.isDigit() }) {
                                revertAllChanges()
                            }
                        },
                        cursorBrush = SolidColor(colorScheme.primary),
                        lineLimits = TextFieldLineLimits.SingleLine,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Done
                        ),
                        onKeyboardAction = {
                            onInputCode()
                        },
                        decorator = { innerTextField ->
                            Box(
                                modifier = Modifier
                                    .border(
                                        width = 2.dp,
                                        color = if (uiState.step is AddTvScreenStep.Error) colorScheme.error else colorScheme.primary,
                                        shape = RoundedCornerShape(20.dp)
                                    )
                                    .padding(vertical = 12.dp, horizontal = 25.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                val textLen = codeFieldState.text.length
                                Text(
                                    text = codeFieldState.text.toString() + "0".repeat(6 - textLen),
                                    style = TextStyle(
                                        fontSize = 25.sp,
                                        letterSpacing = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = colorScheme.onSurface.copy(alpha = 0.2f),
                                        textAlign = TextAlign.Start
                                    ),
                                )
                                innerTextField()
                            }
                        }
                    )
                }
                if (uiState.step is AddTvScreenStep.Error) {
                    Text(
                        text = uiState.step.message,
                        style = MaterialTheme.typography.labelLarge,
                        color = colorScheme.error,
                        modifier = Modifier.padding(top = 10.dp)
                    )
                }
                Button(
                    onClick = onInputCode,
                    enabled = uiState.step != AddTvScreenStep.Loading,
                    modifier = Modifier.width(230.dp).padding(top=10.dp),
                    shape = MaterialTheme.shapes.medium,
                ) {
                    Box(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Добавить экран",
                            style = MaterialTheme.typography.labelLarge,
                            modifier = Modifier
                                .padding(vertical = 6.dp)
                                .align(Alignment.Center)
                        )
                        if (uiState.step == AddTvScreenStep.Loading) {
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


@Preview(group = "light", showSystemUi = true, showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(group = "dark", showSystemUi = true, showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun AddTvScreenScreenContentNormalPreview() {
    UFMControllerTheme {
        var addTvScreenUiState by remember { mutableStateOf(
            AddTvScreenUiState(step = AddTvScreenStep.Normal))
        }
        AddTvScreenScreenContent(
            uiState = addTvScreenUiState,
            codeFieldState = rememberTextFieldState(""),
            onInputCode = {},
            onBack = {},
        )
    }
}


@Preview(group = "light", showSystemUi = true, showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(group = "dark", showSystemUi = true, showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun AddTvScreenScreenContentLoadingPreview() {
    UFMControllerTheme {
        var addTvScreenUiState by remember { mutableStateOf(
            AddTvScreenUiState(step = AddTvScreenStep.Loading))
        }
        AddTvScreenScreenContent(
            uiState = addTvScreenUiState,
            codeFieldState = rememberTextFieldState("1"),
            onInputCode = {},
            onBack = {},
        )
    }
}



@Preview(group = "light", showSystemUi = true, showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(group = "dark", showSystemUi = true, showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun AddTvScreenScreenContentErrorPreview() {
    UFMControllerTheme {
        Box(
            modifier = Modifier
                .fillMaxSize().background(colorScheme.background)
        ) {
            var addTvScreenUiState by remember {
                mutableStateOf(
                    AddTvScreenUiState(step = AddTvScreenStep.Error("Плохой код"))
                )
            }
            AddTvScreenScreenContent(
                uiState = addTvScreenUiState,
                codeFieldState = rememberTextFieldState("123456"),
                onInputCode = {},
                onBack = {},
            )
        }
    }
}