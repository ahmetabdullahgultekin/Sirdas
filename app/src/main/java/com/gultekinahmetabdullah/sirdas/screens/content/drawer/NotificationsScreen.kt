package com.gultekinahmetabdullah.sirdas.screens.content.drawer

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationsScreen() {
    val receiveNotifications = remember { mutableStateOf(true) }
    val soundEnabled = remember { mutableStateOf(true) }
    val vibrateEnabled = remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Notifications") }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                text = "Notification Settings",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            // Toggle Notifications
            SwitchPreference(
                title = "Receive Notifications",
                isChecked = receiveNotifications.value,
                onCheckedChange = { receiveNotifications.value = it }
            )

            // Toggle Sound
            SwitchPreference(
                title = "Sound",
                isChecked = soundEnabled.value,
                onCheckedChange = { soundEnabled.value = it },
                enabled = receiveNotifications.value
            )

            // Toggle Vibration
            SwitchPreference(
                title = "Vibrate",
                isChecked = vibrateEnabled.value,
                onCheckedChange = { vibrateEnabled.value = it },
                enabled = receiveNotifications.value
            )
        }
    }
}

@Composable
fun SwitchPreference(
    title: String,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    enabled: Boolean = true
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = enabled) { onCheckedChange(!isChecked) }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            modifier = Modifier.weight(1f),
            color = if (enabled) MaterialTheme.colorScheme.onBackground else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        )
        Switch(
            checked = isChecked,
            onCheckedChange = onCheckedChange,
            enabled = enabled
        )
    }
}

@Preview(showBackground = true)
@Composable
fun NotificationsScreenPreview() {
    NotificationsScreen()
}
