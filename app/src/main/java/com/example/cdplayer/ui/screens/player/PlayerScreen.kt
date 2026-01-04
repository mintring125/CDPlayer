package com.example.cdplayer.ui.screens.player

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.QueueMusic
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.cdplayer.domain.model.Bookmark
import com.example.cdplayer.ui.components.AudioItemCompact
import com.example.cdplayer.ui.components.LargeCoverArt
import com.example.cdplayer.ui.components.PlaybackControls

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayerScreen(
    viewModel: PlayerViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit
) {
    val playbackState by viewModel.playbackState.collectAsState()
    val uiState by viewModel.uiState.collectAsState()
    val bookmarks by viewModel.bookmarks.collectAsState()
    val currentTrack = playbackState.currentTrack

    var showMenu by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.Default.ExpandMore,
                            contentDescription = "닫기"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { viewModel.showQueueSheet() }) {
                        Icon(
                            imageVector = Icons.Default.QueueMusic,
                            contentDescription = "재생 대기열"
                        )
                    }
                    IconButton(onClick = { showMenu = true }) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "더보기"
                        )
                    }

                    DropdownMenu(
                        expanded = showMenu,
                        onDismissRequest = { showMenu = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("재생 속도") },
                            onClick = {
                                showMenu = false
                                viewModel.showSpeedDialog()
                            },
                            leadingIcon = {
                                Icon(Icons.Default.Speed, contentDescription = null)
                            },
                            trailingIcon = {
                                Text("${playbackState.playbackSpeed}x")
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("슬립 타이머") },
                            onClick = {
                                showMenu = false
                                viewModel.showSleepTimerDialog()
                            },
                            leadingIcon = {
                                Icon(Icons.Default.Timer, contentDescription = null)
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("북마크 추가") },
                            onClick = {
                                showMenu = false
                                viewModel.showBookmarkDialog()
                            },
                            leadingIcon = {
                                Icon(Icons.Default.BookmarkBorder, contentDescription = null)
                            }
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        androidx.compose.foundation.layout.BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            val isLandscape = maxWidth > maxHeight

            if (isLandscape) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 24.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Cover Art (Left)
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                         LargeCoverArt(
                            coverArtPath = currentTrack?.coverArtPath,
                            coverArtUri = currentTrack?.coverArtUri,
                            modifier = Modifier
                                .fillMaxSize(0.8f) // Reduce size slightly for visual balance
                                .aspectRatio(1f)
                        )
                    }
                   
                    Spacer(modifier = Modifier.size(24.dp))

                    // Controls and Info (Right)
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                         // Track Info
                        Text(
                            text = currentTrack?.displayTitle ?: "재생 중인 트랙 없음",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = currentTrack?.displayArtist ?: "",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        
                        // Bookmarks indicator
                        if (bookmarks.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(16.dp))
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Bookmark,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp),
                                    tint = MaterialTheme.colorScheme.primary
                                )
                                Text(
                                    text = "${bookmarks.size}개의 북마크",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(32.dp))

                        PlaybackControls(
                            isPlaying = playbackState.isPlaying,
                            currentPosition = playbackState.currentPosition,
                            duration = playbackState.duration,
                            repeatMode = playbackState.repeatMode,
                            shuffleEnabled = playbackState.shuffleEnabled,
                            onPlayPause = { viewModel.playPause() },
                            onPrevious = { viewModel.previous() },
                            onNext = { viewModel.next() },
                            onSeek = { viewModel.seekTo(it) },
                            onRepeatModeChange = { viewModel.toggleRepeatMode() },
                            onShuffleChange = { viewModel.toggleShuffle() },
                            onSkipBackward = { viewModel.skipBackward() },
                            onSkipForward = { viewModel.skipForward() }
                        )
                    }
                }
            } else {
                // Portrait Layout
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(24.dp))

                    // Cover Art
                    LargeCoverArt(
                        coverArtPath = currentTrack?.coverArtPath,
                        coverArtUri = currentTrack?.coverArtUri,
                        modifier = Modifier
                            .fillMaxWidth(0.85f)
                            .aspectRatio(1f)
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    // Track Info
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = currentTrack?.displayTitle ?: "재생 중인 트랙 없음",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = currentTrack?.displayArtist ?: "",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )

                        if (currentTrack?.album != null) {
                            Text(
                                text = currentTrack.album,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }

                    // Bookmarks indicator
                    if (bookmarks.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(16.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Bookmark,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp),
                                tint = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                text = "${bookmarks.size}개의 북마크",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    // Playback Controls
                    PlaybackControls(
                        isPlaying = playbackState.isPlaying,
                        currentPosition = playbackState.currentPosition,
                        duration = playbackState.duration,
                        repeatMode = playbackState.repeatMode,
                        shuffleEnabled = playbackState.shuffleEnabled,
                        onPlayPause = { viewModel.playPause() },
                        onPrevious = { viewModel.previous() },
                        onNext = { viewModel.next() },
                        onSeek = { viewModel.seekTo(it) },
                        onRepeatModeChange = { viewModel.toggleRepeatMode() },
                        onShuffleChange = { viewModel.toggleShuffle() },
                        onSkipBackward = { viewModel.skipBackward() },
                        onSkipForward = { viewModel.skipForward() }
                    )

                    Spacer(modifier = Modifier.height(32.dp))
                }
            }
        }
    }

    // Speed Dialog
    if (uiState.showSpeedDialog) {
        SpeedDialog(
            currentSpeed = playbackState.playbackSpeed,
            onSpeedSelected = { viewModel.setPlaybackSpeed(it) },
            onDismiss = { viewModel.hideSpeedDialog() }
        )
    }

    // Sleep Timer Dialog
    if (uiState.showSleepTimerDialog) {
        SleepTimerDialog(
            onTimerSelected = { viewModel.setSleepTimer(it) },
            onDismiss = { viewModel.hideSleepTimerDialog() }
        )
    }

    // Bookmark Dialog
    if (uiState.showBookmarkDialog) {
        BookmarkDialog(
            onAddBookmark = { viewModel.addBookmark(it) },
            onDismiss = { viewModel.hideBookmarkDialog() }
        )
    }

    // Queue Sheet
    if (uiState.showQueueSheet) {
        ModalBottomSheet(
            onDismissRequest = { viewModel.hideQueueSheet() },
            sheetState = rememberModalBottomSheetState()
        ) {
            QueueSheet(
                queue = playbackState.queue,
                currentIndex = playbackState.currentQueueIndex
            )
        }
    }
}

@Composable
fun SpeedDialog(
    currentSpeed: Float,
    onSpeedSelected: (Float) -> Unit,
    onDismiss: () -> Unit
) {
    val speeds = listOf(0.5f, 0.75f, 1.0f, 1.25f, 1.5f, 1.75f, 2.0f)

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("재생 속도") },
        text = {
            Column {
                speeds.forEach { speed ->
                    TextButton(
                        onClick = { onSpeedSelected(speed) },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "${speed}x",
                            color = if (speed == currentSpeed) MaterialTheme.colorScheme.primary
                                   else MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("취소")
            }
        }
    )
}

@Composable
fun SleepTimerDialog(
    onTimerSelected: (Int) -> Unit,
    onDismiss: () -> Unit
) {
    val timers = listOf(
        0 to "끄기",
        5 to "5분",
        10 to "10분",
        15 to "15분",
        30 to "30분",
        60 to "1시간"
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("슬립 타이머") },
        text = {
            Column {
                timers.forEach { (minutes, label) ->
                    TextButton(
                        onClick = { onTimerSelected(minutes) },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(label)
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("취소")
            }
        }
    )
}

@Composable
fun BookmarkDialog(
    onAddBookmark: (String?) -> Unit,
    onDismiss: () -> Unit
) {
    var note by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("북마크 추가") },
        text = {
            OutlinedTextField(
                value = note,
                onValueChange = { note = it },
                label = { Text("메모 (선택)") },
                modifier = Modifier.fillMaxWidth()
            )
        },
        confirmButton = {
            TextButton(onClick = { onAddBookmark(note.takeIf { it.isNotBlank() }) }) {
                Text("추가")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("취소")
            }
        }
    )
}

@Composable
fun QueueSheet(
    queue: List<com.example.cdplayer.domain.model.AudioFile>,
    currentIndex: Int
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = "재생 대기열",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        if (queue.isEmpty()) {
            Text(
                text = "대기열이 비어있습니다",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        } else {
            LazyColumn {
                items(queue) { audioFile ->
                    AudioItemCompact(
                        audioFile = audioFile,
                        onClick = { },
                        isPlaying = queue.indexOf(audioFile) == currentIndex
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}
