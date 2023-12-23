package com.example.youthconnect.View.BottomNavigationScreens

import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color.Companion.Green
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.youthconnect.Model.Sealed.Response.Loading
import com.example.youthconnect.Model.Sealed.Response.Success
import com.example.youthconnect.Model.Sealed.Response.Failure
import com.example.youthconnect.ViewModel.NewsViewModel

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
    NewsViewModel : NewsViewModel = hiltViewModel(),
    addImageToDatabase : (downloadUrl: Uri) -> Unit
){
    when (val addImageToStorageResponse =NewsViewModel.addImageToStorageResponse){
        is Loading -> ProgressBar()
        is Success -> addImageToStorageResponse.data?.let { downloadUrl->
            LaunchedEffect(downloadUrl){
                addImageToDatabase(downloadUrl)
            }
        }
        is Failure -> print(addImageToStorageResponse.e)
    }
}


@Composable
fun AddImageToDatabase(
    ImageViewModel: NewsViewModel = hiltViewModel(),
    showSnackBar : (isNewsAddedToDatabase : Boolean)->Unit
){
    when (val addImageToStorageResponse = ImageViewModel.addImageToDatabaseResponse){
        is Loading -> ProgressBar()
        is Success -> addImageToStorageResponse.data?.let {isImageAddedToDatabase ->
            LaunchedEffect(isImageAddedToDatabase) {
                showSnackBar(isImageAddedToDatabase)
            }
        }
        is Failure -> print(addImageToStorageResponse.e)
    }
}


@Composable
fun GetImageFromDatabase(
    ImageViewModel: NewsViewModel = hiltViewModel(),
    createImageContent : @Composable (imageUrl: String) -> Unit,

){
    when (val getImageFromDatabaseResponse = ImageViewModel.getImageFromDatabaseResponse){
        is Loading -> ProgressBar()
        is Success -> getImageFromDatabaseResponse.data?.let {imageUrl ->
            createImageContent(imageUrl)
        }
        is Failure -> print(getImageFromDatabaseResponse.e)
    }
}

@Composable
fun ImageContent(
    imageUrl: String
){
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ){
        AsyncImage(model = ImageRequest.Builder(LocalContext.current)
            .data(imageUrl)
            .crossfade(true)
            .build(),
            contentDescription = null,
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
                .clip(CircleShape))

    }
}