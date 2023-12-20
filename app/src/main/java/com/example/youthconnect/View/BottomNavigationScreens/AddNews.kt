package com.example.youthconnect.View.BottomNavigationScreens

import android.app.Notification.Action
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.contract.ActivityResultContracts.GetContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Password
import androidx.compose.material.icons.filled.PermIdentity
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarResult.ActionPerformed
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.youthconnect.Model.Constants.ALL_IMAGES
import com.example.youthconnect.Model.Object.Instructor
import com.example.youthconnect.Model.Object.News
import com.example.youthconnect.R
import com.example.youthconnect.ViewModel.ImageViewModel
import com.example.youthconnect.ViewModel.NewsViewModel
import com.example.youthconnect.ViewModel.signUpViewModel
import kotlinx.coroutines.launch
import java.util.UUID

@Composable
fun AddNews() {

    val ImageViewModel : ImageViewModel = hiltViewModel()

    val scaffoldState = rememberScaffoldState()
    val coroutineScope = rememberCoroutineScope()
    val id = UUID.randomUUID().toString()

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = GetContent()
    ){ imageUri ->
        imageUri?.let {
            ImageViewModel.addNewsToStorage(imageUri,id)
        }
    }

    var Title  by remember { mutableStateOf("") }
    var Description  by remember { mutableStateOf("") }


    Scaffold(
        content = { padding ->
            Box(modifier = Modifier
                .fillMaxSize()
                .padding(padding))
            {

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CustomOutlinedTextField(
                        value = Title,
                        onValueChange = { Title = it },
                        label = "Title",
                        leadingIconImageVector = Icons.Default.PermIdentity
                    )

                    CustomOutlinedTextField(
                        value = Description,
                        onValueChange = { Description = it },
                        label = "Description",
                        leadingIconImageVector = Icons.Default.CreditCard
                    )


                }

                OpenGallery(
                    openGallery = {
                        galleryLauncher.launch(ALL_IMAGES)
                    }
                )

            }
        },
        scaffoldState = scaffoldState

    )




    val news : News = News(
        id,
        Title,Description
    )

    AddImageToStorage(
        addImageToDatabase = { downloadUrl ->
            ImageViewModel.addNewsToDatabase(downloadUrl, news)
        }
    )


    fun showSnackBar() = coroutineScope.launch {
        val result = scaffoldState.snackbarHostState.showSnackbar(
            message = "Noticia correctamente agregada"
        )
        if (result == ActionPerformed){
            ImageViewModel.getNewsImageFromDatabase()
        }
    }

    AddNewsToDatabase(
        showSnackBar = { isNewsAddedToDatabase ->
            if(isNewsAddedToDatabase){
                showSnackBar()
                Title =""
                Description =""
            }
        }
    )
}



