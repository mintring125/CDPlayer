package com.example.cdplayer.ui.screens.library

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.cdplayer.ui.components.AudioItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlbumDetailScreen(
    onNavigateBack: () -> Unit,
    onNavigateToPlayer: () -> Unit,
    onNavigateToEditMetadata: (Long) -> Unit,
    onAlbumRenamed: (String) -> Unit,
    viewModel: DetailViewModel = hiltViewModel()
) {
    val tracks by viewModel.tracks.collectAsState()
    val playbackState by viewModel.playbackState.collectAsState()
    var showRenameDialog by remember { mutableStateOf(false) }

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
                },
                actions = {
                    IconButton(onClick = { showRenameDialog = true }) {
                        Icon(Icons.Default.Edit, contentDescription = "앨범명 변경")
                    }
                }
            )
        }
    ) { paddingValues ->
        if (tracks.isEmpty()) {
            Box(modifier = Modifier.padding(paddingValues)) {
                // Loading or Empty state could be here
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

        if (showRenameDialog) {
            RenameAlbumDialog(
                currentName = viewModel.title,
                onDismiss = { showRenameDialog = false },
                onConfirm = { newName ->
                    viewModel.updateAlbumName(newName) {
                        showRenameDialog = false
                        onAlbumRenamed(newName)
                    }
                }
            )
        }
    }
}

@Composable
fun RenameAlbumDialog(
    currentName: String,
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    var text by remember { mutableStateOf(currentName) }

    androidx.compose.material3.AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("앨범명 변경") },
        text = {
            androidx.compose.material3.TextField(
                value = text,
                onValueChange = { text = it },
                singleLine = true
            )
        },
        confirmButton = {
            androidx.compose.material3.TextButton(
                onClick = { onConfirm(text) }
            ) {
                Text("저장")
            }
        },
        dismissButton = {
            androidx.compose.material3.TextButton(onClick = onDismiss) {
                Text("취소")
            }
        }
    )
}
