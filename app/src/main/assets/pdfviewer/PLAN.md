# PDF 뷰어 기능 CD Player 앱 통합 계획

## 개요

현재 `D:\pdf viewer\index.html`에 구현된 이북 PDF 뷰어(해설, TTS, 단어 예문 기능 포함)를
**CD Player 안드로이드 앱**에 네이티브로 통합하는 계획.

---

## 현재 PDF 뷰어 기능 (웹 버전)

| 기능 | 설명 |
|------|------|
| PDF 렌더링 | PDF.js 기반 lazy rendering, 캔버스 캐시 |
| 1쪽/2쪽 보기 | 화면 비율에 따라 자동 전환, 수동 토글 |
| 페이지 기억 | localStorage로 마지막 페이지 저장 |
| AI 해설 | Gemini API로 페이지 이미지 분석 → 영어 문장 + 한글 번역 + 해설 |
| TTS | Web Speech API로 영어 문장 읽기, 음성/속도 설정 |
| 단어 예문 | 해설 내 단어 클릭 → Gemini로 예문 4개 생성 |

---

## 통합 전략: 2가지 방안

### 방안 A: WebView 임베딩 (권장 - 빠른 구현)

기존 `index.html`을 앱 assets에 포함하고 WebView로 로드.

**장점:**
- 기존 코드 거의 그대로 재사용
- PDF.js 렌더링 로직 재구현 불필요
- 빠른 개발

**단점:**
- 네이티브 UI와 스타일 차이
- 파일 접근 시 WebView ↔ 네이티브 브릿지 필요

### 방안 B: 완전 네이티브 (장기적 완성도)

Jetpack Compose + Android PDF 라이브러리로 전체 재구현.

**장점:**
- Material 3 UI 통일
- ExoPlayer TTS와 통합 가능
- 성능 최적화

**단점:**
- 개발량 대폭 증가
- PDF 렌더링 라이브러리 별도 필요 (AndroidPdfViewer, PdfRenderer 등)

---

## 방안 A 상세 구현 계획 (WebView 임베딩)

### Phase 1: 프로젝트 구조 추가

```
app/src/main/
├── assets/
│   └── pdfviewer/
│       └── index.html          ← 기존 웹 뷰어 (수정 버전)
├── java/com/example/cdplayer/
│   ├── ui/screens/pdf/
│   │   ├── PdfViewerScreen.kt  ← Compose 래퍼 화면
│   │   └── PdfViewerViewModel.kt
│   └── ...
```

### Phase 2: index.html 수정사항

1. **파일 선택 제거** - 앱에서 파일 경로를 JS로 전달
2. **API 키 분리** - 앱의 BuildConfig에서 JS 브릿지로 전달
3. **TTS 대체** - Android 네이티브 TTS 사용 (JS → 네이티브 호출)
4. **localStorage 대체** - JS 브릿지로 Room DB 또는 DataStore 연동
5. **테마 연동** - 앱의 다크/라이트 테마 CSS 변수로 전달

```javascript
// JS → Android 브릿지 예시
Android.speak(text, rate);       // TTS
Android.savePage(fileName, page); // 페이지 저장
Android.getApiKey();              // API 키
```

### Phase 3: PdfViewerScreen.kt 구현

```kotlin
// 핵심 구성요소
@Composable
fun PdfViewerScreen(filePath: String) {
    AndroidView(factory = { context ->
        WebView(context).apply {
            settings.javaScriptEnabled = true
            settings.allowFileAccess = true
            addJavascriptInterface(PdfBridge(...), "Android")
            loadUrl("file:///android_asset/pdfviewer/index.html")
        }
    })
}
```

**PdfBridge 클래스 (JS ↔ Native 인터페이스):**

| 메서드 | 방향 | 설명 |
|--------|------|------|
| `loadFile(path)` | Native → JS | PDF 파일 경로 전달 |
| `speak(text, rate)` | JS → Native | Android TTS 호출 |
| `stopSpeaking()` | JS → Native | TTS 중지 |
| `savePage(file, page)` | JS → Native | 마지막 페이지 DB 저장 |
| `loadPage(file)` | JS → Native | 저장된 페이지 조회 |
| `getApiKey()` | JS → Native | Gemini API 키 반환 |
| `getTheme()` | JS → Native | 현재 테마 반환 |

### Phase 4: 네비게이션 연동

```
기존 네비게이션:
  Home | Library | Search | Settings

추가:
  Home | Library | Books | Search | Settings
                    ↑
              PDF 파일 목록 화면
              → 파일 선택 → PdfViewerScreen
```

**수정 파일:**
- `MainActivity.kt` - 네비게이션 항목 추가
- `AppNavigation.kt` (또는 해당 네비게이션 파일) - Books 라우트 추가

### Phase 5: PDF 파일 관리

**BooksScreen.kt** (새 화면):
- 기기 내 PDF 파일 스캔 (MediaStore 또는 파일 시스템)
- 파일 목록 표시 (파일명, 크기, 마지막 읽은 페이지)
- 파일 선택 → PdfViewerScreen 으로 이동
- 외부 파일 불러오기 (SAF - Storage Access Framework)

**DB 확장:**
```kotlin
@Entity(tableName = "pdf_books")
data class PdfBookEntity(
    @PrimaryKey val filePath: String,
    val fileName: String,
    val lastPage: Int = 1,
    val totalPages: Int = 0,
    val lastReadAt: Long = 0
)
```

### Phase 6: 기존 기능과 시너지

| CD Player 기존 기능 | PDF 뷰어 연동 |
|---------------------|---------------|
| Android TTS | 해설 문장 읽기에 네이티브 TTS 사용 (더 자연스러운 음성) |
| 사전 기능 | 기존 Dictionary API를 해설 단어 검색에도 활용 |
| 다크/라이트 테마 | PDF 뷰어 배경색 자동 연동 |
| 북마크 시스템 | PDF 페이지 북마크 확장 |
| 슬립 타이머 | PDF 읽기 중에도 슬립 타이머 동작 |
| Room DB | 읽기 진행률 통합 관리 |

---

## 방안 B 상세 구현 계획 (완전 네이티브)

> 방안 A 이후 장기적으로 전환할 경우의 로드맵

### 필요 라이브러리

```kotlin
// PDF 렌더링
implementation("io.github.nicehash:android-pdf-viewer:3.2.0-beta.3")
// 또는 Android 기본 PdfRenderer (API 21+)

// Gemini API
implementation("com.google.ai.client.generativeai:generativeai:0.9.0")
```

### 네이티브 화면 구성

```
PdfViewerScreen (Compose)
├── TopBar (파일명, 설정 버튼)
├── PdfPageView
│   ├── SinglePageMode (Canvas 렌더링)
│   └── DualPageMode (Row { Page, Page })
│       ├── 왼쪽 해설 버튼 (좌상단)
│       └── 오른쪽 해설 버튼 (우상단)
├── ExplainSheet (BottomSheet)
│   ├── 영어 문장 (클릭 → TTS)
│   ├── 한글 번역
│   ├── 해설 + vocab-word (클릭 → 예문)
│   └── 예문 패널 (접기/펼치기)
├── NavigationArrows (좌/우)
└── BottomBar (슬라이더, 페이지 번호)
```

### ViewModel 구조

```kotlin
class PdfViewerViewModel : ViewModel() {
    val pdfState: StateFlow<PdfState>        // 현재 페이지, 총 페이지, 보기 모드
    val explainState: StateFlow<ExplainState> // 해설 결과, 로딩 상태
    val ttsState: StateFlow<TtsState>         // 음성, 속도, 재생 상태

    fun loadPdf(path: String)
    fun goToPage(page: Int)
    fun toggleViewMode()
    fun requestExplain(pageNum: Int)    // Gemini API 호출
    fun requestWordExamples(word: String)
    fun speak(text: String)
}
```

---

## 공통: 필요한 변경 요약

### 새로 만들 파일

| 파일 | 설명 |
|------|------|
| `PdfViewerScreen.kt` | PDF 뷰어 메인 화면 |
| `PdfViewerViewModel.kt` | 상태 관리 |
| `BooksScreen.kt` | PDF 파일 목록 화면 |
| `BooksViewModel.kt` | 파일 스캔/관리 |
| `PdfBookEntity.kt` | Room 엔티티 |
| `PdfBookDao.kt` | DB 쿼리 |
| `assets/pdfviewer/index.html` | WebView용 뷰어 (방안 A) |

### 수정할 기존 파일

| 파일 | 변경 내용 |
|------|----------|
| `MainActivity.kt` | 하단 네비게이션에 Books 탭 추가 |
| `AppDatabase.kt` | PdfBookEntity 테이블 추가, 버전 마이그레이션 |
| `AppModule.kt` | PdfBookDao 제공 추가 |
| `build.gradle.kts` | Gemini SDK 의존성 추가 (방안 B) |
| `AndroidManifest.xml` | 변경 없음 (기존 파일/네트워크 권한 충분) |

---

## 구현 순서 (권장)

```
1단계: WebView 기반 (방안 A)
  ├── index.html assets 포함
  ├── PdfViewerScreen + WebView 래퍼
  ├── JS 브릿지 (TTS, 페이지 저장)
  ├── BooksScreen (파일 목록)
  └── 네비게이션 연동

2단계: 안정화
  ├── 파일 접근 안정성 테스트 (대용량 PDF)
  ├── 메모리 관리 (WebView 리소스 해제)
  ├── 화면 회전 대응
  └── 오프라인 캐시 (해설 결과 DB 저장)

3단계: 점진적 네이티브화 (선택)
  ├── Gemini SDK 직접 호출
  ├── PDF 렌더링 네이티브 전환
  └── Compose UI로 해설 패널 재구현
```
