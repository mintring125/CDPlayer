package com.example.cdplayer.ui.navigation

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Home : Screen("home")
    object Library : Screen("library")
    object Search : Screen("search")
    object Settings : Screen("settings")
    object Player : Screen("player")
    object Playlist : Screen("playlist/{playlistId}") {
        fun createRoute(playlistId: Long) = "playlist/$playlistId"
    }
    object CreatePlaylist : Screen("create_playlist")
    object CoverArt : Screen("cover_art/{audioId}") {
        fun createRoute(audioId: Long) = "cover_art/$audioId"
    }
    object AlbumDetail : Screen("album/{albumName}") {
        fun createRoute(albumName: String) = "album/${android.net.Uri.encode(albumName)}"
    }
    object ArtistDetail : Screen("artist/{artistName}") {
        fun createRoute(artistName: String) = "artist/${android.net.Uri.encode(artistName)}"
    }
    object EditMetadata : Screen("edit/{audioId}") {
        fun createRoute(audioId: Long) = "edit/$audioId"
    }
    object Books : Screen("books")
    object PdfViewer : Screen("pdf_viewer/{filePath}") {
        fun createRoute(filePath: String) = "pdf_viewer/${android.net.Uri.encode(filePath)}"
    }
}
