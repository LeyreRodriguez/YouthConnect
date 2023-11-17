package com.example.youthconnect.Model.Sealed

sealed class NewsNavScreen(val route: String) {
    object NewsScreen : NewsNavScreen("news_screen")
    object NewsDetailsScreen : NewsNavScreen("news_details_screen")
    // Agrega más pantallas si es necesario
}
