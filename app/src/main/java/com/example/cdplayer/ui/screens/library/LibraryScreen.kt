package com.example.cdplayer.ui.screens.library

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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Album
import androidx.compose.material.icons.filled.Audiotrack
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.GroupWork
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlaylistPlay
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.RemoveCircleOutline
import androidx.compose.material.icons.filled.Sort
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.background
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TextButton
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.IconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.cdplayer.domain.model.AudioFile
import com.example.cdplayer.domain.model.Playlist
import com.example.cdplayer.ui.components.AudioItem
import com.example.cdplayer.ui.components.CoverArt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LibraryScreen(
    viewModel: LibraryViewModel = hiltViewModel(),
    onNavigateToPlayer: () -> Unit,
    onNavigateToPlaylist: (Long) -> Unit,
    onNavigateToCreatePlaylist: () -> Unit,
    onNavigateToAlbum: (String) -> Unit,
    onNavigateToArtist: (String) -> Unit,
    onNavigateToEditMetadata: (Long) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val allMusic by viewModel.allMusic.collectAsState()
    val allAudiobooks by viewModel.allAudiobooks.collectAsState()
    val allArtists by viewModel.allArtists.collectAsState()
    val allAlbums by viewModel.allAlbums.collectAsState()
    val allPlaylists by viewModel.allPlaylists.collectAsState()
    val playbackState by viewModel.playbackState.collectAsState()
    val selectionState by viewModel.selectionState.collectAsState()

    var showAlbumNameDialog by remember { mutableStateOf(false) }
    var showDeleteConfirmDialog by remember { mutableStateOf(false) }
    var newAlbumName by remember { mutableStateOf("") }

    val tabs = listOf(
        LibraryTab.MUSIC to "음악",
        LibraryTab.AUDIOBOOKS to "오디오북",
        LibraryTab.ARTISTS to "아티스트",
        LibraryTab.ALBUMS to "앨범",
        LibraryTab.PLAYLISTS to "플레이리스트"
    )

    var showSortMenu by remember { mutableStateOf(false) }
    val showSortOption = uiState.selectedTab == LibraryTab.MUSIC ||
                         uiState.selectedTab == LibraryTab.AUDIOBOOKS

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
                    actions = {
                        // 앨범으로 묶기 버튼
                        IconButton(
                            onClick = {
                                newAlbumName = ""
                                showAlbumNameDialog = true
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.GroupWork,
                                contentDescription = "앨범으로 묶기"
                            )
                        }
                        // 삭제 버튼
                        IconButton(
                            onClick = { showDeleteConfirmDialog = true }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "삭제",
                                tint = MaterialTheme.colorScheme.error
                            )
                        }
                    },
                    colors = androidx.compose.material3.TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                )
            } else {
                TopAppBar(
                    title = {
                        Text(
                            text = "라이브러리",
                            fontWeight = FontWeight.Bold
                        )
                    },
                    actions = {
                        if (showSortOption) {
                            Box {
                                IconButton(onClick = { showSortMenu = true }) {
                                    Icon(
                                        imageVector = Icons.Default.Sort,
                                        contentDescription = "정렬"
                                    )
                                }
                                DropdownMenu(
                                    expanded = showSortMenu,
                                    onDismissRequest = { showSortMenu = false }
                                ) {
                                    SortOption.entries.forEach { option ->
                                        DropdownMenuItem(
                                            text = {
                                                Row(
                                                    verticalAlignment = Alignment.CenterVertically,
                                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                                ) {
                                                    Text(option.displayName)
                                                    if (uiState.sortOption == option) {
                                                        Icon(
                                                            imageVector = Icons.Default.Check,
                                                            contentDescription = null,
                                                            modifier = Modifier.size(16.dp),
                                                            tint = MaterialTheme.colorScheme.primary
                                                        )
                                                    }
                                                }
                                            },
                                            onClick = {
                                                viewModel.setSortOption(option)
                                                showSortMenu = false
                                            }
                                        )
                                    }
                                }
                            }
                        }
                    }
                )
            }
        },
        floatingActionButton = {
            if (uiState.selectedTab == LibraryTab.PLAYLISTS) {
                FloatingActionButton(
                    onClick = onNavigateToCreatePlaylist,
                    containerColor = MaterialTheme.colorScheme.primary
                ) {
                    Icon(Icons.Default.Add, contentDescription = "새 플레이리스트")
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Tabs
            ScrollableTabRow(
                selectedTabIndex = tabs.indexOfFirst { it.first == uiState.selectedTab },
                edgePadding = 16.dp
            ) {
                tabs.forEach { (tab, title) ->
                    Tab(
                        selected = uiState.selectedTab == tab,
                        onClick = { viewModel.selectTab(tab) },
                        text = { Text(title) }
                    )
                }
            }

            // Content
            when (uiState.selectedTab) {
                LibraryTab.MUSIC -> {
                    TrackList(
                        tracks = allMusic,
                        currentTrackId = playbackState.currentTrack?.id,
                        onTrackClick = { index ->
                            if (selectionState.isSelectionMode) {
                                viewModel.toggleSelection(allMusic[index].id)
                            } else {
                                viewModel.playTracks(allMusic, index)
                                onNavigateToPlayer()
                            }
                        },
                        onLongClick = { viewModel.toggleSelection(it.id) },
                        onAddToQueue = { viewModel.addToQueue(it) },
                        onEditMetadata = { onNavigateToEditMetadata(it.id) },
                        onMoveToAudiobook = { viewModel.moveAlbumToType(it, com.example.cdplayer.domain.model.AudioType.AUDIOBOOK) },
                        onDelete = { viewModel.deleteFile(it) },
                        isSelectionMode = selectionState.isSelectionMode,
                        selectedIds = selectionState.selectedIds
                    )
                }
                LibraryTab.AUDIOBOOKS -> {
                    TrackList(
                        tracks = allAudiobooks,
                        currentTrackId = playbackState.currentTrack?.id,
                        onTrackClick = { index ->
                            if (selectionState.isSelectionMode) {
                                viewModel.toggleSelection(allAudiobooks[index].id)
                            } else {
                                viewModel.playTracks(allAudiobooks, index)
                                onNavigateToPlayer()
                            }
                        },
                        onLongClick = { viewModel.toggleSelection(it.id) },
                        onAddToQueue = { viewModel.addToQueue(it) },
                        onEditMetadata = { onNavigateToEditMetadata(it.id) },
                        onMoveToMusic = { viewModel.moveAlbumToType(it, com.example.cdplayer.domain.model.AudioType.MUSIC) },
                        onToggleFavorite = { viewModel.toggleFavorite(it) },
                        onRemoveFromAlbum = { viewModel.removeFromAlbum(it) },
                        onDelete = { viewModel.deleteFile(it) },
                        isSelectionMode = selectionState.isSelectionMode,
                        selectedIds = selectionState.selectedIds
                    )
                }
                LibraryTab.ARTISTS -> {
                    GridList(
                        items = allArtists,
                        icon = Icons.Default.Person,
                        onItemClick = onNavigateToArtist
                    )
                }
                LibraryTab.ALBUMS -> {
                    GridList(
                        items = allAlbums,
                        icon = Icons.Default.Album,
                        onItemClick = onNavigateToAlbum,
                        onMoveToMusic = { viewModel.moveAlbumToType(it, com.example.cdplayer.domain.model.AudioType.MUSIC) },
                        onMoveToAudiobook = { viewModel.moveAlbumToType(it, com.example.cdplayer.domain.model.AudioType.AUDIOBOOK) }
                    )
                }
                LibraryTab.PLAYLISTS -> {
                    PlaylistList(
                        playlists = allPlaylists,
                        onPlaylistClick = onNavigateToPlaylist
                    )
                }
            }
        }
    }

    // Album name input dialog
    if (showAlbumNameDialog) {
        AlertDialog(
            onDismissRequest = { showAlbumNameDialog = false },
            title = { Text("앨범으로 묶기") },
            text = {
                Column {
                    Text(
                        text = "${selectionState.selectedIds.size}개 트랙을 선택함",
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
            title = {
                Text(
                    text = "파일 삭제",
                    color = MaterialTheme.colorScheme.error
                )
            },
            text = {
                Column {
                    Text(
                        text = "${selectionState.selectedIds.size}개 파일을 삭제하시겠습니까?",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "이 작업은 되돌릴 수 없으며, 실제 파일이 삭제됩니다.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.deleteSelectedFiles()
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

@Composable
fun TrackList(
    tracks: List<AudioFile>,
    currentTrackId: Long?,
    onTrackClick: (Int) -> Unit,
    onLongClick: ((AudioFile) -> Unit)? = null,
    onAddToQueue: (AudioFile) -> Unit,
    onEditMetadata: (AudioFile) -> Unit,
    onMoveToMusic: ((AudioFile) -> Unit)? = null,
    onMoveToAudiobook: ((AudioFile) -> Unit)? = null,
    onToggleFavorite: ((Long) -> Unit)? = null,
    onRemoveFromAlbum: ((AudioFile) -> Unit)? = null,
    onDelete: ((AudioFile) -> Unit)? = null,
    isSelectionMode: Boolean = false,
    selectedIds: Set<Long> = emptySet()
) {
    if (tracks.isEmpty()) {
        EmptyState(
            icon = Icons.Default.Audiotrack,
            message = "트랙이 없습니다"
        )
    } else {
        LazyColumn(
            contentPadding = PaddingValues(bottom = 80.dp)
        ) {
            items(tracks.size) { index ->
                val track = tracks[index]
                val isSelected = track.id in selectedIds

                AudioItem(
                    audioFile = track,
                    onClick = { onTrackClick(index) },
                    isPlaying = currentTrackId == track.id,
                    onAddToQueue = { onAddToQueue(track) },
                    onEditMetadata = { onEditMetadata(track) },
                    onMoveToMusic = onMoveToMusic?.let { handler -> { handler(track) } },
                    onMoveToAudiobook = onMoveToAudiobook?.let { handler -> { handler(track) } },
                    isFavorite = track.isFavorite,
                    onToggleFavorite = onToggleFavorite?.let { handler -> { handler(track.id) } },
                    onRemoveFromAlbum = onRemoveFromAlbum?.let { handler -> { handler(track) } },
                    onDelete = onDelete?.let { handler -> { handler(track) } },
                    isSelectionMode = isSelectionMode,
                    isSelected = isSelected,
                    onToggleSelection = { onLongClick?.invoke(track) }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GridList(
    items: List<String>,
    icon: ImageVector,
    onItemClick: (String) -> Unit,
    onMoveToMusic: ((String) -> Unit)? = null,
    onMoveToAudiobook: ((String) -> Unit)? = null
) {
    if (items.isEmpty()) {
        EmptyState(
            icon = icon,
            message = "항목이 없습니다"
        )
    } else {
        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 160.dp),
            contentPadding = PaddingValues(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(items) { item ->
                Card(
                    onClick = { onItemClick(item) },
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp)
                    ) {
                        Column(
                            modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = icon,
                                contentDescription = null,
                                modifier = Modifier.size(48.dp),
                                tint = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                text = item,
                                style = MaterialTheme.typography.bodyMedium,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis,
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }

                        // More options for Grid items (Albums)
                        if (onMoveToMusic != null || onMoveToAudiobook != null) {
                            var showMenu by remember { androidx.compose.runtime.mutableStateOf(false) }
                            Box(modifier = Modifier.align(Alignment.TopEnd)) {
                                IconButton(
                                    onClick = { showMenu = true },
                                    modifier = Modifier.size(24.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.MoreVert,
                                        contentDescription = "더보기",
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                                DropdownMenu(
                                    expanded = showMenu,
                                    onDismissRequest = { showMenu = false }
                                ) {
                                    onMoveToMusic?.let {
                                        DropdownMenuItem(
                                            text = { Text("음악으로 이동") },
                                            onClick = {
                                                showMenu = false
                                                it(item)
                                            }
                                        )
                                    }
                                    onMoveToAudiobook?.let {
                                        DropdownMenuItem(
                                            text = { Text("오디오북으로 이동") },
                                            onClick = {
                                                showMenu = false
                                                it(item)
                                            }
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlaylistList(
    playlists: List<Playlist>,
    onPlaylistClick: (Long) -> Unit
) {
    if (playlists.isEmpty()) {
        EmptyState(
            icon = Icons.Default.PlaylistPlay,
            message = "플레이리스트가 없습니다\n새 플레이리스트를 만들어보세요"
        )
    } else {
        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(playlists) { playlist ->
                Card(
                    onClick = { onPlaylistClick(playlist.id) },
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(56.dp)
                                .clip(RoundedCornerShape(8.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            if (playlist.coverArtPath != null) {
                                CoverArt(
                                    coverArtPath = playlist.coverArtPath,
                                    coverArtUri = null,
                                    modifier = Modifier.fillMaxSize()
                                )
                            } else {
                                Icon(
                                    imageVector = Icons.Default.PlaylistPlay,
                                    contentDescription = null,
                                    modifier = Modifier.size(32.dp),
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }
                        }

                        Column(
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = playlist.name,
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.Medium,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Text(
                                text = "${playlist.trackCount}곡 • ${playlist.formattedDuration}",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun EmptyState(
    icon: ImageVector,
    message: String
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(64.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = message,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
