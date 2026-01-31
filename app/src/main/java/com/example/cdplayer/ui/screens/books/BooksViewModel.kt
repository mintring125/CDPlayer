package com.example.cdplayer.ui.screens.books

import android.content.Context
import android.os.Environment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cdplayer.data.local.dao.PdfBookDao
import com.example.cdplayer.data.local.entity.PdfBookEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject

data class BooksUiState(
    val isScanning: Boolean = false,
    val scannedFiles: List<PdfFileInfo> = emptyList(),
    val error: String? = null
)

data class PdfFileInfo(
    val filePath: String,
    val fileName: String,
    val fileSize: Long,
    val lastModified: Long,
    val lastPage: Int = 1,
    val totalPages: Int = 0,
    val lastReadAt: Long = 0
)

@HiltViewModel
class BooksViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val pdfBookDao: PdfBookDao
) : ViewModel() {

    private val _uiState = MutableStateFlow(BooksUiState())
    val uiState: StateFlow<BooksUiState> = _uiState.asStateFlow()

    val savedBooks = pdfBookDao.getAllBooks()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    init {
        scanPdfFiles()
    }

    fun scanPdfFiles() {
        viewModelScope.launch {
            _uiState.update { it.copy(isScanning = true, error = null) }

            try {
                val pdfFiles = withContext(Dispatchers.IO) {
                    val files = mutableListOf<File>()
                    val searchDirs = listOf(
                        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS),
                        File(Environment.getExternalStorageDirectory(), "Books"),
                        File(Environment.getExternalStorageDirectory(), "eBooks"),
                        File(Environment.getExternalStorageDirectory(), "PDF")
                    )

                    for (dir in searchDirs) {
                        if (dir.exists() && dir.isDirectory) {
                            scanDirectory(dir, files, maxDepth = 3)
                        }
                    }

                    files.distinctBy { it.absolutePath }
                        .sortedByDescending { it.lastModified() }
                }

                val savedBooksMap = withContext(Dispatchers.IO) {
                    val books = mutableMapOf<String, PdfBookEntity>()
                    for (file in pdfFiles) {
                        val book = pdfBookDao.getBook(file.absolutePath)
                        if (book != null) {
                            books[file.absolutePath] = book
                        }
                    }
                    books
                }

                val fileInfos = pdfFiles.map { file ->
                    val saved = savedBooksMap[file.absolutePath]
                    PdfFileInfo(
                        filePath = file.absolutePath,
                        fileName = file.nameWithoutExtension,
                        fileSize = file.length(),
                        lastModified = file.lastModified(),
                        lastPage = saved?.lastPage ?: 1,
                        totalPages = saved?.totalPages ?: 0,
                        lastReadAt = saved?.lastReadAt ?: 0
                    )
                }

                _uiState.update { it.copy(isScanning = false, scannedFiles = fileInfos) }
            } catch (e: Exception) {
                _uiState.update { it.copy(isScanning = false, error = "PDF 파일 스캔 실패: ${e.message}") }
            }
        }
    }

    private fun scanDirectory(dir: File, results: MutableList<File>, maxDepth: Int, currentDepth: Int = 0) {
        if (currentDepth > maxDepth) return
        val files = dir.listFiles() ?: return

        for (file in files) {
            if (file.isFile && file.extension.equals("pdf", ignoreCase = true)) {
                results.add(file)
            } else if (file.isDirectory && !file.name.startsWith(".")) {
                scanDirectory(file, results, maxDepth, currentDepth + 1)
            }
        }
    }

    fun deleteBook(filePath: String) {
        viewModelScope.launch {
            pdfBookDao.delete(filePath)
            _uiState.update { state ->
                state.copy(
                    scannedFiles = state.scannedFiles.map { file ->
                        if (file.filePath == filePath) {
                            file.copy(lastPage = 1, totalPages = 0, lastReadAt = 0)
                        } else file
                    }
                )
            }
        }
    }
}
