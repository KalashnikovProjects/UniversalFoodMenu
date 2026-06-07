package com.example.ufmcontroller.presentation.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ufmcontroller.presentation.viewmodel.InputStates
import com.example.ufmcontroller.presentation.viewmodel.TvScreenUiState

@Composable
fun SelectedItemTabContent(
    uiState: TvScreenUiState,
    inputStates: InputStates,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .padding(15.dp)
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .padding(15.dp)
                .fillMaxWidth()
        ) {
            Text(
                "Стиль элемента",
                modifier = Modifier.padding(vertical = 13.dp),
                color = colorScheme.onBackground,
                fontSize = 17.sp,
                fontWeight = FontWeight(600),
            )
            val style = uiState.selected?.style
            // TODO: Слайдеры для масштабирования
        }
    }
}