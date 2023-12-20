package com.example.youthconnect.View.BottomNavigationScreens



import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.SnackbarResult
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.libraryapp.viewModel.LoginViewModel
import com.example.youthconnect.Model.Constants
import com.example.youthconnect.Model.Enum.NavScreen
import com.example.youthconnect.Model.Object.Child
import com.example.youthconnect.Model.Object.Instructor

import com.example.youthconnect.R
import com.example.youthconnect.ViewModel.ImageViewModel
import com.example.youthconnect.ui.theme.Green
import com.example.youthconnect.ui.theme.Red
import com.example.youthconnect.ViewModel.UserViewModel
import kotlinx.coroutines.launch


@Composable
fun InstructorProfileScreen(instructorId : String,
                              modifier : Modifier = Modifier.background(color = Color.White),
                              navController: NavHostController,
                            loginViewModel : LoginViewModel = viewModel()
) {

    val UserViewModel : UserViewModel = hiltViewModel()
    val ImageViewModel : ImageViewModel = hiltViewModel()
    var instructor by remember { mutableStateOf<Instructor?>(null) }
    var children by remember { mutableStateOf<List<Child?>>(emptyList()) }

    val scaffoldState = rememberScaffoldState()
    val coroutineScope = rememberCoroutineScope()


    LaunchedEffect(UserViewModel) {
        try {
            instructor = UserViewModel.getCurrentInstructorById(instructorId)
            children = UserViewModel.getChildByInstructorId(instructorId)
            Log.i("InstructorProfileScreen", "Instructor: $instructor, Children: $children")
        } catch (e: Exception) {
            Log.e("Firestore", "Error fetching data", e)
        }
    }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ){ imageUri ->
        imageUri?.let {
            ImageViewModel.addUserPhotoToStorage(imageUri,instructorId)
        }
    }

    Log.i("AWA", children.toString())

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

            Image(
                painter = painterResource(id = R.drawable.baseline_person_add_24 ),
                contentDescription = "icon",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(80.dp)
                    .padding(15.dp)
                    .clickable {
                        navController.navigate(NavScreen.AddInstructor.name)
                    }
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

            Column(
                modifier = Modifier
                    .padding(15.dp)
                    .fillMaxSize()
                ,
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Column(horizontalAlignment = Alignment.CenterHorizontally,
                    //modifier = Modifier.wrapContentSize()
                ) {
                    val configuration = LocalConfiguration.current
                    val screenWidth = with(LocalDensity.current) { configuration.screenWidthDp.dp }
                    Image(
                        painter =   painterResource(id = R.drawable.user_icon),
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
                            .clickable {
                                galleryLauncher.launch(Constants.ALL_IMAGES)
                            }
                    )


//                    Log.i("UWU", instructor!!.fullName)
                    instructor?.FullName?.let {

                        Text(
                            text = it,
                            style = TextStyle(
                                fontSize = 20.sp,
                                fontFamily = FontFamily(Font(R.font.annie_use_your_telescope)),
                                fontWeight = FontWeight(400),
                                color = Color(0xFF000000),
                                letterSpacing = 0.9.sp,
                                textAlign = TextAlign.Center
                            ), modifier = Modifier
                                .padding(start = 15.dp, top = 10.dp)
                        )
                    }



                  //  Spacer(modifier = Modifier.size(40.dp))
                }



                Column (
                    modifier = Modifier.fillMaxWidth()
                ){

                    Text(
                        text = "LogOut",
                        style = TextStyle(
                            fontSize = 30.sp,
                            fontFamily = FontFamily(Font(R.font.annie_use_your_telescope)),
                            fontWeight = FontWeight(400),
                            color = Color(0xFF000000),
                            letterSpacing = 0.9.sp,
                            textAlign = TextAlign.Center
                        ), modifier = Modifier
                            .padding(start = 15.dp, top = 10.dp)
                            .clickable { loginViewModel.signOut()
                                navController.navigate("login")}
                    )


                    Text(
                        text = "My kids",
                        style = TextStyle(
                            fontSize = 30.sp,
                            fontFamily = FontFamily(Font(R.font.annie_use_your_telescope)),
                            fontWeight = FontWeight(400),
                            color = Color(0xFF000000),
                            letterSpacing = 0.9.sp,
                            textAlign = TextAlign.Start
                        ), modifier = Modifier
                            .padding(start = 15.dp, top = 10.dp)
                    )



                    LazyColumn(modifier = Modifier
                        .padding(vertical = 4.dp)
                        .height(200.dp)
                    ) {
                        items(items = children) { item ->
                            if (item != null) {
                                user(navController = navController, item)
                            }
                        }
                    }


                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly, // Distribuye las imágenes equitativamente
                    verticalAlignment = Alignment.CenterVertically
                ){


                    val context = LocalContext.current


                    Image(
                        painter = painterResource(id = R.drawable.baseline_qr_code_scanner_24 ),
                        contentDescription = "icon",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(120.dp)
                            .padding(10.dp)
                            .clickable {

                                //context.startActivity(Intent(context, QrScan::class.java))
                                navController.navigate("qr")


                            }
                            .background(Color(0xFFD9D9D9), CircleShape)

                            .padding(4.dp)
                            .clip(CircleShape)

                    )

                    Image(
                        painter = painterResource(id = R.drawable.baseline_format_list_bulleted_24 ),
                        contentDescription = "icon",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(120.dp)
                            .padding(10.dp)
                            .clickable {


                                navController.navigate(NavScreen.ChildList.name + "/${instructorId}")
                            }
                            .background(Color(0xFFD9D9D9), CircleShape)
                            .padding(4.dp)
                            .clip(CircleShape)

                    )
                }

            }


        }

    }



@Composable
fun user(navController: NavController, child: Child){

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {

                navController.navigate("child_profile_screen/${child.ID}")
            }
            .padding(4.dp)

    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
        ) {

            if(child.GoOutAlone) {
                Image(
                    painter = painterResource(id = R.drawable.user_icon),
                    contentDescription = "icon",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(30.dp)
                        .border(
                            BorderStroke(4.dp, SolidColor(Green)),
                            CircleShape
                        )
                        .padding(4.dp)
                        .clip(CircleShape)
                )
            } else {
                Image(
                    painter = painterResource(id = R.drawable.user_icon),
                    contentDescription = "icon",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(30.dp)
                        .border(
                            BorderStroke(4.dp, SolidColor(Red)),
                            CircleShape
                        )
                        .padding(4.dp)
                        .clip(CircleShape)
                )
            }

            Text(
                text = child.FullName,
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                ),
                modifier = Modifier.padding(start = 10.dp)

            )




        }
    }
}


