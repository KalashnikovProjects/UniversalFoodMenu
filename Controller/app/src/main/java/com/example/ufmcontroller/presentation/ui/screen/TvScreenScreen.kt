package com.example.ufmcontroller.presentation.ui.screen

import android.content.res.Configuration
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.Slider
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.example.ufmcontroller.domain.entity.Category
import com.example.ufmcontroller.domain.entity.CategoryWithFoodItems
import com.example.ufmcontroller.domain.entity.DesignItem
import com.example.ufmcontroller.domain.entity.FoodItem
import com.example.ufmcontroller.domain.entity.FoodItemDisplayTypeStyle
import com.example.ufmcontroller.domain.entity.ImageItem
import com.example.ufmcontroller.domain.entity.NotInStockStyle
import com.example.ufmcontroller.domain.entity.ScreenStyle
import com.example.ufmcontroller.domain.entity.ScreenTheme
import com.example.ufmcontroller.domain.entity.Style
import com.example.ufmcontroller.domain.entity.TVScreen
import com.example.ufmcontroller.domain.entity.TVScreenWithDesignItems
import com.example.ufmcontroller.domain.entity.TextItem
import com.example.ufmcontroller.presentation.theme.UFMControllerTheme
import com.example.ufmcontroller.presentation.ui.component.CategoryElement
import com.example.ufmcontroller.presentation.ui.component.DefaultAppTop
import com.example.ufmcontroller.presentation.ui.component.FoodItemRowCard
import com.example.ufmcontroller.presentation.ui.component.SearchBar
import com.example.ufmcontroller.presentation.ui.component.TvScreenCard
import com.example.ufmcontroller.presentation.viewmodel.AddDesignExtended
import com.example.ufmcontroller.presentation.viewmodel.EditCategoryUiState
import com.example.ufmcontroller.presentation.viewmodel.EditMenuTab
import com.example.ufmcontroller.presentation.viewmodel.InputStates
import com.example.ufmcontroller.presentation.viewmodel.OpenedDeleteConfirmationDialog
import com.example.ufmcontroller.presentation.viewmodel.TvScreenTab
import com.example.ufmcontroller.presentation.viewmodel.TvScreenUiState
import com.example.ufmcontroller.presentation.viewmodel.TvScreenViewModel
import com.example.ufmcontroller.presentation.viewmodel.VisualConfigurationScreenUiState
import com.example.ufmcontroller.presentation.viewmodel.VisualConfigurationViewModel
import com.example.ufmcontroller.presentation.viewmodel.fieldsstates.CategoryFieldsStates


@Composable
fun TvScreenScreen(
    screenId: Int,
    viewModel: TvScreenViewModel = hiltViewModel(
        creationCallback = { factory: TvScreenViewModel.Factory ->
            factory.create(screenId)
        }
    ),
    navigateEditFoodItem: (Int) -> Unit,
    navigateEditCategory: (Int) -> Unit,
    onBack: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    TvScreenScreenContent(
        uiState,
        viewModel.inputStates,
        viewModel::setUploadedImageUri,
        viewModel::setOpenedDeleteConfirmationDialog,
        viewModel::selectTab,
        viewModel::selectAddDesignExtended,
        viewModel::selectElement,
        viewModel::setScreen,
        viewModel::editScreen,
        viewModel::deleteScreen,
        viewModel::editCurrentDesignItem,
        viewModel::deleteCurrentDesignItem,
        viewModel::addImageDesignItem,
        viewModel::addTextDesignItem,
        viewModel::addFoodItemDesignItem,
        viewModel::addCategoryDesignItem,
        navigateEditFoodItem,
        navigateEditCategory,
        onBack,
    )
}

@Composable
fun TvScreenScreenContent(
    uiState: TvScreenUiState,
    inputStates: InputStates,
    setUploadedImageUri: (String) -> Unit,
    setOpenedDeleteConfirmationDialog: (OpenedDeleteConfirmationDialog) -> Unit,
    selectTab: (TvScreenTab) -> Unit,
    selectAddDesignExtended: (AddDesignExtended) -> Unit,
    selectElement: (Int?) -> Unit,
    setScreen: (TVScreen) -> Unit,
    editScreen: () -> Unit,
    deleteScreen: () -> Unit,
    editCurrentDesignItem: () -> Unit,
    deleteCurrentDesignItem: () -> Unit,
    addImageDesignItem: () -> Unit,
    addTextDesignItem: () -> Unit,
    addFoodItemDesignItem: (Int) -> Unit,
    addCategoryDesignItem: (Int) -> Unit,
    navigateEditFoodItem: (Int) -> Unit,
    navigateEditCategory: (Int) -> Unit,
    onBack: () -> Unit,
) {
    val pickMedia = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            setUploadedImageUri(uri.toString())
        }
    }
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        if (uiState.openedDeleteConfirmationDialog != OpenedDeleteConfirmationDialog.NONE) {
            AlertDialog(
                title = {
                    Text(text =
                        if (uiState.openedDeleteConfirmationDialog == OpenedDeleteConfirmationDialog.DELETE_DESIGN_ITEM) "Удалить элемент дизайна?"
                            else "Отвязать экран и удалить его настройки отображения?",
                        textAlign = TextAlign.Center)
                },
                onDismissRequest = {
                    setOpenedDeleteConfirmationDialog(OpenedDeleteConfirmationDialog.NONE)
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            if (uiState.openedDeleteConfirmationDialog == OpenedDeleteConfirmationDialog.DELETE_DESIGN_ITEM)
                                deleteCurrentDesignItem()
                            else
                                deleteScreen()
                            setOpenedDeleteConfirmationDialog(OpenedDeleteConfirmationDialog.NONE)
                        }
                    ) {
                        Text("Да")
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = {
                            setOpenedDeleteConfirmationDialog(OpenedDeleteConfirmationDialog.NONE)
                        }
                    ) {
                        Text("Отмена")
                    }
                }
            )
        }
        Column {
            DefaultAppTop(
                "Редактирование экрана",
                onBack,
                false,
                actions = {
                    IconButton(
                        onClick = { setOpenedDeleteConfirmationDialog(OpenedDeleteConfirmationDialog.DELETE_SCREEN) },
                        colors = IconButtonColors(
                            containerColor = Color.Transparent,
                            contentColor = colorScheme.error,
                            disabledContainerColor = Color.Transparent,
                            disabledContentColor = colorScheme.error.copy(alpha = 0.5F),
                        ),
                        modifier = Modifier.size(60.dp)
                    ) {
                        Icon(Icons.Filled.Delete,
                            contentDescription = "Delete")
                    }
                })
            TvScreenCard(
                uiState.screen,
                clickable = false,
                modifier = Modifier.padding(5.dp)
            )
            SecondaryTabRow (
                selectedTabIndex = uiState.tab.ordinal,
                containerColor = colorScheme.surfaceContainerHigh,
                contentColor = colorScheme.primary,
                modifier = Modifier
                    .padding(horizontal = 20.dp, vertical = 10.dp)
                    .clip(RoundedCornerShape(100))
            ) {
                uiState.tabsList.forEach { tab ->
                    key(
                        "tv_screen_tab_row${tab.ordinal}"
                    ) {
                        Tab(
                            selected = (uiState.tab == tab),
                            onClick = { selectTab(tab) },
                            text = {
                                val tabText = when (tab) {
                                    TvScreenTab.SELECTED_ITEM -> "Элемент"
                                    TvScreenTab.ADD_ITEM -> "Добавить"
                                    TvScreenTab.SCREEN -> "Экран"
                                }
                                Text(
                                    text = tabText,
                                    style = MaterialTheme.typography.titleLarge,
                                    textAlign = TextAlign.Center,
                                    color=colorScheme.onSurfaceVariant,
                                    fontSize = 18.sp,
                                )
                            }
                        )
                    }

                }
            }
            Crossfade(
                targetState = uiState.tab,
                label = "TvScreenTabTransition"
            ) {
                tab ->
                    when (tab) {
                        TvScreenTab.SELECTED_ITEM -> {
                            Card (
                                modifier = Modifier.padding(15.dp).fillMaxWidth()
                            ) {
                                Column(
                                    modifier = Modifier.padding(15.dp).fillMaxWidth()
                                ) {
                                    Text(
                                        "Стиль элемента",
                                        modifier = Modifier.padding(vertical = 13.dp),
                                        color = colorScheme.onBackground,
                                        fontSize = 17.sp,
                                        fontWeight = FontWeight(600),
                                    )
                                    val style = uiState.selected!!.style
                                    // TODO
//                                    Slider(
//                                        value = style.scale ?: 1f,
//                                        onValueChange = { inputStates.scaleSliderValue(style.copy(imageScale = it)) },
//                                        valueRange = 0.1f..5f
//                                    )
                                }
                            }
                        }

                        TvScreenTab.ADD_ITEM -> {
                            Column(
                                modifier = Modifier.padding(horizontal = 15.dp)
                            ) {
                                Row(
                                    modifier = Modifier.clickable {
                                        selectAddDesignExtended(
                                            if (uiState.addDesignExtended == AddDesignExtended.FOOD_ITEMS) AddDesignExtended.NONE
                                            else AddDesignExtended.FOOD_ITEMS
                                        )
                                    }
                                ) {
                                    IconButton(
                                        onClick = {
                                            selectAddDesignExtended(
                                                if (uiState.addDesignExtended == AddDesignExtended.FOOD_ITEMS) AddDesignExtended.NONE
                                                else AddDesignExtended.FOOD_ITEMS
                                            )
                                        },
                                        modifier = Modifier.padding(end = 7.dp)
                                    ) {
                                        Icon(
                                            if (uiState.addDesignExtended != AddDesignExtended.FOOD_ITEMS) Icons.Filled.ExpandMore else Icons.Filled.ExpandLess,
                                            contentDescription = "Expand food items",
                                            tint = colorScheme.onBackground,
                                        )
                                    }
                                    Text(
                                        "Позиции меню",
                                        modifier = Modifier.padding(vertical = 13.dp),
                                        color = colorScheme.onBackground,
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight(600),
                                    )
                                }
                                AnimatedVisibility(uiState.addDesignExtended == AddDesignExtended.FOOD_ITEMS) {
                                    Column(
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        SearchBar(
                                            inputStates.foodItemsSearchState,
                                            modifier = Modifier
                                                .padding(5.dp)
                                                .fillMaxWidth()
                                        )

                                        LazyColumn(
                                            modifier = Modifier.fillMaxWidth()
                                        ) {
                                            items(uiState.foodItems) { item ->
                                                key("foodItem_for_design_item_selection${item.id}") {
                                                    FoodItemRowCard(
                                                        item,
                                                        onFoodItemClick = { id, _ ->
                                                            addFoodItemDesignItem(
                                                                id
                                                            )
                                                        },
                                                        onFoodItemLongClick = navigateEditFoodItem,
                                                        showSwitch = false,
                                                        showNotInStock = false,
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                                Row(
                                    modifier = Modifier.clickable {
                                        selectAddDesignExtended(
                                            if (uiState.addDesignExtended == AddDesignExtended.CATEGORIES) AddDesignExtended.NONE
                                            else AddDesignExtended.CATEGORIES
                                        )
                                    }
                                ) {
                                    IconButton(
                                        onClick = {
                                            selectAddDesignExtended(
                                                if (uiState.addDesignExtended == AddDesignExtended.CATEGORIES) AddDesignExtended.NONE
                                                else AddDesignExtended.CATEGORIES
                                            )
                                        },
                                        modifier = Modifier.padding(end = 7.dp)
                                    ) {
                                        Icon(
                                            if (uiState.addDesignExtended != AddDesignExtended.CATEGORIES) Icons.Filled.ExpandMore else Icons.Filled.ExpandLess,
                                            contentDescription = "Expand categories list",
                                            tint = colorScheme.onBackground,
                                        )
                                    }
                                    Text(
                                        "Категории",
                                        modifier = Modifier.padding(vertical = 13.dp),
                                        color = colorScheme.onBackground,
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight(600),
                                    )
                                }
                                AnimatedVisibility(uiState.addDesignExtended == AddDesignExtended.CATEGORIES) {
                                    Column(
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        SearchBar(
                                            inputStates.foodItemsSearchState,
                                            modifier = Modifier
                                                .padding(5.dp)
                                                .fillMaxWidth()
                                        )

                                        LazyColumn(
                                            modifier = Modifier.fillMaxWidth()
                                        ) {
                                            items(uiState.categories) { item ->
                                                key("category_for_design_item_selection${item.id}") {
                                                    CategoryElement(
                                                        category = CategoryWithFoodItems(
                                                            category = item,
                                                            emptyList(),
                                                        ),
                                                        opened = false,
                                                        onFoodItemClick = { _, _ -> },
                                                        onFoodItemLongClick = { },
                                                        showSwitch = false,
                                                        onCategoryClick = { id ->
                                                            addCategoryDesignItem(
                                                                id
                                                            )
                                                        },
                                                        onCategoryLongClick = navigateEditCategory,
                                                        onCategoryToggle = { _, _ -> },
                                                        showExpand = false,
                                                        showNotInStock = false,
                                                        showBG = true,
                                                        doSpaceIfNoExpand = false,
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                                Row(
                                    modifier = Modifier.clickable {
                                        selectAddDesignExtended(
                                            if (uiState.addDesignExtended == AddDesignExtended.TEXT) AddDesignExtended.NONE
                                            else AddDesignExtended.TEXT
                                        )
                                    }
                                ) {
                                    IconButton(
                                        onClick = {
                                            selectAddDesignExtended(
                                                if (uiState.addDesignExtended == AddDesignExtended.TEXT) AddDesignExtended.NONE
                                                else AddDesignExtended.TEXT
                                            )
                                        },
                                        modifier = Modifier.padding(end = 7.dp)
                                    ) {
                                        Icon(
                                            if (uiState.addDesignExtended != AddDesignExtended.TEXT) Icons.Filled.ExpandMore else Icons.Filled.ExpandLess,
                                            contentDescription = "Expand text",
                                            tint = colorScheme.onBackground,
                                        )
                                    }
                                    Text(
                                        "Текст",
                                        modifier = Modifier.padding(vertical = 13.dp),
                                        color = colorScheme.onBackground,
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight(600),
                                    )
                                }
                                AnimatedVisibility(uiState.addDesignExtended == AddDesignExtended.TEXT) {
                                    Column(
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        OutlinedTextField(
                                            state = inputStates.textItemFieldState,
                                            placeholder = {
                                                Text(
                                                    "Текст",
                                                    modifier = Modifier.alpha(0.5F)
                                                )
                                            },
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(7.dp))
                                                .padding(vertical = 5.dp),
                                        )
                                        Button(
                                            onClick = addTextDesignItem,
                                            shape = MaterialTheme.shapes.medium
                                        ) {
                                            Text(
                                                text = "Добавить",
                                            )
                                        }
                                    }
                                }
                                Row(
                                    modifier = Modifier.clickable {
                                        selectAddDesignExtended(
                                            if (uiState.addDesignExtended == AddDesignExtended.IMAGE) AddDesignExtended.NONE
                                            else AddDesignExtended.IMAGE
                                        )
                                    }
                                ) {
                                    IconButton(
                                        onClick = {
                                            selectAddDesignExtended(
                                                if (uiState.addDesignExtended == AddDesignExtended.IMAGE) AddDesignExtended.NONE
                                                else AddDesignExtended.IMAGE
                                            )
                                        },
                                        modifier = Modifier.padding(end = 7.dp)
                                    ) {
                                        Icon(
                                            if (uiState.addDesignExtended != AddDesignExtended.IMAGE) Icons.Filled.ExpandMore else Icons.Filled.ExpandLess,
                                            contentDescription = "Expand image",
                                            tint = colorScheme.onBackground,
                                        )
                                    }
                                    Text(
                                        "Изображение",
                                        modifier = Modifier.padding(vertical = 13.dp),
                                        color = colorScheme.onBackground,
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight(600),
                                    )
                                }
                                AnimatedVisibility(uiState.addDesignExtended == AddDesignExtended.IMAGE) {
                                    Column(
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Button(onClick = {
                                            pickMedia.launch(
                                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                            )
                                        }) {
                                            Text("Выбрать из галереи")
                                        }
                                        if (uiState.uploadedImagUri != null) {
                                            AsyncImage(
                                                model = uiState.uploadedImagUri,
                                                contentDescription = null,
                                                modifier = Modifier
                                                    .padding(bottom = 10.dp)
                                                    .size(100.dp)
                                                    .clip(
                                                        RoundedCornerShape(7.dp)
                                                    ),
                                            )
                                            Button(
                                                onClick = addImageDesignItem,
                                                shape = MaterialTheme.shapes.medium
                                            ) {
                                                Text(
                                                    text = "Добавить",
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        TvScreenTab.SCREEN -> {
                            Column(
                                modifier = Modifier.padding(horizontal = 30.dp, vertical = 15.dp)
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
                    }
            }
        }
    }
}

@Preview(group = "light", showSystemUi = true, showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(group = "dark", showSystemUi = true, showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun TvScreenScreenContentPreview() {
    val tab = remember { mutableStateOf<TvScreenTab>(TvScreenTab.SCREEN) }
    val extended = remember { mutableStateOf<AddDesignExtended>(AddDesignExtended.NONE) }

    UFMControllerTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(colorScheme.background)
        ) {
            TvScreenScreenContent(
                uiState = TvScreenUiState(
                    screen = TVScreenWithDesignItems(
                        TVScreen(
                            1,
                            "Экран Redmi Super Max Ultra Pro 42",
                            1920,
                            1080,
                            ScreenStyle(
                                screenTheme = ScreenTheme.BLACK,
                            ),
                        ),
                        designItems = listOf(
                            DesignItem(
                                1,
                                TextItem(
                                    1,
                                    "Бургерная",
                                ),
                                Style(
                                    x = 0.1F,
                                    y = 0.1F,
                                    scale = 3F,
                                ),
                            ),
                            DesignItem(
                                2,
                                FoodItem(
                                    1,
                                    "Одинокий бургер",
                                    105.9F,
                                    "https://static.vecteezy.com/system/resources/previews/025/064/829/non_2x/hamburger-with-ai-generated-free-png.png",
                                    false,
                                ),
                                Style(
                                    x = 0.9F,
                                    y = 0.1F,
                                    scale = 1.1F,
                                ),
                            ),
                            DesignItem(
                                3,
                                FoodItem(
                                    1,
                                    "Самый крутой бургер",
                                    105.9F,
                                    "https://static.vecteezy.com/system/resources/previews/025/064/829/non_2x/hamburger-with-ai-generated-free-png.png",
                                    true,
                                ),
                                Style(
                                    x = 0.8F,
                                    y = 0.8F,
                                    scale = 2F,
                                    foodItemDisplayTypeStyle = FoodItemDisplayTypeStyle.Cell,
                                ),
                            ),
                            DesignItem(
                                4,
                                CategoryWithFoodItems(
                                    category = Category(
                                        id = 1,
                                        name = "Вкусняшки",
                                        imageUri = "https://i.pinimg.com/originals/b3/24/cb/b324cbabe17d8913e556722973aa6f09.png",
                                        price = 1000.2F,
                                        inStock = null,
                                    ),
                                    foodItems = listOf(
                                        FoodItem(
                                            1,
                                            "Бургер",
                                            105.9F,
                                            "https://static.vecteezy.com/system/resources/previews/025/064/829/non_2x/hamburger-with-ai-generated-free-png.png",
                                            false,
                                        ),
                                        FoodItem(
                                            1,
                                            "Пицца",
                                            105.9F,
                                            "https://static.vecteezy.com/system/resources/previews/025/064/829/non_2x/hamburger-with-ai-generated-free-png.png",
                                            true,
                                        )
                                    ),
                                ),
                                Style(
                                    x = 0.1F,
                                    y = 0.45F,
                                    scale = 1.5F,
                                    notInStockStyle = NotInStockStyle.CROSSED_OUT,
                                    showPrice = true,
                                    categoryItemStyle = Style(
                                        notInStockStyle = NotInStockStyle.CROSSED_OUT
                                    )
                                ),
                            ),
                            DesignItem(
                                5,
                                ImageItem(
                                    1,
                                    "https://yt3.googleusercontent.com/ytc/AIdro_kRmcneiyQOhH6n0KVn7FgbCkAfh0AI0EA9RpgzBnHq3g=s900-c-k-c0x00ffffff-no-rj"
                                ),
                                Style(
                                    x = 0.4F,
                                    y = 0.85F,
                                    scale = 1.7F,
                                ),
                            ),
                        )
                    ),
                    selectedId = 1,
                    selected = DesignItem(
                        1,
                        TextItem(
                            1,
                            "Бургерная",
                        ),
                        Style(
                            x = 0.1F,
                            y = 0.1F,
                            scale = 3F,
                        ),
                    ),
                    tab = tab.value,
                    addDesignExtended = extended.value,
                    openedDeleteConfirmationDialog = OpenedDeleteConfirmationDialog.NONE,
                ),
                setOpenedDeleteConfirmationDialog = { },
                selectTab = {
                    tab.value = it
                },
                selectAddDesignExtended = {
                    extended.value = it
                },
                selectElement = { },
                setScreen = { },
                editScreen = { },
                deleteScreen = { },
                editCurrentDesignItem = { },
                deleteCurrentDesignItem = { },
                onBack = { },
                addImageDesignItem = { },
                addTextDesignItem = { },
                addFoodItemDesignItem = { },
                addCategoryDesignItem = { },
                inputStates = InputStates(),
                navigateEditFoodItem = { },
                navigateEditCategory = { },
                setUploadedImageUri = { },
            )
        }
    }
}