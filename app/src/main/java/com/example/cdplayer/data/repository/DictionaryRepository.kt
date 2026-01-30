package com.example.cdplayer.data.repository

import com.example.cdplayer.data.remote.api.GoogleTranslateApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import javax.inject.Inject
import javax.inject.Singleton

data class TranslationResult(
    val original: String,
    val translated: String,
    val isKoreanToEnglish: Boolean  // true: 한글→영어, false: 영어→한글
)

@Singleton
class DictionaryRepository @Inject constructor(
    private val googleTranslateApi: GoogleTranslateApi
) {
    suspend fun translate(text: String): Result<TranslationResult> = withContext(Dispatchers.IO) {
        try {
            val trimmedText = text.trim()
            val isKorean = containsKorean(trimmedText)
            val translated = translateWithGoogle(trimmedText, isKorean)
            Result.success(TranslationResult(
                original = trimmedText,
                translated = translated,
                isKoreanToEnglish = isKorean
            ))
        } catch (e: Exception) {
            Result.failure(Exception("번역 중 오류가 발생했습니다"))
        }
    }

    private fun containsKorean(text: String): Boolean {
        return text.any { it in '\uAC00'..'\uD7A3' || it in '\u3131'..'\u3163' }
    }

    private suspend fun translateWithGoogle(text: String, isKoreanToEnglish: Boolean): String {
        return try {
            val sourceLang = if (isKoreanToEnglish) "ko" else "en"
            val targetLang = if (isKoreanToEnglish) "en" else "ko"
            val response = googleTranslateApi.translate(
                sourceLang = sourceLang,
                targetLang = targetLang,
                text = text
            )
            val jsonString = response.string()
            parseGoogleTranslateResponse(jsonString)
        } catch (e: Exception) {
            text
        }
    }

    private fun parseGoogleTranslateResponse(jsonString: String): String {
        return try {
            val jsonArray = JSONArray(jsonString)
            val translationsArray = jsonArray.getJSONArray(0)
            val result = StringBuilder()

            for (i in 0 until translationsArray.length()) {
                val item = translationsArray.optJSONArray(i)
                if (item != null && item.length() > 0) {
                    val translatedText = item.optString(0, "")
                    if (translatedText.isNotEmpty()) {
                        result.append(translatedText)
                    }
                }
            }

            result.toString().ifEmpty { jsonString }
        } catch (e: Exception) {
            jsonString
        }
    }
}
