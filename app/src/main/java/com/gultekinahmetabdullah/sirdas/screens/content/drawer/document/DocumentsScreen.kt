package com.gultekinahmetabdullah.sirdas.screens.content.drawer.document

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.gultekinahmetabdullah.sirdas.viewmodels.DocumentViewModel
import kotlinx.coroutines.launch

@Composable
fun DocumentScreen(viewModel: DocumentViewModel = viewModel()) {
    val scope = rememberCoroutineScope()
    val documents by viewModel.documents.collectAsState()

    val pdfPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            scope.launch {
                viewModel.uploadDocument(uri)
            }
        }
    }

        LazyColumn(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(documents) { document ->
                DocumentItem(
                    documentName = document.name,
                    onViewClick = { viewModel.viewDocument(document.uri) },
                    onDeleteClick = { scope.launch { viewModel.deleteDocument(document.name) } }
                )
            }
        }
}

@Composable
fun DocumentItem(
    documentName: String,
    onViewClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable(onClick = onViewClick),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = documentName)
        IconButton(onClick = onDeleteClick) {
            Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete Document")
        }
    }
}



