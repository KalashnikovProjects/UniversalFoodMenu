package com.example.ufmcontroller.presentation.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ufmcontroller.domain.entity.TVScreen
import com.example.ufmcontroller.presentation.viewmodel.InputStates
import com.example.ufmcontroller.presentation.viewmodel.TvScreenUiState


@Composable
fun ScreenTabContent(
    uiState: TvScreenUiState,
    inputStates: InputStates,
    setScreen: (TVScreen) -> Unit,
    editScreen: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .padding(horizontal = 30.dp, vertical = 15.dp)
            .fillMaxWidth()
    ) {
        Text(
            "Название экрана",
            color = colorScheme.onBackground,
            fontSize = 15.sp,
            fontWeight = FontWeight(600),
        )
        OutlinedTextField(
            state = inputStates.screenNameFieldState,
            placeholder = { Text("Название", modifier = Modifier.alpha(0.5F)) },
            modifier = Modifier
                .clip(RoundedCornerShape(7.dp))
                .padding(vertical = 15.dp)
                .onFocusChanged {
                    setScreen(uiState.screen.tvScreen.copy(name = inputStates.screenNameFieldState.text.toString()))
                    editScreen()
                }
        )
    }
}