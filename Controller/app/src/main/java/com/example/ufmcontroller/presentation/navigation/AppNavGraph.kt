package com.example.ufmcontroller.presentation.navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavHostController
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.ufmcontroller.presentation.ui.screen.AboutAppScreen
import com.example.ufmcontroller.presentation.ui.screen.AddCategoryScreen
import com.example.ufmcontroller.presentation.ui.screen.AddFoodItemScreen
import com.example.ufmcontroller.presentation.ui.screen.AddTvScreenScreen
import com.example.ufmcontroller.presentation.ui.screen.EditCategoryScreen
import com.example.ufmcontroller.presentation.ui.screen.EditFoodItemScreen
import com.example.ufmcontroller.presentation.ui.screen.EditMenuScreen
import com.example.ufmcontroller.presentation.ui.screen.HomeScreen
import com.example.ufmcontroller.presentation.ui.screen.LoadingScreen
import com.example.ufmcontroller.presentation.viewmodel.MainViewModel
import com.example.ufmcontroller.presentation.ui.screen.LoginScreen
import com.example.ufmcontroller.presentation.ui.screen.SettingsScreen
import com.example.ufmcontroller.presentation.ui.screen.TvScreenScreen
import com.example.ufmcontroller.presentation.ui.screen.VisualConfigurationScreen

fun NavHostController.navigateSafe(route: Any, builder: NavOptionsBuilder.() -> Unit = {}) {
    val currentLifecycleState = currentBackStackEntry?.lifecycle?.currentState
    if (currentLifecycleState?.isAtLeast(androidx.lifecycle.Lifecycle.State.STARTED) == true) {
        navigate(route, builder)
    }
}

@Composable
fun AppNavGraph(mainViewModel: MainViewModel, navController: NavHostController, onToggleDrawer: () -> Unit) {
    LaunchedEffect(Unit) {
        mainViewModel.uiLogoutEvent.collect {
            Log.d("UFM", "navigate logout")
            navController.navigateSafe(LoginRoute) {
                popUpTo(navController.graph.id) {
                    inclusive = true
                }
                launchSingleTop = true
            }
        }
    }
    LaunchedEffect(Unit) {
        mainViewModel.uiLoadedEvent.collect {
            Log.d("UFM", "navigate loaded")
            navController.navigateSafe(HomeRoute) {
                popUpTo(navController.graph.id) {
                    inclusive = true
                }
                launchSingleTop = true
            }
        }
    }
    NavHost(navController = navController, startDestination = LoadingRoute) {
        composable<LoadingRoute> {
            LoadingScreen()
        }
        composable<HomeRoute> {
            HomeScreen(
                navigateEditFoodItem={
                    id ->
                    navController.navigateSafe(EditItemRoute(id)) {
                        launchSingleTop = true
                    }
                },
                navigateEditCategory={
                    id ->
                    navController.navigateSafe(EditCategoryRoute(id)) {
                        launchSingleTop = true
                    }
                },
                onEditMenu = {
                    navController.navigateSafe(MenuEditRoute) {
                        launchSingleTop = true
                    }
                },
                onToggleDrawer=onToggleDrawer,
            )
        }
        composable<MenuEditRoute> {
            EditMenuScreen(
                onNavigateToEditFoodItem = {
                        id ->
                    navController.navigateSafe(EditItemRoute(id)) {
                        launchSingleTop = true
                    }
                },
                onNavigateToEditCategory = {
                        id ->
                    navController.navigateSafe(EditCategoryRoute(id)) {
                        launchSingleTop = true
                    }
                },
                onNavigateToAddFoodItem = {
                    navController.navigateSafe(AddItemRoute) {
                        launchSingleTop = true
                    }
                },
                onNavigateToAddCategoryItem = {
                    navController.navigateSafe(AddCategoryRoute) {
                        launchSingleTop = true
                    }
                },
                onToggleDrawer = onToggleDrawer,
            )
        }
        composable<AddItemRoute> {
            AddFoodItemScreen(
                onBack = { navController.popBackStack() },
            )
        }
        composable<EditItemRoute> {
                backStackEntry ->
            val route: EditItemRoute = backStackEntry.toRoute()

            EditFoodItemScreen (
                foodId = route.id,
                onBack = { navController.popBackStack() },
            )
        }
        composable<AddCategoryRoute> {
            AddCategoryScreen(
                onBack = { navController.popBackStack() },
                navigateEditFoodItem = {
                        id ->
                    navController.navigateSafe(EditItemRoute(id)) {
                        launchSingleTop = true
                    }
                },
            )
        }
        composable<EditCategoryRoute> {
                backStackEntry ->
            val route: EditCategoryRoute = backStackEntry.toRoute()

            EditCategoryScreen(
                categoryId = route.id,
                onBack = { navController.popBackStack() },
                navigateEditFoodItem = {
                        id ->
                    navController.navigateSafe(EditItemRoute(id)) {
                        launchSingleTop = true
                    }
                },
            )
        }
        composable<TvScreenRoute> {
                backStackEntry ->
            val route: TvScreenRoute = backStackEntry.toRoute()

            TvScreenScreen (
                screenId = route.id,
                navigateEditFoodItem =  {
                        id ->
                    navController.navigateSafe(EditItemRoute(id)) {
                        launchSingleTop = true
                    }
                },
                navigateEditCategory = {
                        id ->
                    navController.navigateSafe(EditCategoryRoute(id)) {
                        launchSingleTop = true
                    }
                },
                onNavigateVisualConfigurationScreen = {
                    navController.navigateSafe(VisualConfigurationRoute) {
                        launchSingleTop = true
                    }
                },
                onBack = { navController.popBackStack() },
            )
        }
        composable<VisualConfigurationRoute> {
            VisualConfigurationScreen (
                onNavigateToScreen = {
                    id ->
                    navController.navigateSafe(TvScreenRoute(id)) {
                        launchSingleTop = true
                    }
                },
                onAddScreen = {
                    navController.navigateSafe(AddTvScreenRoute) {
                        launchSingleTop = true
                    }
                },
                onToggleDrawer=onToggleDrawer,
            )
        }
        composable<AddTvScreenRoute> {
            AddTvScreenScreen(
                onNavigateAfterLogin = {
                    navController.navigateSafe(TvScreenRoute(it)) {
                        popUpTo(AddTvScreenRoute) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    } },
                onBack = {
                    navController.popBackStack() },
            )
        }
        composable<SettingsRoute> {
            SettingsScreen(
                onLogout = {
                    mainViewModel.stopEventsService()

                    navController.navigateSafe(LoginRoute) {
                        popUpTo(navController.graph.id) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                },
                onToggleDrawer=onToggleDrawer,
            )
        }
        composable<AboutAppRoute> {
            AboutAppScreen(
                onToggleDrawer=onToggleDrawer,
            )
        }
        composable<LoginRoute> {
            LoginScreen(
                onSuccessfulLogin = {
                    navController.navigateSafe(LoadingRoute) {
                        popUpTo(navController.graph.id) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                    mainViewModel.startEventsService()
                }
            )
        }
    }
}