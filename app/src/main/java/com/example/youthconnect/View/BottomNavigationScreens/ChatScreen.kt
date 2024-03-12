package com.example.youthconnect.View.BottomNavigationScreens

import android.annotation.SuppressLint
import android.util.Log
import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
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
import com.example.youthconnect.R
import com.example.youthconnect.ViewModel.ChatViewModel
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Divider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.TopCenter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.android.InternalPlatformTextApi
import androidx.compose.ui.text.android.animation.SegmentType
import androidx.compose.ui.unit.Dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.youthconnect.Model.Enum.NavScreen
import com.example.youthconnect.Model.Object.UserData
import androidx.compose.material3.Divider
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment.Companion.BottomCenter
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.Color.Companion.DarkGray
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.youthconnect.Model.Constants
import com.example.youthconnect.Model.Object.Child
import com.example.youthconnect.Model.Object.Parent
import com.example.youthconnect.ViewModel.UserViewModel
import com.example.youthconnect.ui.theme.Line
import com.example.youthconnect.ui.theme.Yellow
import com.google.firebase.Firebase
import com.google.firebase.auth.auth


@Composable
fun HomeScreen(
    navHostController: NavHostController
) {

    val ChatViewModel : ChatViewModel = hiltViewModel()
    var allUsers by remember { mutableStateOf<List<UserData?>>(emptyList()) }

    var user by remember { mutableStateOf<String>("") }

    val UserViewModel : UserViewModel = hiltViewModel()

    LaunchedEffect(UserViewModel) {
        try {
            user = UserViewModel.getCurrentUser().toString()

        } catch (e: Exception) {
            Log.e("Firestore", "Error en ChildList", e)
        }
    }

    LaunchedEffect(Unit) {
        allUsers = ChatViewModel.getAllUsers()!!

    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 30.dp)
        ) {
           // HeaderOrViewStory(allUsers)
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Color.White, RoundedCornerShape(
                            topStart = 30.dp, topEnd = 30.dp
                        )
                    )
            ) {
                BottomSheetSwipeUp(
                    modifier = Modifier
                        .align(TopCenter)
                        .padding(top = 15.dp)
                )
                LazyColumn(
                    modifier = Modifier.padding(bottom = 15.dp, top = 30.dp)
                ) {
                    items(allUsers, key = { it?.userId ?: "" }) {
                        if (it != null) {
                            UserEachRow(person = it) {

                                navHostController.navigate("chatscreen/${it.userId}")
                            }
                        }
                    }
                }
            }
        }

    }

}


@Composable
fun BottomSheetSwipeUp(
    modifier: Modifier
) {

    Box(
        modifier = modifier
            .background(
                Color.Gray,
                RoundedCornerShape(90.dp)
            )
            .width(90.dp)
            .height(5.dp)

    )
}


@OptIn(InternalPlatformTextApi::class)
@Composable
fun UserEachRow(
    person: UserData,
    onClick: () -> Unit
) {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .clickable { onClick() }
            .padding(horizontal = 20.dp, vertical = 5.dp),
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row {
                    SpacerWidth()
                    Column {
                        person.userName?.let {
                            Text(
                                text = it,
                                style = TextStyle(
                                    color =  Color.Black,  // Cambiar el color del texto si hay nuevos mensajes no vistos
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }
                        SpacerHeight(5.dp)
                    }
                }

            }
            SpacerHeight(15.dp)
            Divider(modifier = Modifier.fillMaxWidth(), thickness = 1.dp, color = Line)
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







@Composable
fun SpacerWidth(
    width: Dp = 10.dp
) {
    Spacer(modifier = Modifier.width(width))
}

@Composable
fun SpacerHeight(
    height: Dp = 10.dp
) {
    Spacer(modifier = Modifier.height(height))
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(recipientUserId: String, chatViewModel: ChatViewModel = hiltViewModel()) {

    initRecipientUserId(recipientUserId, chatViewModel)

    val message: String by chatViewModel.message.observeAsState(initial = "")
    val messages: List<Map<String, Any>> by chatViewModel.messages.observeAsState(
        initial = emptyList<Map<String, Any>>().toMutableList()
    )

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
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
            maxLines = 1,
            modifier = Modifier
                .padding(horizontal = 15.dp, vertical = 1.dp)
                .fillMaxWidth()
                .weight(weight = 0.09f, fill = true),
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
                        contentDescription = "Send Button"
                    )
                }
            }
        )
    }
}


fun initRecipientUserId(recipientUserId: String, chatViewModel: ChatViewModel) {
    // Llama a una función en el ViewModel para establecer el recipientUserId
    chatViewModel.initRecipientUserId(recipientUserId)
}