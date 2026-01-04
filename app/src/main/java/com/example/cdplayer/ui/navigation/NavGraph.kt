package com.example.cdplayer.ui.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Alignment
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LibraryMusic
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.LibraryMusic
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.cdplayer.ui.screens.home.HomeScreen
import com.example.cdplayer.ui.screens.library.LibraryScreen
import com.example.cdplayer.ui.screens.player.PlayerScreen
import com.example.cdplayer.ui.screens.playlist.CreatePlaylistScreen
import com.example.cdplayer.ui.screens.playlist.PlaylistDetailScreen
import com.example.cdplayer.ui.screens.search.SearchScreen
import com.example.cdplayer.ui.screens.settings.SettingsScreen
import com.example.cdplayer.ui.screens.splash.SplashScreen
import com.example.cdplayer.ui.screens.edit.EditMetadataScreen
import com.example.cdplayer.ui.components.MiniPlayer

data class BottomNavItem(
    val route: String,
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
)

val bottomNavItems = listOf(
    BottomNavItem(
        route = Screen.Home.route,
        title = "홈",
        selectedIcon = Icons.Filled.Home,
        unselectedIcon = Icons.Outlined.Home
    ),
    BottomNavItem(
        route = Screen.Library.route,
        title = "라이브러리",
        selectedIcon = Icons.Filled.LibraryMusic,
        unselectedIcon = Icons.Outlined.LibraryMusic
    ),
    BottomNavItem(
        route = Screen.Search.route,
        title = "검색",
        selectedIcon = Icons.Filled.Search,
        unselectedIcon = Icons.Outlined.Search
    ),
    BottomNavItem(
        route = Screen.Settings.route,
        title = "설정",
        selectedIcon = Icons.Filled.Settings,
        unselectedIcon = Icons.Outlined.Settings
    )
)

@Composable
fun CDPlayerNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val showBottomBar = currentDestination?.route in listOf(
        Screen.Home.route,
        Screen.Library.route,
        Screen.Search.route,
        Screen.Settings.route
    )

    val showMiniPlayer = showBottomBar && currentDestination?.route != Screen.Player.route

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                NavigationBar {
                    bottomNavItems.forEach { item ->
                        val selected = currentDestination?.hierarchy?.any { it.route == item.route } == true

                        NavigationBarItem(
                            selected = selected,
                            onClick = {
                                navController.navigate(item.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon = {
                                Icon(
                                    imageVector = if (selected) item.selectedIcon else item.unselectedIcon,
                                    contentDescription = item.title
                                )
                            },
                            label = { Text(item.title) }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.fillMaxSize()) {
            NavHost(
                navController = navController,
                startDestination = Screen.Splash.route,
                modifier = modifier.padding(innerPadding),
                enterTransition = {
                    fadeIn(animationSpec = tween(300))
                },
                exitTransition = {
                    fadeOut(animationSpec = tween(300))
                }
            ) {
                composable(Screen.Splash.route) {
                    SplashScreen(
                        onNavigateToHome = {
                            navController.navigate(Screen.Home.route) {
                                popUpTo(Screen.Splash.route) { inclusive = true }
                            }
                        }
                    )
                }

                composable(Screen.Home.route) {
                    HomeScreen(
                        onNavigateToPlayer = {
                            navController.navigate(Screen.Player.route)
                        },
                        onNavigateToAlbum = { albumName ->
                            navController.navigate(Screen.AlbumDetail.createRoute(albumName))
                        },
                        onNavigateToEditMetadata = { audioId ->
                            navController.navigate(Screen.EditMetadata.createRoute(audioId))
                        }
                    )
                }

                composable(Screen.Library.route) {
                    LibraryScreen(
                        onNavigateToPlayer = {
                            navController.navigate(Screen.Player.route)
                        },
                        onNavigateToPlaylist = { playlistId ->
                            navController.navigate(Screen.Playlist.createRoute(playlistId))
                        },
                        onNavigateToCreatePlaylist = {
                            navController.navigate(Screen.CreatePlaylist.route)
                        },
                        onNavigateToAlbum = { albumName ->
                            navController.navigate(Screen.AlbumDetail.createRoute(albumName))
                        },
                        onNavigateToArtist = { artistName ->
                            navController.navigate(Screen.ArtistDetail.createRoute(artistName))
                        },
                        onNavigateToEditMetadata = { audioId ->
                            navController.navigate(Screen.EditMetadata.createRoute(audioId))
                        }
                    )
                }

                composable(Screen.Search.route) {
                    SearchScreen(
                        onNavigateToPlayer = {
                            navController.navigate(Screen.Player.route)
                        },
                        onNavigateToEditMetadata = { audioId ->
                            navController.navigate(Screen.EditMetadata.createRoute(audioId))
                        }
                    )
                }

                composable(
                    route = Screen.AlbumDetail.route,
                    arguments = listOf(navArgument("albumName") { type = NavType.StringType })
                ) {
                    com.example.cdplayer.ui.screens.library.AlbumDetailScreen(
                        onNavigateBack = { navController.popBackStack() },
                        onNavigateToPlayer = { navController.navigate(Screen.Player.route) },
                        onNavigateToEditMetadata = { audioId ->
                            navController.navigate(Screen.EditMetadata.createRoute(audioId))
                        },
                        onAlbumRenamed = { newName ->
                            navController.popBackStack()
                            navController.navigate(Screen.AlbumDetail.createRoute(newName))
                        }
                    )
                }

                composable(
                    route = Screen.ArtistDetail.route,
                    arguments = listOf(navArgument("artistName") { type = NavType.StringType })
                ) {
                    com.example.cdplayer.ui.screens.library.ArtistDetailScreen(
                        onNavigateBack = { navController.popBackStack() },
                        onNavigateToPlayer = { navController.navigate(Screen.Player.route) },
                        onNavigateToEditMetadata = { audioId ->
                            navController.navigate(Screen.EditMetadata.createRoute(audioId))
                        }
                    )
                }

                composable(Screen.Settings.route) {
                    SettingsScreen()
                }

                composable(
                    route = Screen.Player.route,
                    enterTransition = {
                        slideIntoContainer(
                            towards = AnimatedContentTransitionScope.SlideDirection.Up,
                            animationSpec = tween(300)
                        )
                    },
                    exitTransition = {
                        slideOutOfContainer(
                            towards = AnimatedContentTransitionScope.SlideDirection.Down,
                            animationSpec = tween(300)
                        )
                    }
                ) {
                    PlayerScreen(
                        onNavigateBack = { navController.popBackStack() }
                    )
                }

                composable(
                    route = Screen.Playlist.route,
                    arguments = listOf(navArgument("playlistId") { type = NavType.LongType })
                ) { backStackEntry ->
                    val playlistId = backStackEntry.arguments?.getLong("playlistId") ?: return@composable
                    PlaylistDetailScreen(
                        playlistId = playlistId,
                        onNavigateBack = { navController.popBackStack() },
                        onNavigateToPlayer = { navController.navigate(Screen.Player.route) },
                        onNavigateToEditMetadata = { audioId ->
                            navController.navigate(Screen.EditMetadata.createRoute(audioId))
                        }
                    )
                }

                composable(Screen.CreatePlaylist.route) {
                    CreatePlaylistScreen(
                        onNavigateBack = { navController.popBackStack() }
                    )
                }

                composable(
                    route = Screen.EditMetadata.route,
                    arguments = listOf(navArgument("audioId") { type = NavType.LongType })
                ) { backStackEntry ->
                    val audioId = backStackEntry.arguments?.getLong("audioId") ?: return@composable
                    EditMetadataScreen(
                        onNavigateBack = { navController.popBackStack() }
                    )
                }
            }

            // Mini Player
            if (showMiniPlayer) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.BottomCenter
                ) {
                    MiniPlayer(
                        onClick = { navController.navigate(Screen.Player.route) }
                    )
                }
            }
        }
    }
}
