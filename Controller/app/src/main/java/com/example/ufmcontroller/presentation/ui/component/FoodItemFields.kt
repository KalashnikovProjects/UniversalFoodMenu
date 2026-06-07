package com.example.ufmcontroller.presentation.ui.component

import android.content.res.Configuration
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.InputTransformation
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.maxLength
import androidx.compose.foundation.text.input.then
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.example.ufmcontroller.domain.entity.Category
import com.example.ufmcontroller.domain.entity.CategoryWithFoodItems
import com.example.ufmcontroller.presentation.theme.UFMControllerTheme
import com.example.ufmcontroller.presentation.viewmodel.AddDesignExtended
import com.example.ufmcontroller.presentation.viewmodel.fieldsstates.FoodItemFieldsStates
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

@Composable
fun FoodItemFields(
    foodItemFieldsStates: FoodItemFieldsStates,
    categories: List<Category>,
) {
    val onCategoryToggle =  { id: Int, boolean: Boolean ->
        foodItemFieldsStates.selectedCategories.update {
            if (it.contains(id)) {
                it - id
            } else {
                it + id
            }
        }
    }
    val pickMedia = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            foodItemFieldsStates.imageUri.value = uri.toString()
        }
    }
    val selectedImageUri by foodItemFieldsStates.imageUri.collectAsStateWithLifecycle()
    val isExtendedCategoriesSelection by foodItemFieldsStates.isExtendedCategoriesSelection.collectAsStateWithLifecycle()
    val selectedCategories by foodItemFieldsStates.selectedCategories.collectAsStateWithLifecycle()

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter,
    ) {
        Column(
            modifier=Modifier.width(300.dp),
            // horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text("Название",
                modifier = Modifier.padding(13.dp),
                color = colorScheme.onBackground,
                fontSize = 15.sp,
                fontWeight = FontWeight(600),
            )
            TextField(
                state=foodItemFieldsStates.name,
                placeholder = { Text("Название", modifier = Modifier.alpha(0.5F)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(7.dp)),
            )
            Text("Цена",
                modifier = Modifier.padding(13.dp),
                color = colorScheme.onBackground,
                fontSize = 15.sp,
                fontWeight = FontWeight(600),
            )
            TextField(
                state=foodItemFieldsStates.price,
                placeholder = { Text("Цена", modifier = Modifier.alpha(0.5F)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(7.dp)),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                ),
                inputTransformation = InputTransformation.maxLength(6).then {
                    if (!asCharSequence().all { it.isDigit() }) {
                        revertAllChanges()
                    }
                },
                lineLimits = TextFieldLineLimits.SingleLine,
            )
            Text("Изображение",
                modifier = Modifier.padding(13.dp),
                color = colorScheme.onBackground,
                fontSize = 15.sp,
                fontWeight = FontWeight(600),
            )
            if (selectedImageUri != null) {
                AsyncImage(
                    model = selectedImageUri,
                    contentDescription = null,
                    modifier = Modifier
                        .padding(bottom = 10.dp)
                        .size(100.dp)
                        .clip(
                            RoundedCornerShape(7.dp)
                        ),
                )
            }
            Row {
                Button(onClick = {
                    pickMedia.launch(
                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                    )
                }) {
                    Text("Выбрать из галереи")
                }
                if (selectedImageUri != null) {
                    IconButton(onClick = {
                        foodItemFieldsStates.imageUri.value = null
                    },
                        colors=IconButtonColors(
                            containerColor = colorScheme.errorContainer,
                            contentColor = colorScheme.onErrorContainer,
                            disabledContainerColor = colorScheme.errorContainer,
                            disabledContentColor = colorScheme.onErrorContainer
                        ),
                    ) {
                        Icon(
                            Icons.Filled.Clear,
                            contentDescription = "Отмена выбора",
                            modifier = Modifier,
                            tint = colorScheme.onBackground,
                        )
                    }
                }
            }
            Row(
                modifier = Modifier.clickable {
                    foodItemFieldsStates.isExtendedCategoriesSelection.value = !foodItemFieldsStates.isExtendedCategoriesSelection.value
                }
            ) {
                IconButton(
                    onClick = {
                        foodItemFieldsStates.isExtendedCategoriesSelection.value = !foodItemFieldsStates.isExtendedCategoriesSelection.value
                    },
                    modifier= Modifier.padding(end=7.dp)
                ) {
                    Icon(if (!isExtendedCategoriesSelection) Icons.Filled.ExpandMore else Icons.Filled.ExpandLess,
                        contentDescription = "Expand category",
                        tint = colorScheme.onBackground,
                    )
                }
                Text("Категории",
                    modifier = Modifier.padding(vertical = 13.dp),
                    color = colorScheme.onBackground,
                    fontSize = 15.sp,
                    fontWeight = FontWeight(600),
                )
            }
            AnimatedVisibility(isExtendedCategoriesSelection) {
                Column(
                    modifier=Modifier.fillMaxWidth()
                ) {
                    SearchBar(foodItemFieldsStates.categoriesSearch,
                        modifier = Modifier
                            .padding(5.dp)
                            .fillMaxWidth())

                    LazyColumn(
                        modifier=Modifier.fillMaxWidth()
                    ) {
                        items(categories) {
                                item ->
                            key("category_for_selection${item.id}") {
                                CategoryElement(
                                    category = CategoryWithFoodItems(
                                        item,
                                        emptyList(),
                                    ),
                                    isCheckedUp = selectedCategories.contains(item.id),
                                    opened = false,
                                    onCategoryClick = { id -> onCategoryToggle(id, false) },
                                    onCategoryToggle = onCategoryToggle,
                                    showSwitch = true,
                                    showExpand = false,
                                    showHead = true,
                                    showNotInStock = false,
                                    showBG = true,
                                    doSpaceIfNoExpand = false,
                                    categoryFontSize=17.sp,
                                )
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
fun FoodItemFieldsPreview() {
    UFMControllerTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(colorScheme.background)
        ) {
            FoodItemFields(
                FoodItemFieldsStates(
                    name = TextFieldState(""),
                    price = TextFieldState("155.99"),
                    imageUri = MutableStateFlow(null),
                    isExtendedCategoriesSelection = MutableStateFlow(true),
                ),
                categories = listOf(
                    Category(
                        id = 14,
                        name = "Фастфуд",
                        imageUri = null,
                        price = null,
                        inStock = null,
                    ),
                Category(
                    id = 15,
                    name = "Фастфуд 2",
                    imageUri = "https://static.vecteezy.com/system/resources/previews/041/290/624/non_2x/ai-generated-fresh-burger-isolated-on-transparent-background-free-png.png",
                    price = 100.99F,
                    inStock = false,
                ),
            )
            )
        }
    }
}