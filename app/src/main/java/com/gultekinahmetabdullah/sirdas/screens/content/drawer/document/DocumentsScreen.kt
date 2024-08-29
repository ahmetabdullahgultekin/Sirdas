package com.gultekinahmetabdullah.sirdas.screens.content.drawer.document

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.gultekinahmetabdullah.sirdas.R
import com.gultekinahmetabdullah.sirdas.classes.dataclasses.Document
import com.gultekinahmetabdullah.sirdas.viewmodels.DocumentViewModel
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DocumentScreen(viewModel: DocumentViewModel = viewModel()) {
    val context = LocalContext.current
    var selectedUri by remember { mutableStateOf<Uri?>(null) }
    var fileName by remember { mutableStateOf("new") }
    var renameFileItem by remember { mutableStateOf<Document?>(null) }
    var newFileName by remember { mutableStateOf("") }

    // Register file picker launcher
    val filePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        selectedUri = uri
        fileName = context.getFileName(uri!!).toString()
    }

    // Fetch files on first load
    LaunchedEffect(Unit) {
        viewModel.fetchFiles()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0f),
                title = { Text("Documents") }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { filePickerLauncher.launch("*/*") }) {
                Icon(Icons.Default.Add, contentDescription = "Upload File")
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            // File upload section
            if (selectedUri != null) {
                Column {
                    BasicTextField(
                        value = fileName,
                        onValueChange = { fileName = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(
                                1.dp,
                                MaterialTheme.colorScheme.onSurface,
                                MaterialTheme.shapes.small
                            ),
                        textStyle = TextStyle(MaterialTheme.colorScheme.onSurface),
                        decorationBox = { innerTextField ->
                            Box(
                                modifier = Modifier.padding(8.dp)
                            ) {
                                if (fileName.isEmpty()) Text("Enter file name")
                                innerTextField()
                            }
                        }
                    )
                    Button(
                        onClick = {
                            selectedUri?.let {
                                viewModel.uploadFile(it, fileName)
                                fileName = ""
                                selectedUri = null
                            }
                        },
                        modifier = Modifier.padding(top = 8.dp)
                    ) {
                        Text("Upload")
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (viewModel.fileItems.isEmpty()) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            }

            // List of files
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxHeight()
            ) {

                items(viewModel.fileItems.size) { index ->
                    FileItemRow(fileItem = viewModel.fileItems[index],
                        onDownload = {
                            viewModel.downloadFile(
                                context = context,
                                fileName = viewModel.fileItems[index].name,
                                downloadUrl = viewModel.fileItems[index].downloadUrl
                            )
                        },
                        onRename = {
                            renameFileItem = viewModel.fileItems[index]
                            newFileName = viewModel.fileItems[index].name
                        },
                        onDelete = {
                            viewModel.deleteFile(viewModel.fileItems[index])
                        })
                }
            }
        }

        // Rename dialog
        if (renameFileItem != null) {
            AlertDialog(
                onDismissRequest = { renameFileItem = null },
                title = { Text("Rename File") },
                text = {
                    Column {
                        Text("Enter new name for the file:")
                        TextField(
                            value = newFileName,
                            onValueChange = { newFileName = it },
                            placeholder = { Text("New file name") }
                        )
                    }
                },
                confirmButton = {
                    Button(onClick = {
                        renameFileItem?.let {
                            viewModel.updateFileName(context, it, newFileName)
                            renameFileItem = null
                            newFileName = ""
                        }
                    }) {
                        Text("Rename")
                    }
                },
                dismissButton = {
                    Button(onClick = { renameFileItem = null }) {
                        Text("Cancel")
                    }
                }
            )
        }
    }
}

@Composable
fun FileItemRow(
    fileItem: Document, onDownload: () -> Unit, onRename: () -> Unit, onDelete: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Text(
            text = fileItem.name,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.weight(1f)
        )

        IconButton(onClick = { onDownload() }) {
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.baseline_file_download_24),
                contentDescription =
                "Download"
            )
        }

        IconButton(onClick = { onRename() }) {
            Icon(
                Icons.Default.Edit,
                contentDescription = "Rename"
            )
        }

        IconButton(onClick = { onDelete() }) {
            Icon(
                Icons.Default.Delete,
                contentDescription = "Delete",
                tint = MaterialTheme.colorScheme.error
            )
        }
    }
}


fun Context.getFileName(uri: Uri): String? = when (uri.scheme) {
    ContentResolver.SCHEME_CONTENT -> getContentFileName(uri)
    else -> uri.path?.let(::File)?.name
}

private fun Context.getContentFileName(uri: Uri): String? = runCatching {
    contentResolver.query(uri, null, null, null, null)?.use { cursor ->
        cursor.moveToFirst()
        return@use cursor.getColumnIndexOrThrow(OpenableColumns.DISPLAY_NAME).let(cursor::getString)
    }
}.getOrNull()

