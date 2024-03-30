package com.example.youthconnect.View.BottomNavigationScreens

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.youthconnect.ViewModel.ChatViewModel
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material.icons.filled.MarkEmailUnread
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.MarkEmailUnread
import androidx.compose.material3.Card
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.android.InternalPlatformTextApi
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.youthconnect.Model.Object.UserData
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.youthconnect.Model.Constants
import com.example.youthconnect.ViewModel.UserViewModel
import com.example.youthconnect.ui.theme.Green
import com.example.youthconnect.ui.theme.Red
import com.example.youthconnect.ui.theme.Violet
import com.example.youthconnect.ui.theme.Yellow


@Composable
fun HomeScreen(
    navHostController: NavHostController
) {

    val ChatViewModel : ChatViewModel = hiltViewModel()
    var allUsers by remember { mutableStateOf<List<UserData?>>(emptyList()) }
    var user by remember { mutableStateOf<String>("") }
    var userType by remember { mutableStateOf<String>("") }
    val UserViewModel : UserViewModel = hiltViewModel()

    LaunchedEffect(Unit) {
        try {
            val currentUser = UserViewModel.getCurrentUser().toString()
            val userTypeResult = UserViewModel.getUserType(currentUser).toString()
            user = currentUser
            userType = userTypeResult
            allUsers = ChatViewModel.getAllUsers(userTypeResult) ?: emptyList()
        } catch (e: Exception) {
            Log.e("Firestore", "Error en ChildList", e)
        }
    }
    val unseenMessagesState = ChatViewModel.getUnseenMessages().observeAsState(initial = emptyList())

    val unseenMessages by remember {
        unseenMessagesState
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



        Column(modifier = Modifier.fillMaxHeight()) {
            Column(horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(10.dp)) {

                val textState = remember { mutableStateOf(TextFieldValue(""))}

                SearchView(state= textState, placeHolder= "Search here...", modifier = Modifier)

                val searchedText = textState.value.text


                LazyColumn(modifier = Modifier.padding(10.dp)) {
                    items(items = allUsers.filter {
                        it?.userName?.contains(searchedText, ignoreCase = true) ?: false
                    }, key = { it?.userId ?: "" }) { item ->
                        if (item != null) {

                            UserEachRow(
                                person = item,
                                unseenMessages = unseenMessages
                            ) {

                                ChatViewModel.markMessagesAsSeen(ChatViewModel.generateChatId(item.userId,user))
                                navHostController.navigate("chatscreen/${item.userId}")
                            }
                        }
                    }
                }

            }

        }

    }


}

@OptIn(InternalPlatformTextApi::class)
@Composable
fun UserEachRow(
    person: UserData,
    unseenMessages: List<String>,
    onClick: () -> Unit
) {

    val UserViewModel : UserViewModel = hiltViewModel()
    val imageUrlState = remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        UserViewModel.getProfileEspecificImage(person.userId.lowercase() + "@youthconnect.com",
            onSuccess = { url ->
                imageUrlState.value = url
            },
            onFailure = { exception ->
                print("Child not found")
            }
        )
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(4.dp)

    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
        ) {

            AsyncImage(
                model = imageUrlState.value,
                contentDescription = "Profile Picture",
                modifier = Modifier
                    .size(50.dp)

                    .padding(4.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )

            if (person.userId in unseenMessages) {
                Icon(
                    imageVector = Icons.Outlined.MarkEmailUnread,
                    contentDescription = "Recieved",
                    tint = Red,
                    modifier = Modifier.size(20.dp)
                )
            }

            Text(
                text = person.userName ?: "",
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = Color.Black
                ),
                modifier = Modifier
                    .padding(start = 10.dp)
                    .align(CenterVertically)
            )





        }


    }
}






@SuppressLint("UnnecessaryComposedModifier")
fun Modifier.noRippleEffect(onClick: () -> Unit) = composed {
    clickable(
        interactionSource = MutableInteractionSource(),
        indication = null
    ) {
        onClick()
    }
}



@SuppressLint("SuspiciousIndentation")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun ChatScreen(recipientUserId: String, navHostController: NavController, chatViewModel: ChatViewModel = hiltViewModel()) {

    val UserViewModel : UserViewModel = hiltViewModel()
   // var user: UserData? = null
    val userState = remember { mutableStateOf<UserData?>(null) }

    LaunchedEffect(UserViewModel) {
        val user = UserViewModel.getUserById(recipientUserId)

        userState.value = user
    }
    Log.i("USER",userState.value?.profilePictureUrl.toString())


    initRecipientUserId(recipientUserId, chatViewModel)

    val message: String by chatViewModel.message.observeAsState(initial = "")
    val messages: List<Map<String, Any>> by chatViewModel.messages.observeAsState(
        initial = emptyList<Map<String, Any>>().toMutableList()
    )

    Box(
        modifier = Modifier.fillMaxSize(),
      //  contentAlignment = Alignment.TopCenter, // Alinea el texto en la parte superior y central
    ) {

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
          //  verticalArrangement = Arrangement.Bottom
        ) {

            Box(
                //modifier = Modifier.fillMaxSize()
                contentAlignment = Alignment.TopCenter
            ) {
                userState.value?.let { Recipient(userData = it, navController = navHostController) }
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(weight = 0.85f, fill = true),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp),
                reverseLayout = true
            ) {
                items(messages) { message ->
                    val isCurrentUser = message[Constants.IS_CURRENT_USER] as Boolean

                    SingleMessage(
                        message = message[Constants.MESSAGE].toString(),
                        isCurrentUser = isCurrentUser
                    )
                }

            }
            Box(
               // modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.BottomCenter
            ) {
                OutlinedTextField(
                    value = message,
                    onValueChange = {
                        chatViewModel.updateMessage(it)
                    },
                    label = {
                        Text(
                            "Type Your Message"
                        )
                    },
                    maxLines = 5,
                    modifier = Modifier
                        .padding(horizontal = 15.dp, vertical = 1.dp)
                        .fillMaxWidth(),
                       // .weight(weight = 0.09f, fill = true),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text
                    ),
                    singleLine = true,
                    trailingIcon = {
                        IconButton(
                            onClick = {
                                chatViewModel.addMessage(recipientUserId)
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Send,
                                contentDescription = "Send Button",
                                tint = Violet
                            )
                        }
                    }
                )
            }
        }
    }






}

@Composable
fun Recipient(userData: UserData, navController : NavController){

    val UserViewModel : UserViewModel = hiltViewModel()



    val imageUrlState = remember { mutableStateOf("") }
    LaunchedEffect(Unit) {
        UserViewModel.getProfileEspecificImage(userData.userId.lowercase() + "@youthconnect.com",
            onSuccess = { url ->
                imageUrlState.value = url
            },
            onFailure = { exception ->
                // Manejar el error, por ejemplo, mostrar un mensaje
            }
        )
    }

    var userType by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        userType = UserViewModel.getUserType(userData.userId).toString()
    }


    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                if (userType == "Child") {
                    navController.navigate("child_profile_screen/${userData.userId}")
                } else if (userType == "Parents") {
                    navController.navigate("parent_profile_screen/${userData.userId}")
                } else {
                    navController.navigate("instructor_profile_screen/${userData.userId}")
                }
            }
            .padding(4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {




                AsyncImage(
                    model = imageUrlState.value,
                    contentDescription = "Profile Picture",
                    modifier = Modifier
                        .size(50.dp)
                        .border(
                            BorderStroke(4.dp, remember {
                                Brush.sweepGradient(
                                    listOf(
                                        Green, com.example.youthconnect.ui.theme.Red
                                    )
                                )
                            }),
                            CircleShape
                        )
                        .padding(4.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )


            Text(
                text = userData.userName.toString(),
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = Color.Black // Establece el color en rojo si el mensaje está marcado como visto
                ),
                modifier = Modifier
                    .padding(start = 16.dp, end = 16.dp)
                    .weight(1f),
                textAlign = TextAlign.Start
            )

        }
    }
}


fun initRecipientUserId(recipientUserId: String, chatViewModel: ChatViewModel) {

    chatViewModel.initRecipientUserId(recipientUserId)
}