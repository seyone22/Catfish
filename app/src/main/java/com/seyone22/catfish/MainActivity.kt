package com.seyone22.catfish

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.seyone22.catfish.ui.theme.CatfishTheme
import org.tensorflow.lite.Interpreter
import org.tensorflow.lite.support.common.FileUtil
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            CatfishTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    CatfishApp()
                }
            }
        }
    }

    companion object {
        private const val MODEL_PATH = "whisper_english.tflite"
    }

    private val tflite by lazy {
        Interpreter(FileUtil.loadMappedFile(this, MODEL_PATH))
    }

    fun floatArrayToBuffer(floatArray: FloatArray): FloatBuffer? {
        val byteBuffer: ByteBuffer = ByteBuffer
            .allocateDirect(floatArray.size * 4)

        byteBuffer.order(ByteOrder.nativeOrder())

        val floatBuffer: FloatBuffer = byteBuffer.asFloatBuffer()

        floatBuffer.put(floatArray)
        floatBuffer.position(0)
        return floatBuffer
    }
}