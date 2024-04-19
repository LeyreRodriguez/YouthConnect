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
import androidx.compose.material.icons.outlined.Checklist
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
import com.example.youthconnect.Model.Object.Instructor
import com.example.youthconnect.Model.Object.Parent
import com.example.youthconnect.Model.Object.UserData
import com.example.youthconnect.R
import com.example.youthconnect.View.Components.EditIcon
import com.example.youthconnect.View.Components.ProfilePicture
import com.example.youthconnect.View.OverlaysAndMore.ModifyUsers
import com.example.youthconnect.View.QR.DisplayQRCode
import com.example.youthconnect.ViewModel.UserViewModel
import com.example.youthconnect.ui.theme.Green
import com.example.youthconnect.ui.theme.Red

@Composable
fun ChildProfileScreen(
    childId: String,
    modifier: Modifier = Modifier.background(color = Color.White),
    navController: NavController,
){

    var child by remember { mutableStateOf<Child?>(null) }
    var parents by remember { mutableStateOf<List<Parent?>>(emptyList()) }
    var currentUser by remember { mutableStateOf<String?>(null) }
    val userViewModel : UserViewModel = hiltViewModel()

    val userState = remember { mutableStateOf<UserData?>(null) }

    val documentExists = remember { mutableStateOf("-1") }
    var result by remember { mutableStateOf<String?>("") }

    var user by remember { mutableStateOf<String?>("") }



    var currentUserType by remember { mutableStateOf("") }

    LaunchedEffect(userViewModel) {
        val user = userViewModel.getUserById(childId)
        currentUser = userViewModel.getCurrentUser()
        currentUserType = currentUser?.let { userViewModel.getUserType(it).toString() }.toString()
        userState.value = user
    }

    LaunchedEffect(userViewModel) {
        try {
            child = userViewModel.getCurrentChildById(childId)
            parents = child?.parentId?.let { userViewModel.getParentsByParentsID(it) }!!
            user = userViewModel.getCurrentUser()
            result = user?.let { userViewModel.findDocument(it) }

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

        ChildInfo(
            childId = childId,
            child = child ,
            currentUser = currentUser ,
            currentUserType = currentUserType,
            parents = parents,
            documentExists = documentExists.value,
            navController = navController
        )

        }


    }


@Composable

fun ChildInfo(childId: String, child: Child?, currentUser: String?, currentUserType :String?, parents : List<Parent?>, documentExists : String, navController: NavController){
    val userViewModel : UserViewModel = hiltViewModel()
    val loginViewModel : LoginViewModel = hiltViewModel()
    var showDialog by remember { mutableStateOf(false)  }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(15.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center
    ) {

        ProfilePicture(userViewModel = userViewModel, userId = childId, user = child, currentUser = currentUser)

        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center)
        {
            child?.fullName?.let {
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

            EditIcon(currentUserType = currentUserType, user = child, navController = navController)





            if (child?.state ?: "" == false){
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

        child?.course?.let {
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

        child?.observations?.let {
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

        if(currentUser == child?.id){
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


                LazyColumn(modifier = Modifier.padding(vertical = 4.dp)) {
                    items(items = parents) { item ->
                        item?.fullName?.let { Text(text = it) }
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

                LazyColumn(modifier = Modifier.padding(vertical = 4.dp)) {
                    items(items = parents) { item ->
                        item?.phoneNumber?.let { Text(text = it) }
                    }
                }
            }


        }

        Spacer(modifier = Modifier.size(40.dp))
        DisplayQRCode(childId)

        if (documentExists == "0") {

            OutlinedButton(onClick = { showDialog = true }) {
                Text("Ver lista de asistencia")
            }

        }
        if (showDialog) {
            SeeRollCall(onDismiss = { showDialog = false }, childId)
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




@SuppressLint("SuspiciousIndentation")
@Composable
fun SeeRollCall(onDismiss: () -> Unit, childId : String ) {


    val userViewModel : UserViewModel = hiltViewModel()

    var rollCall by remember { mutableStateOf<List<String>?>(emptyList()) }


    LaunchedEffect(userViewModel) {
        try {
            rollCall = userViewModel.getRollState(childId)
        } catch (e: Exception) {
            Log.e("Firestore", "Error en ChildList", e)
        }
    }


    AlertDialog(
        onDismissRequest = onDismiss,
        icon = { Icons.Outlined.Checklist },
        title = { Text(text = "Dias asistidos a grupos de fe") },
        text = {
            LazyColumn(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                items(rollCall!!) { item ->
                    Text(text = item)
                }
            }
        },
        confirmButton = {
            androidx.compose.material3.TextButton(
                onClick = {
                    onDismiss()
                }
            ) {
                Text("Cerrar")
            }
        }
    )
}