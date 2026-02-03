package com.example.cdplayer.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.dp
import com.example.cdplayer.domain.model.Bookmark
import com.example.cdplayer.domain.model.Chapter
import com.example.cdplayer.player.RepeatMode
import kotlin.math.abs

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
    bookmarks: List<Bookmark> = emptyList(),
    chapters: List<Chapter> = emptyList(),
    modifier: Modifier = Modifier
) {
    var sliderPosition by remember(currentPosition) { mutableFloatStateOf(currentPosition.toFloat()) }
    var isDragging by remember { mutableFloatStateOf(0f) }
    var nearbyChapter by remember { mutableStateOf<Chapter?>(null) }
    // 페이드아웃 애니메이션 중 표시할 챕터 유지
    var lastShownChapter by remember { mutableStateOf<Chapter?>(null) }
    if (nearbyChapter != null) lastShownChapter = nearbyChapter

    val maxDuration = duration.toFloat().coerceAtLeast(1f)
    val snapThreshold = maxDuration * 0.015f
    val chapterDisplayThreshold = maxDuration * 0.03f

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
            val bookmarkColor = MaterialTheme.colorScheme.tertiary
            val chapterColor = MaterialTheme.colorScheme.secondary

            val showChapterTooltip = nearbyChapter != null && isDragging > 0

            Box(modifier = Modifier.fillMaxWidth()) {
                Slider(
                    value = if (isDragging > 0) isDragging else sliderPosition,
                    onValueChange = { value ->
                        // Collect all snap points: bookmarks + chapter start times
                        val snapPoints = mutableListOf<Float>()
                        bookmarks.forEach { snapPoints.add(it.position.toFloat()) }
                        chapters.forEach { snapPoints.add(it.startTimeMs.toFloat()) }

                        val snapped = if (snapPoints.isNotEmpty()) {
                            val nearest = snapPoints.minByOrNull { abs(it - value) }
                            if (nearest != null && abs(nearest - value) <= snapThreshold) {
                                nearest
                            } else {
                                value
                            }
                        } else {
                            value
                        }
                        isDragging = snapped

                        // 드래그 위치 근처의 챕터 감지
                        nearbyChapter = chapters
                            .minByOrNull { abs(it.startTimeMs.toFloat() - snapped) }
                            ?.takeIf { abs(it.startTimeMs.toFloat() - snapped) <= chapterDisplayThreshold }
                    },
                    onValueChangeFinished = {
                        onSeek(isDragging.toLong())
                        isDragging = 0f
                        nearbyChapter = null
                    },
                    valueRange = 0f..maxDuration,
                    colors = SliderDefaults.colors(
                        thumbColor = MaterialTheme.colorScheme.primary,
                        activeTrackColor = MaterialTheme.colorScheme.primary,
                        inactiveTrackColor = MaterialTheme.colorScheme.surfaceVariant
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                // Bookmark & chapter markers overlay
                if ((bookmarks.isNotEmpty() || chapters.isNotEmpty()) && duration > 0) {
                    Canvas(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp) // Match Slider touch target height
                            .padding(horizontal = 10.dp) // Match Slider track padding (thumb radius)
                    ) {
                        val trackWidth = size.width
                        val centerY = size.height / 2f

                        // Chapter markers: vertical lines
                        val chapterLineHeight = 10.dp.toPx()
                        val chapterLineWidth = 2.dp.toPx()
                        chapters.forEach { chapter ->
                            val fraction = chapter.startTimeMs.toFloat() / maxDuration
                            if (fraction in 0f..1f) {
                                val x = fraction * trackWidth
                                drawLine(
                                    color = chapterColor,
                                    start = Offset(x, centerY - chapterLineHeight / 2),
                                    end = Offset(x, centerY + chapterLineHeight / 2),
                                    strokeWidth = chapterLineWidth
                                )
                            }
                        }

                        // Bookmark markers: circles (drawn on top)
                        val markerRadius = 4.dp.toPx()
                        bookmarks.forEach { bookmark ->
                            val fraction = bookmark.position.toFloat() / maxDuration
                            if (fraction in 0f..1f) {
                                val x = fraction * trackWidth
                                drawCircle(
                                    color = bookmarkColor,
                                    radius = markerRadius,
                                    center = Offset(x, centerY)
                                )
                            }
                        }
                    }
                }

                // 챕터 제목 툴팁 (드래그 중 챕터 마커 근처일 때 표시, 레이아웃에 영향 없음)
                androidx.compose.animation.AnimatedVisibility(
                    visible = showChapterTooltip,
                    enter = fadeIn(),
                    exit = fadeOut(),
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .fillMaxWidth()
                ) {
                    val chapter = nearbyChapter ?: lastShownChapter
                    if (chapter != null) {
                        val fraction = chapter.startTimeMs.toFloat() / maxDuration
                        val bias = (fraction * 2f - 1f).coerceIn(-1f, 1f)
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 10.dp)
                        ) {
                            Surface(
                                shape = RoundedCornerShape(4.dp),
                                color = MaterialTheme.colorScheme.inverseSurface,
                                modifier = Modifier.align(BiasAlignment(bias, 0f))
                            ) {
                                Text(
                                    text = chapter.title,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.inverseOnSurface,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }
                    }
                }
            }

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
            IconButton(
                onClick = onPrevious,
                modifier = Modifier.size(48.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.SkipPrevious,
                    contentDescription = "이전",
                    modifier = Modifier.size(40.dp)
                )
            }

            // Skip backward
            IconButton(
                onClick = onSkipBackward,
                modifier = Modifier.size(48.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Replay10,
                    contentDescription = "10초 뒤로",
                    modifier = Modifier.size(28.dp)
                )
            }

            // Play/Pause
            FilledIconButton(
                onClick = onPlayPause,
                modifier = Modifier.size(80.dp),
                colors = IconButtonDefaults.filledIconButtonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Icon(
                    imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                    contentDescription = if (isPlaying) "일시정지" else "재생",
                    modifier = Modifier.size(44.dp)
                )
            }

            // Skip forward
            IconButton(
                onClick = onSkipForward,
                modifier = Modifier.size(48.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Forward10,
                    contentDescription = "10초 앞으로",
                    modifier = Modifier.size(28.dp)
                )
            }

            // Next
            IconButton(
                onClick = onNext,
                modifier = Modifier.size(48.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.SkipNext,
                    contentDescription = "다음",
                    modifier = Modifier.size(40.dp)
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
