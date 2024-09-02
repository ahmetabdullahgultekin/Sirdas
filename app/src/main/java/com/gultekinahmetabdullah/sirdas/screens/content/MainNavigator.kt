// HomeScreen.kt
package com.gultekinahmetabdullah.sirdas.screens.content

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.gultekinahmetabdullah.sirdas.R
import com.gultekinahmetabdullah.sirdas.classes.dataclasses.UserPreferences
import com.gultekinahmetabdullah.sirdas.classes.enums.Avatars
import com.gultekinahmetabdullah.sirdas.screens.content.bottombar.calender.CalendarScreen
import com.gultekinahmetabdullah.sirdas.screens.content.bottombar.home.HomeScreen
import com.gultekinahmetabdullah.sirdas.screens.content.bottombar.search.SearchScreen
import com.gultekinahmetabdullah.sirdas.screens.content.drawer.AboutScreen
import com.gultekinahmetabdullah.sirdas.screens.content.drawer.BookScreen
import com.gultekinahmetabdullah.sirdas.screens.content.drawer.DocumentScreen
import com.gultekinahmetabdullah.sirdas.screens.content.drawer.FeedbackScreen
import com.gultekinahmetabdullah.sirdas.screens.content.drawer.HealthScreen
import com.gultekinahmetabdullah.sirdas.screens.content.drawer.JobsScreen
import com.gultekinahmetabdullah.sirdas.screens.content.drawer.SettingsScreen
import com.gultekinahmetabdullah.sirdas.screens.content.drawer.StatisticsScreen
import com.gultekinahmetabdullah.sirdas.screens.content.drawer.StudyScreen
import com.gultekinahmetabdullah.sirdas.screens.content.drawer.ToDoScreen
import com.gultekinahmetabdullah.sirdas.screens.content.drawer.profile.EditProfileScreen
import com.gultekinahmetabdullah.sirdas.screens.content.drawer.profile.ProfileScreen
import com.gultekinahmetabdullah.sirdas.viewmodels.BookViewModel
import com.gultekinahmetabdullah.sirdas.viewmodels.DirectoryViewModel
import com.gultekinahmetabdullah.sirdas.viewmodels.DocumentViewModel
import com.gultekinahmetabdullah.sirdas.viewmodels.HealthViewModel
import com.gultekinahmetabdullah.sirdas.viewmodels.PreferencesViewModel
import com.gultekinahmetabdullah.sirdas.viewmodels.UserViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainNavigator(
    onSavePreferences: (UserPreferences) -> Unit,
    onLogout: () -> Unit,
    userViewModel: UserViewModel,
    preferencesViewModel: PreferencesViewModel,
    healthViewModel: HealthViewModel,
    bookViewModel: BookViewModel,
    documentViewModel: DocumentViewModel,
    directoryViewModel: DirectoryViewModel
) {
    val context = LocalContext.current

    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val selectedItem = remember { mutableIntStateOf(10) }
    val navController = rememberNavController()
    val title = remember { mutableStateOf(context.getString(R.string.home)) }



    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            Column(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.background)
                    .fillMaxWidth(0.85f) // Increase the width of the drawer
                    .fillMaxHeight()
                    .padding(32.dp, 64.dp),
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {

                    Image(
                        painter = painterResource(
                            id = Avatars.entries[userViewModel.userProfile?.profileIconID
                                ?: 0].drawableRes
                        ),
                        contentScale = ContentScale.Crop,
                        contentDescription = "Profile Picture",
                        modifier = Modifier
                            .clip(CircleShape)
                            .size(64.dp)
                            .clickable(onClick = {
                                title.value = R.string.profile.toString()
                                navController.navigate("profile")
                                scope.launch { drawerState.close() }
                            })
                    )


                    Spacer(modifier = Modifier.width(16.dp))

                    IconButton(
                        onClick = {
                            onLogout()
                        },
                        modifier = Modifier
                            .size(48.dp)
                            .shadow(4.dp, shape = MaterialTheme.shapes.small, clip = true)
                            .border(
                                1.dp,
                                MaterialTheme.colorScheme.onSurface,
                                MaterialTheme.shapes.small
                            )
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                            contentDescription = "Logout",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
                HorizontalDivider()
                DrawerItem(
                    icon = ImageVector.vectorResource(id = R.drawable.baseline_picture_as_pdf_24),
                    label = context.getString(R.string.documents),
                    selected = selectedItem.intValue == 0,
                    onClick = {
                        selectedItem.intValue = 0
                        title.value = context.getString(R.string.documents)
                        navController.navigate("documents")
                        scope.launch { drawerState.close() }
                    }
                )
                DrawerItem(
                    icon = ImageVector.vectorResource(id = R.drawable.baseline_school_24),
                    label = context.getString(R.string.study),
                    selected = selectedItem.intValue == 1,
                    onClick = {
                        selectedItem.intValue = 1
                        title.value = context.getString(R.string.study)
                        navController.navigate("study")
                        scope.launch { drawerState.close() }
                    }
                )
                DrawerItem(
                    icon = ImageVector.vectorResource(id = R.drawable.baseline_work_24),
                    label = context.getString(R.string.internships),
                    selected = selectedItem.intValue == 2,
                    onClick = {
                        selectedItem.intValue = 2
                        title.value = context.getString(R.string.internships)
                        navController.navigate("internships")
                        scope.launch { drawerState.close() }
                    }
                )
                DrawerItem(
                    icon = ImageVector.vectorResource(id = R.drawable.baseline_assignment_24),
                    label = context.getString(R.string.todo),
                    selected = selectedItem.intValue == 3,
                    onClick = {
                        selectedItem.intValue = 3
                        title.value = context.getString(R.string.todo)
                        navController.navigate("todo")
                        scope.launch { drawerState.close() }
                    }
                )
                DrawerItem(
                    icon = ImageVector.vectorResource(id = R.drawable.baseline_book_24),
                    label = context.getString(R.string.books),
                    selected = selectedItem.intValue == 4,
                    onClick = {
                        selectedItem.intValue = 4
                        title.value = context.getString(R.string.books)
                        navController.navigate("books")
                        scope.launch { drawerState.close() }
                    })
                DrawerItem(
                    icon = ImageVector.vectorResource(id = R.drawable.baseline_monitor_heart_24),
                    label = context.getString(R.string.health),
                    selected = selectedItem.intValue == 5,
                    onClick = {
                        selectedItem.intValue = 5
                        title.value = context.getString(R.string.health)
                        navController.navigate("health")
                        scope.launch { drawerState.close() }
                    }
                )
                DrawerItem(
                    icon = ImageVector.vectorResource(id = R.drawable.baseline_query_stats_24),
                    label = context.getString(R.string.statistics),
                    selected = selectedItem.intValue == 6,
                    onClick = {
                        selectedItem.intValue = 6
                        title.value = context.getString(R.string.statistics)
                        navController.navigate("statistics")
                        scope.launch { drawerState.close() }
                    }
                )
                DrawerItem(
                    icon = Icons.AutoMirrored.Filled.Send,
                    label = context.getString(R.string.feedback),
                    selected = selectedItem.intValue == 7,
                    onClick = {
                        selectedItem.intValue = 7
                        title.value = context.getString(R.string.feedback)
                        navController.navigate("feedback")
                        scope.launch { drawerState.close() }
                    }
                )
                DrawerItem(icon = Icons.Filled.Settings,
                    label = context.getString(R.string.settings),
                    selected = selectedItem.intValue == 8,
                    onClick = {
                        selectedItem.intValue = 8
                        title.value = context.getString(R.string.settings)
                        navController.navigate("settings")
                        scope.launch { drawerState.close() }
                    }
                )
                DrawerItem(
                    icon = ImageVector.vectorResource(id = R.drawable.baseline_info_outline_24),
                    label = context.getString(R.string.about),
                    selected = selectedItem.intValue == 9,
                    onClick = {
                        selectedItem.intValue = 9
                        title.value = context.getString(R.string.about)
                        navController.navigate("about")
                        scope.launch { drawerState.close() }
                    }
                )
            }
        },
        content = {
            Scaffold(
                topBar = {
                    TopAppBar(
                        modifier = Modifier.fillMaxWidth(),
                        title = { Text(title.value) },
                        navigationIcon = {
                            IconButton(onClick = { scope.launch { drawerState.open() } }) {
                                Icon(Icons.Filled.Menu, contentDescription = "Menu")
                            }
                        }
                    )
                },
                bottomBar = {
                    NavigationBar(
                        modifier = Modifier
                            .fillMaxWidth()
                            .shadow(40.dp),
                    ) {
                        NavigationBarItem(
                            icon = { Icon(Icons.Filled.Home, contentDescription = "Home") },
                            label = { Text(context.getString(R.string.home)) },
                            selected = selectedItem.intValue == 10 && drawerState.isOpen.not(),
                            onClick = {
                                selectedItem.intValue = 10
                                title.value = context.getString(R.string.home)
                                navController.navigate("home")
                            }
                        )
                        NavigationBarItem(
                            icon = { Icon(Icons.Filled.Search, contentDescription = "Search") },
                            label = { Text(context.getString(R.string.search)) },
                            selected = selectedItem.intValue == 11 && drawerState.isOpen.not(),
                            onClick = {
                                selectedItem.intValue = 11
                                title.value = context.getString(R.string.search)
                                navController.navigate("search")
                            }
                        )
                        NavigationBarItem(
                            icon = {
                                Icon(
                                    Icons.Filled.DateRange,
                                    contentDescription = "Calender"
                                )
                            },
                            label = { Text(context.getString(R.string.calendar)) },
                            selected = selectedItem.intValue == 12 && drawerState.isOpen.not(),
                            onClick = {
                                selectedItem.intValue = 12
                                title.value = context.getString(R.string.calendar)
                                navController.navigate("calender")
                            }
                        )
                    }
                },
                content = { padding ->
                    NavHost(
                        navController = navController,
                        startDestination = "home",
                        modifier = Modifier
                            .padding(padding)
                            .padding(16.dp, 16.dp, 16.dp, 0.dp)
                            .fillMaxSize()
                    ) {

                        composable("home") {
                            HomeScreen(
                                viewModel = userViewModel
                            )
                        }

                        composable("search") {
                            SearchScreen()
                        }

                        composable("calender") {
                            CalendarScreen()
                        }

                        composable("profile") {
                            ProfileScreen(
                                viewModel = userViewModel,
                                onEditProfile = {
                                    navController.navigate("editprofile")
                                }
                            )
                        }
                        composable("editprofile") {
                            EditProfileScreen(
                                viewModel = userViewModel,
                                onCancel = {
                                    navController.popBackStack()
                                }
                            )
                        }

                        composable("documents") {
                            DocumentScreen(
                                viewModel = documentViewModel,
                                directoryViewModel = directoryViewModel
                            )
                        }
                        composable("study") { StudyScreen() }
                        composable("internships") { JobsScreen() }
                        composable("todo") { ToDoScreen() }
                        composable("books") {
                            BookScreen(
                                viewModel = bookViewModel
                            )
                        }
                        composable("health") {
                            HealthScreen(
                                viewModel = healthViewModel
                            )
                        }
                        composable("statistics") {
                            StatisticsScreen()
                        }
                        composable("feedback") {
                            FeedbackScreen(
                                viewModel = userViewModel
                            )
                        }
                        composable("settings") {
                            SettingsScreen(
                                onSavePreferences = onSavePreferences,
                                viewModel = preferencesViewModel,
                                onCancel = {
                                    navController.navigate("home")
                                }
                            )
                        }
                        composable("about") {
                            AboutScreen()
                        }
                    }
                }
            )
        }
    )
}

@Composable
fun DrawerItem(icon: ImageVector, label: String, selected: Boolean = false, onClick: () -> Unit) {

    val textStyle = if (selected) {
        MaterialTheme.typography.labelLarge.copy(
            color = MaterialTheme.colorScheme.onSurface
        )
    } else {
        MaterialTheme.typography.labelSmall.copy(
            color = MaterialTheme.colorScheme.onSurface
        )
    }

    val rowColor = if (!selected) {
        Brush.horizontalGradient(
            colors = listOf(
                Color.Magenta.copy(alpha = 0.1f),
                Color.Transparent
            )
        )
    } else {
        Brush.horizontalGradient(
            colors = listOf(
                Color.Transparent,
                Color.Magenta.copy(alpha = 0.1f)
            )
        )
    }
    val rowContentAlignment: Arrangement.Horizontal = if (selected) {
        Arrangement.End
    } else {
        Arrangement.Start
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .background(
                brush = rowColor,
                shape = MaterialTheme.shapes.small
            )
            .clickable(onClick = onClick)
            .padding(4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = rowContentAlignment
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            modifier = Modifier.padding(8.dp),
            tint = textStyle.color
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = label,
            modifier = Modifier.padding(8.dp),
            style = textStyle
        )
    }
}
