package com.example.youthconnect.View.Navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.youthconnect.Model.NavScreen
import com.example.youthconnect.View.BottomNavigationScreens.ChatScreen
import com.example.youthconnect.View.BottomNavigationScreens.NewsScreen
import com.example.youthconnect.View.BottomNavigationScreens.QuizScreen

@Composable
fun NewsNavigation(
    navController: NavHostController
){
    NavHost(navController = navController,
        startDestination = NavScreen.NewsScreen.name
    ){
        composable(NavScreen.NewsScreen.name){ NewsScreen() }

        composable(NavScreen.QuizScreen.name){ QuizScreen() }

        composable(NavScreen.ChatScreen.name){ ChatScreen() }
    }
}
