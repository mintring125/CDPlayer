package com.example.cdplayer.ui.screens.home

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.foundation.combinedClickable
import androidx.compose.material.icons.filled.CheckCircle

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.material3.Card
import androidx.compose.ui.res.painterResource
import com.example.cdplayer.R
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.cdplayer.domain.model.AudioFile
import com.example.cdplayer.ui.components.AudioItem
import com.example.cdplayer.ui.components.BulkActionToolbar
import com.example.cdplayer.ui.components.CoverArt
import com.example.cdplayer.ui.components.FolderPickerDialog

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
    val playbackState by viewModel.playbackState.collectAsState()
    val selectionState by viewModel.selectionState.collectAsState()
    var showFolderPicker by remember { mutableStateOf(false) }

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
                                }
                            )
                        }
                    }
                }

                item { Spacer(modifier = Modifier.height(24.dp)) }

                // Favorite Albums (Replaces All Music)
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
                                    isSelected = audioFile.id in selectionState.selectedIds
                                )
                            }
                        }
                    }

                    item { Spacer(modifier = Modifier.height(24.dp)) }
                }

                // Removed All Music Section


                // Audiobooks
                if (allAudiobooks.isNotEmpty()) {
                    item {
                        SectionHeader(
                            title = "오디오북",
                            subtitle = "${allAudiobooks.size}개",
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                        )
                    }

                    item {
                        LazyRow(
                            contentPadding = PaddingValues(horizontal = 16.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(allAudiobooks.take(10)) { audioFile ->
                                AlbumCard(
                                    audioFile = audioFile,
                                    onClick = {
                                        if (selectionState.isSelectionMode) {
                                            viewModel.toggleSelection(audioFile.id)
                                        } else {
                                            viewModel.playTrack(audioFile)
                                            onNavigateToPlayer()
                                        }
                                    },
                                    onLongClick = {
                                        viewModel.toggleSelection(audioFile.id)
                                    },
                                    isSelectionMode = selectionState.isSelectionMode,
                                    isSelected = audioFile.id in selectionState.selectedIds,
                                    isAudiobook = true
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
    Card(
        modifier = modifier.width(140.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
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
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Text(
                    text = "나은이 아빠",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f),
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
    isAudiobook: Boolean = false
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

                // Menu removed


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
