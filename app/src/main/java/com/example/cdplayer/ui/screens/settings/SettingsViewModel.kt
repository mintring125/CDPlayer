package com.example.cdplayer.ui.screens.settings

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

enum class ThemeMode {
    SYSTEM, LIGHT, DARK
}

data class SettingsUiState(
    val themeMode: ThemeMode = ThemeMode.SYSTEM,
    val autoScanEnabled: Boolean = true,
    val autoDownloadCoverArt: Boolean = true,
    val cacheSize: String = "계산 중..."
)

@HiltViewModel
class SettingsViewModel @Inject constructor(
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    companion object {
        val THEME_MODE = stringPreferencesKey("theme_mode")
        val AUTO_SCAN = booleanPreferencesKey("auto_scan")
        val AUTO_DOWNLOAD_COVER = booleanPreferencesKey("auto_download_cover")
    }

    init {
        loadSettings()
        calculateCacheSize()
    }

    private fun loadSettings() {
        viewModelScope.launch {
            context.dataStore.data.map { preferences ->
                SettingsUiState(
                    themeMode = ThemeMode.valueOf(
                        preferences[THEME_MODE] ?: ThemeMode.SYSTEM.name
                    ),
                    autoScanEnabled = preferences[AUTO_SCAN] ?: true,
                    autoDownloadCoverArt = preferences[AUTO_DOWNLOAD_COVER] ?: true
                )
            }.collect { settings ->
                _uiState.value = _uiState.value.copy(
                    themeMode = settings.themeMode,
                    autoScanEnabled = settings.autoScanEnabled,
                    autoDownloadCoverArt = settings.autoDownloadCoverArt
                )
            }
        }
    }

    fun setThemeMode(mode: ThemeMode) {
        viewModelScope.launch {
            context.dataStore.edit { preferences ->
                preferences[THEME_MODE] = mode.name
            }
            _uiState.value = _uiState.value.copy(themeMode = mode)
        }
    }

    fun setAutoScan(enabled: Boolean) {
        viewModelScope.launch {
            context.dataStore.edit { preferences ->
                preferences[AUTO_SCAN] = enabled
            }
            _uiState.value = _uiState.value.copy(autoScanEnabled = enabled)
        }
    }

    fun setAutoDownloadCoverArt(enabled: Boolean) {
        viewModelScope.launch {
            context.dataStore.edit { preferences ->
                preferences[AUTO_DOWNLOAD_COVER] = enabled
            }
            _uiState.value = _uiState.value.copy(autoDownloadCoverArt = enabled)
        }
    }

    fun clearCache() {
        viewModelScope.launch {
            context.cacheDir.deleteRecursively()
            context.filesDir.resolve("cover_art").deleteRecursively()
            calculateCacheSize()
        }
    }

    private fun calculateCacheSize() {
        viewModelScope.launch {
            val cacheSize = context.cacheDir.walkTopDown().sumOf { it.length() }
            val coverArtSize = context.filesDir.resolve("cover_art").walkTopDown().sumOf { it.length() }
            val totalSize = cacheSize + coverArtSize

            val sizeString = when {
                totalSize < 1024 -> "$totalSize B"
                totalSize < 1024 * 1024 -> "${totalSize / 1024} KB"
                else -> "${totalSize / (1024 * 1024)} MB"
            }

            _uiState.value = _uiState.value.copy(cacheSize = sizeString)
        }
    }
}
