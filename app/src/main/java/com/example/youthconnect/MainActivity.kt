package com.example.youthconnect

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.youthconnect.Model.Enum.NavScreen
import com.example.youthconnect.View.BottomNavigationScreens.ChatScreen
import com.example.youthconnect.View.BottomNavigationScreens.ChildListScreen
import com.example.youthconnect.View.BottomNavigationScreens.ChildProfileScreen
import com.example.youthconnect.View.BottomNavigationScreens.InstructorProfileScreen
import com.example.youthconnect.View.BottomNavigationScreens.LogInScreen
import com.example.youthconnect.View.BottomNavigationScreens.NewsDetails
import com.example.youthconnect.View.BottomNavigationScreens.NewsScreen
import com.example.youthconnect.View.BottomNavigationScreens.ParentsProfileScreen
import com.example.youthconnect.View.BottomNavigationScreens.QuizScreen
import com.example.youthconnect.View.BottomNavigationScreens.SignUpScreen
import com.example.youthconnect.View.Components.BottomNavigation

import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            NavHost(navController = navController,
                startDestination = "firstScreens"
            ){
                navigation(
                    startDestination = "login",
                    route = "firstScreens"
                ) {
                    composable("login") { LogInScreen(navController = navController) }
                    composable("signup") { SignUpScreen(navController = navController) }
                }
                navigation(
                    startDestination = NavScreen.NewsScreen.name,
                    route = "secondScreens"
                ){
                    composable(NavScreen.NewsScreen.name){

                        Scaffold(
                            bottomBar = {
                                BottomNavigation(navController)
                            }
                        ) {padding->
                            Box(
                                modifier = Modifier
                                    .padding(padding)
                                    .fillMaxSize()
                            ) {
                                NewsScreen(navController = navController)
                            }


                        }
                    }

                    composable(NavScreen.QuizScreen.name){
                        Scaffold(
                            bottomBar = {
                                BottomNavigation(navController)
                            }
                        ) {padding->
                            Box(
                                modifier = Modifier
                                    .padding(padding)
                                    .fillMaxSize()
                            ) {
                                QuizScreen()
                            }


                        }

                    }

                    composable(NavScreen.ChatScreen.name){
                        Scaffold(
                            bottomBar = {
                                BottomNavigation(navController)
                            }
                        ) {padding->
                            Box(
                                modifier = Modifier
                                    .padding(padding)
                                    .fillMaxSize()
                            ) {
                                ChatScreen()
                            }


                        }

                    }

              //      composable(NavScreen.AddInstructor.name){ AddInstructorScreen() }

                    composable(NavScreen.ChildList.name){
                        Scaffold(
                            bottomBar = {
                                BottomNavigation(navController)
                            }
                        ) {padding->
                            Box(
                                modifier = Modifier
                                    .padding(padding)
                                    .fillMaxSize()
                            ) {
                               // Log.w("HOLA", "TUS MUERTIS AUN MAS")
                                ChildListScreen(navController = navController)
                            }


                        }

                    }

                //    composable(route = "SpecificChild") { if (startRoute != null) { ChildProfileScreen(childId = startRoute) } }
                    composable(
                        route = "news_details_screen/{newsId}",
                        arguments = listOf(navArgument("newsId") { type = NavType.StringType })
                    ) { backStackEntry ->
                        val newsId = backStackEntry.arguments?.getString("newsId") ?: ""
                        Scaffold(
                            bottomBar = {
                                BottomNavigation(navController)
                            }
                        ) {padding->
                            Box(
                                modifier = Modifier
                                    .padding(padding)
                                    .fillMaxSize()
                            ) {
                                NewsDetails(newsId = newsId)
                            }


                        }


                    }


                    composable(
                        route = "child_profile_screen/{childId}",
                        arguments = listOf(navArgument("childId") { type = NavType.StringType })
                    ) { backStackEntry ->
                        val childId = backStackEntry.arguments?.getString("childId") ?: ""
                        Scaffold(
                            bottomBar = {
                                BottomNavigation(navController)
                            }
                        ) {padding->
                            Box(
                                modifier = Modifier
                                    .padding(padding)
                                    .fillMaxSize()
                            ) {
                                ChildProfileScreen(childId = childId)
                            }


                        }

                    }




                    composable(
                        route = "parent_profile_screen/{parentId}",
                        arguments = listOf(navArgument("parentId") { type = NavType.StringType })
                    ) { backStackEntry ->
                        val parentId = backStackEntry.arguments?.getString("parentId") ?: ""
                        Scaffold(
                            bottomBar = {
                                BottomNavigation(navController)
                            }
                        ) {padding->
                            Box(
                                modifier = Modifier
                                    .padding(padding)
                                    .fillMaxSize()
                            ) {
                                ParentsProfileScreen(parentId = parentId, navController = navController)
                            }


                        }


                    }


                    composable(
                        route = "instructor_profile_screen/{instructorId}",
                        arguments = listOf(navArgument("instructorId") { type = NavType.StringType })
                    ) { backStackEntry ->
                        val instructorId = backStackEntry.arguments?.getString("instructorId") ?: ""
                        Scaffold(
                            bottomBar = {
                                BottomNavigation(navController)
                            }
                        ) {padding->
                            Box(
                                modifier = Modifier
                                    .padding(padding)
                                    .fillMaxSize()
                            ) {
                                InstructorProfileScreen(instructorId = instructorId, navController = navController)
                            }


                        }


                    }
                }
            }



        }
    }
}


/*
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            YouthconnectTheme {
                // A surface container using the 'background' color from the theme

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

                    val extras = intent.extras
                    var textResult: String? = null

                    if (extras != null) {
                        textResult = extras.getString("TEXT_RESULT")
                        Log.d("MainActivity", "Received Text Result: $textResult")
                    } else {
                        Log.d("MainActivity", "No extras received")
                    }

                    MainScreen(textResult)

                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(textResult : String? = null) {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            BottomNavigation(navController)
        }
    ) {padding->
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            HomeNavigation(navController = navController, textResult)
        }


    }
}

*/

