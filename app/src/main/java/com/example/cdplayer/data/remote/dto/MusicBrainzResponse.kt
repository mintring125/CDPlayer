package com.example.cdplayer.data.remote.dto

import com.google.gson.annotations.SerializedName

data class MusicBrainzReleaseSearchResponse(
    @SerializedName("releases")
    val releases: List<MusicBrainzRelease>?
)

data class MusicBrainzRelease(
    @SerializedName("id")
    val id: String?,
    @SerializedName("title")
    val title: String?,
    @SerializedName("artist-credit")
    val artistCredit: List<MusicBrainzArtistCredit>?,
    @SerializedName("date")
    val date: String?,
    @SerializedName("country")
    val country: String?
) {
    fun getArtistName(): String? {
        return artistCredit?.firstOrNull()?.artist?.name
    }

    fun getCoverArtUrl(): String? {
        return id?.let { "https://coverartarchive.org/release/$it/front-500" }
    }
}

data class MusicBrainzArtistCredit(
    @SerializedName("artist")
    val artist: MusicBrainzArtist?
)

data class MusicBrainzArtist(
    @SerializedName("id")
    val id: String?,
    @SerializedName("name")
    val name: String?
)

data class CoverArtArchiveResponse(
    @SerializedName("images")
    val images: List<CoverArtImage>?
)

data class CoverArtImage(
    @SerializedName("front")
    val front: Boolean?,
    @SerializedName("image")
    val image: String?,
    @SerializedName("thumbnails")
    val thumbnails: CoverArtThumbnails?
)

data class CoverArtThumbnails(
    @SerializedName("small")
    val small: String?,
    @SerializedName("large")
    val large: String?,
    @SerializedName("500")
    val medium: String?
)
