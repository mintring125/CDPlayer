package com.example.cdplayer.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Forward10
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material.icons.filled.RepeatOn
import androidx.compose.material.icons.filled.RepeatOne
import androidx.compose.material.icons.filled.Replay10
import androidx.compose.material.icons.filled.Shuffle
import androidx.compose.material.icons.filled.ShuffleOn
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.cdplayer.player.RepeatMode

@Composable
fun PlaybackControls(
    isPlaying: Boolean,
    currentPosition: Long,
    duration: Long,
    repeatMode: RepeatMode,
    shuffleEnabled: Boolean,
    onPlayPause: () -> Unit,
    onPrevious: () -> Unit,
    onNext: () -> Unit,
    onSeek: (Long) -> Unit,
    onRepeatModeChange: () -> Unit,
    onShuffleChange: () -> Unit,
    onSkipBackward: () -> Unit,
    onSkipForward: () -> Unit,
    modifier: Modifier = Modifier
) {
    var sliderPosition by remember(currentPosition) { mutableFloatStateOf(currentPosition.toFloat()) }
    var isDragging by remember { mutableFloatStateOf(0f) }

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Seek bar
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            Slider(
                value = if (isDragging > 0) isDragging else sliderPosition,
                onValueChange = { isDragging = it },
                onValueChangeFinished = {
                    onSeek(isDragging.toLong())
                    isDragging = 0f
                },
                valueRange = 0f..duration.toFloat().coerceAtLeast(1f),
                colors = SliderDefaults.colors(
                    thumbColor = MaterialTheme.colorScheme.primary,
                    activeTrackColor = MaterialTheme.colorScheme.primary,
                    inactiveTrackColor = MaterialTheme.colorScheme.surfaceVariant
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = formatTime(currentPosition),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = formatTime(duration),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // Main controls
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Shuffle
            IconButton(onClick = onShuffleChange) {
                Icon(
                    imageVector = if (shuffleEnabled) Icons.Default.ShuffleOn else Icons.Default.Shuffle,
                    contentDescription = "셔플",
                    tint = if (shuffleEnabled) MaterialTheme.colorScheme.primary
                          else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Previous
            IconButton(onClick = onPrevious) {
                Icon(
                    imageVector = Icons.Default.SkipPrevious,
                    contentDescription = "이전",
                    modifier = Modifier.size(32.dp)
                )
            }

            // Skip backward
            IconButton(onClick = onSkipBackward) {
                Icon(
                    imageVector = Icons.Default.Replay10,
                    contentDescription = "10초 뒤로"
                )
            }

            // Play/Pause
            FilledIconButton(
                onClick = onPlayPause,
                modifier = Modifier.size(64.dp),
                colors = IconButtonDefaults.filledIconButtonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Icon(
                    imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                    contentDescription = if (isPlaying) "일시정지" else "재생",
                    modifier = Modifier.size(32.dp)
                )
            }

            // Skip forward
            IconButton(onClick = onSkipForward) {
                Icon(
                    imageVector = Icons.Default.Forward10,
                    contentDescription = "10초 앞으로"
                )
            }

            // Next
            IconButton(onClick = onNext) {
                Icon(
                    imageVector = Icons.Default.SkipNext,
                    contentDescription = "다음",
                    modifier = Modifier.size(32.dp)
                )
            }

            // Repeat
            IconButton(onClick = onRepeatModeChange) {
                Icon(
                    imageVector = when (repeatMode) {
                        RepeatMode.OFF -> Icons.Default.Repeat
                        RepeatMode.ALL -> Icons.Default.RepeatOn
                        RepeatMode.ONE -> Icons.Default.RepeatOne
                    },
                    contentDescription = "반복",
                    tint = if (repeatMode != RepeatMode.OFF) MaterialTheme.colorScheme.primary
                          else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

private fun formatTime(millis: Long): String {
    val totalSeconds = millis / 1000
    val hours = totalSeconds / 3600
    val minutes = (totalSeconds % 3600) / 60
    val seconds = totalSeconds % 60

    return if (hours > 0) {
        String.format("%d:%02d:%02d", hours, minutes, seconds)
    } else {
        String.format("%d:%02d", minutes, seconds)
    }
}
