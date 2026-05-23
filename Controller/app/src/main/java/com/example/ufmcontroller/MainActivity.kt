package com.example.ufmcontroller

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.ufmcontroller.presentation.navigation.AppNavGraph
import com.example.ufmcontroller.presentation.navigation.LoginRoute
import com.example.ufmcontroller.presentation.theme.UFMControllerTheme
import com.example.ufmcontroller.presentation.ui.component.AppNavigationDrawer
import com.example.ufmcontroller.presentation.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        mainViewModel.startEventsService()
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            val drawerState: DrawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
            val scope = rememberCoroutineScope()
            val currentBackStackEntry by navController.currentBackStackEntryAsState()
            val isLoginScreen = currentBackStackEntry?.destination?.hasRoute<LoginRoute>() == true

            UFMControllerTheme {
                AppNavigationDrawer(
                    navigate={
                        route ->
                        navController.navigate(route)
                        scope.launch {
                            drawerState.close()
                        }
                    },
                    drawerState=drawerState,
                    gesturesEnabled=!isLoginScreen,
                ) {
                    Scaffold { paddingValues ->
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(paddingValues)
                        ) {
                            AppNavGraph(
                                mainViewModel=mainViewModel,
                                navController = navController,
                                onToggleDrawer={
                                    scope.launch {
                                        if (drawerState.isClosed) {
                                            drawerState.open()
                                        } else {
                                            drawerState.close()
                                        }
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mainViewModel.startEventsService()
    }
}