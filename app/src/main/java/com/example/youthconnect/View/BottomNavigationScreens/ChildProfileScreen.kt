package com.example.youthconnect.View.BottomNavigationScreens

import android.annotation.SuppressLint
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.youthconnect.Model.Object.Child
import com.example.youthconnect.Model.Object.Parent
import com.example.youthconnect.R
import com.example.youthconnect.View.QR.DisplayQRCode
import com.example.youthconnect.ViewModel.UserViewModel
import com.example.youthconnect.ui.theme.Green
import com.example.youthconnect.ui.theme.Red

@Composable
fun ChildProfileScreen(childId : String,
                       modifier : Modifier = Modifier.background(color = Color.White)
){

    var child by remember { mutableStateOf<Child?>(null) }
    var parents by remember { mutableStateOf<List<Parent?>>(emptyList()) }

    val UserViewModel : UserViewModel = hiltViewModel()

    LaunchedEffect(UserViewModel) {
        try {
            child = UserViewModel.getCurrentChildById(childId)
            parents = child?.ParentID?.let { UserViewModel.getParentsByParentsID(it) }!!
        } catch (e: Exception) {
            Log.e("Firestore", "Error en ChildList", e)
        }
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

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(15.dp)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center
            ) {


                val configuration = LocalConfiguration.current
                val screenWidth = with(LocalDensity.current) { configuration.screenWidthDp.dp }
                Image(
                    painter = painterResource(id = R.drawable.user_icon),
                    contentDescription = "icon",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(150.dp)
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

                child?.FullName?.let {
                    Text(
                        text = it,
                        style = TextStyle(
                            fontSize = 20.sp,
                            fontFamily = FontFamily(Font(R.font.annie_use_your_telescope)),
                            fontWeight = FontWeight(400),
                            color = Color(0xFF000000),
                            letterSpacing = 0.9.sp,
                        ), modifier = Modifier
                            .padding(start = 15.dp, top = 10.dp)
                    )
                }

                child?.Course?.let {
                    Text(
                        text = it,
                        style = TextStyle(
                            fontSize = 15.sp,
                            fontFamily = FontFamily(Font(R.font.annie_use_your_telescope)),
                            fontWeight = FontWeight(400),
                            color = Color(0xFF000000),
                            letterSpacing = 0.9.sp,
                        ), modifier = Modifier
                            .padding(start = 15.dp, top = 10.dp)
                    )
                }

                Spacer(modifier = Modifier.size(40.dp))


                Row() {

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {

                        Text(
                            text = "Parents",
                            style = TextStyle(
                                fontSize = 15.sp,
                                fontFamily = FontFamily(Font(R.font.annie_use_your_telescope)),
                                fontWeight = FontWeight(400),
                                color = Color(0xFF000000),
                                letterSpacing = 0.9.sp,
                            )
                        )


                        //val parentState = listOf<String>("Florencio Rodriguez Rodriguez", "Juani Quintana Monroy")
                        LazyColumn(modifier = Modifier.padding(vertical = 4.dp)) {
                            items(items = parents) { item ->
                                item?.FullName?.let { Text(text = it) }
                            }
                        }


                    }
                    Spacer(modifier = Modifier.size(20.dp))

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {

                        Text(
                            text = "Telephone",
                            style = TextStyle(
                                fontSize = 15.sp,
                                fontFamily = FontFamily(Font(R.font.annie_use_your_telescope)),
                                fontWeight = FontWeight(400),
                                color = Color(0xFF000000),
                                letterSpacing = 0.9.sp,
                            )
                        )
                        // val numbers = listOf<String>("680806622", "635556961")

                        LazyColumn(modifier = Modifier.padding(vertical = 4.dp)) {
                            items(items = parents) { item ->
                                item?.PhoneNumber?.let { Text(text = it) }
                            }
                        }
                    }


                }

                Spacer(modifier = Modifier.size(40.dp))

                child?.ID?.let { DisplayQRCode(it) }

            }
        }


    }





@SuppressLint("NotConstructor")
@Composable
fun DisplayQRCode(qrText : String) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        QRCodeGenerator(text = qrText)
    }
}


@Composable
fun QRCodeGenerator(text: String, modifier: Modifier = Modifier) {
    val qr = DisplayQRCode()
    val generatedBitmap = qr.generateQRCode(text)
    generatedBitmap?.let {
        Image(
            bitmap = it.asImageBitmap(),
            contentDescription = null,
            modifier = Modifier
                .size(200.dp)
        )
    }
}


@Preview(showBackground = true)
@Composable

fun ChildProfileView() {
    Box(
        modifier = Modifier.fillMaxSize(),
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

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(15.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center
        ) {


            val configuration = LocalConfiguration.current
            val screenWidth = with(LocalDensity.current) { configuration.screenWidthDp.dp }
            Image(
                painter = painterResource(id = R.drawable.user_icon),
                contentDescription = "icon",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(150.dp)
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

            Text(
                text = "Leyre Rodriguez Quintana",
                style = TextStyle(
                    fontSize = 20.sp,
                    fontFamily = FontFamily(Font(R.font.annie_use_your_telescope)),
                    fontWeight = FontWeight(400),
                    color = Color(0xFF000000),
                    letterSpacing = 0.9.sp,
                ), modifier = Modifier
                    .padding(start = 15.dp, top = 10.dp)
            )

            Text(
                text = "4ºESO",
                style = TextStyle(
                    fontSize = 15.sp,
                    fontFamily = FontFamily(Font(R.font.annie_use_your_telescope)),
                    fontWeight = FontWeight(400),
                    color = Color(0xFF000000),
                    letterSpacing = 0.9.sp,
                ), modifier = Modifier
                    .padding(start = 15.dp, top = 10.dp)
            )

            Spacer(modifier = Modifier.size(40.dp))

            Row() {

                Column(horizontalAlignment = Alignment.CenterHorizontally) {

                    Text(
                        text = "Parents",
                        style = TextStyle(
                            fontSize = 15.sp,
                            fontFamily = FontFamily(Font(R.font.annie_use_your_telescope)),
                            fontWeight = FontWeight(400),
                            color = Color(0xFF000000),
                            letterSpacing = 0.9.sp,
                        )
                    )


                    val parentState =
                        listOf<String>("Florencio Rodriguez Rodriguez", "Juani Quintana Monroy")
                    LazyColumn(modifier = Modifier.padding(vertical = 4.dp)) {
                        items(items = parentState) { item ->
                            Text(text = item)
                        }
                    }
                }
                Spacer(modifier = Modifier.size(20.dp))

                Column(horizontalAlignment = Alignment.CenterHorizontally) {

                    Text(
                        text = "Telephone",
                        style = TextStyle(
                            fontSize = 15.sp,
                            fontFamily = FontFamily(Font(R.font.annie_use_your_telescope)),
                            fontWeight = FontWeight(400),
                            color = Color(0xFF000000),
                            letterSpacing = 0.9.sp,
                        )
                    )
                    val numbers = listOf<String>("680806622", "635556961")
                    LazyColumn(modifier = Modifier.padding(vertical = 4.dp)) {
                        items(items = numbers) { item ->
                            Text(text = item)
                        }
                    }
                }


            }

            DisplayQRCode("54148418R")
        }
    }
}


