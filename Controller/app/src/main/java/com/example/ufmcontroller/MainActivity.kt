package com.example.ufmcontroller

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.rememberNavController
import com.example.ufmcontroller.data.local.FoodAPIDataSource
import com.example.ufmcontroller.data.repository.FoodRepositoryImpl
import com.example.ufmcontroller.domain.repository.FoodRepository
import com.example.ufmcontroller.domain.usecase.AddFoodItemUseCase
import com.example.ufmcontroller.domain.usecase.EditFoodItemUseCase
import com.example.ufmcontroller.domain.usecase.GetFoodItemsUseCase
import com.example.ufmcontroller.domain.usecase.ToggleFoodItemUseCase
import com.example.ufmcontroller.presentation.navigation.AppNavGraph
import com.example.ufmcontroller.presentation.theme.UFMControllerTheme
import com.example.ufmcontroller.presentation.ui.component.AppNavigationDrawer
import com.example.ufmcontroller.presentation.viewmodel.MainViewModel
import kotlinx.coroutines.launch
import kotlin.getValue

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                val repository = FoodRepositoryImpl(FoodAPIDataSource(applicationContext))
                return MainViewModel(
                    GetFoodItemsUseCase(repository),
                    ToggleFoodItemUseCase(repository),
                    AddFoodItemUseCase(repository),
                    EditFoodItemUseCase(repository),
                ) as T
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            val drawerState: DrawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
            val scope = rememberCoroutineScope()


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
                ) {
                    Scaffold { paddingValues ->
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(paddingValues)
                        ) {
                            AppNavGraph(
                                navController = navController,
                                viewModel = viewModel,
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
}