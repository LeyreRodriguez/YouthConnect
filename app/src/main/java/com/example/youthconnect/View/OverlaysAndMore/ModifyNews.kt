package com.example.youthconnect.View.OverlaysAndMore

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Title
import androidx.compose.material.icons.outlined.Newspaper
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.youthconnect.Model.Constants
import com.example.youthconnect.Model.Enum.NavScreen
import com.example.youthconnect.Model.Object.Child
import com.example.youthconnect.Model.Object.News
import com.example.youthconnect.Model.Sealed.Response
import com.example.youthconnect.View.Authentication.CustomOutlinedTextField
import com.example.youthconnect.ViewModel.NewsViewModel
import com.example.youthconnect.ViewModel.UserViewModel

@Composable
fun ModifyNews(item : News, navController : NavController, onDismiss: () -> Unit) {
    val newsViewModel: NewsViewModel = hiltViewModel()

    var imageUrlState by remember { mutableStateOf("") }
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }


    title = item.title
    description = item.description
    imageUrlState = item.image.toString()

    var editedTitle by remember {
        mutableStateOf((item as? News)?.title ?: "")
    }
    var editedDescription by remember {
        mutableStateOf((item as? News)?.description ?: "")
    }


    val listState = rememberLazyListState()
    val mcontext = LocalContext.current

    AlertDialog(
        onDismissRequest = onDismiss,
        icon = { Icons.Outlined.Newspaper },
        title = { Text(text = "Inserte una nueva noticia") },
        text = {
            LazyColumn(state = listState) {
                item {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CustomOutlinedTextField(
                            value = editedTitle,
                            onValueChange = { editedTitle = it },
                            label = "Titulo",
                            leadingIconImageVector = Icons.Default.Title
                        )

                        CustomOutlinedTextField(
                            value = editedDescription,
                            onValueChange = { editedDescription = it },
                            label = "Descripcion",
                            leadingIconImageVector = Icons.Default.Description
                        )


                    }



                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {


                    newsViewModel.updateNews(
                        item.copy(title= editedTitle, description = editedDescription, image = item.image)
                    )


                    navController.navigate("news_details_screen/${item.id}")
                 //onDismiss()
                }
            ) {
                Text("Confirmar")
            }
        },
        dismissButton = {
            TextButton(
                onClick = { onDismiss() }
            ) {
                Text("Cancelar")
            }
        }
    )
}