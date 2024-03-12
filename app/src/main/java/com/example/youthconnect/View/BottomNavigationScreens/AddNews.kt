package com.example.youthconnect.View.BottomNavigationScreens

import android.annotation.SuppressLint
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts.GetContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box

import androidx.compose.foundation.layout.Column

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.PermIdentity
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarResult.ActionPerformed
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Newspaper
import androidx.compose.material.icons.filled.QuestionAnswer
import androidx.compose.material.icons.filled.QuestionMark
import androidx.compose.material.icons.filled.Title
import androidx.compose.material.icons.outlined.Newspaper
import androidx.compose.material.icons.outlined.Quiz
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.youthconnect.Model.Constants.ALL_IMAGES
import com.example.youthconnect.Model.Object.Instructor
import com.example.youthconnect.Model.Object.News
import com.example.youthconnect.Model.Object.Question
import com.example.youthconnect.Model.Sealed.Response
import com.example.youthconnect.R
import com.example.youthconnect.ViewModel.NewsViewModel
import com.example.youthconnect.ViewModel.QuizViewModel
import com.example.youthconnect.ViewModel.signUpViewModel
import kotlinx.coroutines.launch
import java.util.UUID


@SuppressLint("SuspiciousIndentation")
@Composable
fun AddNews(onDismiss: () -> Unit) {
    val newsViewModel: NewsViewModel = hiltViewModel()

    val id = UUID.randomUUID().toString()
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var imageUri by remember {
        mutableStateOf(newsViewModel.addImageToStorageResponse as? Uri)
    }
    val listState = rememberLazyListState()

    val galleryLauncher = rememberLauncherForActivityResult(contract = GetContent()) { selectedUri ->
        selectedUri?.let {
            imageUri = it // Actualizar la URI de la imagen seleccionada
            newsViewModel.addNewsToStorage(selectedUri, id) // Podrías mover esta llamada al ViewModel si es necesario
        }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        icon = { Icons.Outlined.Newspaper },
        title = { Text(text = "Inserte una nueva noticia") },
        text = {
            LazyColumn(state = listState) {
                item {
                    CustomOutlinedTextField(
                        value = title,
                        onValueChange = { title = it },
                        label = "Title",
                        leadingIconImageVector = Icons.Default.Title
                    )

                    CustomOutlinedTextField(
                        value = description,
                        onValueChange = { description = it },
                        label = "Description",
                        leadingIconImageVector = Icons.Default.Description
                    )


                    Button(
                        onClick = { galleryLauncher.launch(ALL_IMAGES) }
                    ) {
                        Text("Select Image")
                    }

                    // Mostrar la imagen seleccionada
                    imageUri?.let { uri ->
                        Image(
                            painter = rememberImagePainter(uri),
                            contentDescription = null,
                            modifier = Modifier
                                .size(200.dp)
                                .padding(vertical = 8.dp)
                                .clip(shape = RoundedCornerShape(8.dp))

                        )
                    }
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    val news = News(id, title, description)
                    newsViewModel.addNewsToDatabase(imageUri.toString(), news)

                    title = ""
                    description = ""
                    onDismiss()
                }
            ) {
                Text("Confirm")
            }
        },
        dismissButton = {
            TextButton(
                onClick = { onDismiss() }
            ) {
                Text("Dismiss")
            }
        }
    )
}

