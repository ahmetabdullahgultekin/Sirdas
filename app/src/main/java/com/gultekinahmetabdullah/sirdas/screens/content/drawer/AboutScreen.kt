package com.gultekinahmetabdullah.sirdas.screens.content.drawer

import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.gultekinahmetabdullah.sirdas.R


@Composable
fun AboutScreen() {
    val transitionState = remember { MutableTransitionState(false) }
    val transition = updateTransition(transitionState, label = "AboutScreenTransition")

    LaunchedEffect(Unit) {
        transitionState.targetState = true
    }

    val headerOffset by transition.animateDp(
        transitionSpec = { tween(durationMillis = 800) },
        label = "HeaderOffset"
    ) { if (it) 0.dp else 100.dp }

    val contentAlpha by transition.animateFloat(
        transitionSpec = { tween(durationMillis = 1000) },
        label = "ContentAlpha"
    ) { if (it) 1f else 0f }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        // Header Section
        Text(
            text = "About Our App",
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier
                .offset(y = headerOffset)
                .padding(bottom = 16.dp)
        )

        // Logo or App Image
        Image(
            painter = painterResource(id = R.drawable.ic_launcher_foreground),
            contentDescription = null,
            modifier = Modifier
                .size(100.dp)
                .padding(bottom = 24.dp)
                .alpha(contentAlpha)
        )

        // Features List
        Column(modifier = Modifier.alpha(contentAlpha)) {
            Text(
                text = "Features",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            FeatureItem(
                icon = Icons.Default.Check,
                title = "Easy Task Management",
                description = "Organize your tasks easily."
            )
            FeatureItem(
                icon = Icons.Default.Check,
                title = "Cloud Sync",
                description = "Sync your tasks across devices."
            )
            FeatureItem(
                icon = Icons.Default.Check,
                title = "Reminders",
                description = "Get reminders for important tasks."
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Developer Info
        Card(
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .alpha(contentAlpha)
                .padding(8.dp),
            elevation = CardDefaults.cardElevation(8.dp)
        ) {
            Row(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.avatar10),
                    contentDescription = "Developer Profile Picture",
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(
                        text = "Ahmet Abdullah Gültekin",
                        style = MaterialTheme.typography.headlineMedium
                    )
                    Text(text = "Lead Developer", style = MaterialTheme.typography.bodyMedium)
                }
            }
        }
    }
}

@Composable
fun FeatureItem(icon: ImageVector, title: String, description: String) {
    Row(modifier = Modifier.padding(vertical = 8.dp)) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier
                .size(24.dp)
                .padding(end = 8.dp),
            tint = MaterialTheme.colorScheme.primary
        )
        Column {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold
            )
            Text(text = description, style = MaterialTheme.typography.bodyMedium)
        }
    }
}
