package com.example.youthconnect.View.Components
import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.TextButton
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import coil.compose.AsyncImage
import com.example.youthconnect.Model.Object.Child
import com.example.youthconnect.ViewModel.UserViewModel
import com.example.youthconnect.ui.theme.Green
import com.example.youthconnect.ui.theme.Red
@Composable
fun ProfilePicture(userViewModel : UserViewModel, userId: String, user: Any?, currentUser : String?){
    val context = LocalContext.current
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var showImagePickerDialog by remember { mutableStateOf(false) }
    val imageUrlState = remember { mutableStateOf("") }
    val imagePickerLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        handleImageResult(uri, userViewModel, imageUrlState)
    }

    val takePictureLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        if (success) {
            // Aquí manejas la imagen capturada usando imageUri
            imageUri?.let { uri ->
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
    }
    loadProfileImage(userViewModel = userViewModel, userId = userId, imageUrlState = imageUrlState)

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        handlePermissionResult(isGranted, userViewModel, context, takePictureLauncher)
    }


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

    when(user) {
        is Child -> {
            AsyncImage(
                model = imageUrlState.value,
                contentDescription = "Profile Picture",
                modifier = Modifier
                    .size(150.dp)
                    .border(
                        BorderStroke(
                            4.dp,
                            SolidColor(
                                if (user?.goOutAlone == true) Green else Red)
                        ),
                        CircleShape
                    )
                    .padding(4.dp)
                    .clip(CircleShape)
                    .clickable {

                        if (currentUser == user?.id) {
                            showImagePickerDialog = true
                        }
                    },
                contentScale = ContentScale.Crop
            )
        } else -> {
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
                    showImagePickerDialog = true
                },
            contentScale = ContentScale.Crop
        )
    }
    }
}

private fun handlePermissionResult(
    isGranted: Boolean,
    userViewModel: UserViewModel,
    context: Context,
    takePictureLauncher: ManagedActivityResultLauncher<Uri, Boolean>
) {
    if (isGranted) {
        val imageUri = userViewModel.createImageUri(context)
        imageUri?.let {
            takePictureLauncher.launch(it)
        }
    } else {
        Toast.makeText(
            context,
            "Permiso denegado",
            Toast.LENGTH_LONG
        ).show()

    }
}
fun handleImageResult(uri: Uri?, userViewModel: UserViewModel, imageUrlState: MutableState<String>) {
    uri?.let {
        userViewModel.uploadProfileImage(uri, onSuccess = { _ ->
            userViewModel.getProfileImage(
                onSuccess = { fetchedUrl ->
                    imageUrlState.value = fetchedUrl
                },
                onFailure = { _ ->
                }
            )
        }, onFailure = { _ ->
        })
    }
}
@Composable
fun loadProfileImage(
    userViewModel: UserViewModel,
    userId: String,
    imageUrlState: MutableState<String>
) {
    LaunchedEffect(Unit) {
        userViewModel.getProfileEspecificImage(userId.lowercase() + "@youthconnect.com",
            onSuccess = { url ->
                imageUrlState.value = url
            },
            onFailure = { _ ->
                // Handle error
            }
        )
    }
}