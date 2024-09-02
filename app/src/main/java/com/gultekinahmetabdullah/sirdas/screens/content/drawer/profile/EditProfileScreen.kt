package com.gultekinahmetabdullah.sirdas.screens.content.drawer.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseAuth
import com.gultekinahmetabdullah.sirdas.R
import com.gultekinahmetabdullah.sirdas.classes.dataclasses.User
import com.gultekinahmetabdullah.sirdas.classes.enums.Avatars
import com.gultekinahmetabdullah.sirdas.viewmodels.UserViewModel
import kotlinx.coroutines.launch
import kotlin.enums.EnumEntries

@Composable
fun EditProfileScreen(viewModel: UserViewModel, onCancel: () -> Unit) {
    var name by remember { mutableStateOf(viewModel.userProfile?.name ?: "") }
    var password by remember { mutableStateOf(viewModel.userProfile?.password ?: "") }
    var confirmPassword by remember { mutableStateOf(viewModel.userProfile?.password ?: "") }
    var profileIconID by remember { mutableIntStateOf(viewModel.userProfile?.profileIconID ?: 0) }
    val passwordVisibility = remember { mutableStateOf(false) }
    val currentUser = FirebaseAuth.getInstance().currentUser

    /*    name = viewModel.userProfile?.name.toString()
        password = viewModel.userProfile?.password.toString()
        confirmPassword = viewModel.userProfile?.password.toString()
        //profileIconID = viewModel.userProfile?.profileIconID ?: 0 //Caused image to not change*/

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Name") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation = if (passwordVisibility.value) VisualTransformation.None else PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            trailingIcon = {
                IconButton(onClick = { passwordVisibility.value = !passwordVisibility.value }) {
                    Icon(
                        imageVector = if (passwordVisibility.value) ImageVector.vectorResource(
                            id = R.drawable.baseline_visibility_off_24
                        ) else ImageVector.vectorResource(
                            id = R.drawable.baseline_visibility_24
                        ),
                        contentDescription = if (passwordVisibility.value) "Hide password" else "Show password"
                    )
                }
            }
        )

        OutlinedTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = { Text("Confirm Password") },
            visualTransformation = if (passwordVisibility.value) VisualTransformation.None else PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            trailingIcon = {
                IconButton(onClick = { passwordVisibility.value = !passwordVisibility.value }) {
                    Icon(
                        imageVector = if (passwordVisibility.value) ImageVector.vectorResource(
                            id = R.drawable.baseline_visibility_off_24
                        ) else ImageVector.vectorResource(
                            id = R.drawable.baseline_visibility_24
                        ),
                        contentDescription = if (passwordVisibility.value) "Hide password" else "Show password"
                    )
                }
            }
        )

        HorizontalDivider()

        AvatarPicker(
            avatarList = Avatars.entries,
            onAvatarSelected = { profileIconID = it }
        )

        HorizontalDivider()

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(onClick = { onCancel() }) {
                Text("Cancel")
            }

            Button(onClick = {
                var result = true
                scope.launch {
                    if (password != confirmPassword) {
                        result = false
                        snackbarHostState.showSnackbar("Passwords do not match.")
                        return@launch
                    } else if (password.length < 6) {
                        result = false
                        snackbarHostState.showSnackbar("Password must be at least 6 characters.")
                        return@launch
                    } else if (name.isEmpty()) {
                        result = false
                        snackbarHostState.showSnackbar("Name cannot be empty.")
                        return@launch
                    } else if (profileIconID < 0) {
                        result = false
                        snackbarHostState.showSnackbar("Please select an avatar.")
                        return@launch
                    } else if (currentUser == null) {
                        result = false
                        snackbarHostState.showSnackbar("User not found.")
                        return@launch
                    }
                }

                scope.launch {
                    if (result) {
                        viewModel.saveUserProfile(
                            updatedProfile = User(
                                uid = currentUser?.uid ?: "Not Found",
                                profileIconID = profileIconID,
                                name = name,
                                email = currentUser?.email ?: "Not Found",
                                password = password
                            )
                        )
                        onCancel().also {
                            snackbarHostState.showSnackbar("Profile updated.")
                        }
                    } else {
                        snackbarHostState.showSnackbar("Profile update failed.")
                    }
                }


            }) {
                Text("Save")
            }
        }
    }
}

@Composable
fun AvatarPicker(
    avatarList: EnumEntries<Avatars>,
    onAvatarSelected: (Int) -> Unit
) {
    var selectedAvatarIndex by remember { mutableIntStateOf(0) }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        // Dropdown menu for avatar selection
        var expanded by remember { mutableStateOf(false) }
        Box(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "Select Avatar : ${avatarList[selectedAvatarIndex]}",
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expanded = !expanded }
                    .border(1.dp, Color.Black, CircleShape)
                    .padding(8.dp)
            )
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.fillMaxWidth()
            ) {
                avatarList.forEachIndexed { index, avatar ->
                    DropdownMenuItem(onClick = {
                        selectedAvatarIndex = index
                        onAvatarSelected(avatar.ordinal)
                        expanded = false
                    },
                        text = { Text(text = avatar.name) })
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Display the selected avatar image
        Image(
            painter = painterResource(id = avatarList[selectedAvatarIndex].drawableRes),
            contentDescription = "Selected Avatar",
            modifier = Modifier.fillMaxSize()
        )
    }
}


