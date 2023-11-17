package com.example.youthconnect.Model.Sealed

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Chat
import androidx.compose.material.icons.outlined.Newspaper
import androidx.compose.material.icons.outlined.Quiz
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.youthconnect.Model.Enum.NavScreen


sealed class ItemsBottomBar(
    val icon: ImageVector,
    val title: String,
    val ruta: String
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

    object NewsDetailsScreen : NewsNavScreen("news_details_screen")
    // Agrega más pantallas si es necesario
}