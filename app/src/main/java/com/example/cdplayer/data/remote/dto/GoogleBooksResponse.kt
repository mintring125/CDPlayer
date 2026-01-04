package com.example.cdplayer.data.remote.dto

import com.google.gson.annotations.SerializedName

data class GoogleBooksResponse(
    @SerializedName("totalItems")
    val totalItems: Int?,
    @SerializedName("items")
    val items: List<GoogleBookItem>?
)

data class GoogleBookItem(
    @SerializedName("id")
    val id: String?,
    @SerializedName("volumeInfo")
    val volumeInfo: GoogleBookVolumeInfo?
)

data class GoogleBookVolumeInfo(
    @SerializedName("title")
    val title: String?,
    @SerializedName("authors")
    val authors: List<String>?,
    @SerializedName("publisher")
    val publisher: String?,
    @SerializedName("publishedDate")
    val publishedDate: String?,
    @SerializedName("description")
    val description: String?,
    @SerializedName("imageLinks")
    val imageLinks: GoogleBookImageLinks?,
    @SerializedName("categories")
    val categories: List<String>?
) {
    fun getBestImageUrl(): String? {
        return imageLinks?.let {
            // HTTPS로 변환하고 가장 큰 이미지 선택
            (it.extraLarge ?: it.large ?: it.medium ?: it.thumbnail ?: it.smallThumbnail)
                ?.replace("http://", "https://")
                ?.replace("&edge=curl", "")  // 페이지 말림 효과 제거
        }
    }
}

data class GoogleBookImageLinks(
    @SerializedName("smallThumbnail")
    val smallThumbnail: String?,
    @SerializedName("thumbnail")
    val thumbnail: String?,
    @SerializedName("small")
    val small: String?,
    @SerializedName("medium")
    val medium: String?,
    @SerializedName("large")
    val large: String?,
    @SerializedName("extraLarge")
    val extraLarge: String?
)
