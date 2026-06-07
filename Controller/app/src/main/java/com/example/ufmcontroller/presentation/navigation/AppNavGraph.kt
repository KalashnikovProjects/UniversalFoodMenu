package com.example.ufmcontroller.presentation.navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavHostController
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
import com.example.ufmcontroller.presentation.viewmodel.MainViewModel
import com.example.ufmcontroller.presentation.ui.screen.LoginScreen
import com.example.ufmcontroller.presentation.ui.screen.SettingsScreen
import com.example.ufmcontroller.presentation.ui.screen.TvScreenScreen
import com.example.ufmcontroller.presentation.ui.screen.VisualConfigurationScreen

@Composable
fun AppNavGraph(mainViewModel: MainViewModel, navController: NavHostController, onToggleDrawer: () -> Unit) {
    LaunchedEffect(Unit) {
        Log.d("UFM", "mainViewModel.logoutEvent in AppNavGraph")
        mainViewModel.uiLogoutEvent.collect {
            Log.d("UFM", "navigate logout")
            val currentRoute = navController.currentBackStackEntry?.destination?.route
            if (currentRoute != LoginRoute::class.qualifiedName) {
                navController.navigate(LoginRoute) {
                    popUpTo(0) { inclusive = true }
                    launchSingleTop = true
                }
            }
        }
    }
    NavHost(navController = navController, startDestination = HomeRoute) {
        composable<HomeRoute> {
            HomeScreen(
                navigateEditFoodItem={
                    id ->
                    navController.navigate(EditItemRoute(id))
                },
                navigateEditCategory={
                    id ->
                    navController.navigate(EditCategoryRoute(id))
                },
                onEditMenu = {
                    navController.navigate(MenuEditRoute)
                },
                onToggleDrawer=onToggleDrawer,
            )
        }
        composable<MenuEditRoute> {
            EditMenuScreen(
                onNavigateToEditFoodItem = {
                        id ->
                    navController.navigate(EditItemRoute(id))
                },
                onNavigateToEditCategory = {
                        id ->
                    navController.navigate(EditCategoryRoute(id))
                },
                onNavigateToAddFoodItem = {
                    navController.navigate(AddItemRoute)
                },
                onNavigateToAddCategoryItem = {
                    navController.navigate(AddCategoryRoute)
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
                    navController.navigate(EditItemRoute(id))
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
                    navController.navigate(EditItemRoute(id))
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
                    navController.navigate(EditItemRoute(id))
                },
                navigateEditCategory = {
                        id ->
                    navController.navigate(EditCategoryRoute(id))
                },
                onNavigateVisualConfigurationScreen = {
                    navController.navigate(VisualConfigurationRoute)
                },
                onBack = { navController.popBackStack() },
            )
        }
        composable<VisualConfigurationRoute> {
            VisualConfigurationScreen (
                onNavigateToScreen = {
                    id ->
                    navController.navigate(TvScreenRoute(id))
                },
                onAddScreen = {
                    navController.navigate(AddTvScreenRoute)
                },
                onToggleDrawer=onToggleDrawer,
            )
        }
        composable<AddTvScreenRoute> {
            AddTvScreenScreen(
                onNavigateAfterLogin = { navController.navigate(TvScreenRoute(it)) },
                onBack = { navController.popBackStack() },
            )
        }
        composable<SettingsRoute> {
            SettingsScreen(
                onLogout = {
                    mainViewModel.stopEventsService()

                    navController.navigate(LoginRoute)
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
                    mainViewModel.startEventsService()

                    navController.navigate(HomeRoute)
                }
            )
        }
    }
}