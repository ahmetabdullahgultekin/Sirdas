package com.gultekinahmetabdullah.sirdas.screens.content.drawer

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.gultekinahmetabdullah.sirdas.viewmodels.PreferencesViewModel

@Composable
fun NotificationsScreen(
    viewModel: PreferencesViewModel,
    onNotificationsEnabledChange: (Boolean) -> Unit,
    onSoundEnabledChange: (Boolean) -> Unit,
    onVibrationEnabledChange: (Boolean) -> Unit
) {
    val receiveNotifications =
        remember { mutableStateOf(viewModel.userPreferences.value?.notificationEnabled ?: true) }
    val soundEnabled =
        remember { mutableStateOf(viewModel.userPreferences.value?.soundEnabled ?: true) }
    val vibrateEnabled =
        remember { mutableStateOf(viewModel.userPreferences.value?.vibrationEnabled ?: true) }

    Column(
        modifier = Modifier
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
            onCheckedChange = { onNotificationsEnabledChange(it); receiveNotifications.value = it }
        )

        // Toggle Sound
        SwitchPreference(
            title = "Sound",
            isChecked = soundEnabled.value,
            onCheckedChange = { onSoundEnabledChange(it); soundEnabled.value = it },
            enabled = receiveNotifications.value
        )

        // Toggle Vibration
        SwitchPreference(
            title = "Vibrate",
            isChecked = vibrateEnabled.value,
            onCheckedChange = { onVibrationEnabledChange(it); vibrateEnabled.value = it },
            enabled = receiveNotifications.value
        )
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
            color = if (enabled) MaterialTheme.colorScheme.onBackground else MaterialTheme.colorScheme.onSurface.copy(
                alpha = 0.6f
            )
        )
        Switch(
            checked = isChecked,
            onCheckedChange = onCheckedChange,
            enabled = enabled
        )
    }
}
