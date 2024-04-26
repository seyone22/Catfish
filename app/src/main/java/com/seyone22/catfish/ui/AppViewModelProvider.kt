package com.seyone22.catfish.ui

import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.seyone22.catfish.ui.screen.HomeViewModel

object AppViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            HomeViewModel(

            )
        }
    }
}