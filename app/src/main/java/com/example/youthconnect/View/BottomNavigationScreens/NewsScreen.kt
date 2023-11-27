package com.example.youthconnect.View.BottomNavigationScreens

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.transform.RoundedCornersTransformation
import com.example.youthconnect.Model.DataBase
import com.example.youthconnect.Model.News
import com.example.youthconnect.Model.Users.Child
import com.example.youthconnect.R
import com.example.youthconnect.ViewModel.ChildViewModel
import com.example.youthconnect.ViewModel.NewsViewModel
import com.example.youthconnect.ui.theme.Green
import com.example.youthconnect.ui.theme.Red
import kotlinx.coroutines.runBlocking
import java.util.concurrent.TimeUnit


@SuppressLint("SuspiciousIndentation", "UnrememberedMutableState")
@Composable
fun NewsScreen(
    newsViewModel: NewsViewModel = viewModel(),
    childViewModel: ChildViewModel = viewModel(),
    navController: NavHostController,
    modifier: Modifier = Modifier
    .background(color = Color.White)
) {
    val newsState by newsViewModel.newsState.collectAsState(emptyList())

    LaunchedEffect(Unit) {
        newsViewModel.getNews()
    }




    Box(
        modifier = modifier.fillMaxSize(),
    ) {
        Canvas(
            modifier = Modifier.fillMaxSize(),
            onDraw = {
                // Dibuja un rectángulo blanco como fondo
                drawRect(Color.White)

                // Define el pincel para el borde con el gradiente del Brush
                val borderBrush = Brush.horizontalGradient(
                    listOf(
                        Color(0xFFE15554),
                        Color(0xFF3BB273),
                        Color(0xFFE1BC29),
                        Color(0xFF4D9DE0)
                    )
                )

                // Dibuja el borde con el pincel definido
                drawRect(
                    brush = borderBrush,
                    topLeft = Offset(0f, 0f),
                    size = Size(size.width, size.height),
                    style = Stroke(width = 15.dp.toPx()) // Ancho del borde
                )
            }
        )



        Column( horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(15.dp)) {

            Row (modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically){
                Text(
                    text = "Schedule",
                    style = TextStyle(
                        fontSize = 40.sp,
                        fontFamily = FontFamily(Font(R.font.annie_use_your_telescope)),
                        fontWeight = FontWeight(400),
                        color = Color(0xFF000000),
                        letterSpacing = 0.9.sp,
                    ), modifier = Modifier
                        .padding(start = 15.dp, top = 10.dp )
                )

                val dataBase = DataBase()
               userImage(user = dataBase.getCurrentUserId(), navController = navController )


            }

            Text(
                text = "Fridays\n16:00 - 20:00",
                style = TextStyle(
                    fontSize = 20.sp,
                    fontFamily = FontFamily(Font(R.font.annie_use_your_telescope)),
                    fontWeight = FontWeight(400),
                    color = Color(0xFF000000),

                    textAlign = TextAlign.Center,
                    letterSpacing = 0.6.sp,
                )
            )

            Column(
                modifier
                    .fillMaxWidth()
                    .padding(15.dp)) {
                Text(
                    text = "Timer",
                    style = TextStyle(
                        fontSize = 40.sp,
                        fontFamily = FontFamily(Font(R.font.annie_use_your_telescope)),
                        fontWeight = FontWeight(400),
                        color = Color(0xFF000000),
                        letterSpacing = 0.9.sp,

                        ), modifier = Modifier
                        .padding(start = 15.dp, top = 10.dp )
                )

                Spacer(modifier = modifier.padding(50.dp))

                Text(
                    text = "Last News",
                    style = TextStyle(
                        fontSize = 40.sp,
                        fontFamily = FontFamily(Font(R.font.annie_use_your_telescope)),
                        fontWeight = FontWeight(400),
                        color = Color(0xFF000000),
                        letterSpacing = 0.9.sp,
                    ), modifier = Modifier
                        .padding(start = 15.dp, top = 10.dp )
                )

                RecyclerView(news = newsState, navController = navController)


            }

        }


    }



}


@Composable
fun userImage(user: String,
              navController: NavHostController){
    val documentExists = remember { mutableStateOf("-1") }
    val dataBase = DataBase()
    LaunchedEffect(user) {
        val result = dataBase.buscarDocumento(user)
        documentExists.value = result
    }

    Box(
        modifier = Modifier
            .size(50.dp)
            .clickable {

                    if (documentExists.value == "0") {
                        navController.navigate("instructor_profile_screen/${user}")
                    } else if (documentExists.value == "1") {
                        navController.navigate("parent_profile_screen/${user}")
                    } else {
                        navController.navigate("child_profile_screen/${user}")
                    }

            }
            .border(
                BorderStroke(4.dp, remember {
                    Brush.sweepGradient(
                        listOf(
                            Color.Green,
                            Color.Red // Cambia Green y Red por los colores que prefieras
                        )
                    )
                }),
                CircleShape
            )
            .padding(4.dp)
            .clip(CircleShape)
    ) {
        Image(
            painter = painterResource(id = R.drawable.user_icon),
            contentDescription = "icon",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
    }
}


@Composable
fun ListItem(news: News, navController: NavHostController) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {


                navController.navigate("news_details_screen/${news.id}")
            }

    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
        ) {
            CoilImage(
                imageUrl = news.Image,
                contentDescription = null,
                modifier = Modifier
                    .height(100.dp),
                contentScale = ContentScale.Crop,
                width = Dp(100.0F),
                height = Dp(100.0F)
            )

            Column{
                Text(
                    text = news.Title,
                    style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = if (news.Description.length > 50) {
                        "${news.Description.take(50)}..."
                    } else {
                        news.Description
                    }
                )
            }


        }
    }
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
    news: List<News>,
    navController: NavHostController
){
    LazyColumn(modifier = Modifier.padding(vertical = 4.dp)){
        items(items = news){ item ->
            ListItem(news = item, navController = navController)
        }
    }
}

