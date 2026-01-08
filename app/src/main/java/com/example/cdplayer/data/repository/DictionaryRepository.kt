package com.example.cdplayer.data.repository

import com.example.cdplayer.data.remote.api.GoogleTranslateApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DictionaryRepository @Inject constructor(
    private val googleTranslateApi: GoogleTranslateApi
) {
    suspend fun translate(text: String): Result<Pair<String, String>> = withContext(Dispatchers.IO) {
        try {
            val translated = translateWithGoogle(text.trim())
            Result.success(Pair(text.trim(), translated))
        } catch (e: Exception) {
            Result.failure(Exception("번역 중 오류가 발생했습니다"))
        }
    }

    private suspend fun translateWithGoogle(text: String): String {
        return try {
            val response = googleTranslateApi.translate(text = text)
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
