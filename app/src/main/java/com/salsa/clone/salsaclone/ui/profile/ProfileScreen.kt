package com.salsa.clone.salsaclone.ui.profile

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.compose.rememberImagePainter
import com.salsa.clone.salsaclone.R
import com.salsa.clone.salsaclone.ui.streaming.LiveStreamActivity
import com.salsa.clone.salsaclone.ui.theme.darkGrayButton
import com.salsa.clone.salsaclone.ui.theme.gradient
import java.io.File


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(viewModel: ProfileViewModel = hiltViewModel(), images: List<Painter>) {
    var showEarningsBar by remember { mutableStateOf(true) }
    val customFont = FontFamily(Font(R.font.robotoregular))
    var showBottomSheet by remember { mutableStateOf(false) }
    var context = LocalContext.current
    val state by viewModel.state.collectAsState()


    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        item {
            Spacer(modifier = Modifier.height(24.dp))
            HeaderWithIcons(
                onCameraClick = { /* Camera action */ },
                onMenuClick = { /* Menu action */ },
                font = customFont
            )
        }
        item {
            ProfileHeader(
                avatar = painterResource(id = R.drawable.avtar_placeholder),
                username = "Name of user",
                earned = 34,
                followers = 260,
                following = 260,
                font = customFont,
                onAvatarClick = { showBottomSheet = true },
                avatarURI = state.avatarUri
            )
        }
        item {
            ActionButtons(
                onEdit = { /* Edit action */ },
                onShare = { /* Share action */ },
                font = customFont
            )
        }
        item {
            if (showEarningsBar) {
                EarningsBar(
                    progress = 0.5f,
                    onGoLive = {
                        val intent = Intent(context, LiveStreamActivity::class.java)
                        context.startActivity(intent)
                    },
                    onClose = { showEarningsBar = false },
                    font = customFont
                )
            }
        }
        items(images.chunked(3)) { rowImages ->
            Row(
                Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                rowImages.forEach { painter ->
                    Image(
                        painter = painter,
                        contentDescription = null,
                        modifier = Modifier
                            .weight(1f)
                            .aspectRatio(1f)
                            .clip(RoundedCornerShape(8.dp))
                            .padding(2.dp)
                            .clickable {
                                val intent = Intent(context, PreviewImageActivity::class.java)
                                context.startActivity(intent)
                            },
                        contentScale = ContentScale.Crop
                    )
                }
                // Fill empty cells if last row has less than 3 images
                repeat(3 - rowImages.size) {
                    Spacer(Modifier.weight(1f))
                }
            }
        }
    }


    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = { showBottomSheet = false },
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
                    text = "Media Access",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White
                )
                Spacer(Modifier.height(10.dp))
                Text(
                    text = "To add or send photos, Salsa needs access to your media",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray, textAlign = TextAlign.Center
                )
                Spacer(Modifier.height(32.dp))
                val launcher = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.GetContent()
                ) { uri: Uri? ->
                    uri?.let {

                        val cachedUri = cacheImageToInternal(context, uri)
                        viewModel.handleIntent(ProfileIntent.ImagePicked(cachedUri))
                        showBottomSheet = false
                    }
                }
                Button(
                    onClick = {
                        launcher.launch("image/*")
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFA4401)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text("Gallery")
                }
                Button(
                    onClick = { /* TODO: Open camera */ },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent,
                        contentColor = Color(0xFFFA4401)
                    ),
                    border = BorderStroke(1.dp, Color(0xFFFA4401)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text("Camera")
                }
                Spacer(Modifier.height(16.dp))
            }
        }
    }

}


@Composable
fun HeaderWithIcons(
    username: String = "Username",
    onCameraClick: () -> Unit = {},
    onMenuClick: () -> Unit = {},
    font: FontFamily
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = username,
            fontSize = 16.sp,
            color = Color.White,
            fontFamily = font,
            modifier = Modifier.weight(1f)
        )
        IconButton(onClick = onCameraClick) {
            Icon(
                painter = painterResource(id = R.drawable.camera_icon),
                contentDescription = "Camera",
                tint = Color.White,
                modifier = Modifier.size(18.dp)
            )
        }
        IconButton(onClick = onMenuClick) {
            Icon(
                painter = painterResource(id = R.drawable.burger_icon),
                contentDescription = "Menu",
                tint = Color.White,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}


@Composable
fun ProfileHeader(
    avatar: Painter,
    username: String,
    earned: Int,
    followers: Int,
    following: Int,
    font: FontFamily,
    onAvatarClick: () -> Unit,
    avatarURI: Uri?
) {
    Row(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(), horizontalArrangement = Arrangement.Start
    ) {

        Box(
            contentAlignment = Alignment.BottomEnd,
            modifier = Modifier
                .size(64.dp)
                .clickable { onAvatarClick() }
        ) {
            if(avatarURI ==  null){
                Image(
                    painter = avatar,
                    contentDescription = "Profile Picture",
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                )
            }else{
                println("ProfileHeader ... $avatarURI")
                AsyncImage(
                    model = avatarURI,
                    contentDescription = "Profile",
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                )
            }


            Box(
                modifier = Modifier
                    .size(20.dp)
                    .offset(x = 2.dp, y = 2.dp)
                    .border(
                        width = 1.dp,
                        color = Color.White,
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.profile_camera),
                    contentDescription = "Edit Profile Picture",
                    tint = Color.White,
                    modifier = Modifier.size(12.dp)
                )
            }
        }

//        Spacer(Modifier.width(12.dp))
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = 25.dp)
        ) {
            Text(username, fontFamily = font, fontSize = 16.sp, color = Color.White)
            Spacer(Modifier.height(4.dp))
            Row {
                Stat("Earned", earned)
                Spacer(Modifier.width(20.dp))
                Stat("Followers", followers)
                Spacer(Modifier.width(20.dp))
                Stat("Following", following)
            }
        }
    }
}

@Composable
fun Stat(label: String, value: Int) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text("$value", fontWeight = FontWeight.Bold, color = Color.White)
        if (label == "Earned") {
            Row {
                Image(
                    painter = painterResource(id = R.drawable.gem_icon),
                    contentDescription = "Coin Icon",
                    modifier = Modifier
                        .size(9.dp)
                        .align(Alignment.CenterVertically)
                )
                Spacer(Modifier.width(5.dp))
                Text(label, fontSize = 10.sp, color = Color.White)
            }
        } else {
            Text(label, fontSize = 10.sp, color = Color.White)
        }
    }
}

@Composable
fun ActionButtons(onEdit: () -> Unit, onShare: () -> Unit, font: FontFamily) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp)
    ) {
        Button(
            onClick = onEdit, modifier = Modifier
                .weight(1f)
                .padding(end = 8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = darkGrayButton),
            shape = RoundedCornerShape(10.dp)
        ) { Text("Edit Profile", fontFamily = font) }
        Button(
            onClick = onShare, modifier = Modifier
                .weight(1f)
                .padding(start = 8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = darkGrayButton),
            shape = RoundedCornerShape(10.dp)
        ) { Text("Share Profile", fontFamily = font) }
    }
}

@Composable
fun EarningsBar(progress: Float, onGoLive: () -> Unit, onClose: () -> Unit, font: FontFamily) {
    Box(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
            .height(60.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(darkGrayButton)
            .border(
                width = 0.5.dp,
                color = Color.White,
                shape = RoundedCornerShape(10.dp)
            )
            .background(darkGrayButton, RoundedCornerShape(10.dp))
    ) {
        Row(
            Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text("Earn ", color = Color.White, fontSize = 12.sp, fontFamily = font)
            Image(
                painter = painterResource(id = R.drawable.gem_icon),
                contentDescription = "Coin Icon",
                modifier = Modifier.size(11.dp)
            )
            Text(
                " ${(progress * 500).toInt()} more to redeem \$15",
                color = Color.White,
                fontSize = 12.sp,
                fontFamily = font
            )
            Spacer(Modifier.width(10.dp))
            Button(
                onClick = onGoLive,
                modifier = Modifier
                    .height(30.dp)
                    .background(brush = gradient, shape = RoundedCornerShape(10.dp))
                    .padding(horizontal = 8.dp, vertical = 0.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                contentPadding = PaddingValues(vertical = 0.dp, horizontal = 8.dp)
            ) {
                Text("Go Live", fontFamily = font)
            }
            Spacer(Modifier.width(10.dp))
            Image(
                painter = painterResource(id = R.drawable.close_icon),
                contentDescription = "Coin Icon",
                modifier = Modifier
                    .size(25.dp)
                    .clickable {
                        onClose()
                    }
            )
        }
    }
}


fun cacheImageToInternal(context: Context, uri: Uri): Uri {
    println("cacheImageToInternal ..  $uri")
    val inputStream = context.contentResolver.openInputStream(uri)
    val file = File(context.cacheDir, "profile_avatar_${System.currentTimeMillis()}.jpg")
    inputStream.use { input ->
        file.outputStream().use { output ->
            input?.copyTo(output)
        }
    }
    return file.toUri()
}

@Composable
fun ProfileGrid(images: List<Painter>) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(3), contentPadding = PaddingValues(8.dp)
    ) {
        items(images.size) { idx ->
            Image(
                painter = images[idx],
                contentDescription = null,
                modifier = Modifier
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(8.dp))
                    .padding(4.dp)
                    .clickable { /* Show details or open content */ },
                contentScale = ContentScale.Crop
            )
        }
    }
}





