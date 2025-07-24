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
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

import com.salsa.clone.salsaclone.ui.theme.SalsaCloneTheme
import com.salsa.clone.salsaclone.utils.isInternetAvailable
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
//            MaterialTheme {
//                Scaffold(
////                    bottomBar = { BottomNavigationBar() }
//                )
//                {
//                    HomeScreen(Modifier.padding(it))
//                }
            HomeScreen()
//            }
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