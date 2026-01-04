package com.example.cdplayer.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.compose.SubcomposeAsyncImage

@Composable
fun CoverArt(
    coverArtPath: String?,
    coverArtUri: String?,
    modifier: Modifier = Modifier,
    contentDescription: String? = null
) {
    val imageModel = coverArtPath ?: coverArtUri

    if (imageModel != null) {
        SubcomposeAsyncImage(
            model = imageModel,
            contentDescription = contentDescription,
            modifier = modifier,
            contentScale = ContentScale.Crop,
            loading = {
                PlaceholderCoverArt()
            },
            error = {
                PlaceholderCoverArt()
            }
        )
    } else {
        PlaceholderCoverArt(modifier = modifier)
    }
}

@Composable
fun PlaceholderCoverArt(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .background(MaterialTheme.colorScheme.surfaceVariant),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Default.MusicNote,
            contentDescription = null,
            modifier = Modifier.size(32.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun LargeCoverArt(
    coverArtPath: String?,
    coverArtUri: String?,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
    ) {
        CoverArt(
            coverArtPath = coverArtPath,
            coverArtUri = coverArtUri,
            modifier = Modifier.fillMaxSize()
        )
    }
}
