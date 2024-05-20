package com.seyone22.catfish.ui.screen

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.GolfCourse
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.seyone22.catfish.R
import com.seyone22.catfish.asr.IRecorderListener
import com.seyone22.catfish.asr.IWhisperListener
import com.seyone22.catfish.asr.Recorder
import com.seyone22.catfish.asr.Whisper
import com.seyone22.catfish.ui.AppViewModelProvider
import com.seyone22.catfish.ui.navigation.NavigationDestination
import com.seyone22.catfish.utils.WaveUtil
import org.vosk.Model
import org.vosk.android.SpeechService
import org.vosk.android.SpeechStreamService
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream


object HomeDestination : NavigationDestination {
    override val route = "Home"
    override val titleRes = R.string.app_name
    override val routeId = 0
    override val icon = Icons.Outlined.Home
    override val iconFilled = Icons.Filled.Home
}

private const val TAG = "HomeScreen"


@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun HomeScreen(
    modifier: Modifier,
    viewModel: HomeViewModel = viewModel(factory = AppViewModelProvider.Factory),
    navigateToScreen: (screen: String) -> Unit,
) {
    var mWhisper: Whisper? = null
    var mRecorder: Recorder? = null

    val waveFileName = arrayOf<String?>(null)
    val handler = Handler(Looper.getMainLooper())

    val PERMISSIONS_REQUEST_RECORD_AUDIO = 1

    var model: Model? = null        //from pre-trained model
    var speechService: SpeechService? = null        //from vosk android
    var speechStreamService: SpeechStreamService? = null    //from vosk android

    val context = LocalContext.current

    fun copyAssetsWithExtensionsToDataFolder(context: Context, extensions: Array<String>) {
        val assetManager = context.assets
        try {
            // Specify the destination directory in the app's data folder
            val destFolder = context.filesDir.absolutePath
            for (extension in extensions) {
                // List all files in the assets folder with the specified extension
                val assetFiles = assetManager.list("")
                for (assetFileName in assetFiles!!) {
                    if (assetFileName.endsWith(".$extension")) {
                        val outFile = File(destFolder, assetFileName)
                        if (outFile.exists()) continue
                        val inputStream = assetManager.open(assetFileName)
                        val outputStream: OutputStream = FileOutputStream(outFile)

                        // Copy the file from assets to the data folder
                        val buffer = ByteArray(1024)
                        var read: Int
                        while (inputStream.read(buffer).also { read = it } != -1) {
                            outputStream.write(buffer, 0, read)
                        }
                        inputStream.close()
                        outputStream.flush()
                        outputStream.close()
                    }
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    // Call the method to copy specific file types from assets to data folder
    val extensionsToCopy = arrayOf("pcm", "bin", "wav", "tflite")
    copyAssetsWithExtensionsToDataFolder(context, extensionsToCopy)

    // Returns file path from data folder
    fun getFilePath(assetName: String): String {
        val outfile: File = File(context.filesDir, assetName)
        if (!outfile.exists()) {
            Log.d(TAG, "File not found - " + outfile.absolutePath)
        }
        Log.d(TAG, "Returned asset path: " + outfile.absolutePath)
        return outfile.absolutePath
    }

    // Recording calls
    fun startRecording() {
        val waveFilePath: String = getFilePath(WaveUtil.RECORDING_FILE)
        waveFileName[0] = WaveUtil.RECORDING_FILE
        mRecorder!!.setFilePath(waveFilePath)
        mRecorder!!.start()
    }

    fun stopRecording() {
        mRecorder!!.stop()
    }

    fun startTranscription(waveFilePath: String) {
        mWhisper!!.setFilePath(waveFilePath)
        mWhisper!!.setAction(Whisper.ACTION_TRANSCRIBE)
        mWhisper!!.start()
    }

    fun stopTranscription() {
        mWhisper!!.stop()
    }
    var modelPath: String
    var vocabPath: String

    // English-only model and vocab
    modelPath = getFilePath("whisper-tiny-en.tflite")
    vocabPath = getFilePath("filters_vocab_en.bin")

    mWhisper = Whisper(context)
    mWhisper.loadModel(modelPath, vocabPath, false)
    mWhisper.setListener(object : IWhisperListener {
        override fun onUpdateReceived(message: String) {
            Log.d(TAG, "Update is received, Message: $message")
            if (message == Whisper.MSG_PROCESSING) {
            } else if (message == Whisper.MSG_FILE_NOT_FOUND) {
                // write code as per need to handled this error
                Log.d(TAG, "File not found error...!")
            }
        }

        override fun onResultReceived(result: String) {
            Log.d(TAG, "Result: $result")
        }
    })

    mRecorder = Recorder(context)
    mRecorder.setListener(object : IRecorderListener {
        override fun onUpdateReceived(message: String) {
            Log.d(TAG, "Update is received, Message: $message")
            if (message == Recorder.MSG_RECORDING) {
            } else if (message == Recorder.MSG_RECORDING_DONE) {
            }
        }

        override fun onDataReceived(samples: FloatArray?) {
            //mWhisper.writeBuffer(samples);
        }
    })

    Column(modifier = modifier) {
        IconButton(onClick = {
            if (mRecorder != null && mRecorder.isInProgress) {
                Log.d(TAG, "Recording is in progress... stopping...")
                stopRecording()
                Log.d(TAG, "HomeScreen: ${waveFileName}")
            } else {
                Log.d(TAG, "Start recording...")
                startRecording()
            }
        }) {
            Icon(
                imageVector = Icons.Default.Mic,
                contentDescription = null
            )
        }
        IconButton(onClick = {
            if (waveFileName[0] == null) {
                Log.d(TAG, "No recording available to transcribe.")
                return@IconButton
            }
            if (mWhisper != null && mWhisper.isInProgress) {
                Log.d(TAG, "Whisper is already in progress...!")
                stopTranscription()
            } else {
                Log.d(TAG, "Start transcription...")
                val waveFilePath = getFilePath(waveFileName[0]!!)
                startTranscription(waveFilePath)
            }
        }) {
            Icon(
                imageVector = Icons.Default.GolfCourse,
                contentDescription = null
            )
        }
    }
}
