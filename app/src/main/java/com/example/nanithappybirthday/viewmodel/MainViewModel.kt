package com.example.nanithappybirthday.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nanithappybirthday.model.BirthdayData
import com.example.nanithappybirthday.model.NanitRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class MainUiState(
    val ipAddress: String = "",
    val birthdayData: BirthdayData? = null
)

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repo: NanitRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            loadSavedData()
        }
    }

    private suspend fun loadSavedData() {
        val savedIp = repo.getIpAddress()
        val savedResult = repo.getLastBirthdayData()

        _uiState.update {
            it.copy(
                ipAddress = savedIp,
                birthdayData = savedResult
            )
        }

        // Make request if:
        // 1. No cached result exists, OR
        // 2. IP exists but doesn't match the last requested IP
        if (savedIp.isNotEmpty() && savedResult == null) {
            requestBirthdayFromServer()
        }
    }

    fun saveIpAddress(ip: String) {
        viewModelScope.launch {
            repo.saveIpAddress(ip)

            _uiState.update { it.copy(ipAddress = ip) }

            requestBirthdayFromServer()
        }
    }

    private fun requestBirthdayFromServer() {
        viewModelScope.launch {
            try {
                val result = repo.makeHttpRequest()
                result.onSuccess { data ->
                    // Save to DataStore for persistence
                    repo.saveLastBirthdayData(data)

                    _uiState.update {
                        it.copy(
                            birthdayData = data,
                        )
                    }
                }.onFailure { error ->
                    Log.d("MainViewModel", "${error.message}")
                }
            } catch (e: Exception) {
                Log.d("MainViewModel", "${e.message}")
            }
        }
    }
}