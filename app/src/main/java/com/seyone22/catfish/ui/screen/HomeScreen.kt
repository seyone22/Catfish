package com.seyone22.catfish.ui.screen

import android.content.Context
import android.speech.tts.TextToSpeech
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
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
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.seyone22.catfish.R
import com.seyone22.catfish.ui.AppViewModelProvider
import com.seyone22.catfish.ui.navigation.NavigationDestination
import org.vosk.Model
import org.vosk.android.SpeechService
import org.vosk.android.SpeechStreamService
import org.vosk.android.StorageService
import java.io.IOException
import java.util.Locale


object HomeDestination : NavigationDestination {
    override val route = "Home"
    override val titleRes = R.string.app_name
    override val routeId = 0
    override val icon = Icons.Outlined.Home
    override val iconFilled = Icons.Filled.Home
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun HomeScreen(
    modifier: Modifier,
    viewModel: HomeViewModel = viewModel(factory = AppViewModelProvider.Factory),
    navigateToScreen: (screen: String) -> Unit,
) {
    val PERMISSIONS_REQUEST_RECORD_AUDIO = 1

    var model: Model? = null        //from pre-trained model
    var speechService: SpeechService? = null        //from vosk android
    var speechStreamService: SpeechStreamService? = null    //from vosk android

    val context = LocalContext.current
    var textToSpeak by remember { mutableStateOf("") }
    var ttsInitialized by remember { mutableStateOf(false) }
    var tts: TextToSpeech? by remember { mutableStateOf(null) }

    LaunchedEffect(Unit) {
        tts = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                // Set language to US English
                tts?.language = Locale.US
                ttsInitialized = true
            }
        }
    }

    val audioPermissionState = rememberPermissionState(permission = android.Manifest.permission.RECORD_AUDIO )

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    Log.d("TAG", "HomeScreen: Clicked")
                    if (audioPermissionState.status.isGranted) {

                    } else {
                        Toast.makeText(context, "Microphone permissions not granted!", Toast.LENGTH_SHORT).show()
                        audioPermissionState.launchPermissionRequest()
                    }
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Mic,
                    contentDescription = null
                )
            }
        },
        topBar = { CenterAlignedTopAppBar(title = { Text(text = (stringResource(id = R.string.app_name)).toString()) }) },
        bottomBar = {
            NavigationBar {

            }
        }
    ) {
        Column(modifier = Modifier.padding(it)) {
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
}
