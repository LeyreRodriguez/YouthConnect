package com.example.youthconnect.View.OverlaysAndMore

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.youthconnect.Model.Object.News
import com.example.youthconnect.R
import com.example.youthconnect.View.BottomNavigationScreens.CoilImage
import com.example.youthconnect.ViewModel.NewsViewModel
import com.example.youthconnect.ViewModel.UserViewModel

@Composable
fun NewsDetails(
    newsId: String,
    modifier: Modifier = Modifier.background(color = Color.White)
) {



    val newsViewModel: NewsViewModel = hiltViewModel()
    val userViewModel: UserViewModel = hiltViewModel()
    var news by remember { mutableStateOf<News?>(null) }
    val imageUrlState = remember { mutableStateOf("") }

    LaunchedEffect(newsViewModel) {
        try {
            news = newsViewModel.getNewsById(newsId)
        } catch (e: Exception) {
            Log.e("Firestore", "Error en ChildList", e)
        }
    }



    LaunchedEffect(Unit) {
        userViewModel.getProfileImage(
            onSuccess = { url ->
                imageUrlState.value = url
            },
            onFailure = { _ ->
                // Manejar el error, por ejemplo, mostrar un mensaje
            }
        )
    }

    LazyColumn(
        modifier = modifier.fillMaxSize().padding(15.dp)
    ) {
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                news?.title?.let {
                    Text(
                        text = it,
                        style = TextStyle(
                            fontSize = 30.sp,
                          //  fontFamily = FontFamily(Font(R.font.annie_use_your_telescope)),
                            fontWeight = FontWeight(400),
                            color = Color(0xFF000000),
                            letterSpacing = 0.9.sp,
                        ), modifier = Modifier
                            .padding(start = 15.dp, top = 10.dp)
                    )
                }
            }

            val configuration = LocalConfiguration.current
            val screenWidth = with(LocalDensity.current) { configuration.screenWidthDp.dp }
            news?.image?.let {
                CoilImage(
                    imageUrl = it,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f), // Proporción de aspecto cuadrado, puedes ajustarlo según tus necesidades
                    contentScale = ContentScale.Crop,
                    width = screenWidth,
                    height = Dp(300.0F) // Altura inicial
                )
            }

            news?.description?.let {
                Text(
                    text = it,
                    style = TextStyle(
                        fontSize = 20.sp,
                  //      fontFamily = FontFamily(Font(R.font.annie_use_your_telescope)),
                        fontWeight = FontWeight(400),
                        color = Color(0xFF000000),
                        letterSpacing = 0.9.sp,
                    ), modifier = Modifier
                        .padding(start = 15.dp, top = 10.dp)
                )
            }
        }
    }
}
