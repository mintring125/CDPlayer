package com.example.cdplayer.ui.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.cdplayer.domain.model.AudioFile

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AudioItem(
    audioFile: AudioFile,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isPlaying: Boolean = false,
    showTrackNumber: Boolean = false,
    onAddToQueue: (() -> Unit)? = null,
    onAddToPlaylist: (() -> Unit)? = null,
    onShowInfo: (() -> Unit)? = null,
    onEditMetadata: (() -> Unit)? = null,
    onRemoveFromPlaylist: (() -> Unit)? = null,
    isSelectionMode: Boolean = false,
    isSelected: Boolean = false,
    onToggleSelection: () -> Unit = {}
) {
    var showMenu by remember { mutableStateOf(false) }

    val backgroundColor = if (isSelected) {
        MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
    } else {
        androidx.compose.ui.graphics.Color.Transparent
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(backgroundColor)
            .combinedClickable(
                onClick = {
                    if (isSelectionMode) {
                        onToggleSelection()
                    } else {
                        onClick()
                    }
                },
                onLongClick = {
                    onToggleSelection()
                }
            )
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Selection Checkbox or Track Number/Cover Art
        if (isSelectionMode) {
            androidx.compose.material3.Checkbox(
                checked = isSelected,
                onCheckedChange = { onToggleSelection() },
                modifier = Modifier.size(24.dp)
            )
        } else if (showTrackNumber && audioFile.trackNumber != null) {
            Text(
                text = "${audioFile.trackNumber}",
                style = MaterialTheme.typography.bodyMedium,
                color = if (isPlaying) MaterialTheme.colorScheme.primary
                       else MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(40.dp),
            )
        } else {
            CoverArt(
                coverArtPath = audioFile.coverArtPath,
                coverArtUri = audioFile.coverArtUri,
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(8.dp))
            )
        }

        // Track Info
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Center
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                if (isPlaying) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
                Text(
                    text = audioFile.displayTitle,
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (isPlaying) MaterialTheme.colorScheme.primary
                           else MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Text(
                text = audioFile.displayArtist,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        // Duration
        Text(
            text = audioFile.formattedDuration,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        // More options
        if (onAddToQueue != null || onAddToPlaylist != null || onShowInfo != null || onEditMetadata != null || onRemoveFromPlaylist != null) {
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
                onAddToQueue?.let {
                    DropdownMenuItem(
                        text = { Text("대기열에 추가") },
                        onClick = {
                            showMenu = false
                            it()
                        }
                    )
                }
                onAddToPlaylist?.let {
                    DropdownMenuItem(
                        text = { Text("플레이리스트에 추가") },
                        onClick = {
                            showMenu = false
                            it()
                        }
                    )
                }
                onRemoveFromPlaylist?.let {
                    DropdownMenuItem(
                        text = { Text("플레이리스트에서 제거") },
                        onClick = {
                            showMenu = false
                            it()
                        }
                    )
                }
                onEditMetadata?.let {
                    DropdownMenuItem(
                        text = { Text("정보 편집") },
                        onClick = {
                            showMenu = false
                            it()
                        }
                    )
                }
                onShowInfo?.let {
                    DropdownMenuItem(
                        text = { Text("정보 보기") },
                        onClick = {
                            showMenu = false
                            it()
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun AudioItemCompact(
    audioFile: AudioFile,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isPlaying: Boolean = false
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        CoverArt(
            coverArtPath = audioFile.coverArtPath,
            coverArtUri = audioFile.coverArtUri,
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(4.dp))
        )

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = audioFile.displayTitle,
                style = MaterialTheme.typography.bodySmall,
                color = if (isPlaying) MaterialTheme.colorScheme.primary
                       else MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = audioFile.displayArtist,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}
