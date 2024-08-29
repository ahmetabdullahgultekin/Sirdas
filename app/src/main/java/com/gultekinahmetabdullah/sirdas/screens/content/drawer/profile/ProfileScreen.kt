package com.gultekinahmetabdullah.sirdas.screens.content.drawer.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.gultekinahmetabdullah.sirdas.classes.enums.Avatars
import com.gultekinahmetabdullah.sirdas.viewmodels.UserViewModel

@Composable
fun ProfileScreen(viewModel: UserViewModel, onEditProfile: () -> Unit) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(MaterialTheme.colorScheme.background),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Profile Image
        Image(
            painter = painterResource(
                id = Avatars.entries[viewModel.userProfile?.profileIconID ?: 0].drawableRes
            ),
            contentDescription = "Profile Image",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .clip(CircleShape)
                .size(400.dp)
                .background(MaterialTheme.colorScheme.primary)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // User Name
        Text(
            text = viewModel.userProfile?.name ?: "",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        // Email
        Text(
            text = viewModel.userProfile?.email ?: "",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(vertical = 4.dp)
        )

        // Date of Joining TODO: Change to actual date
        /*        Text(
                    text = viewModel.userProfile?: "",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(vertical = 4.dp)
                )*/

        Spacer(modifier = Modifier.height(24.dp))

        // Edit Profile Button
        Button(
            onClick = { onEditProfile() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Edit Profile")
        }

        /*
        Spacer(modifier = Modifier.height(16.dp))

        // Logout Button
        OutlinedButton(
            onClick = { /* Handle logout click */ },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Logout", fontSize = 16.sp, textAlign = TextAlign.Center)
        }

         */
    }
}
