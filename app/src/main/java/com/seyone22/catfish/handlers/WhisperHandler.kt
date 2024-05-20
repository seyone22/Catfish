package com.seyone22.catfish.handlers

import android.content.Context
import org.tensorflow.lite.Interpreter;
import org.tensorflow.lite.support.common.FileUtil
import java.io.IOException
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.MappedByteBuffer

class WhisperHandler(context: Context){
    private var tflite: Interpreter? = null

    init {
        try {
            // Load the model from assets
            val tfliteModel: MappedByteBuffer = FileUtil.loadMappedFile(context, "whisper_english.tflite")
            // Initialize the interpreter with the model
            tflite = Interpreter(tfliteModel)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun runInference(inputBuffer: ByteBuffer): ByteBuffer {
        // Assumming hte model's output is 256 floats, adjust as needed
        val outputBuffer = ByteBuffer.allocateDirect(256 * 4)
        outputBuffer.order(ByteOrder.nativeOrder())

        // Run the model
        tflite?.run(inputBuffer, outputBuffer)
        outputBuffer.rewind()
        return outputBuffer
    }
}