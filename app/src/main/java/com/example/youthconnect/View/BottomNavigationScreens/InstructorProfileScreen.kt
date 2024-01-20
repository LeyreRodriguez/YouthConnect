package com.example.youthconnect.View.BottomNavigationScreens



import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.util.Log
import android.widget.Toast
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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material.TextButton
import androidx.compose.material3.AlertDialog
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
import androidx.compose.ui.text.toLowerCase
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.libraryapp.viewModel.LoginViewModel
import com.example.youthconnect.Model.Enum.NavScreen
import com.example.youthconnect.Model.Object.Child
import com.example.youthconnect.Model.Object.Instructor

import com.example.youthconnect.R
import com.example.youthconnect.ui.theme.Green
import com.example.youthconnect.ui.theme.Red
import com.example.youthconnect.ViewModel.UserViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InstructorProfileScreen(instructorId : String,
                              modifier : Modifier = Modifier.background(color = Color.White),
                              navController: NavHostController,
                            loginViewModel : LoginViewModel = viewModel()
) {

    val UserViewModel : UserViewModel = hiltViewModel()
   // val ProfileViewModel : profileViewModel = hiltViewModel()
    var instructor by remember { mutableStateOf<Instructor?>(null) }
    var children by remember { mutableStateOf<List<Child?>>(emptyList()) }


    LaunchedEffect(UserViewModel) {
        try {
            instructor = UserViewModel.getCurrentInstructorById(instructorId)
            children = UserViewModel.getChildByInstructorId(instructorId)
            Log.i("InstructorProfileScreen", "Instructor: $instructor, Children: $children")
        } catch (e: Exception) {
            Log.e("Firestore", "Error fetching data", e)
        }
    }

    val context = LocalContext.current
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var showImagePickerDialog by remember { mutableStateOf(false) }


    val imageUrlState = remember { mutableStateOf("") }

    val imagePickerLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            UserViewModel.uploadProfileImage(uri, onSuccess = { newImageUrl ->
                UserViewModel.getProfileImage(
                    onSuccess = { fetchedUrl ->
                        imageUrlState.value = fetchedUrl
                        Toast.makeText(
                            context,
                            "Imagen Actualizada",
                            Toast.LENGTH_LONG
                        ).show()
                    },
                    onFailure = { exception ->
                        Toast.makeText(
                            context,
                            "Error al bajar la imagen",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                )
            }, onFailure = { exception ->
                Toast.makeText(
                    context,
                    "Error al subir la imagen",
                    Toast.LENGTH_LONG
                ).show()
            })
        }
    }

    // Lanzador para tomar foto con la cámara
    val takePictureLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        if (success) {
            // Aquí manejas la imagen capturada usando imageUri
            imageUri?.let { uri ->
                UserViewModel.uploadProfileImage(uri, onSuccess = { newImageUrl ->
                    UserViewModel.getProfileImage(
                        onSuccess = { fetchedUrl ->
                            imageUrlState.value = fetchedUrl
                            Toast.makeText(
                                context,
                                "Imagen Actualizada",
                                Toast.LENGTH_LONG
                            ).show()
                        },
                        onFailure = { exception ->
                            Toast.makeText(
                                context,
                                "Error al bajar la imagen",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    )
                }, onFailure = { exception ->
                    Toast.makeText(
                        context,
                        "Error al subir la imagen",
                        Toast.LENGTH_LONG
                    ).show()
                })
            }
        }
    }

    LaunchedEffect(Unit) {
        UserViewModel.getProfileImage(
            onSuccess = { url ->
                imageUrlState.value = url
            },
            onFailure = { exception ->
                // Manejar el error, por ejemplo, mostrar un mensaje
            }
        )
    }


    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted: Boolean ->
            if (isGranted) {
                // Permiso concedido, proceder con la acción
                imageUri = UserViewModel.createImageUri(context)
                imageUri?.let { uri ->
                    takePictureLauncher.launch(uri)
                }
            } else {
                // Permiso denegado, mostrar un mensaje
                Toast.makeText(
                    context,
                    "No se puede abrir la cámara",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    )


    if (showImagePickerDialog) {
        AlertDialog(
            onDismissRequest = { showImagePickerDialog = false },
            title = { Text("Seleccionar Imagen") },
            text = { Text("Elige de dónde quieres seleccionar la imagen.") },
            confirmButton = {
                TextButton(onClick = {
                    imagePickerLauncher.launch("image/*")
                    showImagePickerDialog = false
                }) {
                    Text("Galería")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    when (PackageManager.PERMISSION_GRANTED) {
                        ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) -> {
                            // Permiso ya concedido, proceder con la acción
                            imageUri = UserViewModel.createImageUri(context)
                            imageUri?.let { uri ->
                                takePictureLauncher.launch(uri)
                            }
                        }
                        else -> {
                            // Solicitar permiso
                            permissionLauncher.launch(Manifest.permission.CAMERA)
                        }
                    }
                    showImagePickerDialog = false
                }) {
                    Text("Cámara")
                }
            }
        )
    }


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


                        // Profile Image
                        AsyncImage(
                            model = imageUrlState.value,
                            contentDescription = "Profile Picture",
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
                                    // TODO: Acciones al hacer clic en la imagen
                                    showImagePickerDialog = true
                                },
                            contentScale = ContentScale.Crop
                        )

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


    val UserViewModel : UserViewModel = hiltViewModel()

    val imageUrlState = remember { mutableStateOf("") }
    LaunchedEffect(Unit) {
        UserViewModel.getProfileEspecificImage(child.ID.lowercase() + "@youthconnect.com",
            onSuccess = { url ->
                imageUrlState.value = url
            },
            onFailure = { exception ->
                // Manejar el error, por ejemplo, mostrar un mensaje
            }
        )
    }
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

                AsyncImage(
                    model = imageUrlState.value,
                    contentDescription = "Profile Picture",
                    modifier = Modifier
                        .size(30.dp)
                        .border(
                            BorderStroke(4.dp, SolidColor(Green)),
                            CircleShape
                        )
                        .padding(4.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
            } else {
                AsyncImage(
                    model = imageUrlState.value,
                    contentDescription = "Profile Picture",
                    modifier = Modifier
                        .size(30.dp)
                        .border(
                            BorderStroke(4.dp, SolidColor(Red)),
                            CircleShape
                        )
                        .padding(4.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
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


