package com.example.youthconnect.Navigation


import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.youthconnect.Model.Enum.NavScreen
import com.example.youthconnect.QrScan
import com.example.youthconnect.View.BottomNavigationScreens.AddInstructorScreen
import com.example.youthconnect.View.BottomNavigationScreens.ChatScreen
import com.example.youthconnect.View.BottomNavigationScreens.ChildListScreen
import com.example.youthconnect.View.BottomNavigationScreens.ChildProfileScreen
import com.example.youthconnect.View.BottomNavigationScreens.InstructorProfileScreen
import com.example.youthconnect.View.BottomNavigationScreens.NewsDetails
import com.example.youthconnect.View.BottomNavigationScreens.NewsScreen
import com.example.youthconnect.View.BottomNavigationScreens.ParentsProfileScreen
import com.example.youthconnect.View.BottomNavigationScreens.QuizScreen
import com.example.youthconnect.ViewModel.NewsViewModel

@Composable
fun HomeNavigation(
    navController: NavHostController,
    startRoute : String? = null
){
    val startDestination = if (startRoute != null) {
        "SpecificChild"
    } else {
        NavScreen.NewsScreen.name
    }

    NavHost(navController = navController,
        startDestination = startDestination
    ){
        composable(NavScreen.NewsScreen.name){ NewsScreen(navController = navController) }

        composable(NavScreen.QuizScreen.name){ QuizScreen() }

        composable(NavScreen.ChatScreen.name){ ChatScreen() }

        composable(NavScreen.AddInstructor.name){ AddInstructorScreen() }

        composable(NavScreen.ChildList.name){ ChildListScreen(navController = navController) }

        composable(route = "SpecificChild") { if (startRoute != null) { ChildProfileScreen(childId = startRoute, navController = navController) } }
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
            val childId = startRoute ?: backStackEntry.arguments?.getString("childId") ?: ""
            ChildProfileScreen(childId = childId, navController = navController)
        }




        composable(
            route = "parent_profile_screen/{parentId}",
            arguments = listOf(navArgument("parentId") { type = NavType.StringType })
        ) { backStackEntry ->
            val parentId = backStackEntry.arguments?.getString("parentId") ?: ""
            ParentsProfileScreen(parentId = parentId, navController = navController)
        }


        composable(
            route = "instructor_profile_screen/{instructorId}",
            arguments = listOf(navArgument("instructorId") { type = NavType.StringType })
        ) { backStackEntry ->
            val instructorId = backStackEntry.arguments?.getString("instructorId") ?: ""
            InstructorProfileScreen(instructorId = instructorId, navController = navController)
        }




    }


}


