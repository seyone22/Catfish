package com.seyone22.catfish.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.seyone22.catfish.R
import com.seyone22.catfish.ui.navigation.NavigationDestination

object SettingsDestination : NavigationDestination {
    override val route = "Settings"
    override val titleRes = R.string.app_name
    override val routeId = 1
    override val icon = Icons.Outlined.Home
    override val iconFilled = Icons.Filled.Home
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    modifier: Modifier,
    navigateToScreen: (screen: String) -> Unit,
) {
    Scaffold(
        topBar = { LargeTopAppBar(title = { Text("Settings") }) }
    ) {
        Column(modifier = Modifier.padding(it)) {

        }
    }
}