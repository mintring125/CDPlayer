package com.example.cdplayer.ui.screens.playlist

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.PlaylistPlay
import androidx.compose.material.icons.filled.Shuffle
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.cdplayer.domain.model.AudioFile
import com.example.cdplayer.ui.components.AudioItem
import com.example.cdplayer.ui.components.CoverArt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlaylistDetailScreen(
    playlistId: Long,
    viewModel: PlaylistViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit,
    onNavigateToPlayer: () -> Unit,
    onNavigateToEditMetadata: (Long) -> Unit
) {
    LaunchedEffect(playlistId) {
        viewModel.loadPlaylist(playlistId)
    }

    val playlistWithTracks by viewModel.playlistWithTracks.collectAsState()
    val playbackState by viewModel.playbackState.collectAsState()

    val playlist = playlistWithTracks?.playlist
    val tracks = playlistWithTracks?.tracks ?: emptyList()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(playlist?.name ?: "플레이리스트") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "뒤로"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(bottom = 80.dp)
        ) {
            // Playlist Header
            item {
                PlaylistHeader(
                    playlist = playlist,
                    trackCount = tracks.size,
                    onPlayAll = {
                        viewModel.playAll()
                        onNavigateToPlayer()
                    },
                    onShuffle = {
                        viewModel.shufflePlay()
                        onNavigateToPlayer()
                    }
                )
            }

            // Track List
            if (tracks.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "플레이리스트가 비어있습니다",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            } else {
                itemsIndexed(tracks) { index, audioFile ->
                    PlaylistTrackItem(
                        audioFile = audioFile,
                        isPlaying = playbackState.currentTrack?.id == audioFile.id,
                        onClick = {
                            viewModel.playTrack(index)
                            onNavigateToPlayer()
                        },
                        onAddToQueue = { viewModel.addToQueue(audioFile) },
                        onRemove = { viewModel.removeTrack(audioFile.id) },
                        onEditMetadata = { onNavigateToEditMetadata(audioFile.id) }
                    )
                }
            }
        }
    }
}

@Composable
fun PlaylistHeader(
    playlist: com.example.cdplayer.domain.model.Playlist?,
    trackCount: Int,
    onPlayAll: () -> Unit,
    onShuffle: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Cover
        Card(
            modifier = Modifier.size(200.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                if (playlist?.coverArtPath != null) {
                    CoverArt(
                        coverArtPath = playlist.coverArtPath,
                        coverArtUri = null,
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.PlaylistPlay,
                        contentDescription = null,
                        modifier = Modifier.size(80.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = playlist?.name ?: "",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )

        if (playlist?.description != null) {
            Text(
                text = playlist.description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Text(
            text = "${trackCount}곡 • ${playlist?.formattedDuration ?: ""}",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Action Buttons
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Button(
                onClick = onPlayAll,
                enabled = trackCount > 0
            ) {
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Text("재생", modifier = Modifier.padding(start = 4.dp))
            }

            OutlinedButton(
                onClick = onShuffle,
                enabled = trackCount > 0
            ) {
                Icon(
                    imageVector = Icons.Default.Shuffle,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Text("셔플", modifier = Modifier.padding(start = 4.dp))
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun PlaylistTrackItem(
    audioFile: AudioFile,
    isPlaying: Boolean,
    onClick: () -> Unit,
    onAddToQueue: () -> Unit,
    onRemove: () -> Unit,
    onEditMetadata: () -> Unit
) {
    AudioItem(
        audioFile = audioFile,
        onClick = onClick,
        isPlaying = isPlaying,
        onAddToQueue = onAddToQueue,
        onRemoveFromPlaylist = onRemove,
        onEditMetadata = onEditMetadata
    )
}
