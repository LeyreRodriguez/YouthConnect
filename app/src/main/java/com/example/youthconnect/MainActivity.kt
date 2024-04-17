package com.example.youthconnect

import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.libraryapp.viewModel.LoginViewModel
import com.example.youthconnect.Model.Enum.NavScreen
import com.example.youthconnect.Model.Firebase.Authentication.GoogleAuthUiClient
import com.example.youthconnect.View.BottomNavigationScreens.ChatScreen
import com.example.youthconnect.View.OverlaysAndMore.ChildListScreen
import com.example.youthconnect.View.Profiles.ChildProfileScreen
import com.example.youthconnect.View.BottomNavigationScreens.HomeScreen
import com.example.youthconnect.View.Profiles.InstructorProfileScreen
import com.example.youthconnect.View.Authentication.LoginView
import com.example.youthconnect.View.OverlaysAndMore.NewsDetails
import com.example.youthconnect.View.BottomNavigationScreens.NewsScreen
import com.example.youthconnect.View.Profiles.ParentsProfileScreen
import com.example.youthconnect.View.BottomNavigationScreens.QuizScreen
import com.example.youthconnect.View.BottomNavigationScreens.Scores
import com.example.youthconnect.View.Authentication.SignUpView
import com.example.youthconnect.View.Components.BottomNavigation
import com.example.youthconnect.ViewModel.UserViewModel
import com.google.android.gms.auth.api.identity.Identity
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private var textResult = mutableStateOf("")

    private val barCodeLauncher = registerForActivityResult(ScanContract()) { result ->
        if(result.contents == null){
            Toast.makeText(this@MainActivity, "Cancelled", Toast.LENGTH_SHORT).show()
        } else{
            textResult.value = result.contents
        }
    }


    private fun showCamera(){
        val options = ScanOptions()
        options.setDesiredBarcodeFormats(ScanOptions.QR_CODE)
        options.setPrompt("Scan a QR code")
        options.setCameraId(0)
        options.setBeepEnabled(false)
        options.setOrientationLocked(false)
        barCodeLauncher.launch(options)
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {
            isGranted ->
        if(isGranted){
            showCamera()
        }
    }

    fun checkCameraPermission(context: Context) {
        if(ContextCompat.checkSelfPermission(
                context,
                android.Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            showCamera()
        } else if (shouldShowRequestPermissionRationale(android.Manifest.permission.CAMERA)){
            Toast.makeText(this@MainActivity, "Camera Required", Toast.LENGTH_SHORT).show()
        } else {
            requestPermissionLauncher.launch(android.Manifest.permission.CAMERA)
        }
    }


    public val googleAuthUiClient by lazy {
        GoogleAuthUiClient(
            context = applicationContext,
            oneTapClient = Identity.getSignInClient(applicationContext)
        )
    }

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
                    composable("login") {
                        val viewModel = viewModel<LoginViewModel>()
                        val state by viewModel.state.collectAsStateWithLifecycle()

                        LaunchedEffect(key1 = Unit){
                            if(googleAuthUiClient.getSignedInUser() != null){
                                navController.navigate("secondScreens")
                            }
                        }

                        val launcher = rememberLauncherForActivityResult(
                            contract = ActivityResultContracts.StartIntentSenderForResult(),
                            onResult = {result ->
                                if(result.resultCode == RESULT_OK) {
                                    lifecycleScope.launch {
                                        val signInResult = googleAuthUiClient.signInWithIntent(
                                            intent = result.data ?: return@launch
                                        )
                                        viewModel.onSignInResult(signInResult)
                                    }
                                }
                            }
                        )

                        LaunchedEffect(key1 = state.isSignInSuccessful) {
                            if(state.isSignInSuccessful) {
                                Toast.makeText(
                                    applicationContext,
                                    "Sesión Iniciada",
                                    Toast.LENGTH_LONG
                                ).show()

                                navController.navigate("secondScreens")
                                viewModel.resetState()
                            }
                        }

                        LoginView(
                            navController = navController,
                            state = state,
                            onSignInClick = {
                                lifecycleScope.launch {
                                    val signInIntentSender = googleAuthUiClient.signIn()
                                    launcher.launch(
                                        IntentSenderRequest.Builder(
                                            signInIntentSender ?: return@launch
                                        ).build()
                                    )
                                }
                            }

                        )
                    }
                    composable("signup") { SignUpView(navController = navController) }



                    composable("qr") {
                        var showDialog by remember { mutableStateOf(false)  }

                        BackHandler {
                            navController.popBackStack()
                        }


                        checkCameraPermission(this@MainActivity )
                        if ( textResult.value.isNotEmpty()) {
                            QrScannerScreen(textResult.value, navController)
                        }



                        LaunchedEffect(textResult.value) {
                            if (textResult.value.isEmpty()) {
                                // Navegar de vuelta a la pantalla anterior si no hay texto de resultado
                                navController.popBackStack()
                            }
                        }
                    }
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
                                QuizScreen(navController)
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
                               // ChatScreen()
                                HomeScreen(navController)
                            }


                        }

                    }



                    composable(NavScreen.Profile.name){

                        val userViewModel : UserViewModel = hiltViewModel()
                        var user by remember { mutableStateOf<String?>("") }
                        var userType by remember { mutableStateOf<String?>("") }

                        LaunchedEffect(key1 = Unit){
                            user = userViewModel.getCurrentUser()
                            userType = user?.let { it1 -> userViewModel.getUserType(it1) }
                        }

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
                                userType?.let { type ->
                                    when (type) {
                                        "Child" -> {
                                            user?.let { childId ->
                                                ChildProfileScreen(
                                                    childId = childId,
                                                    navController = navController
                                                )
                                            }
                                        }

                                        "Parents" -> {
                                            user?.let { parentId ->
                                                ParentsProfileScreen(
                                                    parentId = parentId,
                                                    navController = navController
                                                )
                                            }
                                        }

                                        "Instructor" -> {
                                            user?.let { instructorId ->
                                                InstructorProfileScreen(
                                                    instructorId = instructorId,
                                                    navController = navController
                                                )
                                            }
                                        }

                                        else -> {
                                            // Manejar caso por defecto o error
                                        }
                                    }
                                }
                            }


                        }

                    }

                    composable(
                        route = "chatscreen/{recipientUserId}",
                        arguments = listOf(navArgument("recipientUserId") { type = NavType.StringType })
                    ) { backStackEntry ->
                        val recipientUserId = backStackEntry.arguments?.getString("recipientUserId") ?: ""
                        Scaffold(
                            bottomBar = {
                                BottomNavigation(navController)
                            }
                        ) { padding ->
                            Box(
                                modifier = Modifier
                                    .padding(padding)
                                    .fillMaxSize()
                            ) {
                                ChatScreen(recipientUserId = recipientUserId, navController)
                            }
                        }
                    }




                    composable(
                        route = NavScreen.ChildList.name +"/{instructorID}",
                        arguments = listOf(navArgument("instructorID") { type = NavType.StringType })
                    ) { backStackEntry ->
                        val instructorID = backStackEntry.arguments?.getString("instructorID") ?: ""
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
                                ChildListScreen(navController = navController, instructorID = instructorID)
                            }


                        }


                    }



                    composable("Scores"){
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

                                Scores(navController)
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
                                ChildProfileScreen(childId = childId, navController = navController)
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



@Composable
fun QrScannerScreen( childId : String, navController: NavController){

    val userViewModel : UserViewModel = hiltViewModel()

    LaunchedEffect(userViewModel) {
        try {
            userViewModel.changeState(childId)

        } catch (e: Exception) {
            Log.e("Firestore", "Error en QR", e)
        }
    }

    Column(modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly ){
        Button(
            onClick = {
                navController.navigate("qr")
            },
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
                .height(56.dp)
                .clip(RoundedCornerShape(8.dp))
        ) {
            Text(
                text = "Escanear otro QR",
                style = TextStyle(
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color.White,
                    letterSpacing = 0.3.sp,
                )
            )
        }


        Button(
            onClick = {
                navController.navigate("child_profile_screen/${childId}")
            },
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
                .height(56.dp)
                .clip(RoundedCornerShape(8.dp))
        ) {
            Text(
                text = childId,
                style = TextStyle(
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color.White,
                    letterSpacing = 0.3.sp,
                )
            )
        }
    }








}
