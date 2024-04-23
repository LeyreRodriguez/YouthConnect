package com.example.youthconnect.View.Profiles



import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.libraryapp.viewModel.LoginViewModel
import com.example.youthconnect.Model.Enum.NavScreen
import com.example.youthconnect.Model.Object.Child
import com.example.youthconnect.Model.Object.Instructor
import com.example.youthconnect.R
import com.example.youthconnect.View.Components.EditIcon
import com.example.youthconnect.View.Components.ProfilePicture
import com.example.youthconnect.View.OverlaysAndMore.AddInstructor
import com.example.youthconnect.View.OverlaysAndMore.MyChildren
import com.example.youthconnect.ui.theme.Green
import com.example.youthconnect.ui.theme.Red
import com.example.youthconnect.ViewModel.UserViewModel


@Composable
fun InstructorProfileScreen(instructorId : String,
                              modifier : Modifier = Modifier.background(color = Color.White),
                              navController: NavHostController,
                            loginViewModel : LoginViewModel = viewModel()
) {

    val userViewModel : UserViewModel = hiltViewModel()
    var instructor by remember { mutableStateOf<Instructor?>(null) }
    var currentUser by remember { mutableStateOf<String?>(null) }
    var children by remember { mutableStateOf<List<Child?>>(emptyList()) }
    var editUser by remember { mutableStateOf(false)  }

    var currentUserType by remember { mutableStateOf("") }



    LaunchedEffect(userViewModel) {
        try {
            instructor = userViewModel.getCurrentInstructorById(instructorId)
            children = userViewModel.getChildByInstructorIdThatIsInSchool(instructorId)
   //         currentUser = userViewModel.getCurrentUser()
    //        currentUserType = currentUser?.let { userViewModel.getUserType(it).toString() }.toString()

        } catch (e: Exception) {
            Log.e("Firestore", "Error fetching data", e)
        }
    }

    LaunchedEffect(userViewModel) {
        try {
      //      instructor = userViewModel.getCurrentInstructorById(instructorId)
      //      children = userViewModel.getChildByInstructorIdThatIsInSchool(instructorId)
            currentUser = userViewModel.getCurrentUser()
            currentUserType = currentUser?.let { userViewModel.getUserType(it).toString() }.toString()

        } catch (e: Exception) {
            Log.e("Firestore", "Error fetching data", e)
        }
    }

            Box(
                modifier = Modifier.fillMaxSize(),
            ) {

                AddInstructorFragment(currentUser, instructor)


                Column(
                    modifier = Modifier
                        .padding(15.dp)
                        .fillMaxSize()
                    ,
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Column(horizontalAlignment = Alignment.CenterHorizontally,
                        //modifier = Modifier.wrapContentSize()
                    ) {
                        val configuration = LocalConfiguration.current
                        val screenWidth = with(LocalDensity.current) { configuration.screenWidthDp.dp }


                        ProfilePicture(
                            userViewModel = userViewModel,
                            userId = instructorId,
                            user = instructor,
                            currentUser = currentUser
                        )

                        Row(verticalAlignment = Alignment.CenterVertically){
                            instructor?.fullName?.let {

                                Text(
                                    text = it,
                                    style = TextStyle(
                                        fontSize = 20.sp,
                                        fontFamily = FontFamily(Font(R.font.annie_use_your_telescope)),
                                        fontWeight = FontWeight(400),
                                        color = Color(0xFF000000),
                                        letterSpacing = 0.9.sp,
                                        textAlign = TextAlign.Center
                                    ), modifier = Modifier
                                        .padding(start = 15.dp, top = 10.dp)
                                )
                            }

                            EditIcon(
                                currentUserType = currentUserType,
                                user = instructor,
                                navController = navController
                            )


                        }


                    }


                    Column (
                        modifier = Modifier.fillMaxWidth()
                    ){
                        if(currentUser == instructor?.id){
                            Text(
                                text = "Salir",
                                style = TextStyle(
                                    fontSize = 30.sp,
                                    fontFamily = FontFamily(Font(R.font.annie_use_your_telescope)),
                                    fontWeight = FontWeight(400),
                                    color = Color(0xFF000000),
                                    letterSpacing = 0.9.sp,
                                    textAlign = TextAlign.Center
                                ), modifier = Modifier
                                    .padding(start = 15.dp, top = 10.dp)
                                    .fillMaxWidth()
                                    .clickable {
                                        loginViewModel.signOut()
                                        navController.navigate("login")
                                    }
                            )
                        }


                        GroupsOfFaith(children = children, navController = navController )



                    }

                    FunctionalitiesButtons(
                        currentUser = currentUser,
                        instructor = instructor,
                        navController = navController,
                        instructorId = instructorId
                    )

                }


            }
        }


@Composable
fun AddInstructorFragment(currentUser : String?, instructor: Instructor?){
    var showDialog by remember { mutableStateOf(false)  }

    if(currentUser == instructor?.id) {

        Image(
            painter = painterResource(id = R.drawable.baseline_person_add_24),
            contentDescription = "icon",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(80.dp)
                .padding(15.dp)
                .clickable {
                    showDialog = true
                }
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
                .clip(CircleShape)

        )
        if (showDialog) {
            AddInstructor(onDismiss = { showDialog = false })
        }
    }
}
@Composable
fun GroupsOfFaith(children : List<Child?>, navController: NavHostController){
    Text(
        text = "Asistencia de grupos de fe",
        style = TextStyle(
            fontSize = 20.sp,
            fontFamily = FontFamily(Font(R.font.annie_use_your_telescope)),
            fontWeight = FontWeight(400),
            color = Color(0xFF000000),
            letterSpacing = 0.9.sp,
            textAlign = TextAlign.Start
        ), modifier = Modifier
            .padding(start = 15.dp, top = 10.dp)
    )

    LazyColumn(modifier = Modifier
        .padding(vertical = 4.dp)
        .height(200.dp)
    ) {
        items(items = children) { item ->
            if (item != null) {
                MyChildren(navController = navController, item)
            }
        }
    }
}
@Composable
fun FunctionalitiesButtons(currentUser : String?, instructor: Instructor?, navController : NavHostController, instructorId: String){
    if(currentUser == instructor?.id) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly, // Distribuye las imágenes equitativamente
            verticalAlignment = Alignment.CenterVertically
        ) {

            AsyncImage(
                model = "https://media.istockphoto.com/id/1358621997/vector/qr-code-smartphone-scanner-linear-icon-vector-illustration.jpg?s=612x612&w=0&k=20&c=ePiWZHIbseW9GwmM498rRKC_Dvk8IsKv41nqnC8iZhQ=",
                contentDescription = "Scan Qr",
                modifier = Modifier
                    .size(120.dp)
                    .padding(14.dp)

                    .clip(CircleShape)
                    .clickable {


                        navController.navigate("qr")


                    },
                contentScale = ContentScale.Crop
            )


            AsyncImage(
                model = "https://static.vecteezy.com/system/resources/previews/006/692/364/original/list-icon-template-black-color-editable-list-icon-symbol-flat-sign-isolated-on-white-background-simple-logo-illustration-for-graphic-and-web-design-free-vector.jpg",
                contentDescription = "Child List",
                modifier = Modifier
                    .size(120.dp)
                    .padding(14.dp)

                    .clip(CircleShape)
                    .clickable {
                        navController.navigate(NavScreen.ChildList.name + "/${instructorId}")
                    },
                contentScale = ContentScale.Crop
            )

        }
    }
}










