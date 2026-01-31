package com.example.cdplayer.ui.screens.books

import android.content.Context
import android.graphics.Bitmap
import android.graphics.pdf.PdfRenderer
import android.os.Environment
import android.os.ParcelFileDescriptor
import android.util.Log
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
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
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
    val lastReadAt: Long = 0,
    val isFavorite: Boolean = false,
    val coverPath: String? = null
)

@HiltViewModel
class BooksViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val pdfBookDao: PdfBookDao
) : ViewModel() {

    // 1. Raw files from disk scan
    private val _scannedFiles = MutableStateFlow<List<File>>(emptyList())
    
    // 2. Scan status
    private val _isScanning = MutableStateFlow(false)
    private val _error = MutableStateFlow<String?>(null)

    // 3. Database data (Source of Truth for metadata)
    val savedBooks = pdfBookDao.getAllBooks()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val pdfFavorites = pdfBookDao.getFavorites()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // 4. Combined UI State
    val uiState: StateFlow<BooksUiState> = combine(
        _scannedFiles,
        savedBooks,
        _isScanning,
        _error
    ) { files, books, isScanning, error ->
        val booksMap = books.associateBy { it.filePath }
        
        val fileInfos = files.map { file ->
            val saved = booksMap[file.absolutePath]
            PdfFileInfo(
                filePath = file.absolutePath,
                fileName = file.nameWithoutExtension,
                fileSize = file.length(),
                lastModified = file.lastModified(),
                lastPage = saved?.lastPage ?: 1,
                totalPages = saved?.totalPages ?: 0,
                lastReadAt = saved?.lastReadAt ?: 0,
                isFavorite = saved?.isFavorite ?: false,
                coverPath = saved?.coverPath
            )
        }
        
        BooksUiState(isScanning, fileInfos, error)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = BooksUiState(isScanning = true)
    )

    init {
        scanPdfFiles()
    }

    fun scanPdfFiles() {
        viewModelScope.launch {
            _isScanning.value = true
            _error.value = null

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

                _scannedFiles.value = pdfFiles
                _isScanning.value = false
            } catch (e: Exception) {
                _error.value = "PDF 파일 스캔 실패: ${e.message}"
                _isScanning.value = false
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
        }
    }

    fun toggleFavorite(filePath: String, fileName: String) {
        viewModelScope.launch {
            val book = pdfBookDao.getBook(filePath)
            val isFavorite = !(book?.isFavorite ?: false)
            
            if (book != null) {
                pdfBookDao.updateFavorite(filePath, isFavorite)
                
                // If becoming favorite and no cover exists, generate it
                if (isFavorite && book.coverPath == null) {
                    generateCoverImage(filePath)
                }
            } else {
                // Create book entry
                val newBook = PdfBookEntity(
                    filePath = filePath,
                    fileName = fileName,
                    isFavorite = true
                )
                pdfBookDao.upsert(newBook)
                generateCoverImage(filePath)
            }
        }
    }
    
    private suspend fun generateCoverImage(filePath: String) {
        withContext(Dispatchers.IO) {
            try {
                // Check if file exists
                val pdfFile = File(filePath)
                if (!pdfFile.exists()) return@withContext

                // Create covers directory
                val coversDir = File(context.filesDir, "pdf_covers")
                if (!coversDir.exists()) coversDir.mkdirs()
                
                // Generate unique filename
                val coverFileName = filePath.hashCode().toString() + ".png"
                val coverFile = File(coversDir, coverFileName)
                
                // If cover exists, update DB and return
                if (coverFile.exists()) {
                    pdfBookDao.updateCoverPath(filePath, coverFile.absolutePath)
                    return@withContext
                }

                // Render first page natively
                val pfd = ParcelFileDescriptor.open(pdfFile, ParcelFileDescriptor.MODE_READ_ONLY)
                val renderer = PdfRenderer(pfd)
                
                if (renderer.pageCount > 0) {
                    val page = renderer.openPage(0)
                    
                    // Create bitmap with proper aspect ratio (limit width to 400px)
                    val width = 400
                    val height = (width * page.height / page.width)
                    val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
                    
                    // Render
                    page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)
                    
                    // Save to file
                    val out = FileOutputStream(coverFile)
                    bitmap.compress(Bitmap.CompressFormat.PNG, 80, out)
                    out.flush()
                    out.close()
                    
                    page.close()
                    
                    // Update DB
                    pdfBookDao.updateCoverPath(filePath, coverFile.absolutePath)
                }
                
                renderer.close()
                pfd.close()
                
            } catch (e: Exception) {
                Log.e("BooksViewModel", "Failed to generate cover native: ${e.message}")
            }
        }
    }
}


