package com.example.ufmcontroller.presentation.viewmodel

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.clearText
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ufmcontroller.domain.entity.*
import com.example.ufmcontroller.domain.usecase.category.GetCategoriesUseCase
import com.example.ufmcontroller.domain.usecase.design.*
import com.example.ufmcontroller.domain.usecase.food.GetFoodItemsUseCase
import com.example.ufmcontroller.domain.usecase.screen.DeleteScreenUseCase
import com.example.ufmcontroller.domain.usecase.screen.EditScreenUseCase
import com.example.ufmcontroller.domain.usecase.screen.GetScreenWithDesignItemsUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

enum class TvScreenTab { SELECTED_ITEM, ADD_ITEM, SCREEN }
enum class AddDesignExtended { NONE, FOOD_ITEMS, CATEGORIES, TEXT, IMAGE }
enum class OpenedDeleteConfirmationDialog { NONE, DELETE_SCREEN, DELETE_DESIGN_ITEM }

data class TvScreenUiState(
    val screen: TVScreenWithDesignItems,
    val selectedId: Int? = null,
    val selected: DesignItem? = null,
    val tab: TvScreenTab = TvScreenTab.SCREEN,
    val addDesignExtended: AddDesignExtended = AddDesignExtended.NONE,
    val openedDeleteConfirmationDialog: OpenedDeleteConfirmationDialog = OpenedDeleteConfirmationDialog.NONE,
    val tabsList: List<TvScreenTab> = listOf(TvScreenTab.SELECTED_ITEM, TvScreenTab.ADD_ITEM, TvScreenTab.SCREEN),
    val foodItems: List<FoodItem> = emptyList(),
    val categories: List<Category> = emptyList(),
    val uploadedImagUri: String? = null,
)

data class StyleInputStates(
    val scaleSliderValue: MutableStateFlow<Float?> = MutableStateFlow(null),
    val notInStockStyle: MutableStateFlow<NotInStockStyle?> = MutableStateFlow(null),
    val textColorHex: MutableStateFlow<String?> = MutableStateFlow(null),
    val showImage: MutableStateFlow<Boolean?> = MutableStateFlow(null),
    val showPrice: MutableStateFlow<Boolean?> = MutableStateFlow(null),
    val foodItemDisplayTypeStyle: MutableStateFlow<FoodItemDisplayTypeStyle?> = MutableStateFlow(null),
    val imageScale: MutableStateFlow<Float?> = MutableStateFlow(null),
    val itemWidthScale: MutableStateFlow<Float?> = MutableStateFlow(null),
)

data class InputStates(
    val scaleSliderValue: MutableStateFlow<Float> = MutableStateFlow(1F),
    val textItemFieldState: TextFieldState = TextFieldState(""),
    val screenNameFieldState: TextFieldState = TextFieldState(""),
    val foodItemsSearchState: TextFieldState = TextFieldState(""),
    val categoriesSearchState: TextFieldState = TextFieldState(""),
    val selectedScreenTheme: MutableStateFlow<ScreenTheme?> = MutableStateFlow(null),
    val selectedBackgroundColorHex: MutableStateFlow<String?> = MutableStateFlow(null),
    val selectedStyleInputStates: StyleInputStates = StyleInputStates(),
    val childStyleInputStates: StyleInputStates = StyleInputStates(),
    val defaultStyleInputStates: StyleInputStates = StyleInputStates(),
)

@HiltViewModel(assistedFactory = TvScreenViewModel.Factory::class)
class TvScreenViewModel @AssistedInject constructor(
    private val getFoodItemsUseCase: GetFoodItemsUseCase,
    private val getCategoriesUseCase: GetCategoriesUseCase,
    private val getScreenWithDesignItemsUseCase: GetScreenWithDesignItemsUseCase,
    private val editScreenUseCase: EditScreenUseCase,
    private val deleteScreenUseCase: DeleteScreenUseCase,
    private val deleteTextItemUseCase: DeleteTextItemUseCase,
    private val deleteImageItemUseCase: DeleteImageItemUseCase,
    private val getDesignItemUseCase: GetDesignItemUseCase,
    private val getDesignItemsByScreenUseCase: GetDesignItemsByScreenUseCase,
    private val addDesignItemUseCase: AddDesignItemUseCase,
    private val addDesignItemWithImageUseCase: AddDesignItemWithImageUseCase,
    private val addDesignItemWithTextUseCase: AddDesignItemWithTextUseCase,
    private val editDesignItemUseCase: EditDesignItemUseCase,
    private val deleteDesignItemUseCase: DeleteDesignItemUseCase,
    @Assisted private val id: Int
) : ViewModel() {

    private val foodItemsFlow = getFoodItemsUseCase()
    private val categoriesFlow = getCategoriesUseCase()

    private var screenSaveJob: Job? = null
    private val designItemSaveJobs = mutableMapOf<Int, Job>()

    private val remoteDesignItems = getDesignItemsByScreenUseCase(id)
    private val localModifiedItems = MutableStateFlow<Map<Int, DesignItem>>(emptyMap())

    val designItems: StateFlow<List<DesignItem>> = combine(
        remoteDesignItems, localModifiedItems
    ) { remote, local ->
        remote.map { remoteItem -> local[remoteItem.id] ?: remoteItem }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    private val remoteScreen = getScreenWithDesignItemsUseCase(id).map { it.tvScreen }
    private val localModifiedScreen = MutableStateFlow<TVScreen?>(null)

    val screenStateFlow: StateFlow<TVScreen> = combine(
        remoteScreen, localModifiedScreen
    ) { remote, local ->
        local ?: remote
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = defaultTVScreen()
    )

    val inputStates: InputStates = InputStates()

    private val foodSearchQueryFlow = snapshotFlow { inputStates.foodItemsSearchState.text }
    private val categoriesQueryFlow = snapshotFlow { inputStates.categoriesSearchState.text }

    private val filteredFoodItemsFlow = combine(foodItemsFlow, foodSearchQueryFlow) { items, query ->
        if (query.isBlank()) items else items.filter { it.name.contains(query, ignoreCase = true) }
    }
    private val filteredCategoriesFlow = combine(categoriesFlow, categoriesQueryFlow) { items, query ->
        if (query.isBlank()) items else items.filter { it.name.contains(query, ignoreCase = true) }
    }

    private val _selectedId = MutableStateFlow<Int?>(null)
    private val _selected = MutableStateFlow<DesignItem?>(null)
    private val _tab = MutableStateFlow<TvScreenTab>(TvScreenTab.SCREEN)
    private val _addDesignExtended = MutableStateFlow<AddDesignExtended>(AddDesignExtended.NONE)
    private val _openedDeleteConfirmationDialog = MutableStateFlow<OpenedDeleteConfirmationDialog>(OpenedDeleteConfirmationDialog.NONE)
    private val _uploadImageUriFlow = MutableStateFlow<String?>(null)

    init {
        loadInitialData()
        observeRemoteDesignItems()
        observeRemoteScreen()
        observeScreenNameChanges()
    }

    private fun observeScreenNameChanges() {
        viewModelScope.launch {
            snapshotFlow { inputStates.screenNameFieldState.text }
                .drop(1)
                .distinctUntilChanged()
                .debounce(500)
                .collect { newName ->
                    val currentScreen = screenStateFlow.value
                    if (newName.toString() != currentScreen.name) {
                        val updatedScreen = currentScreen.copy(name = newName.toString())
                        localModifiedScreen.value = updatedScreen
                        editScreenUseCase(id, updatedScreen)
                    }
                }
        }
    }
    private fun loadInitialData() {
        viewModelScope.launch {
            val screen = getScreenWithDesignItemsUseCase(id).first().tvScreen
            inputStates.screenNameFieldState.edit { replace(0, length, screen.name) }
            inputStates.selectedScreenTheme.value = screen.style.screenTheme
            inputStates.selectedBackgroundColorHex.value = screen.style.backgroundColorHex
        }
    }

    private fun observeRemoteDesignItems() {
        viewModelScope.launch {
            remoteDesignItems.collect { remoteItems ->
                val currentLocal = localModifiedItems.value
                if (currentLocal.isNotEmpty()) {
                    val updatedLocal = currentLocal.filter { (id, localItem) ->
                        val remoteItem = remoteItems.find { it.id == id }
                        remoteItem != null && remoteItem.style != localItem.style
                    }
                    if (updatedLocal.size != currentLocal.size) {
                        localModifiedItems.value = updatedLocal
                    }
                }
            }
        }
    }

    private fun observeRemoteScreen() {
        viewModelScope.launch {
            remoteScreen.collect { remote ->
                val local = localModifiedScreen.value
                if (local != null && local.name == remote.name && local.style == remote.style) {
                    localModifiedScreen.value = null
                }
            }
        }
    }

    val uiState: StateFlow<TvScreenUiState> = combine(
        screenStateFlow, _selectedId, _selected, _tab, _addDesignExtended,
        _openedDeleteConfirmationDialog, filteredFoodItemsFlow, filteredCategoriesFlow,
        _uploadImageUriFlow, designItems
    ) { flows ->
        TvScreenUiState(
            screen = TVScreenWithDesignItems(flows[0] as TVScreen, flows[9] as List<DesignItem>),
            selectedId = flows[1] as Int?,
            selected = flows[2] as DesignItem?,
            tab = flows[3] as TvScreenTab,
            addDesignExtended = flows[4] as AddDesignExtended,
            openedDeleteConfirmationDialog = flows[5] as OpenedDeleteConfirmationDialog,
            tabsList = if (flows[2] as DesignItem? != null) listOf(TvScreenTab.SELECTED_ITEM, TvScreenTab.ADD_ITEM, TvScreenTab.SCREEN)
            else listOf(TvScreenTab.ADD_ITEM, TvScreenTab.SCREEN),
            foodItems = flows[6] as List<FoodItem>,
            categories = flows[7] as List<Category>,
            uploadedImagUri = flows[8] as String?,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = TvScreenUiState(screen = TVScreenWithDesignItems(defaultTVScreen(), emptyList()))
    )

    fun setOpenedDeleteConfirmationDialog(dialog: OpenedDeleteConfirmationDialog) = run { _openedDeleteConfirmationDialog.value = dialog }
    fun setUploadedImageUri(uri: String) = run { _uploadImageUriFlow.value = uri }
    fun selectTab(tab: TvScreenTab) = run { _tab.value = tab }
    fun selectAddDesignExtended(extended: AddDesignExtended) = run { _addDesignExtended.value = extended }

    private fun styleToInputStates(states: StyleInputStates, style: Style) {
        states.scaleSliderValue.value = style.scale ?: 1F
        states.notInStockStyle.value = style.notInStockStyle
        states.showPrice.value = style.showPrice
        states.showImage.value = style.showImage
        states.textColorHex.value = style.textColorHex
        states.foodItemDisplayTypeStyle.value = style.foodItemDisplayTypeStyle
        states.imageScale.value = style.imageScale
        states.itemWidthScale.value = style.itemWidthScale
    }


    private fun styleCopyFromInputStates(states: StyleInputStates, style: Style,
                                         categoryItemStyle: Style=Style()) = style.copy(
        scale = states.scaleSliderValue.value,
        notInStockStyle = states.notInStockStyle.value,
        textColorHex = states.textColorHex.value,
        showImage = states.showImage.value,
        showPrice = states.showPrice.value,
        foodItemDisplayTypeStyle = states.foodItemDisplayTypeStyle.value,
        imageScale = states.imageScale.value,
        itemWidthScale = states.itemWidthScale.value,
        categoryItemStyle = categoryItemStyle)

    fun selectElement(id: Int?) {
        viewModelScope.launch {
            _selectedId.value = id
            if (id == null) {
                _selected.value = null
                if (_tab.value == TvScreenTab.SELECTED_ITEM) selectTab(TvScreenTab.SCREEN)
            } else {
                val localDesign = designItems.value.find { it.id == id }
                val design = localDesign ?: getDesignItemUseCase(id).first()

                _selected.value = design
                styleToInputStates(inputStates.selectedStyleInputStates, design.style)
                selectTab(TvScreenTab.SELECTED_ITEM)
            }
        }
    }

    fun editCurrentScreen() {
        val currentScreen = screenStateFlow.value
        val updatedScreen = currentScreen.copy(
            name = inputStates.screenNameFieldState.text.toString(),
            style = currentScreen.style.copy(
                screenTheme = inputStates.selectedScreenTheme.value,
                backgroundColorHex = inputStates.selectedBackgroundColorHex.value,
                defaultStyle = styleCopyFromInputStates(
                    inputStates.defaultStyleInputStates,
                    currentScreen.style.defaultStyle ?: Style()
                )
            ),
        )

        localModifiedScreen.value = updatedScreen

        screenSaveJob?.cancel()
        screenSaveJob = viewModelScope.launch {
            delay(500)
            editScreenUseCase(id, updatedScreen)
        }
    }

    fun moveElement(id: Int, newX: Float, newY: Float) {
        val designItem = designItems.value.firstOrNull { it.id == id } ?: return
        val newElement = designItem.copy(
            element = designItem.element,
            style = designItem.style.copy(x = newX, y = newY)
        )

        localModifiedItems.value = localModifiedItems.value + (id to newElement)

        designItemSaveJobs[id]?.cancel()
        designItemSaveJobs[id] = viewModelScope.launch {
            delay(500)
            val cleanElement = newElement.copy(
                element = if (newElement.element is CategoryWithFoodItems) newElement.element.category else newElement.element
            )
            editDesignItemUseCase(id, cleanElement.toDesignItemWithScreenId(this@TvScreenViewModel.id))
        }
    }

    fun deleteScreen() = run { viewModelScope.launch { deleteScreenUseCase(id) } }

    fun editCurrentDesignItem() {
        _selected.value?.let { currentSelected ->
            val updatedStyle = styleCopyFromInputStates(
                inputStates.selectedStyleInputStates,
                currentSelected.style,
                categoryItemStyle = styleCopyFromInputStates(
                    inputStates.childStyleInputStates,
                    currentSelected.style.categoryItemStyle?: Style())
            )
            val element = currentSelected.copy(style = updatedStyle)
            localModifiedItems.value = localModifiedItems.value + (element.id to element)
            _selected.value = element

            designItemSaveJobs[element.id]?.cancel()
            designItemSaveJobs[element.id] = viewModelScope.launch {
                delay(500)
                val cleanElement = element.copy(
                    element = if (element.element is CategoryWithFoodItems) element.element.category else element.element
                )
                editDesignItemUseCase(element.id, cleanElement.toDesignItemWithScreenId(this@TvScreenViewModel.id))
            }
        }
    }

    fun deleteCurrentDesignItem() {
        viewModelScope.launch {
            _selectedId.value?.let { currentId ->
                val element = _selected.value?.element

                designItemSaveJobs[currentId]?.cancel()
                designItemSaveJobs.remove(currentId)

                localModifiedItems.value = localModifiedItems.value - currentId
                when (element) {
                    is TextItem -> deleteTextItemUseCase(element.id)
                    is ImageItem -> deleteImageItemUseCase(element.id)
                    else -> deleteDesignItemUseCase(id, currentId)
                }
            }
        }
    }

    fun addImageDesignItem() {
        viewModelScope.launch {
            _uploadImageUriFlow.value?.let {
                val item = ImageItem(imageUri = it)
                addDesignItemWithImageUseCase(DesignItemWithScreenId(screenId = id, element = item, style = Style()))
                _uploadImageUriFlow.value = null
            }
        }
    }

    fun addTextDesignItem() {
        viewModelScope.launch {
            val item = TextItem(text = inputStates.textItemFieldState.text.toString())
            addDesignItemWithTextUseCase(DesignItemWithScreenId(screenId = id, element = item, style = Style()))
            inputStates.textItemFieldState.clearText()
        }
    }

    fun addFoodItemDesignItem(foodId: Int) {
        viewModelScope.launch {
            val item = FoodItem(id = foodId, name = "", price = 0F, imageUri = "", inStock = false)
            addDesignItemUseCase(DesignItemWithScreenId(screenId = id, element = item, style = Style()))
        }
    }

    fun addCategoryDesignItem(categoryId: Int) {
        viewModelScope.launch {
            val item = Category(id = categoryId, name = "")
            addDesignItemUseCase(DesignItemWithScreenId(screenId = id, element = item, style = Style()))
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(id: Int): TvScreenViewModel
    }
}