# 안드로이드 MP3 플레이어 앱 기획서

## 1. 프로젝트 개요

### 1.1 프로젝트 명
**CD Player** - 갤럭시 탭용 MP3 플레이어

### 1.2 목적
갤럭시 탭에 저장된 MP3 파일(음악 및 영어원서 오디오북)을 효율적으로 관리하고 재생할 수 있는 앱 개발

### 1.3 주요 타겟
- 음악 감상을 즐기는 사용자
- 영어원서 오디오북을 듣는 학습자
- 오프라인 미디어 라이브러리를 선호하는 사용자

---

## 2. 핵심 기능

### 2.1 미디어 스캔 및 관리
- **자동 미디어 스캔**
  - 갤럭시 탭 내부 저장소 전체 스캔
  - 외부 SD 카드 지원
  - MP3, M4A, FLAC, OGG 등 주요 오디오 포맷 지원
  - ID3 태그 정보 자동 추출 (제목, 아티스트, 앨범, 장르, 트랙 번호 등)

- **분류 기능**
  - 음악과 오디오북 자동 구분
  - 폴더 구조 기반 분류
  - 사용자 정의 플레이리스트 생성

### 2.2 표지 이미지 자동 검색

#### 2.2.1 메타데이터 추출 및 분석 전략

MP3 파일에는 **ID3 태그**라는 메타데이터가 내장되어 있어, AI 없이도 정확한 정보를 추출할 수 있습니다.

**1단계: ID3 태그 우선 사용**
```
MP3 파일 → ID3v2/ID3v1 태그 읽기
└─ 추출 정보:
   ├─ 제목 (Title)
   ├─ 아티스트/저자 (Artist/Author)
   ├─ 앨범/책 제목 (Album)
   ├─ 장르 (Genre)
   ├─ 발행년도 (Year)
   ├─ 트랙 번호 (Track)
   └─ 내장 앨범 아트 (Embedded Cover Art)
```

- ID3 태그에 이미 앨범 아트가 있으면 → 즉시 사용
- 없으면 → 태그 정보로 API 검색

**2단계: ID3 태그 기반 API 검색**
```
음악 파일:
  "{Artist} - {Album}" → Last.fm API
  "{Artist} {Album}" → MusicBrainz API

오디오북:
  "{Title} {Author}" → Google Books API
  "{Title}" → Open Library API
```

**3단계: 파일명/폴더 구조 분석 (ID3 태그 없을 시)**
```
파일명 패턴 인식:
  "아티스트 - 앨범 - 01. 제목.mp3"
  "Harry Potter - Chapter 01.mp3"
  "01 - Track Name.mp3"

폴더 구조 분석:
  "/Music/Beatles/Abbey Road/01 Come Together.mp3"
  "/Audiobooks/Harry Potter/Philosopher's Stone/Chapter 01.mp3"

→ 정규표현식으로 파싱 후 API 검색
```

**4단계: 오디오 지문 분석 (음악 전용, 선택사항)**
```
AcoustID + Chromaprint 사용
→ 실제 오디오를 분석해서 MusicBrainz DB와 매칭
→ 태그가 완전히 없거나 잘못된 경우에도 인식 가능
→ 오디오북에는 부적합 (각 파일이 고유하므로)
```

#### 2.2.2 표지 이미지 다운로드

- **자동 다운로드**
  - 음악: Last.fm API, Spotify API, MusicBrainz API
  - 오디오북: Google Books API, Open Library API
  - 여러 API에서 검색하여 최적의 이미지 선택 (해상도, 정확도)
  - 검색 신뢰도 점수 표시

- **수동 편집**
  - 사용자가 직접 이미지 업로드 가능
  - 갤러리에서 이미지 선택
  - API 검색 결과 중 선택
  - ID3 태그에 앨범 아트 직접 삽입 옵션

- **캐싱 및 저장**
  - 다운로드한 이미지 로컬 저장
  - 썸네일/원본 이미지 분리 저장
  - 데이터 사용량 절감
  - 오프라인 사용 가능

### 2.3 재생 기능
- **기본 재생 컨트롤**
  - 재생/일시정지
  - 이전/다음 트랙
  - 탐색 바를 통한 구간 이동
  - 반복 재생 (한 곡 반복, 전체 반복, 반복 안 함)
  - 셔플 재생

- **오디오북 특화 기능**
  - 북마크 기능 (여러 개 저장 가능)
  - 마지막 재생 위치 자동 저장
  - 챕터 구분 및 네비게이션
  - 재생 속도 조절 (0.5x ~ 2.0x)
  - 슬립 타이머

- **음질 설정**
  - 이퀄라이저 (프리셋 및 사용자 정의)
  - 베이스 부스트
  - 버츄얼라이저

### 2.4 UI/UX
- **메인 화면**
  - 최근 재생 목록
  - 앨범/책 커버 그리드 뷰
  - 검색 기능

- **플레이어 화면**
  - 대형 앨범 아트 표시
  - 재생 컨트롤
  - 진행 바
  - 플레이리스트/대기열

- **라이브러리 화면**
  - 아티스트별 보기
  - 앨범별 보기
  - 장르별 보기
  - 폴더별 보기
  - 플레이리스트

- **테마**
  - 라이트/다크 모드
  - 앨범 아트 기반 동적 색상

---

## 3. 기술 스펙

### 3.1 개발 환경
- **언어**: Kotlin
- **최소 SDK**: Android 8.0 (API 26)
- **타겟 SDK**: Android 14 (API 34)
- **IDE**: Android Studio

### 3.2 주요 라이브러리 및 프레임워크
- **UI**
  - Jetpack Compose (모던 UI)
  - Material Design 3

- **아키텍처**
  - MVVM + Repository Pattern
  - Jetpack Components (ViewModel, LiveData/Flow, Room)

- **미디어 처리**
  - ExoPlayer (Google 공식 미디어 플레이어)
  - MediaStore API (미디어 스캔)
  - JAudioTagger (ID3 태그 읽기/쓰기)
  - Chromaprint + AcoustID (오디오 지문 분석, 선택사항)

- **네트워크**
  - Retrofit (API 통신)
  - Coil (이미지 로딩)
  - OkHttp (HTTP 클라이언트)

- **데이터베이스**
  - Room (로컬 DB)

- **비동기 처리**
  - Kotlin Coroutines
  - Flow

### 3.3 API 통합
- **음악 메타데이터**
  - Last.fm API (앨범 정보, 커버 아트)
  - MusicBrainz API (음악 메타데이터 DB)
  - Spotify Web API (선택)
  - AcoustID API (오디오 지문 매칭, 선택사항)

- **오디오북 메타데이터**
  - Google Books API (책 정보, 표지 이미지)
  - Open Library API (오픈소스 책 DB)

### 3.4 권한
```xml
<uses-permission android:name="android.permission.READ_MEDIA_AUDIO" />
<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE"
                 android:maxSdkVersion="32" />
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
<uses-permission android:name="android.permission.FOREGROUND_SERVICE" />
<uses-permission android:name="android.permission.WAKE_LOCK" />
```

---

## 4. 데이터 모델

### 4.1 주요 엔티티

```kotlin
// 오디오 파일
data class AudioFile(
    val id: Long,
    val title: String,
    val artist: String?,
    val album: String?,
    val duration: Long,
    val path: String,
    val type: AudioType, // MUSIC, AUDIOBOOK
    val coverArtPath: String?,
    val lastPlayedPosition: Long = 0,
    val addedDate: Long,
    val genre: String?
)

// 플레이리스트
data class Playlist(
    val id: Long,
    val name: String,
    val createdDate: Long,
    val trackCount: Int,
    val coverArtPath: String?
)

// 북마크 (오디오북용)
data class Bookmark(
    val id: Long,
    val audioFileId: Long,
    val position: Long,
    val note: String?,
    val createdDate: Long
)
```

---

## 5. 화면 구성

### 5.1 화면 목록
1. **스플래시 화면** - 앱 로딩
2. **홈 화면** - 최근 재생, 추천 콘텐츠
3. **라이브러리 화면** - 전체 음악/오디오북 목록
4. **플레이어 화면** - 현재 재생 중인 미디어
5. **검색 화면** - 미디어 검색
6. **플레이리스트 화면** - 플레이리스트 관리
7. **설정 화면** - 앱 설정
8. **표지 편집 화면** - 앨범 아트 수정

---

## 6. 개발 로드맵

### Phase 1: 기본 기능 (MVP)
- [ ] 프로젝트 초기 설정
- [ ] 미디어 스캔 기능
- [ ] 기본 재생 기능
- [ ] 간단한 UI (목록, 플레이어)
- [ ] Room 데이터베이스 구축

### Phase 2: 표지 검색 기능
- [ ] ID3 태그 읽기 및 파싱 (JAudioTagger)
- [ ] 내장 앨범 아트 추출
- [ ] API 통합 (Last.fm, Google Books)
- [ ] 파일명/폴더 구조 분석 로직
- [ ] 자동 표지 다운로드 및 매칭
- [ ] 이미지 캐싱
- [ ] 수동 편집 기능
- [ ] (선택) 오디오 지문 분석 (AcoustID)

### Phase 3: 고급 기능
- [ ] 플레이리스트 관리
- [ ] 북마크 기능
- [ ] 재생 속도 조절
- [ ] 이퀄라이저
- [ ] 슬립 타이머

### Phase 4: UI/UX 개선
- [ ] Material Design 3 적용
- [ ] 다크 모드
- [ ] 애니메이션 추가
- [ ] 접근성 개선

### Phase 5: 최적화 및 배포
- [ ] 성능 최적화
- [ ] 배터리 사용량 최적화
- [ ] 버그 수정
- [ ] Google Play Store 배포 준비

---

## 7. ID3 태그 및 메타데이터 처리 상세

### 7.1 ID3 태그 버전 지원
- **ID3v2.4** (최신, UTF-8 지원)
- **ID3v2.3** (가장 널리 사용됨)
- **ID3v1** (레거시, 호환성을 위해 지원)

### 7.2 추출 가능한 태그 필드

| 필드명 | ID3 태그 | 용도 |
|--------|----------|------|
| 제목 | TIT2 | 곡/챕터 제목 |
| 아티스트 | TPE1 | 가수/저자 |
| 앨범 | TALB | 앨범/책 제목 |
| 앨범 아티스트 | TPE2 | 컴필레이션 앨범용 |
| 장르 | TCON | 분류 |
| 발행년도 | TDRC/TYER | 출시 연도 |
| 트랙 번호 | TRCK | 순서 정렬 |
| 디스크 번호 | TPOS | 멀티 디스크 |
| 커버 아트 | APIC | 내장 이미지 |
| 코멘트 | COMM | 메모 |

### 7.3 처리 플로우

```
파일 스캔 시작
    ↓
MediaStore API로 오디오 파일 검색
    ↓
각 파일마다:
    ├─ JAudioTagger로 ID3 태그 읽기
    ├─ 태그 정보 추출 및 정규화
    ├─ 내장 앨범 아트 확인
    │   ├─ 있음 → 로컬 저장
    │   └─ 없음 → 표지 검색 큐에 추가
    └─ Room DB에 저장
    ↓
표지 검색 큐 처리:
    ├─ ID3 태그 기반 API 검색
    ├─ 태그 없음 → 파일명/폴더 분석
    ├─ 여전히 없음 → 오디오 지문 분석 (선택)
    └─ 검색 결과 저장 및 캐싱
```

### 7.4 파일명 파싱 패턴

```kotlin
// 일반적인 파일명 패턴
"Artist - Album - 01 - Title.mp3"
"01. Title.mp3"
"Track 01 - Title.mp3"
"Harry Potter and the Philosopher's Stone - Chapter 01.mp3"

// 정규표현식 예시
val pattern1 = Regex("(.*) - (.*) - \\d+ - (.*)\\.mp3")
val pattern2 = Regex("\\d+\\.? (.*)\\.mp3")
val audioBookPattern = Regex("(.*) - Chapter (\\d+)\\.mp3")
```

### 7.5 음악 vs 오디오북 구분 로직

```kotlin
fun detectAudioType(file: AudioFile): AudioType {
    return when {
        // 1. 폴더 경로로 판단
        file.path.contains("/Audiobooks/", ignoreCase = true) -> AUDIOBOOK
        file.path.contains("/Music/", ignoreCase = true) -> MUSIC

        // 2. 장르 태그로 판단
        file.genre?.contains("Audiobook", ignoreCase = true) == true -> AUDIOBOOK
        file.genre?.contains("Spoken", ignoreCase = true) == true -> AUDIOBOOK

        // 3. 파일명 패턴으로 판단
        file.title?.contains("Chapter", ignoreCase = true) == true -> AUDIOBOOK

        // 4. 재생 시간으로 판단 (오디오북은 보통 30분 이상)
        file.duration > 30 * 60 * 1000 -> AUDIOBOOK

        // 5. 기본값
        else -> MUSIC
    }
}
```

---

## 8. 주의사항

### 8.1 저작권
- 앨범 아트 및 메타데이터 사용 시 각 API의 이용 약관 준수
- 사용자가 합법적으로 소유한 미디어 파일만 재생

### 8.2 성능
- 대량의 미디어 파일 처리 시 메모리 관리
- 백그라운드 재생 시 배터리 효율성
- 이미지 로딩 최적화 (썸네일, 압축)
- ID3 태그 읽기 최적화 (백그라운드 스레드, 배치 처리)

### 8.3 호환성
- 다양한 화면 크기 지원 (특히 태블릿)
- Android 버전별 권한 처리 (Android 13+ 미디어 권한)
- 다양한 오디오 포맷 지원
- 다양한 ID3 태그 인코딩 처리 (UTF-8, ISO-8859-1 등)

---

## 9. 차별화 포인트

1. **오디오북 특화 기능**: 일반 음악 플레이어와 달리 오디오북 학습자를 위한 북마크, 속도 조절 등 제공
2. **자동 표지 검색**: 수동으로 앨범 아트를 추가하는 번거로움 제거
3. **듀얼 모드**: 음악과 오디오북을 하나의 앱에서 관리
4. **깔끔한 UI**: 태블릿에 최적화된 넓은 화면 활용
5. **지능형 메타데이터 추출**: ID3 태그, 파일명, 폴더 구조, 오디오 지문 등 다층적 분석

---

## 10. 향후 확장 가능성

- 클라우드 동기화 (Google Drive, Dropbox)
- 가사 표시 기능
- 팟캐스트 지원
- 스트리밍 서비스 통합
- 위젯 지원
- 차량용 Android Auto 지원
- 웨어러블 기기 지원
