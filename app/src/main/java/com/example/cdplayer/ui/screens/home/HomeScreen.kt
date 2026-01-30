package com.example.cdplayer.ui.screens.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.foundation.combinedClickable
import androidx.compose.material.icons.filled.CheckCircle

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.ui.res.painterResource
import com.example.cdplayer.R
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import android.media.AudioManager
import android.media.ToneGenerator
import com.example.cdplayer.domain.model.AudioFile
import com.example.cdplayer.ui.components.AudioItem
import com.example.cdplayer.ui.components.BulkActionToolbar
import com.example.cdplayer.ui.components.CoverArt
import com.example.cdplayer.ui.components.FolderPickerDialog
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material.icons.filled.GroupWork
import androidx.compose.material.icons.filled.Delete

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    onNavigateToPlayer: () -> Unit,
    onNavigateToAlbum: (String) -> Unit,
    onNavigateToEditMetadata: (Long) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val recentlyPlayed by viewModel.recentlyPlayed.collectAsState()
    val favorites by viewModel.favorites.collectAsState()
    // val allMusic by viewModel.allMusic.collectAsState() // Removed
    val allAudiobooks by viewModel.allAudiobooks.collectAsState()
    val audiobookAlbums by viewModel.audiobookAlbums.collectAsState()
    val playbackState by viewModel.playbackState.collectAsState()
    val selectionState by viewModel.selectionState.collectAsState()
    val audiobookSelectionState by viewModel.audiobookSelectionState.collectAsState()
    var showFolderPicker by remember { mutableStateOf(false) }
    var selectedAudiobookAlbum by remember { mutableStateOf<AudiobookAlbum?>(null) }
    var showAlbumNameDialog by remember { mutableStateOf(false) }
    var newAlbumName by remember { mutableStateOf("") }
    var showDeleteConfirmDialog by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            topBar = {
                if (selectionState.isSelectionMode) {
                    TopAppBar(
                        title = {
                            Text(
                                text = "${selectionState.selectedIds.size}개 선택됨",
                                fontWeight = FontWeight.Bold
                            )
                        },
                        navigationIcon = {
                            IconButton(onClick = { viewModel.clearSelection() }) {
                                Icon(Icons.Default.Close, contentDescription = "취소")
                            }
                        },
                        colors = androidx.compose.material3.TopAppBarDefaults.topAppBarColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant
                        )
                    )
                } else {
                    TopAppBar(
                        title = {
                            Text(
                                text = "CD Player",
                                fontWeight = FontWeight.Bold
                            )
                        },
                        actions = {
                            if (uiState.isScanning) {
                                CircularProgressIndicator(
                                    modifier = Modifier
                                        .size(24.dp)
                                        .padding(end = 16.dp),
                                    strokeWidth = 2.dp
                                )
                            } else {
                                IconButton(onClick = { viewModel.scanMedia() }) {
                                    Icon(
                                        imageVector = Icons.Default.Refresh,
                                        contentDescription = "미디어 스캔"
                                    )
                                }
                            }
                        }
                    )
                }
            }
        ) { paddingValues ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentPadding = PaddingValues(bottom = if (selectionState.isSelectionMode) 140.dp else 80.dp)
            ) {
                // Scanning indicator
                if (uiState.isScanning && uiState.scanProgress != null) {
                    item {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            Text(
                                text = "미디어 스캔 중... (${uiState.scanProgress!!.current}/${uiState.scanProgress!!.total})",
                                style = MaterialTheme.typography.bodySmall
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            LinearProgressIndicator(
                                progress = uiState.scanProgress!!.current.toFloat() / uiState.scanProgress!!.total.coerceAtLeast(1),
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }

                // Recently Played (항상 표시 - 더미 카드 포함)
                item {
                    SectionHeader(
                        title = "최근 재생",
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                    )
                }

                item {
                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // 첫 번째: 인사 카드 (더미)
                        item {
                            GreetingCard()
                        }

                        // 나머지: 실제 최근 재생 목록
                        items(recentlyPlayed) { audioFile ->
                            AlbumCard(
                                audioFile = audioFile,
                                onClick = {
                                    if (!selectionState.isSelectionMode) {
                                        viewModel.playTrack(audioFile)
                                        onNavigateToPlayer()
                                    }
                                },
                                showFavoriteIcon = true,
                                isFavorite = audioFile.isFavorite,
                                onFavoriteClick = { viewModel.toggleFavorite(audioFile.id) }
                            )
                        }
                    }
                }

                item { Spacer(modifier = Modifier.height(24.dp)) }

                // Audiobooks (앨범별 그룹화)
                if (audiobookAlbums.isNotEmpty()) {
                    item {
                        SectionHeader(
                            title = if (audiobookSelectionState.isSelectionMode) 
                                "${audiobookSelectionState.selectedIds.size}개 선택됨" 
                            else "오디오북",
                            subtitle = if (audiobookSelectionState.isSelectionMode) 
                                "길게 누르면 선택" 
                            else "${audiobookAlbums.size}개 앨범",
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                        )
                    }

                    item {
                        LazyRow(
                            contentPadding = PaddingValues(horizontal = 16.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(audiobookAlbums) { album ->
                                val isAlbumSelected = album.tracks.any { it.id in audiobookSelectionState.selectedIds }
                                AudiobookAlbumCard(
                                    album = album,
                                    onClick = {
                                        if (audiobookSelectionState.isSelectionMode) {
                                            // In selection mode, toggle selection for all tracks in album
                                            if (isAlbumSelected) {
                                                // Deselect all tracks in this album
                                                album.tracks.forEach { track ->
                                                    if (track.id in audiobookSelectionState.selectedIds) {
                                                        viewModel.toggleAudiobookSelection(track.id)
                                                    }
                                                }
                                            } else {
                                                // Select all tracks in this album
                                                album.tracks.forEach { track ->
                                                    viewModel.toggleAudiobookSelection(track.id)
                                                }
                                            }
                                        } else {
                                            selectedAudiobookAlbum = album
                                        }
                                    },
                                    onLongClick = {
                                        // Start selection mode and select all tracks in this album
                                        album.tracks.forEach { track ->
                                            if (track.id !in audiobookSelectionState.selectedIds) {
                                                viewModel.toggleAudiobookSelection(track.id)
                                            }
                                        }
                                    },
                                    isSelected = isAlbumSelected
                                )
                            }
                        }
                    }

                    // Audiobook selection toolbar
                    if (audiobookSelectionState.isSelectionMode) {
                        item {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp, vertical = 8.dp),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                TextButton(
                                    onClick = { viewModel.clearAudiobookSelection() }
                                ) {
                                    Text("취소")
                                }
                                TextButton(
                                    onClick = {
                                        newAlbumName = ""
                                        showAlbumNameDialog = true
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.GroupWork,
                                        contentDescription = null,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("앨범으로 묶기")
                                }
                                TextButton(
                                    onClick = { showDeleteConfirmDialog = true }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = null,
                                        modifier = Modifier.size(18.dp),
                                        tint = MaterialTheme.colorScheme.error
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "파일 삭제",
                                        color = MaterialTheme.colorScheme.error
                                    )
                                }
                            }
                        }
                    }

                    item { Spacer(modifier = Modifier.height(24.dp)) }
                }

                // Favorite Albums (오디오북 아래로 이동됨)
                if (favorites.isNotEmpty()) {
                    item {
                        SectionHeader(
                            title = "즐겨찾기 앨범",
                            subtitle = "${favorites.size}개",
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                        )
                    }

                    item {
                        LazyRow(
                            contentPadding = PaddingValues(horizontal = 16.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(favorites) { audioFile ->
                                AlbumCard(
                                    audioFile = audioFile,
                                    onClick = {
                                        if (selectionState.isSelectionMode) {
                                            viewModel.toggleSelection(audioFile.id)
                                        } else {
                                            // Play favorite (as album/track)
                                            viewModel.playTrack(audioFile)
                                            onNavigateToPlayer()
                                        }
                                    },
                                    onLongClick = {
                                        viewModel.toggleSelection(audioFile.id)
                                    },
                                    isSelectionMode = selectionState.isSelectionMode,
                                    isSelected = audioFile.id in selectionState.selectedIds,
                                    showFavoriteIcon = true,
                                    isFavorite = audioFile.isFavorite,
                                    onFavoriteClick = { viewModel.toggleFavorite(audioFile.id) }
                                )
                            }
                        }
                    }
                }

                // Empty State
                if (!uiState.isScanning && favorites.isEmpty() && allAudiobooks.isEmpty()) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(32.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "음악이 없습니다",
                                    style = MaterialTheme.typography.titleMedium
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "기기에 음악 파일을 추가하고 스캔해주세요",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }
        }

        // Bulk Action Toolbar
        if (selectionState.isSelectionMode) {
            BulkActionToolbar(
                selectedCount = selectionState.selectedIds.size,
                onMove = { showFolderPicker = true },
                onDelete = { viewModel.deleteSelectedFiles() },
                onAddToPlaylist = { /* Show playlist picker */ },
                onChangeToAudiobook = { viewModel.changeSelectedAudioType(com.example.cdplayer.domain.model.AudioType.AUDIOBOOK) },
                onChangeToMusic = { viewModel.changeSelectedAudioType(com.example.cdplayer.domain.model.AudioType.MUSIC) },
                onCancel = { viewModel.clearSelection() },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 80.dp) // Lift above BottomNavigation
            )
        }

        if (showFolderPicker) {
            FolderPickerDialog(
                initialPath = "/storage/emulated/0/Music", // Default music path
                onDismissRequest = { showFolderPicker = false },
                onFolderSelected = { path ->
                    viewModel.moveSelectedFiles(path)
                    showFolderPicker = false
                }
            )
        }

        // Audiobook Tracks Dialog
        selectedAudiobookAlbum?.let { album ->
            AudiobookTracksDialog(
                album = album,
                onDismiss = { selectedAudiobookAlbum = null },
                onTrackClick = { track ->
                    viewModel.playTrack(track)
                    selectedAudiobookAlbum = null
                    onNavigateToPlayer()
                },
                onPlayAll = {
                    if (album.tracks.isNotEmpty()) {
                        viewModel.playTracks(album.tracks, 0)
                        selectedAudiobookAlbum = null
                        onNavigateToPlayer()
                    }
                },
                onRemoveFromAlbum = { track ->
                    viewModel.removeFromAlbum(track)
                }
            )
        }

        // Album name input dialog
        if (showAlbumNameDialog) {
            AlertDialog(
                onDismissRequest = { showAlbumNameDialog = false },
                title = { Text("앨범으로 묶기") },
                text = {
                    Column {
                        Text(
                            text = "${audiobookSelectionState.selectedIds.size}개 트랙을 선택함",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        OutlinedTextField(
                            value = newAlbumName,
                            onValueChange = { newAlbumName = it },
                            label = { Text("새 앨범 이름") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            if (newAlbumName.isNotBlank()) {
                                viewModel.groupSelectedAsAlbum(newAlbumName.trim())
                                showAlbumNameDialog = false
                            }
                        },
                        enabled = newAlbumName.isNotBlank()
                    ) {
                        Text("확인")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showAlbumNameDialog = false }) {
                        Text("취소")
                    }
                }
            )
        }

        // Delete confirmation dialog
        if (showDeleteConfirmDialog) {
            AlertDialog(
                onDismissRequest = { showDeleteConfirmDialog = false },
                title = { Text("파일 삭제") },
                text = {
                    Column {
                        Text(
                            text = "${audiobookSelectionState.selectedIds.size}개 파일을 삭제하시겠습니까?",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "이 작업은 되돌릴 수 없습니다. 실제 파일이 기기에서 삭제됩니다.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            viewModel.deleteSelectedAudiobooks()
                            showDeleteConfirmDialog = false
                        }
                    ) {
                        Text(
                            text = "삭제",
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDeleteConfirmDialog = false }) {
                        Text("취소")
                    }
                }
            )
        }
    }
}

@Composable
fun SectionHeader(
    title: String,
    modifier: Modifier = Modifier,
    subtitle: String? = null
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        if (subtitle != null) {
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun GreetingCard(
    modifier: Modifier = Modifier
) {
    // 색깔 목록 (보라색부터 시작)
    val colors = listOf(
        Color(0xFF9C27B0), // Purple
        Color(0xFF2196F3), // Blue
        Color(0xFF4CAF50), // Green
        Color(0xFFFFEB3B), // Yellow
        Color(0xFFFF9800), // Orange
        Color(0xFFE91E63)  // Pink
    )

    var colorIndex by remember { mutableIntStateOf(0) }
    var rotationTarget by remember { mutableStateOf(0f) }

    val rotation by animateFloatAsState(
        targetValue = rotationTarget,
        animationSpec = tween(durationMillis = 500),
        label = "card_rotation"
    )

    val currentColor = colors[colorIndex % colors.size]

    // 카드 뒤집기 효과음
    val toneGenerator = remember {
        try {
            ToneGenerator(AudioManager.STREAM_MUSIC, 50)
        } catch (e: Exception) {
            null
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            toneGenerator?.release()
        }
    }

    Card(
        modifier = modifier
            .width(140.dp)
            .graphicsLayer {
                rotationY = rotation
            }
            .clickable {
                // 효과음 재생 (짧은 두 톤으로 뒤집기 느낌)
                toneGenerator?.startTone(ToneGenerator.TONE_PROP_BEEP, 50)
                rotationTarget += 360f
                colorIndex = (colorIndex + 1) % colors.size
            },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = currentColor
        )
    ) {
        Column {
            Image(
                painter = painterResource(id = R.drawable.ic_launcher),
                contentDescription = "앱 아이콘",
                modifier = Modifier
                    .size(width = 140.dp, height = 140.dp)
                    .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp))
            )

            Column(
                modifier = Modifier.padding(12.dp)
            ) {
                Text(
                    text = "오늘도 화이팅!",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = Color.White
                )
                Text(
                    text = "나은이 아빠",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.White.copy(alpha = 0.8f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun AlbumCard(
    audioFile: AudioFile,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    onLongClick: () -> Unit = {},
    isSelectionMode: Boolean = false,
    isSelected: Boolean = false,
    isAudiobook: Boolean = false,
    showFavoriteIcon: Boolean = false,
    isFavorite: Boolean = false,
    onFavoriteClick: (() -> Unit)? = null
) {
    val height = if (isAudiobook) 210.dp else 140.dp
    
    Card(
        modifier = modifier
            .width(140.dp)
            .let {
                if (isSelectionMode) {
                    it.clickable(onClick = onClick) // In selection mode, click toggles selection (passed as onClick)
                } else {
                    it.combinedClickable(
                        onClick = onClick,
                        onLongClick = onLongClick
                    )
                }
            },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Box {
            Column {
                CoverArt(
                    coverArtPath = audioFile.coverArtPath,
                    coverArtUri = audioFile.coverArtUri,
                    modifier = Modifier
                        .size(width = 140.dp, height = height)
                        .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp))
                )

                Column(
                    modifier = Modifier.padding(12.dp)
                ) {
                    Text(
                        text = audioFile.displayTitle,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = audioFile.displayArtist,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            // Favorite heart icon (top-right)
            if (showFavoriteIcon) {
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp)
                        .size(28.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(
                            color = Color.Black.copy(alpha = 0.4f)
                        )
                        .clickable { onFavoriteClick?.invoke() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = if (isFavorite) "즐겨찾기 해제" else "즐겨찾기 추가",
                        tint = if (isFavorite) Color(0xFFFF4081) else Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            if (isSelected) {
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = "Selected",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(48.dp)
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AudiobookAlbumCard(
    album: AudiobookAlbum,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    onLongClick: () -> Unit = {},
    isSelected: Boolean = false
) {
    Card(
        modifier = modifier
            .width(140.dp)
            .combinedClickable(
                onClick = onClick,
                onLongClick = onLongClick
            ),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Box {
            Column {
                CoverArt(
                    coverArtPath = album.coverArtPath,
                    coverArtUri = album.coverArtUri,
                    modifier = Modifier
                        .size(width = 140.dp, height = 140.dp)
                        .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp))
                )

                Column(
                    modifier = Modifier.padding(12.dp)
                ) {
                    Text(
                        text = album.albumName,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = "${album.trackCount}개 트랙",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            // Selection overlay
            if (isSelected) {
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = "선택됨",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(32.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun AudiobookTracksDialog(
    album: AudiobookAlbum,
    onDismiss: () -> Unit,
    onTrackClick: (AudioFile) -> Unit,
    onPlayAll: () -> Unit,
    onRemoveFromAlbum: ((AudioFile) -> Unit)? = null
) {
    // 제거된 트랙들을 추적 (애니메이션용)
    var removedTrackIds by remember { mutableStateOf(setOf<Long>()) }
    // 표시할 트랙 수 (제거된 것 제외)
    val visibleTrackCount = album.tracks.count { it.id !in removedTrackIds }

    AlertDialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false),
        modifier = Modifier
            .fillMaxWidth(0.95f)
            .padding(8.dp),
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = album.albumName,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = "${visibleTrackCount}개 트랙",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                IconButton(onClick = onPlayAll) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = "전체 재생",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }
        },
        text = {
            // Horizontal scrolling layout like home screen
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                itemsIndexed(
                    items = album.tracks,
                    key = { _, track -> track.id }
                ) { index, track ->
                    val isVisible = track.id !in removedTrackIds

                    AnimatedVisibility(
                        visible = isVisible,
                        exit = slideOutHorizontally(
                            targetOffsetX = { -it },
                            animationSpec = tween(300)
                        ) + shrinkHorizontally(
                            animationSpec = tween(300)
                        ) + fadeOut(
                            animationSpec = tween(200)
                        )
                    ) {
                        // Card with same ratio as home audiobook cards (140x210)
                        Card(
                            modifier = Modifier
                                .width(140.dp)
                                .clickable { onTrackClick(track) },
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surfaceVariant
                            )
                        ) {
                            Box {
                                Column {
                                    // Tall album cover (same ratio as home: 140x210)
                                    CoverArt(
                                        coverArtPath = track.coverArtPath,
                                        coverArtUri = track.coverArtUri,
                                        modifier = Modifier
                                            .size(width = 140.dp, height = 210.dp)
                                            .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp))
                                    )
                                    // Minimal text info
                                    Column(
                                        modifier = Modifier.padding(10.dp)
                                    ) {
                                        Text(
                                            text = track.displayTitle,
                                            style = MaterialTheme.typography.bodySmall,
                                            fontWeight = FontWeight.Medium,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                        Text(
                                            text = formatDuration(track.duration),
                                            style = MaterialTheme.typography.labelSmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                                            maxLines = 1
                                        )
                                    }
                                }
                                // Track number badge
                                Box(
                                    modifier = Modifier
                                        .align(Alignment.TopStart)
                                        .padding(6.dp)
                                        .size(24.dp)
                                        .background(
                                            color = Color.Black.copy(alpha = 0.6f),
                                            shape = RoundedCornerShape(12.dp)
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "${index + 1}",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                                // Remove from album button
                                onRemoveFromAlbum?.let { removeHandler ->
                                    Box(
                                        modifier = Modifier
                                            .align(Alignment.TopEnd)
                                            .padding(6.dp)
                                            .size(24.dp)
                                            .background(
                                                color = Color.Black.copy(alpha = 0.6f),
                                                shape = RoundedCornerShape(12.dp)
                                            )
                                            .clickable {
                                                // 먼저 UI에서 애니메이션으로 제거 표시
                                                removedTrackIds = removedTrackIds + track.id
                                                // 실제 데이터 삭제
                                                removeHandler(track)
                                            },
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Close,
                                            contentDescription = "앨범에서 빼기",
                                            tint = Color.White,
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                }
                            }
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

private fun formatDuration(durationMs: Long): String {
    val totalSeconds = durationMs / 1000
    val hours = totalSeconds / 3600
    val minutes = (totalSeconds % 3600) / 60
    val seconds = totalSeconds % 60
    
    return if (hours > 0) {
        String.format("%d:%02d:%02d", hours, minutes, seconds)
    } else {
        String.format("%d:%02d", minutes, seconds)
    }
}
