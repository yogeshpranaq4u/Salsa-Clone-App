import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.material.placeholder
import com.google.accompanist.placeholder.material.shimmer
import com.salsa.clone.salsaclone.R
import com.salsa.clone.salsaclone.data.model.Creator
import com.salsa.clone.salsaclone.ui.home.HomeViewModel
import com.salsa.clone.salsaclone.utils.isInternetAvailable

@Composable
fun HomeScreen(viewModel: HomeViewModel = hiltViewModel()) {
    val state = viewModel.state
    val context = LocalContext.current

    Column(
        Modifier
            .background(Color.Black)
            .fillMaxSize()
    ) {
        TopBar()
        Spacer(modifier = Modifier.height(8.dp))

        if (state.isLoading) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            if (!isInternetAvailable(context = context)) {
                // Show shimmer effect when no internet
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    // Show a fixed number of shimmer placeholders, e.g. 6
                    items(6) {
                        CreatorCardShimmer()
                    }
                }
            } else {
                // Normal content when internet available
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    items(state.creators) { creator ->
                        CreatorCard(creator)
                    }
                }
            }
        }
//        BottomNavigationBar(selected = "For you")
    }
}


@Composable
fun TopBar() {
    val customFont = FontFamily(Font(R.font.robotoregular))
    Box(modifier = Modifier.padding(top = 25.dp)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Black)
                .padding(top = 24.dp, start = 16.dp, end = 16.dp, bottom = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(id = R.drawable.coin_icon),
                    contentDescription = "Coins",
                    modifier = Modifier.size(20.dp)
                )
                Text(
                    "260",
                    color = Color.White,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(start = 4.dp, end = 8.dp), fontFamily = customFont
                )
                Spacer(modifier = Modifier.width(5.dp))
                Box(
                    modifier = Modifier
                        .background(Color(0xFCFAF8F8), shape = RoundedCornerShape(50))

                ) {
                    Icon(
                        Icons.Default.Add,
                        contentDescription = "Add Coins",
                        tint = Color.Black,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            GradientButtonWithIcon(
                onClick = { /* Go Live action */ },
                text = "Go Live",
                fontFamily = customFont,
                iconResId = R.drawable.go_live_icon
            )
        }
    }
}

@Composable
fun GradientButtonWithIcon(onClick: () -> Unit, text: String, fontFamily: FontFamily, iconResId: Int) {
    val gradient = Brush.horizontalGradient(
        colors = listOf(Color(0xFFDF3818), Color(0xFFE75B0F))
    )

    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
        contentPadding = PaddingValues(0.dp),
        shape = RoundedCornerShape(20.dp),
        modifier = Modifier
            .width(110.dp)
            .height(20.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(gradient)
    ) {

            Row(
                modifier = Modifier
                    .padding(horizontal = 10.dp, vertical = 0.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(id = iconResId),
                    contentDescription = "Icon",
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = text,
                    color = Color.White,
                    fontFamily = fontFamily, fontSize = 15.sp
                )
            }

    }

}


@Composable
fun CreatorCard(creator: Creator) {
    Card(
        modifier = Modifier
            .padding(horizontal = 4.dp, vertical = 4.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(3.dp)
    ) {
        Box(Modifier.fillMaxSize()) {
            AsyncImage(
                model = creator.thumbImage,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxWidth().height(240.dp)
            )

            Box(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(horizontal = 8.dp, vertical = 8.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.eye_icon),
                        contentDescription = "Eye Icon",
                        modifier = Modifier.size(14.dp)
                    )
                    Text(
                        text = "${creator.views}",
                        color = Color.White,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(start = 4.dp)
                    )
                }
            }
            Row(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(start = 8.dp, bottom = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(
                    model = creator.profileImage,
                    contentDescription = "Profile Image",
                    modifier = Modifier
                        .size(24.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.width(8.dp))

                Column {
                    Text(
                        text = creator.name,
                        color = Color.White,
                        fontSize = 12.sp
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.gem_icon),
                            contentDescription = "Coin Icon",
                            modifier = Modifier.size(11.dp)
                        )
                        Text(
                            text = "${creator.coins}",
                            color = Color.White,
                            fontSize = 12.sp,
                            modifier = Modifier.padding(start = 4.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CreatorCardShimmer() {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp, vertical = 4.dp),
        shape = RoundedCornerShape(3.dp),
        color = Color.Transparent
    ) {
        Box(Modifier.fillMaxWidth()) {
            Column(Modifier.fillMaxWidth()) {

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(240.dp)
                        .placeholder(
                            visible = true,
                            highlight = PlaceholderHighlight.shimmer(),
                            shape = RoundedCornerShape(3.dp)
                        )
                )

                Spacer(modifier = Modifier.height(8.dp))


                Row(
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .height(20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(14.dp)
                            .placeholder(
                                visible = true,
                                highlight = PlaceholderHighlight.shimmer(),
                                shape = RoundedCornerShape(7.dp)
                            )
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Box(
                        modifier = Modifier
                            .width(40.dp)
                            .height(14.dp)
                            .placeholder(
                                visible = true,
                                highlight = PlaceholderHighlight.shimmer(),
                                shape = RoundedCornerShape(7.dp)
                            )
                    )
                }

                Spacer(modifier = Modifier.height(130.dp))
            }


            Row(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(start = 8.dp, bottom = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .placeholder(
                            visible = true,
                            highlight = PlaceholderHighlight.shimmer(),
                            shape = CircleShape
                        )
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Box(
                        modifier = Modifier
                            .width(80.dp)
                            .height(12.dp)
                            .placeholder(
                                visible = true,
                                highlight = PlaceholderHighlight.shimmer(),
                                shape = RoundedCornerShape(4.dp)
                            )
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(11.dp)
                                .placeholder(
                                    visible = true,
                                    highlight = PlaceholderHighlight.shimmer(),
                                    shape = RoundedCornerShape(4.dp)
                                )
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Box(
                            modifier = Modifier
                                .width(30.dp)
                                .height(12.dp)
                                .placeholder(
                                    visible = true,
                                    highlight = PlaceholderHighlight.shimmer(),
                                    shape = RoundedCornerShape(4.dp)
                                )
                        )
                    }
                }
            }
        }
    }
}

