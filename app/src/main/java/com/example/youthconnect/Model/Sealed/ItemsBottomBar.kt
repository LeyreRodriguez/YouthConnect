package com.example.youthconnect.Model.Sealed

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Chat
import androidx.compose.material.icons.outlined.Newspaper
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.PersonOutline
import androidx.compose.material.icons.outlined.Quiz
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.youthconnect.Model.Enum.NavScreen
import com.example.youthconnect.Model.Sealed.ItemsBottomBar.ChatBottom.userType


sealed class ItemsBottomBar(
    val icon: ImageVector,
    val title: String,
    val ruta: String,
    val userType: String? = null // Nuevo parámetro userType
){

    object NewsBottom : ItemsBottomBar
        (
        Icons.Outlined.Newspaper ,
        "News",
        NavScreen.NewsScreen.name
    )

    object QuizBottom : ItemsBottomBar
        (
        Icons.Outlined.Quiz,
        "Quiz",
        NavScreen.QuizScreen.name
    )

    object ChatBottom : ItemsBottomBar
        (
        Icons.Outlined.Chat,
        "Chat",
        NavScreen.ChatScreen.name
    )

    object ProfileBottom : ItemsBottomBar(
        Icons.Outlined.PersonOutline,
        "Profile",
        NavScreen.Profile.name
    )



}