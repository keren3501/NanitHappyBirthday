package com.example.nanithappybirthday.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nanithappybirthday.model.BirthdayData
import com.example.nanithappybirthday.model.NanitRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

data class MainUiState(
    val ipAddress: String = "",
    val birthdayData: BirthdayData? = null,
    val babyImagePath: String? = null
)

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repo: NanitRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            combine(
                repo.birthdayDataFlow,
                repo.ipAddressFlow,
                repo.babyImagePathFlow
            ) { birthdayData, ip, imagePath ->
                MainUiState(
                    birthdayData = birthdayData,
                    ipAddress = ip,
                    babyImagePath = imagePath
                )
            }.collect {
                _uiState.value = it
            }
        }

        requestBirthdayFromServer()
    }

    fun saveIpAddress(ip: String) {
        viewModelScope.launch {
            repo.saveIpAddress(ip)
        }

        requestBirthdayFromServer()
    }

    private fun requestBirthdayFromServer() {
        viewModelScope.launch {
            repo.makeWebSocketRequest()
        }
    }

    fun updateBabyImage(babyImageUri: Uri?) {
        if (babyImageUri != null) {
            viewModelScope.launch {
                repo.updateBabyImage(babyImageUri)
            }
        }
    }
}