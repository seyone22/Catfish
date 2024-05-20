package com.seyone22.catfish.ui.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.seyone22.catfish.ui.screen.HomeDestination
import com.seyone22.catfish.ui.screen.HomeScreen
import com.seyone22.catfish.ui.screen.SettingsDestination
import com.seyone22.catfish.ui.screen.SettingsScreen
import com.seyone22.catfish.ui.screen.TTSDestination
import com.seyone22.catfish.ui.screen.TTSScreen


/**
 * Provides Navigation graph for the application.
 */

@Composable
fun CatfishNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    innerPadding : PaddingValues,
) {
    NavHost(
        modifier = modifier.padding(innerPadding),
        navController = navController,
        startDestination = TTSDestination.route,
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None },
    ) {
        // Routes to main Navbar destinations
        composable(route = HomeDestination.route) {
            HomeScreen(
                navigateToScreen = { screen -> navController.navigate(screen) },
                modifier = modifier,
            )
        }

        // Route to Text-to-Speech screen
        composable(route = TTSDestination.route) {
            TTSScreen(
                navigateToScreen = { screen -> navController.navigate(screen) },
                modifier = modifier
            )
        }
        // Routes to settings screen
        composable(route = SettingsDestination.route) {
            SettingsScreen(
                navigateToScreen = { screen -> navController.navigate(screen) },
                modifier = modifier
            )
        }
        /*
        composable(route = SettingsDetailDestination.route + "/{setting}",
            arguments = listOf(navArgument("setting") { type = NavType.StringType })
        ) {
            SettingsDetailScreen(
                navigateToScreen = { screen -> navController.navigate(screen) },
                dataStoreManager = dataStoreManager,
                navigateBack = { navController.popBackStack() },
                backStackEntry = it.arguments?.getString("setting") ?: "-1"
            )
        }*/
    }
}
