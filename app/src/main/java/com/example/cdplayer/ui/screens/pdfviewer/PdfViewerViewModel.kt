package com.example.cdplayer.ui.screens.pdfviewer

import android.content.Context
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cdplayer.data.local.dao.PdfBookDao
import com.example.cdplayer.data.local.entity.PdfBookEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class PdfViewerViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val pdfBookDao: PdfBookDao
) : ViewModel(), TextToSpeech.OnInitListener {

    private var tts: TextToSpeech? = null
    private var ttsReady = false

    private val _isSpeaking = MutableStateFlow(false)
    val isSpeaking: StateFlow<Boolean> = _isSpeaking.asStateFlow()

    companion object {
        private const val TAG = "PdfViewerVM"
        const val GEMINI_API_KEY = "AIzaSyB5ZGyzyLxCAXcH8J1nAJcjSqN5Sfm2_2w"
    }

    init {
        tts = TextToSpeech(context, this)
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            tts?.language = Locale.US
            ttsReady = true

            tts?.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
                override fun onStart(utteranceId: String?) {
                    _isSpeaking.value = true
                }
                override fun onDone(utteranceId: String?) {
                    _isSpeaking.value = false
                }
                @Deprecated("Deprecated in Java")
                override fun onError(utteranceId: String?) {
                    _isSpeaking.value = false
                }
            })
        } else {
            Log.e(TAG, "TTS initialization failed")
        }
    }

    fun speak(text: String, rate: Float = 0.9f) {
        if (!ttsReady) return
        tts?.setSpeechRate(rate)
        tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, "pdf_tts")
    }

    fun stopSpeaking() {
        tts?.stop()
        _isSpeaking.value = false
    }

    fun savePage(filePath: String, page: Int, totalPages: Int) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val fileName = File(filePath).nameWithoutExtension
                pdfBookDao.upsert(
                    PdfBookEntity(
                        filePath = filePath,
                        fileName = fileName,
                        lastPage = page,
                        totalPages = totalPages,
                        lastReadAt = System.currentTimeMillis()
                    )
                )
            }
        }
    }

    suspend fun loadPage(filePath: String): Int {
        return withContext(Dispatchers.IO) {
            pdfBookDao.getBook(filePath)?.lastPage ?: 1
        }
    }

    fun getApiKey(): String = GEMINI_API_KEY

    fun readFileAsBase64(filePath: String): String? {
        return try {
            val file = File(filePath)
            if (file.exists()) {
                android.util.Base64.encodeToString(file.readBytes(), android.util.Base64.NO_WRAP)
            } else null
        } catch (e: Exception) {
            Log.e(TAG, "Failed to read file: $filePath", e)
            null
        }
    }

    override fun onCleared() {
        super.onCleared()
        tts?.stop()
        tts?.shutdown()
        tts = null
    }
}
