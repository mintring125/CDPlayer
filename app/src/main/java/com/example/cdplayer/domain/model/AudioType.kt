package com.example.cdplayer.domain.model

enum class AudioType {
    MUSIC,
    AUDIOBOOK;

    companion object {
        fun fromString(value: String?): AudioType {
            return when (value?.uppercase()) {
                "AUDIOBOOK" -> AUDIOBOOK
                else -> MUSIC
            }
        }
    }
}
