package com.example.cdplayer.ui.screens.pdfviewer

import android.annotation.SuppressLint
import android.view.ViewGroup
import android.webkit.JavascriptInterface
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.launch

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun PdfViewerScreen(
    filePath: String,
    onNavigateBack: () -> Unit,
    viewModel: PdfViewerViewModel = hiltViewModel()
) {
    val coroutineScope = rememberCoroutineScope()
    var webView by remember { mutableStateOf<WebView?>(null) }

    BackHandler {
        viewModel.stopSpeaking()
        onNavigateBack()
    }

    DisposableEffect(Unit) {
        onDispose {
            viewModel.stopSpeaking()
            webView?.destroy()
        }
    }

    AndroidView(
        modifier = Modifier.fillMaxSize(),
        factory = { context ->
            WebView(context).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )

                settings.apply {
                    javaScriptEnabled = true
                    domStorageEnabled = true
                    allowFileAccess = true
                    allowContentAccess = true
                    mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
                    useWideViewPort = true
                    loadWithOverviewMode = true
                    setSupportZoom(false)
                    builtInZoomControls = false
                    mediaPlaybackRequiresUserGesture = false
                }

                webChromeClient = WebChromeClient()

                webViewClient = object : WebViewClient() {
                    override fun shouldOverrideUrlLoading(
                        view: WebView?,
                        request: WebResourceRequest?
                    ): Boolean {
                        val url = request?.url?.toString() ?: return false
                        // Allow CDN resources (pdf.js)
                        if (url.startsWith("https://cdnjs.cloudflare.com/") ||
                            url.startsWith("https://generativelanguage.googleapis.com/")) {
                            return false
                        }
                        return true
                    }

                    override fun onPageFinished(view: WebView?, url: String?) {
                        super.onPageFinished(view, url)
                        // After page loads, send the PDF file data
                        coroutineScope.launch {
                            val savedPage = viewModel.loadPage(filePath)
                            val base64Data = viewModel.readFileAsBase64(filePath)
                            if (base64Data != null) {
                                val fileName = java.io.File(filePath).name
                                // Call JS function to load PDF from base64
                                view?.evaluateJavascript(
                                    "if(typeof loadPdfFromBase64==='function'){loadPdfFromBase64('$fileName',$savedPage);}",
                                    null
                                )
                            }
                        }
                    }
                }

                // Add JS bridge
                addJavascriptInterface(
                    PdfBridge(viewModel, filePath, coroutineScope),
                    "PdfBridge"
                )

                webView = this

                // Load the HTML file from assets
                loadUrl("file:///android_asset/pdfviewer/index.html")
            }
        }
    )
}

private class PdfBridge(
    private val viewModel: PdfViewerViewModel,
    private val filePath: String,
    private val coroutineScope: kotlinx.coroutines.CoroutineScope
) {
    @JavascriptInterface
    fun getFileBase64(): String {
        return viewModel.readFileAsBase64(filePath) ?: ""
    }

    @JavascriptInterface
    fun getFileName(): String {
        return java.io.File(filePath).name
    }

    @JavascriptInterface
    fun savePage(page: Int, totalPages: Int) {
        viewModel.savePage(filePath, page, totalPages)
    }

    @JavascriptInterface
    fun loadPage(): Int {
        var result = 1
        val latch = java.util.concurrent.CountDownLatch(1)
        coroutineScope.launch {
            result = viewModel.loadPage(filePath)
            latch.countDown()
        }
        try {
            latch.await(2, java.util.concurrent.TimeUnit.SECONDS)
        } catch (_: Exception) {}
        return result
    }

    @JavascriptInterface
    fun speak(text: String, rate: Float) {
        viewModel.speak(text, rate)
    }

    @JavascriptInterface
    fun stopSpeaking() {
        viewModel.stopSpeaking()
    }

    @JavascriptInterface
    fun getApiKey(): String {
        return viewModel.getApiKey()
    }

    @JavascriptInterface
    fun getTheme(): String {
        return "dark"
    }

    @JavascriptInterface
    fun saveCover(base64Image: String) {
        viewModel.saveCoverImage(filePath, base64Image)
    }

    @JavascriptInterface
    fun saveRating(rating: Float) {
        viewModel.saveRating(filePath, rating)
    }

    @JavascriptInterface
    fun loadRating(): Float {
        var result = 0f
        val latch = java.util.concurrent.CountDownLatch(1)
        coroutineScope.launch {
            result = viewModel.loadRating(filePath)
            latch.countDown()
        }
        try {
            latch.await(2, java.util.concurrent.TimeUnit.SECONDS)
        } catch (_: Exception) {}
        return result
    }
}

