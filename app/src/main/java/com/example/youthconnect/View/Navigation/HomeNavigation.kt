package com.example.youthconnect.View.Navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.youthconnect.Model.Enum.NavScreen
import com.example.youthconnect.View.BottomNavigationScreens.ChatScreen
import com.example.youthconnect.View.BottomNavigationScreens.ChildProfileScreen
import com.example.youthconnect.View.BottomNavigationScreens.NewsDetails
import com.example.youthconnect.View.BottomNavigationScreens.NewsScreen
import com.example.youthconnect.View.BottomNavigationScreens.ParentsProfileScreen
import com.example.youthconnect.View.BottomNavigationScreens.QuizScreen
import com.example.youthconnect.ViewModel.NewsViewModel

@Composable
fun HomeNavigation(
    navController: NavHostController
){

    NavHost(navController = navController,
        startDestination = NavScreen.NewsScreen.name
    ){
        composable(NavScreen.NewsScreen.name){ NewsScreen(navController = navController) }

        composable(NavScreen.QuizScreen.name){ QuizScreen() }

        composable(NavScreen.ChatScreen.name){ ChatScreen() }


        composable(
            route = "news_details_screen/{newsId}",
            arguments = listOf(navArgument("newsId") { type = NavType.StringType })
        ) { backStackEntry ->
            val newsId = backStackEntry.arguments?.getString("newsId") ?: ""
            NewsDetails(newsId = newsId)
        }


        composable(
            route = "child_profile_screen/{childId}",
            arguments = listOf(navArgument("childId") { type = NavType.StringType })
        ) { backStackEntry ->
            val childId = backStackEntry.arguments?.getString("childId") ?: ""
            ChildProfileScreen(childId = childId)
        }


        composable(
            route = "parent_profile_screen/{parentId}",
            arguments = listOf(navArgument("parentId") { type = NavType.StringType })
        ) { backStackEntry ->
            val parentId = backStackEntry.arguments?.getString("parentId") ?: ""
            ParentsProfileScreen(parentId = parentId, navController = navController)
        }


    }
}


