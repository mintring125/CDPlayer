package com.example.cdplayer.ui.screens.library

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.cdplayer.ui.components.AudioItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArtistDetailScreen(
    onNavigateBack: () -> Unit,
    onNavigateToPlayer: () -> Unit,
    onNavigateToEditMetadata: (Long) -> Unit,
    viewModel: DetailViewModel = hiltViewModel()
) {
    val tracks by viewModel.tracks.collectAsState()
    val playbackState by viewModel.playbackState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = viewModel.title,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "뒤로")
                    }
                }
            )
        }
    ) { paddingValues ->
        if (tracks.isEmpty()) {
            Box(modifier = Modifier.padding(paddingValues)) {
                // Loading or Empty state
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentPadding = PaddingValues(bottom = 80.dp)
            ) {
                items(tracks) { audioFile ->
                    AudioItem(
                        audioFile = audioFile,
                        onClick = {
                            viewModel.playTracks(tracks, tracks.indexOf(audioFile))
                            onNavigateToPlayer()
                        },
                        isPlaying = playbackState.currentTrack?.id == audioFile.id,
                        onAddToQueue = { viewModel.addToQueue(audioFile) },
                        onEditMetadata = { onNavigateToEditMetadata(audioFile.id) }
                    )
                }
            }
        }
    }
}
