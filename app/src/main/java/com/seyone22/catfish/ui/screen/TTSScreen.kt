package com.seyone22.catfish.ui.screen

import android.speech.tts.TextToSpeech
import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.automirrored.outlined.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.seyone22.catfish.R
import com.seyone22.catfish.ui.AppViewModelProvider
import com.seyone22.catfish.ui.navigation.NavigationDestination
import java.util.Locale

object TTSDestination : NavigationDestination {
    override val route = "TTS"
    override val titleRes = R.string.app_name
    override val routeId = 1
    override val icon = Icons.AutoMirrored.Outlined.VolumeUp
    override val iconFilled = Icons.AutoMirrored.Filled.VolumeUp
}

@Composable
fun TTSScreen(
    modifier: Modifier,
    viewModel: TTSViewModel = viewModel(factory = AppViewModelProvider.Factory),
    navigateToScreen: (screen: String) -> Unit
) {
    val context = LocalContext.current

    var textToSpeak by remember { mutableStateOf("") }
    var ttsInitialized by remember { mutableStateOf(false) }
    var tts: TextToSpeech? by remember { mutableStateOf(null) }

    LaunchedEffect(Unit) {
        tts = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                // Set language to US English
                tts?.language = Locale.GERMANY
                ttsInitialized = true
            }
        }
    }


    Column(modifier = modifier) {
        TextField(value = textToSpeak, onValueChange = { textToSpeak = it })

        Button(onClick = {
            if (ttsInitialized) {
                tts?.speak(textToSpeak, TextToSpeech.QUEUE_FLUSH, null, null)
            }
        }) {
            Text(text = "Speak, My Creation!!!")
        }
    }
}