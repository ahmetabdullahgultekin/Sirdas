package com.gultekinahmetabdullah.sirdas.screens.content.drawer

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseAuth
import com.gultekinahmetabdullah.sirdas.classes.dataclasses.UserPreferences
import com.gultekinahmetabdullah.sirdas.classes.enums.Languages
import com.gultekinahmetabdullah.sirdas.viewmodels.PreferencesViewModel
import kotlinx.coroutines.launch

@Composable
fun SettingsScreen(
    viewModel: PreferencesViewModel,
    onSavePreferences: (UserPreferences) -> Unit,
    onCancel: () -> Unit
) {
    val darkModeEnabled = remember {
        mutableStateOf(viewModel.userPreferences.value?.themeDark ?: false)
    }
    val languageOptions = Languages.entries
    val selectedLanguage = remember {
        mutableStateOf(viewModel.userPreferences.value?.language ?: Languages.ENGLISH)
    }
    val notificationsEnabled = remember {
        mutableStateOf(viewModel.userPreferences.value?.notificationEnabled ?: true)
    }
    val soundEnabled = remember {
        mutableStateOf(viewModel.userPreferences.value?.soundEnabled ?: true)
    }
    val vibrationEnabled = remember {
        mutableStateOf(viewModel.userPreferences.value?.vibrationEnabled ?: true)
    }
    val userId = FirebaseAuth.getInstance().currentUser?.uid ?: ""

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {

        Text(
            text = "Theme",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(16.dp)
        )

        SwitchPreference(
            title = "Dark Mode",
            isChecked = darkModeEnabled.value,
            onCheckedChange = { darkModeEnabled.value = it },
        )

        HorizontalDivider()

        LanguagePreference(
            title = "Language",
            options = languageOptions,
            selectedOption = selectedLanguage.value,
            onOptionSelected = { selectedLanguage.value = it }
        )

        HorizontalDivider()

        NotificationsScreen(
            viewModel = viewModel,
            onNotificationsEnabledChange = { notificationsEnabled.value = it },
            onSoundEnabledChange = { soundEnabled.value = it },
            onVibrationEnabledChange = { vibrationEnabled.value = it }
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(onClick = { onCancel() }) {
                Text("Cancel")
            }

            Button(onClick = {
                scope.launch {
                    if (userId.isNotEmpty()) {

                        val userPreferences = UserPreferences(
                            userId = userId,
                            themeDark = darkModeEnabled.value,
                            language = selectedLanguage.value,
                            notificationEnabled = notificationsEnabled.value,
                            soundEnabled = soundEnabled.value,
                            vibrationEnabled = vibrationEnabled.value
                        )
                        onSavePreferences(userPreferences)
                        viewModel.updateUserPreferences(userPreferences)
                    } else {
                        snackbarHostState.showSnackbar("User ID not found.")
                    }
                    onCancel().also {
                        snackbarHostState.showSnackbar("Settings saved.")
                    }
                }
            }) {
                Text("Save")
            }
        }
    }
}

@Composable
fun SwitchPreference(
    title: String,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onCheckedChange(!isChecked)
            }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = title, modifier = Modifier.weight(1f))
        Switch(
            checked = isChecked,
            onCheckedChange = onCheckedChange
        )
    }
}

@Composable
fun LanguagePreference(
    title: String,
    options: List<Languages>,
    selectedOption: Languages,
    onOptionSelected: (Languages) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(text = title)
        Spacer(modifier = Modifier.height(8.dp))
        options.forEach { option ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(
                        enabled = option.ordinal < 2,
                        onClick = { onOptionSelected(option) }
                    )
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = selectedOption == option,
                    onClick = {
                        if (option.ordinal < 2)
                            onOptionSelected(option)
                    },
                    enabled = option.ordinal < 2
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = option.name.lowercase().replaceFirstChar { it.uppercase() },
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (option.ordinal < 2) MaterialTheme.colorScheme.onSurface
                    else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                    modifier = Modifier.weight(1f),
                )
            }
        }
    }
}