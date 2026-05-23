package com.example.ufmcontroller.presentation.ui.component
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.ufmcontroller.presentation.navigation.AboutAppRoute
import com.example.ufmcontroller.presentation.navigation.HomeRoute
import com.example.ufmcontroller.presentation.navigation.MenuEditRoute
import com.example.ufmcontroller.presentation.navigation.SettingsRoute
import com.example.ufmcontroller.presentation.navigation.VisualConfigurationRoute
import kotlinx.serialization.Serializable

@Composable
fun AppNavigationDrawer(
    navigate: (@Serializable Any) -> Unit,
    drawerState: DrawerState = rememberDrawerState(initialValue = DrawerValue.Closed),
    gesturesEnabled: Boolean = true,
    content: @Composable () -> Unit,
) {
    ModalNavigationDrawer(
        drawerContent = {
            ModalDrawerSheet {
                Column(
                    modifier = Modifier.padding(horizontal = 16.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    Spacer(Modifier.height(12.dp))
                    ElevatedButton (onClick = {
                        navigate(HomeRoute)
                    }) {
                        Icon(
                            Icons.Filled.Home,
                            contentDescription = "to home screen"
                        )
                        Text("Universal food menu",
                            color=MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.padding(8.dp),
                            style = MaterialTheme.typography.titleLarge)
                    }
                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                    NavigationDrawerItem(
                        label = { Text("Редактировать меню") },
                        selected = false,
                        icon = {
                            Icon(Icons.Filled.Create, contentDescription = null)
                               },
                        onClick = {
                            navigate(MenuEditRoute)
                        }
                    )
                    NavigationDrawerItem(
                        label = { Text("Настроить экраны") },
                        selected = false,
                        icon = {
                            Icon(Icons.Filled.Build, contentDescription = null)
                        },
                        onClick = {
                            navigate(VisualConfigurationRoute)
                        }
                    )

                    NavigationDrawerItem(
                        label = { Text("Настройки") },
                        selected = false,
                        onClick = {
                            navigate(SettingsRoute)
                        }
                    )
                    NavigationDrawerItem(
                        label = { Text("О приложении") },
                        selected = false,
                        onClick = {
                            navigate(AboutAppRoute)
                        }
                    )
                }
            }
        },
        gesturesEnabled=gesturesEnabled,
        drawerState = drawerState
    ) {
        content()
    }
}