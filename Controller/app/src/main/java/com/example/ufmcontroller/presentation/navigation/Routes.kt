package com.example.ufmcontroller.presentation.navigation
import kotlinx.serialization.Serializable

@Serializable
object LoginRoute

@Serializable
object HomeRoute

@Serializable
object MenuEditRoute

@Serializable
object AddItemRoute

@Serializable
data class EditItemRoute(val id: Int)


@Serializable
object AddCategoryRoute

@Serializable
data class EditCategoryRoute(val id: Int)

@Serializable
object VisualConfigurationRoute

@Serializable
data class TvScreenRoute(val id: Int)

@Serializable
object AddTvScreenRoute

@Serializable
object SettingsRoute

@Serializable
object AboutAppRoute