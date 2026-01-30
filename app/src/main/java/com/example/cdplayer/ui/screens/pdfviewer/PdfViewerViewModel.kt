package com.example.cdplayer.ui.screens.pdfviewer

import androidx.lifecycle.ViewModel
import com.example.cdplayer.data.local.dao.PdfBookDao
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * PDF 뷰어 ViewModel
 *
 * TODO: 구현 예정
 * - JS 브릿지에서 호출할 페이지 저장/로드
 * - Android TTS 관리
 * - Gemini API 키 제공
 */
@HiltViewModel
class PdfViewerViewModel @Inject constructor(
    private val pdfBookDao: PdfBookDao
) : ViewModel() {
    // TODO: implement
}
