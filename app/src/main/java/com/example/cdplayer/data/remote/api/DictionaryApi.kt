package com.example.cdplayer.data.remote.api

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface DictionaryApi {
    @GET("api/v2/entries/en/{word}")
    suspend fun getDefinition(@Path("word") word: String): List<DictionaryResponse>

    companion object {
        const val BASE_URL = "https://api.dictionaryapi.dev/"
    }
}

interface GoogleTranslateApi {
    @GET("translate_a/single")
    suspend fun translate(
        @Query("client") client: String = "gtx",
        @Query("sl") sourceLang: String = "en",
        @Query("tl") targetLang: String = "ko",
        @Query("dt") dataType: String = "t",
        @Query("q") text: String
    ): okhttp3.ResponseBody

    companion object {
        const val BASE_URL = "https://translate.googleapis.com/"
    }
}

data class DictionaryResponse(
    val word: String,
    val phonetic: String?,
    val phonetics: List<Phonetic>,
    val meanings: List<Meaning>
)

data class Phonetic(
    val text: String?,
    val audio: String?
)

data class Meaning(
    val partOfSpeech: String,
    val definitions: List<Definition>
)

data class Definition(
    val definition: String,
    val example: String?
)

// 단어 검색 결과 (깔끔한 버전)
data class DictionaryResultWithTranslation(
    val word: String,
    val phonetic: String?,
    val meanings: List<SimpleMeaning>  // 품사별 간단한 뜻
)

data class SimpleMeaning(
    val partOfSpeech: String,        // 영어 품사
    val partOfSpeechKorean: String,  // 한글 품사
    val koreanMeaning: String        // 한글 뜻 (간단하게)
)
