package com.example.youthconnect.View.Profiles


import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
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
import com.example.libraryapp.viewModel.LoginViewModel
import com.example.youthconnect.Model.Object.Child
import com.example.youthconnect.Model.Object.Parent
import com.example.youthconnect.R
import com.example.youthconnect.View.Components.EditIcon
import com.example.youthconnect.View.Components.ProfilePicture
import com.example.youthconnect.View.OverlaysAndMore.MyChildren
import com.example.youthconnect.ViewModel.UserViewModel



@Composable
fun ParentsProfileScreen(parentId : String,
                         modifier : Modifier = Modifier.background(color = Color.White),
                         navController: NavHostController,
                         loginViewModel: LoginViewModel = viewModel()
) {

    var parent by remember { mutableStateOf<Parent?>(null) }
    var currentUser by remember { mutableStateOf<String?>(null) }
    var currentUserType by remember { mutableStateOf("") }
    var children by remember { mutableStateOf<List<Child?>>(emptyList()) }

    val userViewModel : UserViewModel = hiltViewModel()






    LaunchedEffect(userViewModel) {
        try {
            parent = userViewModel.getCurrentUserById(parentId)
            children = userViewModel.getChildByParentsId(parentId)
            currentUser = userViewModel.getCurrentUser()
            currentUserType = currentUser?.let { userViewModel.getUserType(it).toString() }.toString()
        } catch (e: Exception) {
            Log.e("Firestore", "Error en ChildList", e)
        }
    }




        Box(
            modifier = Modifier.fillMaxSize(),
        ) {
            Column(
                modifier = Modifier
                    .padding(15.dp)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center
            ) {

                Column(horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()) {
                    val configuration = LocalConfiguration.current
                    val screenWidth = with(LocalDensity.current) { configuration.screenWidthDp.dp }

                    ProfilePicture(
                        userViewModel = userViewModel,
                        userId = parentId,
                        user = parent,
                        currentUser = currentUser
                    )

                    Row(verticalAlignment = Alignment.CenterVertically){
                        parent?.fullName?.let {
                            Text(
                                text = it,
                                style = TextStyle(
                                    fontSize = 20.sp,
                                    fontFamily = FontFamily(Font(R.font.annie_use_your_telescope)),
                                    fontWeight = FontWeight(400),
                                    color = Color(0xFF000000),
                                    letterSpacing = 0.9.sp,
                                ), modifier = Modifier
                                    .padding(start = 15.dp, top = 10.dp)
                            )
                        }

                        EditIcon(
                            currentUserType = currentUserType,
                            user = parent,
                            navController = navController
                        )


                    }

                    Spacer(modifier = Modifier.size(40.dp))
                }

                Column ( modifier = Modifier.fillMaxWidth()
                ){
                    if(currentUser == parent?.id){
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
                                    navController.navigate("firstScreens")
                                }
                        )
                    }

                    Text(
                        text = "Mis hijos/hijas",
                        style = TextStyle(
                            fontSize = 30.sp,
                            fontFamily = FontFamily(Font(R.font.annie_use_your_telescope)),
                            fontWeight = FontWeight(400),
                            color = Color(0xFF000000),
                            letterSpacing = 0.9.sp,
                        ), modifier = Modifier
                            .padding(start = 15.dp, top = 10.dp)
                    )



                    LazyColumn(modifier = Modifier.padding(vertical = 4.dp)) {
                        items(items = children) { item ->
                            if (item != null) {
                                MyChildren(navController = navController, item)
                            }
                        }
                    }
                }

            }


        }

    }


