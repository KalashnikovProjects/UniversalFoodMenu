package com.example.ufmcontroller.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.ufmcontroller.presentation.ui.screen.AboutAppScreen
import com.example.ufmcontroller.presentation.ui.screen.AddTvScreenScreen
import com.example.ufmcontroller.presentation.viewmodel.MainViewModel
import com.example.ufmcontroller.presentation.ui.screen.LoginScreen
import com.example.ufmcontroller.presentation.ui.screen.SettingsScreen
import com.example.ufmcontroller.presentation.ui.screen.VisualConfigurationScreen

// import com.example.ufmcontroller.presentation.ui.screen.HomeScreen
// import com.example.ufmcontroller.presentation.ui.screen.SettingsScreen

@Composable
fun AppNavGraph(mainViewModel: MainViewModel,navController: NavHostController, onToggleDrawer: () -> Unit) {
    NavHost(navController = navController, startDestination = HomeRoute) {
        composable<HomeRoute> {
//            HomeScreen(
//                navigateEditFoodItem={
//                    id ->
//                    navController.navigate(ItemRoute(id))
//                },
//                onToggleDrawer=onToggleDrawer,
//            )
        }
        composable<VisualConfigurationRoute> {
            VisualConfigurationScreen (
                onNavigateToScreen = {
                    id ->
                    navController.navigate(ScreenRoute(id))
                },
                onAddScreen = {
                    navController.navigate(AddTvScreenRoute)
                },
                onToggleDrawer=onToggleDrawer,
            )
        }
        composable<AddTvScreenRoute> {
            AddTvScreenScreen(
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
//        composable<ItemRoute> { backStackEntry ->
//            val route: ItemRoute = backStackEntry.toRoute()
//
//            val todo = foodItems.find { it.id == route.id }
//
//            todo?.let {
//                ItemScreen(
//                    todo = it,
//                    onBack = { navController.popBackStack() },
//                    onToggle = viewModel::toggleTodo,
//                )
//            }
//        }
    }
}