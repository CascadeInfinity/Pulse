package com.example.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.*
import com.example.data.AppDatabase

sealed class Screen(val route: String, val title: String, val icon: ImageVector) {
    object Home : Screen("home", "Home", Icons.Filled.Home)
    object Finances : Screen("finances", "Finances", Icons.Filled.AttachMoney)
    object Pipeline : Screen("pipeline", "Pipeline", Icons.Filled.ViewKanban)
    object Invoices : Screen("invoices", "Invoices", Icons.Filled.Receipt)
    object Content : Screen("content", "Content", Icons.Filled.AutoAwesome)
    object Settings : Screen("settings", "Settings", Icons.Filled.Settings)
}

val items = listOf(
    Screen.Home,
    Screen.Finances,
    Screen.Pipeline,
    Screen.Invoices,
    Screen.Content
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PulseApp(viewModel: PulseViewModel) {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route
            if (currentRoute != Screen.Settings.route) {
                NavigationBar {
                    items.forEach { screen ->
                        NavigationBarItem(
                            icon = { Icon(screen.icon, contentDescription = screen.title) },
                            label = { Text(screen.title) },
                            selected = currentRoute == screen.route,
                            onClick = {
                                navController.navigate(screen.route) {
                                    popUpTo(navController.graph.startDestinationId) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Home.route) { HomeScreen(viewModel, onNavigateToSettings = { navController.navigate(Screen.Settings.route) }) }
            composable(Screen.Finances.route) { FinancesScreen(viewModel) }
            composable(Screen.Pipeline.route) { PipelineScreen(viewModel) }
            composable(Screen.Invoices.route) { InvoicesScreen(viewModel) }
            composable(Screen.Content.route) { ContentScreen(viewModel) }
            composable(Screen.Settings.route) { SettingsScreen(viewModel, onBack = { navController.navigateUp() }) }
        }
    }
}
