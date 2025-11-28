package com.example.nanithappybirthday.ui

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import com.example.nanithappybirthday.ui.theme.NanitHappyBirthdayTheme
import com.example.nanithappybirthday.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            NanitHappyBirthdayTheme {
                val uiState = viewModel.uiState.collectAsState().value

                val imagePicker = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.GetContent()
                ) { uri: Uri? ->
                    viewModel.updateBabyImage(uri)
                }

                MainScreen(
                    ipAddress = uiState.ipAddress,
                    onIpAddressChange = viewModel::saveIpAddress,
                    birthdayData = uiState.birthdayData,
                    babyImagePath = uiState.babyImagePath,
                    onUploadImageClicked = { imagePicker.launch("image/*") }
                )
            }
        }
    }
}