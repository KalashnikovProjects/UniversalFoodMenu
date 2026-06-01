package com.example.ufmcontroller.presentation.navigation
import kotlinx.serialization.Serializable

@Serializable
object LoginRoute

@Serializable
object HomeRoute

@Serializable
object MenuEditRoute

@Serializable
data class ItemRoute(val id: Int) // TODO

@Serializable
object VisualConfigurationRoute

@Serializable
data class ScreenRoute(val id: Int) // TODO

@Serializable
object AddTvScreenRoute

@Serializable
object SettingsRoute

@Serializable
object AboutAppRoute