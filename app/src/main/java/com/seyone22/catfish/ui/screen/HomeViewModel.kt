package com.seyone22.catfish.ui.screen

import android.content.Context
import android.content.Intent
import android.speech.RecognizerIntent
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.lifecycle.ViewModel

class HomeViewModel(): ViewModel() {
    companion object {
        private const val SPEECH_REQUEST_CODE = 0
    }

    fun displaySpeechRecognizer(context: Context, launcher: ActivityResultLauncher<*>) {
        Log.d("SUCCESS", "HomeScreen: Viewmodel")

        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        }
        if (intent.resolveActivity(context.packageManager) != null) {
            // Launch the speech recognition activity
            context.startActivity(intent)
        } else {
            Log.d("ERROR", "HomeScreen: Failure to launch")
            Toast.makeText(context, "Speech recognition not available", Toast.LENGTH_SHORT).show()
        }
    }
}