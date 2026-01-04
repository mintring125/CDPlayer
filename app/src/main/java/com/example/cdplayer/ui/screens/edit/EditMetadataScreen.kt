package com.example.cdplayer.ui.screens.edit

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.material.icons.filled.Image
import androidx.compose.foundation.layout.width
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.cdplayer.ui.components.CoverArt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditMetadataScreen(
    viewModel: EditMetadataViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    // 에러 표시
    LaunchedEffect(uiState.error) {
        uiState.error?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearError()
        }
    }

    // 저장 성공 시 뒤로가기
    LaunchedEffect(uiState.saveSuccess) {
        if (uiState.saveSuccess) {
            onNavigateBack()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("정보 편집", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "뒤로")
                    }
                },
                actions = {
                    if (uiState.isSaving) {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .size(24.dp)
                                .padding(end = 16.dp),
                            strokeWidth = 2.dp
                        )
                    } else {
                        IconButton(
                            onClick = { viewModel.saveChanges() },
                            enabled = !uiState.isLoading
                        ) {
                            Icon(Icons.Default.Check, contentDescription = "저장")
                        }
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        if (uiState.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // 앨범 아트
                uiState.audioFile?.let { audioFile ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            // 커버 아트 미리보기 (선택된 URI 우선, 없으면 기존 파일)
                            val displayUri = uiState.selectedCoverArtUri?.toString() ?: audioFile.coverArtUri
                            val displayPath = if (uiState.selectedCoverArtUri == null) audioFile.coverArtPath else null
                            
                            CoverArt(
                                coverArtPath = displayPath,
                                coverArtUri = displayUri,
                                modifier = Modifier
                                    .size(180.dp)
                                    .padding(bottom = 16.dp)
                            )
                            
                            // 버튼들
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                // 갤러리 선택
                                val launcher = rememberLauncherForActivityResult(
                                    contract = ActivityResultContracts.GetContent()
                                ) { uri: android.net.Uri? ->
                                    uri?.let { viewModel.onCoverArtSelected(it) }
                                }
                                
                                androidx.compose.material3.Button(
                                    onClick = { launcher.launch("image/*") }
                                ) {
                                    Icon(Icons.Default.Image, contentDescription = null)
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("사진 선택")
                                }
                            }
                            
                            Spacer(modifier = Modifier.height(8.dp))
                            
                            // URL 입력
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                OutlinedTextField(
                                    value = uiState.coverArtUrl,
                                    onValueChange = { viewModel.updateCoverArtUrl(it) },
                                    label = { Text("이미지 URL") },
                                    modifier = Modifier.weight(1f),
                                    singleLine = true,
                                    placeholder = { Text("https://example.com/image.jpg") }
                                )
                                androidx.compose.material3.Button(
                                    onClick = { viewModel.applyCoverArtUrl() },
                                    enabled = uiState.coverArtUrl.isNotBlank() && !uiState.isCoverArtLoading
                                ) {
                                    if (uiState.isCoverArtLoading) {
                                        CircularProgressIndicator(
                                            modifier = Modifier.size(16.dp),
                                            strokeWidth = 2.dp,
                                            color = MaterialTheme.colorScheme.onPrimary
                                        )
                                    } else {
                                        Text("적용")
                                    }
                                }
                            }
                        }
                    }
                }

                // 파일명
                OutlinedTextField(
                    value = uiState.newFileName,
                    onValueChange = { viewModel.updateFileName(it) },
                    label = { Text("파일명") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    suffix = {
                        Text(
                            ".${uiState.audioFile?.path?.substringAfterLast(".") ?: "mp3"}",
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                )

                // 제목
                OutlinedTextField(
                    value = uiState.title,
                    onValueChange = { viewModel.updateTitle(it) },
                    label = { Text("제목") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                // 아티스트
                OutlinedTextField(
                    value = uiState.artist,
                    onValueChange = { viewModel.updateArtist(it) },
                    label = { Text("아티스트") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                // 앨범
                OutlinedTextField(
                    value = uiState.album,
                    onValueChange = { viewModel.updateAlbum(it) },
                    label = { Text("앨범") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                // 장르
                OutlinedTextField(
                    value = uiState.genre,
                    onValueChange = { viewModel.updateGenre(it) },
                    label = { Text("장르") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                // 연도 & 트랙 번호
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    OutlinedTextField(
                        value = uiState.year,
                        onValueChange = { viewModel.updateYear(it) },
                        label = { Text("연도") },
                        modifier = Modifier.weight(1f),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )

                    OutlinedTextField(
                        value = uiState.trackNumber,
                        onValueChange = { viewModel.updateTrackNumber(it) },
                        label = { Text("트랙 번호") },
                        modifier = Modifier.weight(1f),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                }

                // 파일 정보 (읽기 전용)
                uiState.audioFile?.let { audioFile ->
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Text(
                        text = "파일 정보",
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                    
                    Text(
                        text = "경로: ${audioFile.path}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    
                    Text(
                        text = "재생시간: ${audioFile.formattedDuration}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    
                    audioFile.bitrate?.let {
                        Text(
                            text = "비트레이트: ${it}kbps",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}
