package com.example.youthconnect.View.BottomNavigationScreens

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.material.Divider
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.outlined.ChildCare
import androidx.compose.material.icons.outlined.FamilyRestroom
import androidx.compose.material.icons.outlined.MarkEmailUnread
import androidx.compose.material.icons.outlined.School
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.android.InternalPlatformTextApi
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.youthconnect.Model.Object.UserData
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.youthconnect.Model.Constants
import com.example.youthconnect.View.Components.SingleMessage
import com.example.youthconnect.View.OverlaysAndMore.SearchView
import com.example.youthconnect.ViewModel.UserViewModel
import com.example.youthconnect.ui.theme.Green
import com.example.youthconnect.ui.theme.Red
import com.example.youthconnect.ui.theme.Violet


@Composable
fun HomeScreen(
    navHostController: NavHostController
) {

    val chatViewModel : ChatViewModel = hiltViewModel()
    var allUsers by remember { mutableStateOf<List<UserData?>>(emptyList()) }
    var user by remember { mutableStateOf<String>("") }
    var userType by remember { mutableStateOf<String>("") }
    val userViewModel : UserViewModel = hiltViewModel()


    LaunchedEffect(Unit) {
        try {
            val currentUser = userViewModel.getCurrentUser().toString()
            val userTypeResult = userViewModel.getUserType(currentUser).toString()
            user = currentUser
            userType = userTypeResult
            allUsers = chatViewModel.getAllUsers(userTypeResult) ?: emptyList()
        } catch (e: Exception) {
            Log.e("Firestore", "Error en ChildList", e)
        }
    }


    val unseenMessagesState = chatViewModel.getUnseenMessages().observeAsState(initial = emptyList())

    val unseenMessages by remember {
        unseenMessagesState
    }

    Box(
        modifier = Modifier.fillMaxSize(),
    ) {

        Column(modifier = Modifier.fillMaxHeight()) {
            Column(horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(10.dp)) {

                val textState = remember { mutableStateOf(TextFieldValue(""))}

                SearchView(state= textState, placeHolder= "Buscar aqui...", modifier = Modifier)

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

                                chatViewModel.markMessagesAsSeen(chatViewModel.generateChatId(item.userId,user))
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

    val userViewModel : UserViewModel = hiltViewModel()
    val imageUrlState = remember { mutableStateOf("") }
    var userType by remember { mutableStateOf("") }
    var currentUserType by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        userType = userViewModel.getUserType(person.userId).toString()
        currentUserType = userViewModel.getCurrentUser()
            ?.let { userViewModel.getUserType(it).toString() }.toString()
        userViewModel.getProfileEspecificImage(person.userId.lowercase() + Constants.ROUTE,
            onSuccess = { url ->
                imageUrlState.value = url
            },
            onFailure = { _ ->
                print("Child not found")
            }
        )
    }


    LaunchedEffect(Unit) {
        userViewModel.getProfileEspecificImage(person.userId.lowercase() + Constants.ROUTE,
            onSuccess = { url ->
                imageUrlState.value = url
            },
            onFailure = { _ ->
                print("Child not found")
            }
        )
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(4.dp),

        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        )

    ) {

        Row (modifier = Modifier.fillMaxSize()){
            Row(
                modifier = Modifier
                    .padding(16.dp),
            ) {

                ProfileImage(imageUrlState.value)

                Column(){
                    UserTypeIcon(userType, currentUserType)
                    if (person.userId in unseenMessages) {
                        Icon(
                            imageVector = Icons.Outlined.MarkEmailUnread,
                            contentDescription = "Recibido",
                            tint = Red,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                }



                val userName = person.userName ?: ""
                val maxWordsPerLine = 3

                val lines = mutableListOf<String>()
                var currentLine = ""
                var wordsCount = 0

                userName.split(" ").forEach { word ->
                    if (wordsCount < maxWordsPerLine) {
                        currentLine += "$word "
                        wordsCount++
                    } else {
                        lines.add(currentLine.trim())
                        currentLine = "$word "
                        wordsCount = 1
                    }
                }

                DisplayName(userName)

            }

        }
        Divider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            color = Color.Gray,
            thickness = 1.dp
        )

    }
}
@Composable
private fun UserTypeIcon(userType: String, currentUserType: String) {
    if (currentUserType == "Instructor") {
        val iconResource = when (userType) {
            "Child" -> Icons.Outlined.ChildCare
            "Parents" -> Icons.Outlined.FamilyRestroom
            "Instructor" -> Icons.Outlined.School
            else -> null
        }
        iconResource?.let {
            Icon(
                imageVector = it,
                contentDescription = userType,
                tint = Color.Black,
                modifier = Modifier
                    .size(20.dp)

            )
        }
    }
}
@Composable
private fun ProfileImage(url: String) {
    AsyncImage(
        model = url,
        contentDescription = "Foto de perfil",
        modifier = Modifier
            .size(50.dp)
            .padding(4.dp)
            .clip(CircleShape),
        contentScale = ContentScale.Crop
    )
}

@Composable
private fun DisplayName(userName: String) {
    val maxWordsPerLine = 3
    val lines = userName.split(" ").chunked(maxWordsPerLine) { it.joinToString(" ") }

    if(lines.size < 1){
        Column(
            verticalArrangement = Arrangement.Center
        ) {
            lines.forEach { line ->
                Text(
                    text = line,
                    style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = Color.Black
                    ),
                    modifier = Modifier.padding(start = 10.dp)
                )
            }
        }
    }else{

        Column(
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = userName,
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = Color.Black
                ),
                modifier = Modifier.padding(start = 10.dp)
            )
        }


    }


}




@SuppressLint("SuspiciousIndentation")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun ChatScreen(recipientUserId: String, navHostController: NavController, chatViewModel: ChatViewModel = hiltViewModel()) {

    val userViewModel : UserViewModel = hiltViewModel()
    val userState = remember { mutableStateOf<UserData?>(null) }

    LaunchedEffect(userViewModel) {
        val user = userViewModel.getUserById(recipientUserId)

        userState.value = user
    }


    initRecipientUserId(recipientUserId, chatViewModel)

    val message: String by chatViewModel.message.observeAsState(initial = "")
    val messages: List<Map<String, Any>> by chatViewModel.messages.observeAsState(
        initial = emptyList<Map<String, Any>>().toMutableList()
    )

    Box(
        modifier = Modifier.fillMaxSize(),
    ) {

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {

            Box(
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
                contentAlignment = Alignment.BottomCenter
            ) {
                OutlinedTextField(
                    value = message,
                    onValueChange = {
                        chatViewModel.updateMessage(it)
                    },
                    label = {
                        Text(
                            "Escribe tu mensaje"
                        )
                    },
                    maxLines = 5,
                    modifier = Modifier
                        .padding(horizontal = 15.dp, vertical = 1.dp)
                        .fillMaxWidth(),
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
                                contentDescription = "Boton de enviar",
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

    val userViewModel : UserViewModel = hiltViewModel()



    val imageUrlState = remember { mutableStateOf("") }
    LaunchedEffect(Unit) {
        userViewModel.getProfileEspecificImage(userData.userId.lowercase() + Constants.ROUTE,
            onSuccess = { url ->
                imageUrlState.value = url
            },
            onFailure = { _ ->
            }
        )
    }

    var userType by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        userType = userViewModel.getUserType(userData.userId).toString()
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
                    contentDescription = "Foto de perfil",
                    modifier = Modifier
                        .size(50.dp)
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
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )


            Text(
                text = userData.userName.toString(),
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = Color.Black
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