package com.example.linkgrabber.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.linkgrabber.data.model.DownloadItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class DownloadUiState(
    val inputUrl: String = "",
    val isLoading: Boolean = false,
    val downloadProgress: Int = 0,
    val downloadedItems: List<DownloadItem> = emptyList(),
    val error: String? = null,
    val toastMessage: String? = null
)

class LinkGrabberViewModel(private val application: Application) : AndroidViewModel(application) {
    
    private val _uiState = MutableStateFlow(DownloadUiState())
    val uiState: StateFlow<DownloadUiState> = _uiState.asStateFlow()
    
    private var downloadJob: Job? = null
    
    fun setInputUrl(url: String) {
        _uiState.update { it.copy(inputUrl = url) }
    }
    
    fun startDownload(url: String) {
        downloadJob = viewModelScope.launch(Dispatchers.IO) {
            if (url.isEmpty()) {
                showError("لطفاً یک لینک معتبر وارد کنید")
                return@launch
            }
            
            try {
                _uiState.update { it.copy(isLoading = true, error = null) }
                
                // شبیه‌سازی دانلود
                for (i in 0..100 step 10) {
                    _uiState.update { it.copy(downloadProgress = i) }
                    kotlinx.coroutines.delay(500)
                }
                
                completeDownload()
                
            } catch (e: Exception) {
                showError(e.message ?: "خطای نامشخص")
            } finally {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }
    
    private fun completeDownload() {
        _uiState.update { state ->
            val newItem = DownloadItem(
                id = System.currentTimeMillis().toString(),
                title = state.inputUrl,
                url = state.inputUrl,
                platform = "Unknown",
                thumbnail = "",
                downloadedAt = System.currentTimeMillis(),
                quality = "720p"
            )
            state.copy(
                downloadProgress = 100,
                downloadedItems = state.downloadedItems + newItem,
                toastMessage = "دانلود با موفقیت انجام شد"
            )
        }
    }
    
    private fun showError(message: String) {
        _uiState.update { it.copy(error = message, isLoading = false) }
    }
    
    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }
    
    override fun onCleared() {
        super.onCleared()
        downloadJob?.cancel()
    }
}

class LinkGrabberViewModelFactory(
    private val application: Application
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LinkGrabberViewModel::class.java)) {
            return LinkGrabberViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
