package com.example.nanithappybirthday.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.nanithappybirthday.model.BirthdayData
import com.example.nanithappybirthday.ui.general.TextFieldDialog
import com.example.nanithappybirthday.ui.theme.NanitHappyBirthdayTheme
import com.example.nanithappybirthday.ui.theme.TextColor

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainScreen(
    ipAddress: String,
    onIpAddressChange: (String) -> Unit,
    birthdayData: BirthdayData?,
    onUploadImageClicked: () -> Unit = {}
) {
    var showDialog by remember { mutableStateOf(false) }

    Scaffold { _ ->
        Box(modifier = Modifier.fillMaxSize()) {
            if (birthdayData != null) {
                BirthdayScreen(birthdayData, onUploadImageClicked)
            } else {
                Text(
                    if (ipAddress.isEmpty()) "No IP address was set yet. Press the settings button to configure it." else "Oops! No birthday data was received.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = TextColor,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(30.dp)
                )
            }

            IconButton(
                onClick = { showDialog = true },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(50.dp),
                )
            }
        }
    }

    if (showDialog) {
        // todo add validation!
        EnterIpDialog(
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
fun EnterIpDialog(
    currentIp: String,
    onConfirm: (String) -> Unit,
    onDismiss: () -> Unit
) {
    TextFieldDialog(title = "Enter IP Address",
        textFieldLabel = "IP Address",
        currValue = currentIp,
        confirmText = "Save & Request",
        onConfirm = onConfirm,
        dismissText = "Cancel",
        onDismiss = onDismiss)
}

@Preview
@Composable
fun EnterDialogPreview() {
    NanitHappyBirthdayTheme {
        EnterIpDialog("", {}, {})
    }
}

@Preview
@Composable
fun MainNoIpPreview() {
    NanitHappyBirthdayTheme {
        MainScreen("", {}, null)
    }
}

@Preview
@Composable
fun MainNoBirthdayPreview() {
    NanitHappyBirthdayTheme {
        MainScreen("127.0.0.1", {}, null)
    }
}

@Preview
@Composable
fun MainWithBirthdayPreview() {
    NanitHappyBirthdayTheme {
        MainScreen("127.0.0.1", {}, BirthdayData("keren", 1760044800, "fox"))
    }
}