package com.salsa.clone.salsaclone.ui.search

import CreatorCardShimmer
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults

import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import com.salsa.clone.salsaclone.R
import com.salsa.clone.salsaclone.data.model.Category
import com.salsa.clone.salsaclone.data.model.Creator
import com.salsa.clone.salsaclone.utils.isInternetAvailable

@Composable
fun SearchScreen(viewModel: SearchViewModel = hiltViewModel()) {
    val state = viewModel.state
    var searchText by remember { mutableStateOf("") }
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1A1A1A))
    ) {
        Spacer(modifier = Modifier.height(24.dp))
        OutlinedTextField(
            value = searchText,
            onValueChange = {
                searchText = it
                viewModel.onEvent(SearchEvent.Search(it))
            },
            leadingIcon = {
                Icon(
                    Icons.Default.Search,
                    contentDescription = null,
                    tint = Color.White
                )
            },
            placeholder = { Text("Search", color = Color.White) },
            singleLine = true,
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(50),
            colors = TextFieldDefaults.colors(
                focusedTextColor = Color.White,
                cursorColor = Color.White,
                focusedIndicatorColor = Color.Gray,
                unfocusedIndicatorColor = Color.Gray,
                focusedContainerColor = Color.Gray,
                unfocusedContainerColor = Color(0xFF232222),
                focusedPlaceholderColor = Color.Gray,
                focusedLeadingIconColor = Color.Gray
            )
        )

        if (state.categories.isEmpty()) {
            // Show "Not Found" message when no matching results
            Text(
                text = "No results found",
                color = Color.Gray,
                modifier = Modifier
                    .padding(start = 16.dp, top = 8.dp)
                    .align(Alignment.CenterHorizontally)
            )
        }

        if (state.isLoading) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Color.White)
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

                    LazyColumn(
                        contentPadding = PaddingValues(bottom = 32.dp)
                    ) {
                        item {
                            Text(
                                text = "Recommended",
                                color = Color.Gray,
                                fontSize = 14.sp,
                                modifier = Modifier.padding(start = 12.dp, top = 15.dp, bottom = 15.dp)
                            )
                        }
                        items(state.categories) { category ->
                            CategorySection(category)
                        }
                    }

            }
        }
    }
}

@Composable
fun CategorySection(category: Category) {
    val customFont = FontFamily(Font(R.font.robotoregular))
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "#${category.title}",
            color = Color.White,
            fontFamily = customFont,
            fontSize = 14.sp,
            modifier = Modifier.padding(start = 12.dp, top = 15.dp)
        )
        LazyRow(
            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp),
        ) {
            items(category.creators) { creator ->
                CreatorCard(creator, customFont)
            }
        }
    }
}

@Composable
fun CreatorCard(creator: Creator, customFont: FontFamily) {
    Card(
        modifier = Modifier
            .padding(horizontal = 1.dp, vertical = 4.dp)
            .width(140.dp)
            .height(210.dp),
        shape = RoundedCornerShape(3.dp)
    ) {
        Box(Modifier.fillMaxSize()) {
            AsyncImage(
                model = creator.thumbImage,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(240.dp)
            )

            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
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
                        fontFamily = customFont,
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
//                AsyncImage(
//                    model = creator.profileImage,
//                    contentDescription = "Profile Image",
//                    modifier = Modifier
//                        .size(24.dp)
//                        .clip(CircleShape),
//                    contentScale = ContentScale.Crop
//                )
//
//                Spacer(modifier = Modifier.width(8.dp))

                Column {
                    Text(
                        text = creator.name,
                        color = Color.White,
                        fontSize = 12.sp,
                        fontFamily = customFont
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
                            modifier = Modifier.padding(start = 4.dp),
                            fontFamily = customFont
                        )
                    }
                }
            }
        }
    }
}

