package com.example.cdplayer.ui.screens.pdfviewer

import android.content.Context
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cdplayer.BuildConfig
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
    }

    // API Key loaded from BuildConfig (sourced from local.properties, not committed to git)
    private val geminiApiKey: String = BuildConfig.GEMINI_API_KEY

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
                val existingBook = pdfBookDao.getBook(filePath)
                if (existingBook != null) {
                    // Update only page info, preserve isFavorite and coverPath
                    pdfBookDao.updateLastPage(filePath, page)
                } else {
                    // New book entry
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
    }

    suspend fun loadPage(filePath: String): Int {
        return withContext(Dispatchers.IO) {
            pdfBookDao.getBook(filePath)?.lastPage ?: 1
        }
    }

    fun getApiKey(): String = geminiApiKey

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

    fun saveRating(filePath: String, rating: Float) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val book = pdfBookDao.getBook(filePath)
                if (book != null) {
                    pdfBookDao.updateRating(filePath, rating)
                } else {
                    // Create book entry if not exists
                    val fileName = File(filePath).nameWithoutExtension
                    pdfBookDao.upsert(
                        PdfBookEntity(
                            filePath = filePath,
                            fileName = fileName,
                            rating = rating
                        )
                    )
                }
            }
        }
    }

    suspend fun loadRating(filePath: String): Float {
        return withContext(Dispatchers.IO) {
            pdfBookDao.getRating(filePath) ?: 0f
        }
    }

    fun saveCoverImage(filePath: String, base64Image: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    // Check if cover already exists
                    val book = pdfBookDao.getBook(filePath)
                    if (book?.coverPath != null && File(book.coverPath).exists()) {
                        return@withContext
                    }

                    // Create covers directory
                    val coversDir = File(context.filesDir, "pdf_covers")
                    if (!coversDir.exists()) coversDir.mkdirs()

                    // Generate unique filename
                    val coverFileName = filePath.hashCode().toString() + ".png"
                    val coverFile = File(coversDir, coverFileName)

                    // Decode base64 and save
                    val imageBytes = android.util.Base64.decode(base64Image, android.util.Base64.DEFAULT)
                    coverFile.writeBytes(imageBytes)

                    // Update database
                    pdfBookDao.updateCoverPath(filePath, coverFile.absolutePath)
                    Log.d(TAG, "Cover saved: ${coverFile.absolutePath}")
                } catch (e: Exception) {
                    Log.e(TAG, "Failed to save cover image", e)
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        tts?.stop()
        tts?.shutdown()
        tts = null
    }
}

