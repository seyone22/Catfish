package com.seyone22.catfish

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.seyone22.catfish.ui.navigation.CatfishNavHost

@Composable
fun CatfishApp(

) {
    CatfishNavHost(navController = rememberNavController(), innerPadding = PaddingValues()) {
    }
}