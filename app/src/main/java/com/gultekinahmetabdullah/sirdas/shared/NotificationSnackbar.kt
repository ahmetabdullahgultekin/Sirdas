package com.gultekinahmetabdullah.sirdas.shared

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.gultekinahmetabdullah.sirdas.classes.enums.PopupType

@Composable
fun NotificationSnackbar(
    message: String,
    actionLabel: String? = null,
    onActionClick: (() -> Unit)? = null,
    snackbarHostState: SnackbarHostState
) {
    LaunchedEffect(snackbarHostState) {
        snackbarHostState.showSnackbar(
            message = message,
            actionLabel = actionLabel,
            duration = SnackbarDuration.Short
        )
    }

    SnackbarHost(hostState = snackbarHostState)
}

@Composable
fun ShowPopup(
    type: PopupType,
    snackbarHostState: SnackbarHostState
) {
    val message = when (type) {
        PopupType.INFO -> "This is an information message."
        PopupType.WARNING -> "This is a warning message."
        PopupType.ERROR -> "This is an error message."
    }

    val actionLabel = when (type) {
        PopupType.INFO -> "Got it"
        PopupType.WARNING -> "Be Careful"
        PopupType.ERROR -> "Fix it"
    }

    NotificationSnackbar(
        message = message,
        actionLabel = actionLabel,
        onActionClick = { /* Optional: Handle action click */ },
        snackbarHostState = snackbarHostState
    )
}

