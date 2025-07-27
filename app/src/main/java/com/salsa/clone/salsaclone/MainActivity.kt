package com.salsa.clone.salsaclone

import HomeScreen
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.salsa.clone.salsaclone.ui.components.BottomNavItem
import com.salsa.clone.salsaclone.ui.profile.ProfileScreen
import com.salsa.clone.salsaclone.ui.search.SearchScreen

import com.salsa.clone.salsaclone.ui.theme.SalsaCloneTheme
import com.salsa.clone.salsaclone.utils.isInternetAvailable
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SetDarkStatusBar()
            MainScreen()
        }

        if (!isInternetAvailable(this)) {
            Toast.makeText(this, "No Internet", Toast.LENGTH_LONG).show()
        }
    }
}


@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    SalsaCloneTheme {
        Greeting("Android")
    }
}

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    Scaffold(
        bottomBar = {
            MainBottomNavigation(navController, unreadMessages = 4) // Pass badge count here
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = BottomNavItem.ForYou.route,
            Modifier.padding(innerPadding)
        ) {
            composable(BottomNavItem.ForYou.route,
                enterTransition = { fadeIn(animationSpec = tween(300)) + slideInHorizontally(initialOffsetX = { 300 }, animationSpec = tween(300)) },
                exitTransition = { fadeOut(animationSpec = tween(300)) + slideOutHorizontally(targetOffsetX = { -300 }, animationSpec = tween(300)) },
                popEnterTransition = { fadeIn(animationSpec = tween(300)) + slideInHorizontally(initialOffsetX = { -300 }, animationSpec = tween(300)) },
                popExitTransition = { fadeOut(animationSpec = tween(300)) + slideOutHorizontally(targetOffsetX = { 300 }, animationSpec = tween(300)) }
                ) { HomeScreen() }
            composable(BottomNavItem.Search.route,
                enterTransition = { fadeIn(animationSpec = tween(1000)) + slideInHorizontally(initialOffsetX = { 600 }, animationSpec = tween(600)) },
                exitTransition = { fadeOut(animationSpec = tween(300)) + slideOutHorizontally(targetOffsetX = { -300 }, animationSpec = tween(300)) },
                popEnterTransition = { fadeIn(animationSpec = tween(300)) + slideInHorizontally(initialOffsetX = { -300 }, animationSpec = tween(300)) },
                popExitTransition = { fadeOut(animationSpec = tween(300)) + slideOutHorizontally(targetOffsetX = { 300 }, animationSpec = tween(300)) }
                ) { SearchScreen() }
            composable(BottomNavItem.Chat.route) {  }
            composable(BottomNavItem.Match.route) {  }
            composable(BottomNavItem.Profile.route,
                enterTransition = { fadeIn(animationSpec = tween(300)) + slideInHorizontally(initialOffsetX = { 300 }, animationSpec = tween(300)) },
                exitTransition = { fadeOut(animationSpec = tween(300)) + slideOutHorizontally(targetOffsetX = { -300 }, animationSpec = tween(300)) },
                popEnterTransition = { fadeIn(animationSpec = tween(300)) + slideInHorizontally(initialOffsetX = { -300 }, animationSpec = tween(300)) },
                popExitTransition = { fadeOut(animationSpec = tween(300)) + slideOutHorizontally(targetOffsetX = { 300 }, animationSpec = tween(300)) }
                ) { ProfileScreen(images = listOf(
                painterResource(id = R.drawable.place_1),
                painterResource(id = R.drawable.place_2),
                painterResource(id = R.drawable.place_1),
                painterResource(id = R.drawable.place_2),
                painterResource(id = R.drawable.place_1),
                painterResource(id = R.drawable.place_2),
                painterResource(id = R.drawable.place_1),
                painterResource(id = R.drawable.place_2),
            )) }
        }
    }
}


@Composable
fun MainBottomNavigation(
    navController: NavController,
    unreadMessages: Int = 0 // For the chat badge
) {
    val items = listOf(
        BottomNavItem.ForYou,
        BottomNavItem.Search,
        BottomNavItem.Chat,
        BottomNavItem.Match,
        BottomNavItem.Profile
    )
    NavigationBar(
        containerColor = Color.Black
    ) {
        val navBackStackEntry = navController.currentBackStackEntryAsState().value
        val currentRoute = navBackStackEntry?.destination?.route
        items.forEach { item ->
            NavigationBarItem(
                icon = {
                    Box {
                        Icon(
                            painter = painterResource(id = item.icon),
                            contentDescription = item.label,
                            tint = if (currentRoute == item.route) Color.White else Color.Gray,

                        )
                        if (item is BottomNavItem.Chat && unreadMessages > 0) {
                            // Badge for unread messages
                            Box(
                                modifier = Modifier
                                    .offset(x = 10.dp, y = (-2).dp)
                                    .size(16.dp)
                                    .background(Color(0xFFFFC107), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = unreadMessages.toString(),
                                    color = Color.Black,
                                    fontSize = 10.sp
                                )
                            }
                        }
                    }
                },
                label = {
                    Text(
                        item.label,
                        color = if (currentRoute == item.route) Color.White else Color.Gray,
                        fontSize = 12.sp
                    )
                },
                selected = currentRoute == item.route,
                onClick = {
                    if (currentRoute != item.route) {
                        navController.navigate(item.route) {
                            popUpTo(navController.graph.startDestinationId) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = Color.Transparent
                ),
            )
        }
    }
}

@Composable
fun SetDarkStatusBar() {
    val systemUiController = rememberSystemUiController()
    SideEffect {
        systemUiController.setStatusBarColor(
            color = Color.Black,     // or any dark color you want
            darkIcons = false        // false = white icons
        )
    }
}
