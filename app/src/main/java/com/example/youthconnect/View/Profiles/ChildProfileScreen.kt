package com.example.youthconnect.View.Profiles

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.net.Uri
import android.util.Log
import android.widget.Toast
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
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.OutlinedButton
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Park
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.libraryapp.viewModel.LoginViewModel
import com.example.youthconnect.Model.Object.Child
import com.example.youthconnect.Model.Object.Parent
import com.example.youthconnect.Model.Object.UserData
import com.example.youthconnect.R
import com.example.youthconnect.View.OverlaysAndMore.ModifyUsers
import com.example.youthconnect.View.OverlaysAndMore.SeeRollCall
import com.example.youthconnect.View.QR.DisplayQRCode
import com.example.youthconnect.ViewModel.UserViewModel
import com.example.youthconnect.ui.theme.Green
import com.example.youthconnect.ui.theme.Red

@Composable
fun ChildProfileScreen(
    childId: String,
    modifier: Modifier = Modifier.background(color = Color.White),
    loginViewModel: LoginViewModel = viewModel(),
    navController: NavController,
    onDismiss: (() -> Unit)? = null
){

    var child by remember { mutableStateOf<Child?>(null) }
    var parents by remember { mutableStateOf<List<Parent?>>(emptyList()) }
    var currentUser by remember { mutableStateOf<String?>(null) }
    val UserViewModel : UserViewModel = hiltViewModel()

    val userState = remember { mutableStateOf<UserData?>(null) }

    val documentExists = remember { mutableStateOf("-1") }
    var result by remember { mutableStateOf<String?>("") }
    var showDialog by remember { mutableStateOf(false)  }
    var user by remember { mutableStateOf<String?>("") }

    var editUser by remember { mutableStateOf(false)  }

    var currentUserType by remember { mutableStateOf("") }





    LaunchedEffect(UserViewModel) {
        val user = UserViewModel.getUserById(childId)
        currentUser = UserViewModel.getCurrentUser()
        currentUserType = currentUser?.let { UserViewModel.getUserType(it).toString() }.toString()
        userState.value = user
    }

    LaunchedEffect(UserViewModel) {
        try {
            child = UserViewModel.getCurrentChildById(childId)
            parents = child?.ParentID?.let { UserViewModel.getParentsByParentsID(it) }!!
            user = UserViewModel.getCurrentUser()
            result = user?.let { UserViewModel.findDocument(it) }

            if (result != null) {
                documentExists.value = result.toString()
            }
        } catch (e: Exception) {
            Log.e("Firestore", "Error en ChildList", e)
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
        UserViewModel.getProfileEspecificImage(childId.lowercase() + "@youthconnect.com",
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
            modifier = modifier.fillMaxSize(),
        ) {


            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(15.dp)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center
            ) {


                val configuration = LocalConfiguration.current

                AsyncImage(
                    model = imageUrlState.value,
                    contentDescription = "Profile Picture",
                    modifier = Modifier
                        .size(150.dp)
                        .border(
                            BorderStroke(
                                4.dp,
                                SolidColor(if (child?.GoOutAlone == true) Green else Red)
                            ),
                            CircleShape
                        )
                        .padding(4.dp)
                        .clip(CircleShape)
                        .clickable {
                            // TODO: Acciones al hacer clic en la imagen

                            if (currentUser == child?.ID) {
                                showImagePickerDialog = true
                            }

                        },
                    contentScale = ContentScale.Crop
                )

                Row(modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center){
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
                                .padding(start = 15.dp, top = 10.dp, end = 5.dp)
                        )
                    }



                        if(currentUserType == "Instructor"){
                            Icon(
                                imageVector = Icons.Outlined.Edit ,
                                contentDescription = "Edit",
                                tint = Color.Black,
                                modifier = Modifier
                                    .padding(4.dp)
                                    .clickable {
                                        editUser = true
                                    }
                            )
                        }

                        if (editUser) {
                            child?.let {
                                ModifyUsers(onDismiss = { editUser = false },
                                    it, navController
                                )
                            }
                        }





                    if (child?.State ?: "" == false){
                        Icon(
                            imageVector = Icons.Outlined.Park ,
                            contentDescription = "Recieved",
                            tint = Color.Black,
                            modifier = Modifier.size(30.dp)
                        )
                    }else{
                        Icon(
                            imageVector = Icons.Outlined.Home ,
                            contentDescription = "Recieved",
                            tint =  Color.Black,
                            modifier = Modifier.size(30.dp)
                        )
                    }
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

                child?.Observations?.let {
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










                if(currentUser == child?.ID){
                    Text(
                        text = "Salir",
                        style = TextStyle(
                            fontSize = 30.sp,
                            fontFamily = FontFamily(Font(R.font.annie_use_your_telescope)),
                            fontWeight = FontWeight(400),
                            color = Color(0xFF000000),
                            letterSpacing = 0.9.sp,
                            textAlign = TextAlign.Center
                        ), modifier = Modifier
                            .padding(start = 15.dp, top = 10.dp)
                            .fillMaxWidth()
                            .clickable {
                                loginViewModel.signOut()
                                navController.navigate("login")
                            }
                    )
                }




                Row() {

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {

                        Text(
                            text = "Padres",
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
                            text = "Teléfono",
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
               // Log.i("AWA", child.ID)
                DisplayQRCode(childId)

                if (documentExists.value == "0") {

                    OutlinedButton(onClick = { showDialog = true }) {
                        Text("Ver lista de asistencia")
                    }

                }
                if (showDialog) {
                    SeeRollCall(onDismiss = { showDialog = false }, childId)
                }


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




