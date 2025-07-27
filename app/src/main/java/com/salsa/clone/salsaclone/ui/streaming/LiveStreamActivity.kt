package com.salsa.clone.salsaclone.ui.streaming

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.salsa.clone.salsaclone.R
import com.salsa.clone.salsaclone.data.model.GiftItem
import com.salsa.clone.salsaclone.ui.theme.darkGrayButton
import com.salsa.clone.salsaclone.ui.theme.gradient
import kotlinx.coroutines.delay

class LiveStreamActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LiveStreamScreen()
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterial3Api::class)
@Composable
fun LiveStreamScreen(
) {
    val permissionState = rememberMultiplePermissionsState(
        listOf(
            android.Manifest.permission.CAMERA, android.Manifest.permission.RECORD_AUDIO
        )
    )
    var isLive by remember { mutableStateOf(false) }
    var showBottomSheet by remember { mutableStateOf(false) }
    val activity = LocalContext.current as? Activity

    LaunchedEffect(Unit) {
        if (!permissionState.allPermissionsGranted) {
            permissionState.launchMultiplePermissionRequest()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // Display camera preview when permission is granted
        if (permissionState.allPermissionsGranted) {
            CameraPreview(modifier = Modifier.fillMaxSize())
        } else {
            // Show a placeholder if permission not granted
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black),
                contentAlignment = Alignment.Center
            ) {
                Text("Camera permission required", color = Color.White)
            }
        }

        if (!isLive) {
            Button(
                onClick = { isLive = true },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 40.dp, start = 20.dp, end = 20.dp)
                    .fillMaxWidth()
                    .height(48.dp)
                    .background(gradient, shape = RoundedCornerShape(15.dp)),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                shape = RoundedCornerShape(24.dp)
            ) {
                Text("Go Live")
            }
        } else {

            Row(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Live red dot + text
                LiveIndicator()

                // Cross close button
                IconButton(
                    onClick = {
                        showBottomSheet = true
                    }, modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close Live",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            // Timer at center top
            TimerDisplay(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 16.dp)
            )

            var selectedGiftId by remember { mutableStateOf<Int?>(null) }
            val gifts = listOf(
                GiftItem(0, "Heart", R.drawable.ic_gift_heart, 399),
                GiftItem(1, "Shoe", R.drawable.ic_gift_shoe, 980),
                GiftItem(2, "Party", R.drawable.ic_gift_part, 499),
                GiftItem(3, "Charm", R.drawable.ic_gift_charm, 799),
                GiftItem(4, "Puzzle", R.drawable.ic_gift_puzzle, 120),
                GiftItem(5, "Fire", R.drawable.ic_gift_fire, 99)
            )

            Column (modifier = Modifier.align(Alignment.BottomCenter)){
                GiftCoinList(
                    gifts = gifts,
                    onGiftClick = { gift ->  },
                    selectedGiftId = selectedGiftId
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 24.dp, end = 24.dp, bottom = 24.dp, top = 15.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    IconButton(onClick = { /* Chat */ }) {
                        Icon(
                            painter = painterResource(id = R.drawable.chat_icon),
                            contentDescription = "Chat",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    IconButton(onClick = { /* Invite */ }) {
                        Icon(
                            painter = painterResource(R.drawable.invite),
                            contentDescription = "Invite",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    IconButton(onClick = { /* Reply */ }) {
                        Icon(
                            painter = painterResource(R.drawable.reply),
                            contentDescription = "Reply",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    IconButton(onClick = { /* Menu */ }) {
                        Icon(
                            Icons.Default.MoreVert,
                            contentDescription = "Menu",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }
    }

    if(showBottomSheet){
        ModalBottomSheet(
            onDismissRequest = {showBottomSheet = false },
            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
            containerColor = darkGrayButton
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "End Stream?",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White
                )
                Spacer(Modifier.height(25.dp))

                Button(
                    onClick = {
                        activity?.finish()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent, contentColor = Color(0xFFFA4401)
                    ),
                    border = BorderStroke(1.dp, Color(0xFFFA4401)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text("End Stream")
                }
                Spacer(Modifier.height(16.dp))
                Button(
                    onClick = {
                        showBottomSheet = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFA4401)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text("Continue")
                }
            }
        }
    }
}

@Composable
fun LiveIndicator() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .background(Color.Red, RoundedCornerShape(12.dp))
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .background(Color.White, CircleShape)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = "LIVE",
            color = Color.White,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun TimerDisplay(modifier: Modifier = Modifier) {
    // Example: just a placeholder timer counting seconds
    var seconds by remember { mutableStateOf(0) }

    LaunchedEffect(Unit) {
        while (true) {
            delay(1000)
            seconds++
        }
    }

    Text(
        text = String.format("%02d:%02d", seconds / 60, seconds % 60),
        color = Color.White,
        style = MaterialTheme.typography.bodyLarge,
        modifier = modifier
            .background(
                color = Color.Black.copy(alpha = 0.5f), shape = RoundedCornerShape(10.dp)
            )
            .padding(horizontal = 16.dp, vertical = 6.dp)
    )
}

@Composable
fun CameraPreview(modifier: Modifier = Modifier) {
    AndroidView(
        factory = { ctx ->
            val previewView = PreviewView(ctx)
            val cameraProviderFuture = ProcessCameraProvider.getInstance(ctx)
            cameraProviderFuture.addListener({
                val cameraProvider = cameraProviderFuture.get()
                val preview = Preview.Builder().build()
                preview.setSurfaceProvider(previewView.surfaceProvider)
                val cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    ctx as LifecycleOwner, cameraSelector, preview
                )
            }, ContextCompat.getMainExecutor(ctx))
            previewView
        }, modifier = modifier
    )
}

@Composable
fun GiftCoinList(
    gifts: List<GiftItem>,
    onGiftClick: (GiftItem) -> Unit,
    selectedGiftId: Int? = null // Optionally highlight selection
) {
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp, horizontal = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        contentPadding = PaddingValues(horizontal = 12.dp)
    ) {
        items(gifts) { gift ->
            GiftCoinItem(
                gift = gift,
                selected = (gift.id == selectedGiftId),
                onClick = { onGiftClick(gift) }
            )
        }
    }
}

@Composable
fun GiftCoinItem(
    gift: GiftItem,
    selected: Boolean = false,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .background(if (selected) Color(0x22FFD700) else Color.Transparent)
            .clickable { onClick() }
            .padding(vertical = 4.dp, horizontal = 12.dp)
    ) {
        // Replace 'gift.iconRes' with your own resources (vector/PNG)
        Image(
            painter = painterResource(id = gift.iconRes),
            contentDescription = gift.name,
            modifier = Modifier.size(40.dp)
        )
        Spacer(Modifier.height(6.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.coin_icon), // Coin icon resource
                contentDescription = "Coins",
                modifier = Modifier.size(16.dp),
                tint = Color.Unspecified // No tint if your icon is colored, else use Color.Yellow
            )
            Spacer(Modifier.width(4.dp))
            Text(
                text = gift.coinAmount.toString(),
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp
            )
        }
    }
}



