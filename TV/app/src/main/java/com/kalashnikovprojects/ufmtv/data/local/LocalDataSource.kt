package com.kalashnikovprojects.ufmtv.data.local

import com.kalashnikovprojects.ufmtv.domain.entity.Category
import com.kalashnikovprojects.ufmtv.domain.entity.CategoryWithFoodItems
import com.kalashnikovprojects.ufmtv.domain.entity.DesignItem
import com.kalashnikovprojects.ufmtv.domain.entity.FoodItem
import com.kalashnikovprojects.ufmtv.domain.entity.ScreenStyle
import com.kalashnikovprojects.ufmtv.domain.entity.TVScreen
import com.kalashnikovprojects.ufmtv.domain.entity.TextItem
import com.kalashnikovprojects.ufmtv.domain.entity.defaultTVScreen
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalDataSource @Inject constructor(
) {
    private val _designItemsRaw = MutableStateFlow<Map<Int, DesignItem>>(emptyMap())
    private val _categoriesRaw = MutableStateFlow<Map<Int, Category>>(emptyMap())
    private val _foodItemsRaw = MutableStateFlow<Map<Int, FoodItem>>(emptyMap())
    private val _relationsRaw = MutableStateFlow<List<Pair<Int, Int>>>(emptyList())
    private val _currentScreen = MutableStateFlow(defaultTVScreen())


    // val foodItems: Flow<List<FoodItem>> = _foodItemsRaw.map { it.map { it.value } }
    // val categories: Flow<List<Category>> = _categoriesRaw.map { it.map { it.value } }
    val screen: Flow<TVScreen> = _currentScreen

    val categoriesWithFoodItems: Flow<List<CategoryWithFoodItems>> = combine(
        _categoriesRaw, _foodItemsRaw, _relationsRaw
    ) { categories, foods, relations ->
        categories.map { (id, category) ->
            CategoryWithFoodItems(
                category,
                relations.filter { it.first == id }.mapNotNull { foods[it.second] }
            )
        }
    }
    val designItems: Flow<List<DesignItem>> = combine(
        _designItemsRaw, categoriesWithFoodItems, _foodItemsRaw
    ) { raws, categoriesWithFoodItems, foods ->
        raws.map { designItem ->
            when (val element = designItem.value.element) {
                is CategoryWithFoodItems -> {
                    designItem.value.copy(
                        element = categoriesWithFoodItems.find { it.category.id == element.category.id } ?: element
                    )
                }
                is FoodItem -> {
                    designItem.value.copy(element = foods[element.id] ?: element)
                }
                else -> designItem.value
            }
        }
    }

    fun getScreenStyle(): Flow<ScreenStyle> {
        return screen.map { it.style }
    }

    fun updateCurrentScreen(screen: TVScreen) {
        _currentScreen.value = screen
    }

    fun updateFoodItem(id: Int, foodItem: FoodItem) {
        _foodItemsRaw.update { it + (id to foodItem) }
    }

    fun toggleFoodInStock(id: Int, inStock: Boolean) {
        _foodItemsRaw.update { map ->
            map.mapValues { if (it.value.id == id) it.value.copy(inStock = inStock) else it.value }
        }
    }

    fun deleteFoodItem(id: Int) {
        _foodItemsRaw.update { it - id }
        _relationsRaw.update {
            it.filter { pair -> pair.second != id }
        }
    }

    fun updateCategory(id: Int, category: Category) {
        _categoriesRaw.update { it + (id to category) }
    }

    fun toggleCategoryInStock(id: Int, inStock: Boolean) {
        _categoriesRaw.update { map ->
            map.mapValues { if (it.value.id == id) it.value.copy(inStock = inStock) else it.value }
        }
    }

    fun deleteCategory(id: Int) {
        _categoriesRaw.update { it - id }
        _relationsRaw.update {
            it.filter { pair -> pair.first != id }
        }
    }

    fun setFoodCategories(foodId: Int, categoryIds: List<Int>) {
        _relationsRaw.update { list ->
            list.filter { it.second != foodId } + categoryIds.map { catId -> Pair(catId, foodId) }
        }
    }

    fun setCategoryItems(categoryId: Int, foodIds: List<Int>) {
        _relationsRaw.update { list ->
            list.filter { it.first != categoryId } + foodIds.map { foodId -> Pair(categoryId, foodId) }
        }
    }

    fun updateDesignItem(id: Int, designItem: DesignItem) {
        _designItemsRaw.update { it + (id to designItem) }
    }

    fun deleteDesignItem(id: Int) {
        _designItemsRaw.update { it - id }
    }

    fun updateTextItemInDesign(id: Int, textItem: TextItem) {
        _designItemsRaw.update { map ->
            map.mapValues { entry ->
                val element = entry.value.element
                if (element is TextItem && element.id == id) {
                    entry.value.copy(element = textItem)
                } else {
                    entry.value
                }
            }
        }
    }

    fun reloadDesignItems(items: Map<Int, DesignItem>) {
        _designItemsRaw.value = items
    }

    fun reloadAllCategorizedItems(
        categories: Map<Int, Category>,
        foodItems: Map<Int, FoodItem>,
        relations: List<Pair<Int, Int>>
    ) {
        _categoriesRaw.value = categories
        _foodItemsRaw.value = foodItems
        _relationsRaw.value = relations
    }
}