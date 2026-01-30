package com.example.cdplayer.ui.screens.pdfviewer

import androidx.compose.runtime.Composable

/**
 * WebView 기반 PDF 뷰어 화면
 *
 * TODO: 구현 예정
 * - WebView로 assets/pdfviewer/index.html 로드
 * - PdfBridge JS 인터페이스 연결:
 *   - loadFile(path): 네이티브에서 JS로 PDF 파일 전달
 *   - speak(text, rate): Android TTS 호출
 *   - stopSpeaking(): TTS 중지
 *   - savePage(file, page): Room DB에 마지막 페이지 저장
 *   - loadPage(file): 저장된 페이지 조회
 *   - getApiKey(): Gemini API 키 반환
 *   - getTheme(): 현재 테마(dark/light) 반환
 * - 화면 회전 시 WebView 상태 유지
 * - 뒤로가기 처리
 */
@Composable
fun PdfViewerScreen(
    filePath: String,
    onNavigateBack: () -> Unit
) {
    // TODO: implement
}
