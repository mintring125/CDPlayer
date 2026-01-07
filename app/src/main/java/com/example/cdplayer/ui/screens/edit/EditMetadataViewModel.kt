package com.example.cdplayer.ui.screens.edit

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cdplayer.data.repository.AudioRepository
import com.example.cdplayer.domain.model.AudioFile
import com.example.cdplayer.util.Id3TagWriter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

data class EditMetadataUiState(
    val audioFile: AudioFile? = null,
    val title: String = "",
    val artist: String = "",
    val album: String = "",
    val genre: String = "",
    val year: String = "",
    val trackNumber: String = "",
    val newFileName: String = "",
    val coverArtUrl: String = "",
    val selectedCoverArtUri: android.net.Uri? = null,
    val isLoading: Boolean = true,
    val isCoverArtLoading: Boolean = false,
    val isSaving: Boolean = false,
    val saveSuccess: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class EditMetadataViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val audioRepository: AudioRepository,
    private val id3TagWriter: Id3TagWriter,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val audioId: Long = savedStateHandle.get<Long>("audioId") ?: -1

    private val _uiState = MutableStateFlow(EditMetadataUiState())
    val uiState: StateFlow<EditMetadataUiState> = _uiState.asStateFlow()

    init {
        loadAudioFile()
    }

    private fun loadAudioFile() {
        viewModelScope.launch {
            try {
                val audioFile = audioRepository.getAudioFileById(audioId)
                if (audioFile != null) {
                    val fileName = audioFile.path.substringAfterLast("/").substringBeforeLast(".")
                    _uiState.update {
                        it.copy(
                            audioFile = audioFile,
                            title = audioFile.title,
                            artist = audioFile.artist ?: "",
                            album = audioFile.album ?: "",
                            genre = audioFile.genre ?: "",
                            year = audioFile.year?.toString() ?: "",
                            trackNumber = audioFile.trackNumber?.toString() ?: "",
                            newFileName = fileName,
                            isLoading = false
                        )
                    }
                } else {
                    _uiState.update { it.copy(isLoading = false, error = "파일을 찾을 수 없습니다") }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = e.message) }
            }
        }
    }

    fun updateTitle(value: String) {
        _uiState.update { it.copy(title = value) }
    }

    fun updateArtist(value: String) {
        _uiState.update { it.copy(artist = value) }
    }

    fun updateAlbum(value: String) {
        _uiState.update { it.copy(album = value) }
    }

    fun updateGenre(value: String) {
        _uiState.update { it.copy(genre = value) }
    }

    fun updateYear(value: String) {
        _uiState.update { it.copy(year = value.filter { it.isDigit() }) }
    }

    fun updateTrackNumber(value: String) {
        _uiState.update { it.copy(trackNumber = value.filter { it.isDigit() }) }
    }

    fun updateFileName(value: String) {
        // 파일명으로 사용할 수 없는 문자 제거
        val sanitized = value.replace(Regex("[\\\\/:*?\"<>|]"), "")
        _uiState.update { it.copy(newFileName = sanitized) }
    }

    fun updateCoverArtUrl(url: String) {
        _uiState.update { it.copy(coverArtUrl = url) }
    }

    fun onCoverArtSelected(uri: android.net.Uri) {
         _uiState.update { it.copy(selectedCoverArtUri = uri, coverArtUrl = "") }
    }

    fun applyCoverArtUrl() {
        if (_uiState.value.coverArtUrl.isBlank()) return

        viewModelScope.launch {
            _uiState.update { it.copy(isCoverArtLoading = true, error = null) } // 에러 초기화 
            try {
                val url = _uiState.value.coverArtUrl
                
                // 캐시 디렉토리 사용 (안전)
                val tempMap = File(context.cacheDir, "artwork_temp")
                if (!tempMap.exists()) tempMap.mkdirs()
                val tempFile = File(tempMap, "download_${System.currentTimeMillis()}.jpg")
                
                with(kotlinx.coroutines.Dispatchers.IO) {
                    val connection = java.net.URL(url).openConnection() as java.net.HttpURLConnection
                    connection.connectTimeout = 10000 // 타임아웃 10초로 증가
                    connection.readTimeout = 10000
                    connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36")
                    connection.doInput = true
                    connection.connect()
                    
                    if (connection.responseCode == 200) {
                        val input = connection.inputStream
                        val output = java.io.FileOutputStream(tempFile)
                        input.copyTo(output)
                        output.close()
                        input.close()
                    } else {
                        throw java.io.IOException("HTTP 서버 응답 오류: ${connection.responseCode}")
                    }
                    connection.disconnect()
                }

                if (tempFile.exists() && tempFile.length() > 0) {
                    _uiState.update { 
                        it.copy(
                            selectedCoverArtUri = android.net.Uri.fromFile(tempFile), 
                            isCoverArtLoading = false
                        ) 
                    }
                } else {
                     throw java.io.IOException("파일 다운로드 실패: 파일이 비어있습니다.")
                }
            } catch (e: Exception) {
                e.printStackTrace()
                _uiState.update { 
                    it.copy(
                        isCoverArtLoading = false, 
                        error = "이미지 다운로드 실패: ${e.message}" 
                    ) 
                }
            }
        }
    }

    fun saveChanges() {
        viewModelScope.launch {
            val state = _uiState.value
            val audioFile = state.audioFile ?: return@launch

            _uiState.update { it.copy(isSaving = true, error = null) }

            try {
                // 0. 커버 아트 준비
                var artworkFile: File? = null
                val tempMap = File(context.cacheDir, "artwork_temp") // 다운로드용 임시 폴더
                if (!tempMap.exists()) tempMap.mkdirs()
                
                // URL은 입력되었지만 아직 적용(다운로드)되지 않은 경우 자동 처리
                if (state.selectedCoverArtUri == null && state.coverArtUrl.isNotBlank()) {
                     try {
                        val url = state.coverArtUrl
                        val tempFile = File(tempMap, "art_${System.currentTimeMillis()}.jpg")
                        
                        // HttpURLConnection 사용
                        with(kotlinx.coroutines.Dispatchers.IO) {
                            val connection = java.net.URL(url).openConnection() as java.net.HttpURLConnection
                            connection.connectTimeout = 5000
                            connection.readTimeout = 5000
                            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36")
                            connection.doInput = true
                            connection.connect()
                            
                            if (connection.responseCode == 200) {
                                val input = connection.inputStream
                                val output = java.io.FileOutputStream(tempFile)
                                input.copyTo(output)
                                output.close()
                                input.close()
                            } else {
                                throw java.io.IOException("HTTP Error: ${connection.responseCode}")
                            }
                            connection.disconnect()
                        }
                        
                        if (tempFile.exists() && tempFile.length() > 0) {
                            artworkFile = tempFile
                        } else {
                             // 파일이 생성되지 않았거나 크기가 0인 경우
                             throw java.io.IOException("File download failed or empty")
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                        // 실패 시 사용자에게 알림
                        _uiState.update { it.copy(isSaving = false, error = "URL 이미지 다운로드 실패: ${e.message}") }
                        return@launch
                    }
                } else if (state.selectedCoverArtUri != null) {
                    // 선택된 URI 처리 (이미 다운로드되었거나 갤러리에서 선택됨)
                    try {
                        // URI가 이미 파일 스킴이고 tempMap에 있다면 그대로 사용, 아니면 복사
                        if (state.selectedCoverArtUri.scheme == "file") {
                             val file = File(state.selectedCoverArtUri.path!!)
                             artworkFile = file
                        } else {
                            val tempFile = File(tempMap, "art_${System.currentTimeMillis()}.jpg")
                            context.contentResolver.openInputStream(state.selectedCoverArtUri)?.use { input ->
                                java.io.FileOutputStream(tempFile).use { output ->
                                    input.copyTo(output)
                                }
                            }
                            if (tempFile.exists() && tempFile.length() > 0) {
                                artworkFile = tempFile
                            }
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                         _uiState.update { it.copy(isSaving = false, error = "이미지 처리 실패: ${e.message}") }
                        return@launch
                    }
                }

                // *** 중요: 이미지를 내부 저장소(filesDir)로 영구 저장 ***
                var persistentUriString: String? = audioFile.coverArtUri
                
                if (artworkFile != null && artworkFile.exists()) {
                    try {
                        // cover_art 폴더 생성
                        val coversDir = File(context.filesDir, "cover_art")
                        if (!coversDir.exists()) coversDir.mkdirs()
                        
                        // 고유 파일명 생성 (ID + Timestamp)
                        val persistentFile = File(coversDir, "cover_${audioFile.id}_${System.currentTimeMillis()}.jpg")
                        
                        // 임시 파일을 영구 위치로 복사
                        artworkFile.copyTo(persistentFile, overwrite = true)
                        
                        persistentUriString = android.net.Uri.fromFile(persistentFile).toString()
                        
                        // 기존 임시 파일 삭제 (선택사항)
                        // artworkFile.delete() 
                    } catch (e: Exception) {
                        e.printStackTrace()
                         _uiState.update { it.copy(isSaving = false, error = "이미지 저장 실패: ${e.message}") }
                        return@launch
                    }
                }

                // 1. ID3 태그 쓰기
                val tagWriteSuccess = id3TagWriter.writeMetadata(
                    filePath = audioFile.path,
                    title = state.title,
                    artist = state.artist.takeIf { it.isNotBlank() },
                    album = state.album.takeIf { it.isNotBlank() },
                    genre = state.genre.takeIf { it.isNotBlank() },
                    year = state.year.toIntOrNull(),
                    trackNumber = state.trackNumber.toIntOrNull(),
                )

                if (!tagWriteSuccess) {
                    _uiState.update { it.copy(isSaving = false, error = "태그 저장 실패: 파일 권한을 확인하거나 다른 앱에서 사용 중인지 확인해주세요.") }
                    return@launch
                }

                // 2. 파일명 변경 (필요한 경우)
                var newPath = audioFile.path
                val originalFileName = audioFile.path.substringAfterLast("/").substringBeforeLast(".")
                val extension = audioFile.path.substringAfterLast(".")
                
                if (state.newFileName != originalFileName && state.newFileName.isNotBlank()) {
                    val parentDir = audioFile.path.substringBeforeLast("/")
                    newPath = "$parentDir/${state.newFileName}.$extension"
                    
                    val oldFile = File(audioFile.path)
                    val newFile = File(newPath)
                    
                    if (newFile.exists()) {
                        _uiState.update { it.copy(isSaving = false, error = "같은 이름의 파일이 이미 존재합니다") }
                        return@launch
                    }
                    
                    if (!oldFile.renameTo(newFile)) {
                        _uiState.update { it.copy(isSaving = false, error = "파일명 변경 실패") }
                        return@launch
                    }
                }

                // 3. DB 업데이트
                val updatedAudioFile = audioFile.copy(
                    title = state.title,
                    artist = state.artist.takeIf { it.isNotBlank() },
                    album = state.album.takeIf { it.isNotBlank() },
                    genre = state.genre.takeIf { it.isNotBlank() },
                    year = state.year.toIntOrNull(),
                    trackNumber = state.trackNumber.toIntOrNull(),
                    path = newPath,
                    // 영구 저장된 URI 사용
                    coverArtUri = persistentUriString
                )

                audioRepository.updateAudioFile(updatedAudioFile)

                // 4. 같은 앨범의 다른 파일들도 앨범명 일괄 변경
                if (state.album != audioFile.album && state.album.isNotBlank() && audioFile.album != null) {
                   try {
                       val albumTracks = audioRepository.getAudioFilesByAlbum(audioFile.album).first()
                       albumTracks.filter { it.id != audioFile.id }.forEach { track ->
                           id3TagWriter.writeMetadata(
                               filePath = track.path,
                               title = track.title, 
                               artist = track.artist, 
                               album = state.album, 
                               genre = track.genre,
                               year = track.year,
                               trackNumber = track.trackNumber
                           )
                           audioRepository.updateAudioFile(track.copy(album = state.album))
                       }
                   } catch (e: Exception) {
                       e.printStackTrace()
                   }
                }

                _uiState.update { it.copy(isSaving = false, saveSuccess = true) }

            } catch (e: Exception) {
                _uiState.update { it.copy(isSaving = false, error = e.message ?: "저장 중 오류 발생") }
            } finally {
                 // 임시 파일 정리 (필요시)
            }
        }
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }
}
