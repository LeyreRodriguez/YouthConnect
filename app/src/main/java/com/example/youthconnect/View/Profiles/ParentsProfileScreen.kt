package com.example.youthconnect.View.Profiles

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Edit
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
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
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.libraryapp.viewModel.LoginViewModel
import com.example.youthconnect.Model.Object.Child
import com.example.youthconnect.Model.Object.Parent
import com.example.youthconnect.R
import com.example.youthconnect.View.OverlaysAndMore.ModifyUsers
import com.example.youthconnect.View.OverlaysAndMore.MyChildren
import com.example.youthconnect.ViewModel.UserViewModel
import com.example.youthconnect.ui.theme.Green
import com.example.youthconnect.ui.theme.Red


@Composable
fun ParentsProfileScreen(parentId : String,
                         modifier : Modifier = Modifier.background(color = Color.White),
                         navController: NavHostController,
                         loginViewModel: LoginViewModel = viewModel()
) {

    var parent by remember { mutableStateOf<Parent?>(null) }
    var currentUser by remember { mutableStateOf<String?>(null) }
    var children by remember { mutableStateOf<List<Child?>>(emptyList()) }

    val userViewModel : UserViewModel = hiltViewModel()

    var editUser by remember { mutableStateOf(false)  }

    var currentUserType by remember { mutableStateOf("") }




    LaunchedEffect(userViewModel) {
        try {
            parent = userViewModel.getCurrentUserById(parentId)
            children = userViewModel.getChildByParentsId(parentId)
            currentUser = userViewModel.getCurrentUser()
            currentUserType = currentUser?.let { userViewModel.getUserType(it).toString() }.toString()
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
            userViewModel.uploadProfileImage(uri, onSuccess = { _ ->
                userViewModel.getProfileImage(
                    onSuccess = { fetchedUrl ->
                        imageUrlState.value = fetchedUrl
                        Toast.makeText(
                            context,
                            "Imagen Actualizada",
                            Toast.LENGTH_LONG
                        ).show()
                    },
                    onFailure = { _ ->
                        Toast.makeText(
                            context,
                            "Error al bajar la imagen",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                )
            }, onFailure = { _ ->
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
                userViewModel.uploadProfileImage(uri, onSuccess = { newImageUrl ->
                    userViewModel.getProfileImage(
                        onSuccess = { fetchedUrl ->
                            imageUrlState.value = fetchedUrl
                            Toast.makeText(
                                context,
                                "Imagen Actualizada",
                                Toast.LENGTH_LONG
                            ).show()
                        },
                        onFailure = { _ ->
                            Toast.makeText(
                                context,
                                "Error al bajar la imagen",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    )
                }, onFailure = { _ ->
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
        userViewModel.getProfileEspecificImage(parentId.lowercase() + "@youthconnect.com",
            onSuccess = { url ->
                imageUrlState.value = url
            },
            onFailure = { _ ->
                // Manejar el error, por ejemplo, mostrar un mensaje
            }
        )
    }


    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted: Boolean ->
            if (isGranted) {
                // Permiso concedido, proceder con la acción
                imageUri = userViewModel.createImageUri(context)
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
                            imageUri = userViewModel.createImageUri(context)
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
            Column(
                modifier = Modifier
                    .padding(15.dp)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center
            ) {

                Column(horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()) {
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
                                if(currentUser == parent?.id){
                                    showImagePickerDialog = true
                                }

                            },
                        contentScale = ContentScale.Crop
                    )
                    Row(verticalAlignment = Alignment.CenterVertically){
                        parent?.fullName?.let {
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
                            parent?.let {
                                ModifyUsers(onDismiss = { editUser = false },
                                    it, navController
                                )
                            }
                        }


                    }






                    Spacer(modifier = Modifier.size(40.dp))
                }



                Column ( modifier = Modifier.fillMaxWidth()
                ){
                    if(currentUser == parent?.id){
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

                                .clickable { loginViewModel.signOut()
                                    navController.navigate("firstScreens")}
                        )
                    }

                    Text(
                        text = "Mis hijos/hijas",
                        style = TextStyle(
                            fontSize = 30.sp,
                            fontFamily = FontFamily(Font(R.font.annie_use_your_telescope)),
                            fontWeight = FontWeight(400),
                            color = Color(0xFF000000),
                            letterSpacing = 0.9.sp,
                        ), modifier = Modifier
                            .padding(start = 15.dp, top = 10.dp)
                    )



                    LazyColumn(modifier = Modifier.padding(vertical = 4.dp)) {
                        items(items = children) { item ->
                            if (item != null) {
                                MyChildren(navController = navController, item)
                            }
                        }
                    }
                }

            }


        }

    }


