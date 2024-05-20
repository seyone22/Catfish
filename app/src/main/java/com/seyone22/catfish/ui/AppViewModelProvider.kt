package com.seyone22.catfish.ui

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.seyone22.catfish.CatfishApplication
import com.seyone22.catfish.ui.screen.HomeViewModel
import com.seyone22.catfish.ui.screen.TTSViewModel

object AppViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            HomeViewModel(

            )
        }
        initializer {
            TTSViewModel(

            )
        }
    }
}

fun CreationExtras.catfishApplication(): CatfishApplication =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as CatfishApplication)