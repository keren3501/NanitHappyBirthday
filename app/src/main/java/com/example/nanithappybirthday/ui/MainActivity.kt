package com.example.nanithappybirthday.ui

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.nanithappybirthday.ui.theme.NanitHappyBirthdayTheme
import com.example.nanithappybirthday.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NanitHappyBirthdayTheme {
                val uiState = viewModel.uiState.collectAsState().value

                MainScreen(
                    ipAddress = uiState.ipAddress,
                    onIpAddressChange = viewModel::saveIpAddress,
                    birthdayData = uiState.birthdayData)
            }
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainScreen(ipAddress: String, onIpAddressChange: (String) -> Unit, birthdayData: String) {
    var showDialog by remember { mutableStateOf(false) }

    Scaffold { _ ->
        Box(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column() {
                    Text("Current IP: ${ipAddress.ifEmpty { "Not set" }}")
                    Text("Birthday data: ${birthdayData}")
                }
            }

            IconButton(
                onClick = { showDialog = true },
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(16.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(50.dp)
                )
            }
        }
    }

    if (showDialog) {
        // todo add validation!
        IpAddressDialog(
            currentIp = ipAddress,
            onDismiss = { showDialog = false },
            onConfirm = { newIp ->
                onIpAddressChange(newIp)
                showDialog = false
            }
        )
    }
}

@Composable
fun IpAddressDialog(
    currentIp: String,
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    var ipText by remember { mutableStateOf(currentIp) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Enter IP Address") },
        text = {
            Column {
                OutlinedTextField(
                    value = ipText,
                    onValueChange = { ipText = it },
                    label = { Text("IP Address") },
                    placeholder = { Text("192.168.1.115") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            TextButton(onClick = { onConfirm(ipText) }) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Preview
@Composable
fun ScreenPreview(){
    MainScreen("192.168.1.115", {}, "birthday")
}

@Preview
@Composable
fun DialogPreview(){
    IpAddressDialog("192.168.1.115", {}, {})
}