package com.example.cdplayer.data.remote.dto

import com.google.gson.annotations.SerializedName

data class LastFmAlbumInfoResponse(
    @SerializedName("album")
    val album: LastFmAlbum?
)

data class LastFmAlbumSearchResponse(
    @SerializedName("results")
    val results: LastFmSearchResults?
)

data class LastFmSearchResults(
    @SerializedName("albummatches")
    val albumMatches: LastFmAlbumMatches?
)

data class LastFmAlbumMatches(
    @SerializedName("album")
    val albums: List<LastFmAlbum>?
)

data class LastFmAlbum(
    @SerializedName("name")
    val name: String?,
    @SerializedName("artist")
    val artist: String?,
    @SerializedName("image")
    val images: List<LastFmImage>?
) {
    fun getLargestImageUrl(): String? {
        return images?.findLast { it.size == "extralarge" || it.size == "large" || it.size == "medium" }?.url
            ?.takeIf { it.isNotBlank() }
    }
}

data class LastFmImage(
    @SerializedName("#text")
    val url: String?,
    @SerializedName("size")
    val size: String?
)

data class LastFmArtistInfoResponse(
    @SerializedName("artist")
    val artist: LastFmArtist?
)

data class LastFmArtist(
    @SerializedName("name")
    val name: String?,
    @SerializedName("image")
    val images: List<LastFmImage>?
) {
    fun getLargestImageUrl(): String? {
        return images?.findLast { it.size == "extralarge" || it.size == "large" || it.size == "medium" }?.url
            ?.takeIf { it.isNotBlank() }
    }
}
