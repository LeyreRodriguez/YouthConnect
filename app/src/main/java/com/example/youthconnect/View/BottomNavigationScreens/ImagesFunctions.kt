package com.example.youthconnect.View.BottomNavigationScreens

import android.net.Uri
import android.widget.ProgressBar
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.youthconnect.Model.Sealed.Response
import com.example.youthconnect.ViewModel.ImageViewModel

@Composable
fun OpenGallery(
    openGallery: ()->Unit
){
    Box(modifier = Modifier
        .fillMaxSize()
        .padding(64.dp),
        contentAlignment = Alignment.BottomCenter
    ){
        Button(onClick = openGallery) {
            Text(text = "Select Image",
                fontSize = 18.sp)

        }
    }
}


@Composable
fun AddImageToStorage(
    ImageViewModel : ImageViewModel = hiltViewModel(),
    addImageToDatabase : (downloadUrl: Uri) -> Unit
){
    when (val addImageToStorageResponse =ImageViewModel.addImageToStorageResponse){
        is Response.Loading -> ProgressBar()
        is Response.Success -> addImageToStorageResponse.data?.let { downloadUrl->
            LaunchedEffect(downloadUrl){
                addImageToDatabase(downloadUrl)
            }
        }
        is Response.Failure -> print(addImageToStorageResponse.e)
    }
}


@Composable
fun AddNewsToDatabase(
    ImageViewModel: ImageViewModel = hiltViewModel(),
    showSnackBar : (isNewsAddedToDatabase : Boolean)->Unit
){
    when (val addImageToStorageResponse = ImageViewModel.addImageToDatabaseResponse){
        is Response.Loading -> ProgressBar()
        is Response.Success -> addImageToStorageResponse.data?.let {isNewsAddedToDatabase ->
            LaunchedEffect(isNewsAddedToDatabase) {
                showSnackBar(isNewsAddedToDatabase)
            }
        }
        is Response.Failure -> print(addImageToStorageResponse.e)
    }
}