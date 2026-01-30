package com.example.cdplayer.ui.screens.books

import androidx.lifecycle.ViewModel
import com.example.cdplayer.data.local.dao.PdfBookDao
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * PDF 도서 관리 ViewModel
 *
 * TODO: 구현 예정
 * - PDF 파일 스캔 (MediaStore / 파일시스템)
 * - 읽기 기록 관리 (PdfBookDao)
 * - 파일 삭제/이름변경
 */
@HiltViewModel
class BooksViewModel @Inject constructor(
    private val pdfBookDao: PdfBookDao
) : ViewModel() {
    // TODO: implement
}
