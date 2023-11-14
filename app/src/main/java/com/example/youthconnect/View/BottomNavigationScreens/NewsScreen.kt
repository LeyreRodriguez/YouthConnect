package com.example.youthconnect.View.BottomNavigationScreens

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.transform.RoundedCornersTransformation
import com.example.youthconnect.Model.DataBase
import com.example.youthconnect.R
import com.example.youthconnect.ui.theme.Green
import com.example.youthconnect.ui.theme.Red
import java.util.concurrent.TimeUnit


@Composable
fun NewsScreen(modifier: Modifier = Modifier
    .background(color = Color.White)
) {
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

                Image(
                    painter = painterResource(id = R.drawable.user_icon),
                    contentDescription = "icon",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(50.dp)
                        .border(
                            BorderStroke(4.dp, remember {
                                Brush.sweepGradient(
                                    listOf(
                                        Green, Red
                                    )
                                )
                            }),
                            CircleShape
                        )
                        .padding(4.dp)
                        .clip(CircleShape)
                )


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

                val dataBase = DataBase()
                RecyclerView(dataBase.getImage())
            }

        }


    }



}


@Composable

fun ListItem(imageUrl : String){

    CoilImage(
        imageUrl = imageUrl,
        contentDescription = null,
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp),
        contentScale = ContentScale.Crop
    )
}

@Composable
fun CoilImage(
    imageUrl: String,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Fit
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
            .size(50.dp)
            .padding(15.dp)
            .clip(CircleShape),
        contentScale = contentScale
    )
}
@Composable
fun RecyclerView(names : List<String>){
    Log.i("UWU", names.toString())
    LazyColumn(modifier = Modifier.padding(vertical = 4.dp)){
        items(items = names){ name ->
            ListItem(name)
        }
    }
}
@Preview(showBackground = true)
@Composable
fun NewsScreenPreview() {
    NewsScreen()
}