package com.seyone22.catfish.ui.screen

import android.speech.tts.TextToSpeech
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.automirrored.outlined.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
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
    modifier: Modifier = Modifier,
    viewModel: TTSViewModel = viewModel(factory = AppViewModelProvider.Factory),
    navigateToScreen: (screen: String) -> Unit
) {
    val context = LocalContext.current

    var textToSpeak by remember { mutableStateOf("") }
    var ttsInitialized by remember { mutableStateOf(false) }
    var tts: TextToSpeech? by remember { mutableStateOf(null) }
    var selectedLocale by remember { mutableStateOf(Locale.US) }

    LaunchedEffect(Unit) {
        tts = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                tts?.language = selectedLocale
                ttsInitialized = true
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        TextField(modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
            value = textToSpeak,
            onValueChange = { textToSpeak = it },
            label = { Text("Text to Speak") })

        SpeechSettings(onPitchSelected = { tts?.setPitch(it) },
            onRateSelected = { tts?.setSpeechRate(it) })

        LanguageDropdown { selectedLanguage ->
            val result = tts?.setLanguage(selectedLanguage)
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Toast.makeText(context, "Language not supported!", Toast.LENGTH_SHORT).show()
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(modifier = Modifier.align(Alignment.CenterHorizontally), onClick = {
            if (ttsInitialized) {
                tts?.speak(textToSpeak, TextToSpeech.QUEUE_FLUSH, null, null)
            }
        }) {
            Text(text = "Speak, My Creation!!!")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LanguageDropdown(
    onLocaleSelected: (Locale) -> Unit
) {
    // State to manage the dropdown menu expansion
    var statusExpanded by remember { mutableStateOf(false) }

    var selectedLanguage by remember { mutableStateOf(supportedLanguages[0]) }

    // Dropdown menu content
    ExposedDropdownMenuBox(expanded = statusExpanded,
        onExpandedChange = { statusExpanded = !statusExpanded }) {
        OutlinedTextField(
            modifier = Modifier
                .padding(0.dp, 8.dp)
                .clickable(enabled = true) { statusExpanded = true }
                .menuAnchor(),
            value = selectedLanguage.displayName,
            readOnly = true,
            onValueChange = {
                selectedLanguage = supportedLanguages.find { i -> i.displayName == it }
            },
            label = { Text(text = "Speech Language") },
            singleLine = true,
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = statusExpanded) },
        )

        ExposedDropdownMenu(expanded = statusExpanded,
            onDismissRequest = { statusExpanded = false }) {
            supportedLanguages.forEach { locale ->
                DropdownMenuItem(text = { Text(text = locale.displayName) }, onClick = {
                    selectedLanguage =
                        supportedLanguages.find { i -> i.displayName == locale.displayName }
                    onLocaleSelected(locale)
                    statusExpanded = false
                })
            }
        }
    }
}

// Example list of languages
val supportedLanguages = listOf(
    Locale.US,
    Locale.FRENCH,
    Locale.GERMAN,
    Locale("es", "ES"),
    Locale("ta", "IN"),
    Locale("si", "LK"),
    Locale.JAPAN,
    Locale("ru", "RU")
)

@Composable
fun SpeechSettings(
    onPitchSelected: (Float) -> Unit, onRateSelected: (Float) -> Unit
) {
    var pitch by remember { mutableStateOf(1.0f) }
    var speechRate by remember { mutableStateOf(1.0f) }

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Adjust Speech Settings", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(16.dp))

        // Slider for Pitch
        Slider(
            value = pitch, onValueChange = { newPitch ->
                pitch = newPitch
                onPitchSelected(newPitch)
            }, valueRange = 0.5f..5.0f, steps = 100, modifier = Modifier.padding(horizontal = 16.dp)
        )
        Text("Pitch: $pitch", modifier = Modifier.padding(start = 16.dp))

        Spacer(modifier = Modifier.height(16.dp))

        // Slider for Speech Rate
        Slider(
            value = speechRate, onValueChange = { newSpeechRate ->
                speechRate = newSpeechRate
                onRateSelected(newSpeechRate)
            }, valueRange = 0.1f..5.0f, steps = 100, modifier = Modifier.padding(horizontal = 16.dp)
        )
        Text("Speech Rate: $speechRate", modifier = Modifier.padding(start = 16.dp))
    }
}