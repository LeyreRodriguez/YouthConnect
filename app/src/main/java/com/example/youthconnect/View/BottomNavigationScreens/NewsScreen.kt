package com.example.youthconnect.View.BottomNavigationScreens

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.transform.RoundedCornersTransformation
import com.example.youthconnect.Model.Object.News
import com.example.youthconnect.R
import com.example.youthconnect.View.Components.FloatingButton
import com.example.youthconnect.View.OverlaysAndMore.AddNews
import com.example.youthconnect.ViewModel.NewsViewModel
import com.example.youthconnect.ViewModel.UserViewModel


@SuppressLint("SuspiciousIndentation", "UnrememberedMutableState")
@Composable
fun NewsScreen(
    navController: NavHostController,
    modifier: Modifier = Modifier
    .background(color = Color.White)
) {

    var news by remember { mutableStateOf<List<News?>>(emptyList()) }
    var user by remember { mutableStateOf<String?>("") }

    val newsViewModel : NewsViewModel = hiltViewModel()
    val userViewModel : UserViewModel = hiltViewModel()

    val documentExists = remember { mutableStateOf("-1") }
    var result by remember { mutableStateOf<String?>("") }
    var showDialog by remember { mutableStateOf(false)  }


    LaunchedEffect(newsViewModel ) {
        news = newsViewModel.getAllNews()
    }
    LaunchedEffect(Unit) {
        try {

            user = userViewModel.getCurrentUser()
            result = user?.let { userViewModel.findDocument(it)

            }
            if (result != null) {
                documentExists.value = result.toString()
            }

        } catch (e: Exception) {
            Log.e("Firestore", "Error en ChildList", e)
        }

    }



    Box(
        modifier = modifier.fillMaxSize(),
    ) {


        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(15.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            NewsHeader()
            NewsSchedule()
            Column(
                modifier
                    .fillMaxWidth()
                    .padding(15.dp)) {

                Text(
                    text = "Últimas noticias",
                    style = TextStyle(
                        fontSize = 40.sp,
                        fontFamily = FontFamily(Font(R.font.annie_use_your_telescope)),
                        fontWeight = FontWeight(400),
                        color = Color(0xFF000000),
                        letterSpacing = 0.9.sp,
                    ), modifier = Modifier
                        .padding(start = 15.dp, top = 10.dp )
                )

                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.BottomEnd
                ){
                    RecyclerView(news = news, navController = navController)

                    if (documentExists.value == "0") {
                        Column {
                            FloatingButton(Icons.Outlined.Add) {
                                // Aquí puedes agregar la lógica que se activará al hacer clic en el botón
                                // Por ejemplo, puedes navegar a una nueva pantalla
                                // navController.navigate("addNews")
                                showDialog = true
                            }

                        }

                    }

                    if (showDialog) {
                        AddNews(onDismiss = { showDialog = false }, navController = navController)
                    }


                }
            }

        }


    }

}


@Composable
fun NewsHeader() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Horario",
            style = TextStyle(
                fontSize = 40.sp,
                fontFamily = FontFamily(Font(R.font.annie_use_your_telescope)),
                fontWeight = FontWeight(400),
                color = Color(0xFF000000),
                letterSpacing = 0.9.sp
            ),
            modifier = Modifier.padding(start = 15.dp, top = 10.dp)
        )
    }
}

@Composable
fun NewsSchedule() {
    Text(
        text = "Viernes\n16:00 - 20:00",
        style = TextStyle(
            fontSize = 20.sp,
            fontFamily = FontFamily(Font(R.font.annie_use_your_telescope)),
            fontWeight = FontWeight(400),
            color = Color(0xFF000000),
            textAlign = TextAlign.Center,
            letterSpacing = 0.6.sp
        )
    )
}




@Composable
fun ListItem(news: News, navController: NavHostController) {


    Card(
        modifier = Modifier
            .fillMaxSize()
            .clickable {
                navController.navigate("news_details_screen/${news.id}")
            }
            .padding(bottom = 4.dp)
            ,
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        )

    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
        ) {
            news.image?.let {
                CoilImage(
                    imageUrl = it,
                    contentDescription = null,
                    modifier = Modifier
                        .height(100.dp),
                    contentScale = ContentScale.Crop,
                    width = Dp(100.0F),
                    height = Dp(100.0F)
                )
            }

            Column{
                Text(
                    text = news.title,
                    style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = if (news.description.length > 50) {
                        "${news.description.take(50)}..."
                    } else {
                        news.description
                    }
                )


            }


        }

    }

    Divider(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        color = Color.Gray,
        thickness = 1.dp
    )

}

@Composable
fun CoilImage(
    imageUrl: String,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Fit,
    width: Dp,
    height : Dp
){
    val painter = rememberAsyncImagePainter(
        ImageRequest
            .Builder(LocalContext.current)
            .data(imageUrl)
            .apply(fun ImageRequest.Builder.() {
                crossfade(true)
                transformations(RoundedCornersTransformation(20f))
            })
            .build()
    )


    Image(
        painter = painter,
        contentDescription = contentDescription,
        modifier = Modifier
            .width(width)
            .height(height)
            .padding(5.dp),
        contentScale = contentScale
    )
}
@Composable
fun RecyclerView(
    news: List<News?>,
    navController: NavHostController
){
    LazyColumn(modifier = Modifier.padding(vertical = 4.dp)){
        items(items = news){ item ->
            if (item != null) {
                ListItem(news = item, navController = navController)
            }
        }
    }
}




