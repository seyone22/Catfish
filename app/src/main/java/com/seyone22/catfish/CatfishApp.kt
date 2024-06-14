package com.seyone22.catfish

import android.Manifest
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.outlined.AccountBalance
import androidx.compose.material.icons.outlined.AccountBalanceWallet
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.seyone22.catfish.ui.navigation.CatfishNavHost
import com.seyone22.catfish.ui.navigation.NavigationDestination
import com.seyone22.catfish.ui.screen.HomeDestination
import com.seyone22.catfish.ui.screen.SettingsDestination
import com.seyone22.catfish.ui.screen.TTSDestination

@OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterial3Api::class)
@Composable
fun CatfishApp() {
    val context = LocalContext.current
    val audioPermissionState = rememberPermissionState(permission = Manifest.permission.RECORD_AUDIO)
    val navController = rememberNavController()
    val selectedDestination = remember { mutableStateOf<NavigationDestination>(HomeDestination) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = (stringResource(id = R.string.app_name)).toString()) }
            )
        },
        bottomBar = {
            NavigationBar {
                activitiesAndIcons.forEach { activityIconPair ->
                    NavigationBarItem(
                        icon = {
                            Icon(
                                imageVector = if (selectedDestination.value == activityIconPair.destination) {
                                    activityIconPair.iconSelected
                                } else {
                                    activityIconPair.icon
                                },
                                contentDescription = activityIconPair.name
                            )
                        },
                        label = { Text(text = activityIconPair.name) },
                        selected = selectedDestination.value == activityIconPair.destination,
                        onClick = {
                            selectedDestination.value = activityIconPair.destination
                            navController.navigate(activityIconPair.destination.route) {
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
    ) {
        CatfishNavHost(
            navController = navController,
            innerPadding = PaddingValues(),
            modifier = Modifier.padding(it)
        )
    }
}

data class ActivityIconPair(
    val name: String,
    val destination: NavigationDestination,
    val icon: ImageVector,
    val iconSelected: ImageVector
)

val activitiesAndIcons = listOf(
    ActivityIconPair(
        name = "Home",
        HomeDestination,
        Icons.Outlined.AccountBalanceWallet,
        Icons.Filled.AccountBalanceWallet
    ),
    ActivityIconPair(
        name = "TTS",
        TTSDestination,
        Icons.Outlined.AccountBalance,
        Icons.Filled.AccountBalance
    ),
)
