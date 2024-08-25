// HomeScreen.kt
package com.gultekinahmetabdullah.sirdas.screens.content

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.automirrored.outlined.ExitToApp
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.gultekinahmetabdullah.sirdas.R
import com.gultekinahmetabdullah.sirdas.screens.content.bottombar.calender.CalendarScreen
import com.gultekinahmetabdullah.sirdas.screens.content.bottombar.home.HomeScreen
import com.gultekinahmetabdullah.sirdas.screens.content.bottombar.search.SearchScreen
import com.gultekinahmetabdullah.sirdas.screens.content.drawer.document.DocumentScreen
import com.gultekinahmetabdullah.sirdas.screens.content.drawer.FeedbackScreen
import com.gultekinahmetabdullah.sirdas.screens.content.drawer.InternshipsScreen
import com.gultekinahmetabdullah.sirdas.screens.content.drawer.NotificationsScreen
import com.gultekinahmetabdullah.sirdas.screens.content.drawer.SettingsScreen
import com.gultekinahmetabdullah.sirdas.screens.content.drawer.StudyScreen
import com.gultekinahmetabdullah.sirdas.screens.content.drawer.ToDoScreen
import com.gultekinahmetabdullah.sirdas.screens.content.drawer.profile.EditProfileScreen
import com.gultekinahmetabdullah.sirdas.screens.content.drawer.profile.ProfileScreen
import com.gultekinahmetabdullah.sirdas.viewmodels.UserViewModel
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainNavigator(onLogout: () -> Unit) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val selectedItem = remember { mutableIntStateOf(0) }
    val navController = rememberNavController()
    val title = remember { mutableStateOf("Home") }
    val userViewModel = UserViewModel()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            Column(
                modifier = Modifier
                    .background(Color.White)
                    .fillMaxWidth(0.8f) // Increase the width of the drawer
                    .fillMaxHeight()
                    .padding(32.dp, 64.dp),
            ) {
                Button(
                    onClick = onLogout,
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text(text = "Sign Out")
                    Icon(Icons.AutoMirrored.Outlined.ExitToApp, contentDescription = "SignOut")
                }
                Spacer(modifier = Modifier.height(16.dp))
                HorizontalDivider()
                DrawerItem(
                    icon = ImageVector.vectorResource(id = R.drawable.baseline_picture_as_pdf_24),
                    label = "Documents",
                    onClick = {
                        selectedItem.intValue = 0
                        title.value = "Documents"
                        navController.navigate("documents")
                        scope.launch { drawerState.close() }
                    })
                DrawerItem(
                    icon = ImageVector.vectorResource(id = R.drawable.baseline_school_24),
                    label = "Study",
                    onClick = {
                        selectedItem.intValue = 1
                        title.value = "Study"
                        navController.navigate("study")
                        scope.launch { drawerState.close() }
                    })
                DrawerItem(
                    icon = ImageVector.vectorResource(id = R.drawable.baseline_work_24),
                    label = "Internships",
                    onClick = {
                        selectedItem.intValue = 2
                        title.value = "Internships"
                        navController.navigate("internships")
                        scope.launch { drawerState.close() }
                    })
                DrawerItem(
                    icon = ImageVector.vectorResource(id = R.drawable.baseline_assignment_24),
                    label = "To - Do",
                    onClick = {
                        selectedItem.intValue = 3
                        title.value = "To - Do"
                        navController.navigate("todo")
                        scope.launch { drawerState.close() }
                    })
                DrawerItem(icon = Icons.Filled.AccountCircle, label = "Profile", onClick = {
                    selectedItem.intValue = 4
                    title.value = "Profile"
                    navController.navigate("profile")
                    scope.launch { drawerState.close() }
                })
                DrawerItem(icon = Icons.Filled.Notifications, label = "Notifications", onClick = {
                    selectedItem.intValue = 5
                    title.value = "Notifications"
                    navController.navigate("notifications")
                    scope.launch { drawerState.close() }
                })
                DrawerItem(icon = Icons.AutoMirrored.Filled.Send, label = "Feedback", onClick = {
                    selectedItem.intValue = 6
                    title.value = "Feedback"
                    navController.navigate("feedback")
                    scope.launch { drawerState.close() }
                })
                DrawerItem(icon = Icons.Filled.Settings, label = "Settings", onClick = {
                    selectedItem.intValue = 7
                    title.value = "Settings"
                    navController.navigate("settings")
                    scope.launch { drawerState.close() }
                })
            }
        },
        content = {
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = { Text(title.value) },
                        navigationIcon = {
                            IconButton(onClick = { scope.launch { drawerState.open() } }) {
                                Icon(Icons.Filled.Menu, contentDescription = "Menu")
                            }
                        }
                    )
                },
                bottomBar = {
                    NavigationBar {
                        NavigationBarItem(
                            icon = { Icon(Icons.Filled.Home, contentDescription = "Home") },
                            label = { Text("Home") },
                            selected = selectedItem.intValue == 0 && drawerState.isOpen.not(),
                            onClick = {
                                selectedItem.intValue = 0
                                title.value = "Home"
                                navController.navigate("home")
                            }
                        )
                        NavigationBarItem(
                            icon = { Icon(Icons.Filled.Search, contentDescription = "Search") },
                            label = { Text("Search") },
                            selected = selectedItem.intValue == 1 && drawerState.isOpen.not(),
                            onClick = {
                                selectedItem.intValue = 1
                                title.value = "Search"
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
                            label = { Text("Calender") },
                            selected = selectedItem.intValue == 2 && drawerState.isOpen.not(),
                            onClick = {
                                selectedItem.intValue = 2
                                title.value = "Calender"
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
                            .padding(16.dp)
                            .fillMaxSize()
                    ) {
                        composable("home") { HomeScreen() }
                        composable("search") { SearchScreen() }
                        composable("calender") { CalendarScreen() }
                        composable("documents") { DocumentScreen() }
                        composable("study") { StudyScreen() }
                        composable("internships") { InternshipsScreen() }
                        composable("todo") { ToDoScreen() }
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
                        composable("notifications") { NotificationsScreen() }
                        composable("feedback") {
                            FeedbackScreen(
                                viewModel = userViewModel
                            )
                        }
                        composable("settings") { SettingsScreen() }
                    }
                }
            )
        }
    )
}

@Composable
fun DrawerItem(icon: ImageVector, label: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable(onClick = onClick),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(imageVector = icon, contentDescription = label)
        Spacer(modifier = Modifier.width(16.dp))
        Text(text = label,
            modifier = Modifier.padding(8.dp))
    }
}
