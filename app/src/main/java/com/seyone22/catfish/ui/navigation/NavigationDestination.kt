package com.seyone22.catfish.ui.navigation

import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Interface to describe the navigation destinations for the app
 */
interface NavigationDestination {
    val route: String
    val titleRes: Int
    val routeId : Int
    val icon : ImageVector
    val iconFilled : ImageVector
}
