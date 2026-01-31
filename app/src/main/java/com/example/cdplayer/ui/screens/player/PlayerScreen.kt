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
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.QueueMusic
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Close
import androidx.compose.foundation.clickable
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Divider
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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import android.speech.tts.TextToSpeech
import java.util.Locale
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
    var searchQuery by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.Default.ExpandMore,
                            contentDescription = "닫기"
                        )
                    }
                },
                title = {
                    // Dictionary Search in Header - Kid-friendly rounded style
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        placeholder = {
                            Text(
                                "영어 단어 검색",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                            )
                        },
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        textStyle = MaterialTheme.typography.bodyMedium,
                        shape = RoundedCornerShape(24.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                            unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.outline
                        ),
                        trailingIcon = {
                            if (uiState.isDictionaryLoading) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(18.dp),
                                    strokeWidth = 2.dp,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            } else {
                                IconButton(
                                    onClick = {
                                        if (searchQuery.isNotBlank()) {
                                            viewModel.searchWord(searchQuery)
                                        }
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Search,
                                        contentDescription = "검색",
                                        modifier = Modifier.size(20.dp),
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                }
                            }
                        },
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                        keyboardActions = KeyboardActions(
                            onSearch = {
                                if (searchQuery.isNotBlank()) {
                                    viewModel.searchWord(searchQuery)
                                }
                            }
                        )
                    )
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
                        // 챕터 메뉴 (챕터가 있을 때만 표시)
                        if (playbackState.hasChapters) {
                            DropdownMenuItem(
                                text = { Text("챕터") },
                                onClick = {
                                    showMenu = false
                                    viewModel.showChapterDialog()
                                },
                                leadingIcon = {
                                    Icon(Icons.Default.List, contentDescription = null)
                                },
                                trailingIcon = {
                                    Text("${playbackState.chapters.size}개")
                                }
                            )
                        }
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
                            onSkipForward = { viewModel.skipForward() },
                            bookmarks = bookmarks,
                            chapters = playbackState.chapters
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
                    Spacer(modifier = Modifier.weight(1f))

                    // Cover Art
                    LargeCoverArt(
                        coverArtPath = currentTrack?.coverArtPath,
                        coverArtUri = currentTrack?.coverArtUri,
                        modifier = Modifier
                            .fillMaxWidth(0.65f)
                            .aspectRatio(1f)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

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
                        onSkipForward = { viewModel.skipForward() },
                        bookmarks = bookmarks,
                        chapters = playbackState.chapters
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
            bookmarks = bookmarks,
            currentPosition = playbackState.currentPosition,
            onAddBookmark = { viewModel.addBookmark(it) },
            onSeekToBookmark = { viewModel.seekToBookmark(it) },
            onDeleteBookmark = { viewModel.deleteBookmark(it.id) },
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

    // Dictionary Result Dialog
    uiState.dictionaryResult?.let { result ->
        when (result) {
            is DictionaryResult.Translation -> {
                TranslationResultDialog(
                    original = result.original,
                    translated = result.translated,
                    isKoreanToEnglish = result.isKoreanToEnglish,
                    onDismiss = { viewModel.clearDictionaryResult() }
                )
            }
            is DictionaryResult.Error -> {
                AlertDialog(
                    onDismissRequest = { viewModel.clearDictionaryResult() },
                    title = { Text("검색 결과") },
                    text = { Text(result.message) },
                    confirmButton = {
                        TextButton(onClick = { viewModel.clearDictionaryResult() }) {
                            Text("확인")
                        }
                    }
                )
            }
        }
    }

    // Chapter Dialog
    if (uiState.showChapterDialog && playbackState.hasChapters) {
        ChapterDialog(
            chapters = playbackState.chapters,
            currentChapter = playbackState.currentChapter,
            onChapterSelected = { viewModel.seekToChapter(it) },
            onDismiss = { viewModel.hideChapterDialog() }
        )
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
    bookmarks: List<com.example.cdplayer.domain.model.Bookmark>,
    currentPosition: Long,
    onAddBookmark: (String?) -> Unit,
    onSeekToBookmark: (com.example.cdplayer.domain.model.Bookmark) -> Unit,
    onDeleteBookmark: (com.example.cdplayer.domain.model.Bookmark) -> Unit,
    onDismiss: () -> Unit
) {
    var note by remember { mutableStateOf("") }
    var showAddForm by remember { mutableStateOf(false) }

    // Format current position
    val currentPositionFormatted = remember(currentPosition) {
        val totalSeconds = currentPosition / 1000
        val hours = totalSeconds / 3600
        val minutes = (totalSeconds % 3600) / 60
        val seconds = totalSeconds % 60
        if (hours > 0) {
            String.format("%d:%02d:%02d", hours, minutes, seconds)
        } else {
            String.format("%d:%02d", minutes, seconds)
        }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Bookmark,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
                Text("북마크")
            }
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                // Add bookmark section
                if (showAddForm) {
                    Text(
                        text = "현재 위치: $currentPositionFormatted",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = note,
                        onValueChange = { note = it },
                        label = { Text("메모 (선택)") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        TextButton(onClick = { showAddForm = false }) {
                            Text("취소")
                        }
                        TextButton(
                            onClick = {
                                onAddBookmark(note.takeIf { it.isNotBlank() })
                                note = ""
                                showAddForm = false
                            }
                        ) {
                            Text("저장")
                        }
                    }
                } else {
                    TextButton(
                        onClick = { showAddForm = true },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(
                            imageVector = Icons.Default.BookmarkBorder,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.size(8.dp))
                        Text("현재 위치에 북마크 추가 ($currentPositionFormatted)")
                    }
                }

                if (bookmarks.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Divider()
                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "저장된 북마크 (${bookmarks.size})",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    LazyColumn(
                        modifier = Modifier.height(200.dp)
                    ) {
                        items(bookmarks) { bookmark ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                                    .padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                // Bookmark info (clickable to seek)
                                Column(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(4.dp))
                                ) {
                                    TextButton(
                                        onClick = { onSeekToBookmark(bookmark) },
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Column(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalAlignment = Alignment.Start
                                        ) {
                                            Text(
                                                text = bookmark.formattedPosition,
                                                style = MaterialTheme.typography.titleSmall,
                                                fontWeight = FontWeight.Bold,
                                                color = MaterialTheme.colorScheme.primary
                                            )
                                            if (!bookmark.note.isNullOrBlank()) {
                                                Spacer(modifier = Modifier.height(2.dp))
                                                Text(
                                                    text = bookmark.note,
                                                    style = MaterialTheme.typography.bodySmall,
                                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                                    maxLines = 1,
                                                    overflow = TextOverflow.Ellipsis
                                                )
                                            }
                                        }
                                    }
                                }

                                // Delete button
                                IconButton(
                                    onClick = { onDeleteBookmark(bookmark) },
                                    modifier = Modifier.size(32.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Close,
                                        contentDescription = "삭제",
                                        tint = MaterialTheme.colorScheme.error,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }
                } else if (!showAddForm) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "저장된 북마크가 없습니다",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("닫기")
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

@Composable
fun TranslationResultDialog(
    original: String,
    translated: String,
    isKoreanToEnglish: Boolean = false,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current

    // TTS 초기화
    var ttsReady by remember { mutableStateOf(false) }
    val tts = remember {
        TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                ttsReady = true
            }
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            tts.stop()
            tts.shutdown()
        }
    }

    // 영어 텍스트 결정 (한글→영어면 translated가 영어, 영어→한글이면 original이 영어)
    val englishText = if (isKoreanToEnglish) translated else original
    val koreanText = if (isKoreanToEnglish) original else translated

    fun speakEnglish(text: String) {
        if (ttsReady) {
            tts.language = Locale.US
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
        }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("번역 결과") },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                if (isKoreanToEnglish) {
                    // 한글 → 영어
                    // 원문 (한글)
                    Text(
                        text = "한글",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = koreanText,
                        style = MaterialTheme.typography.bodyMedium
                    )

                    Spacer(modifier = Modifier.height(16.dp))
                    Divider()
                    Spacer(modifier = Modifier.height(16.dp))

                    // 번역 (영어) - 클릭하면 TTS
                    Text(
                        text = "영어 (터치하면 발음)",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = englishText,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .clickable { speakEnglish(englishText) }
                            .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f))
                            .padding(8.dp)
                    )
                } else {
                    // 영어 → 한글
                    // 원문 (영어) - 클릭하면 TTS
                    Text(
                        text = "영어 (터치하면 발음)",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = englishText,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .clickable { speakEnglish(englishText) }
                            .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f))
                            .padding(8.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))
                    Divider()
                    Spacer(modifier = Modifier.height(16.dp))

                    // 번역 (한글)
                    Text(
                        text = "한글",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = koreanText,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("닫기")
            }
        }
    )
}

@Composable
fun ChapterDialog(
    chapters: List<com.example.cdplayer.domain.model.Chapter>,
    currentChapter: com.example.cdplayer.domain.model.Chapter?,
    onChapterSelected: (com.example.cdplayer.domain.model.Chapter) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { 
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.List,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
                Text("챕터 (${chapters.size}개)")
            }
        },
        text = {
            LazyColumn {
                items(chapters) { chapter ->
                    val isCurrentChapter = chapter.index == currentChapter?.index
                    
                    TextButton(
                        onClick = { onChapterSelected(chapter) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(
                                    if (isCurrentChapter) MaterialTheme.colorScheme.primaryContainer
                                    else MaterialTheme.colorScheme.surface,
                                    RoundedCornerShape(8.dp)
                                )
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = chapter.title,
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = if (isCurrentChapter) FontWeight.Bold else FontWeight.Normal,
                                color = if (isCurrentChapter) MaterialTheme.colorScheme.onPrimaryContainer
                                        else MaterialTheme.colorScheme.onSurface,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.weight(1f)
                            )
                            
                            Text(
                                text = chapter.formatStartTime(),
                                style = MaterialTheme.typography.bodySmall,
                                color = if (isCurrentChapter) MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                                        else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("닫기")
            }
        }
    )
}
