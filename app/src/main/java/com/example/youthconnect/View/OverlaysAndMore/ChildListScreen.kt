package com.example.youthconnect.View.OverlaysAndMore

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.youthconnect.Model.Enum.NavScreen
import com.example.youthconnect.Model.Object.Child
import com.example.youthconnect.Model.Object.Instructor
import com.example.youthconnect.R
import com.example.youthconnect.View.Components.CustomDropdownMenu
import com.example.youthconnect.ViewModel.UserViewModel
import com.example.youthconnect.ViewModel.SignUpViewModel
import com.example.youthconnect.ui.theme.Blue
import com.example.youthconnect.ui.theme.Blue50
import com.example.youthconnect.ui.theme.Green
import com.example.youthconnect.ui.theme.Red
import java.time.LocalDate


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchView(
    state: MutableState<TextFieldValue>,
    placeHolder: String,
    modifier: Modifier
) {
    var isTextFieldEmpty by remember { mutableStateOf(true) }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        TextField(
            value = state.value,
            onValueChange = {value ->
                state.value = value
                isTextFieldEmpty = value.text.isEmpty()
            },
            modifier = Modifier
                .weight(1f)
                .padding(10.dp)
                .clip(RoundedCornerShape(30.dp))
                .border(2.dp, Color.DarkGray, RoundedCornerShape(30.dp)),
            placeholder = {
                Text(text = placeHolder)
            },
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color.White
            ),
            maxLines = 1,
            singleLine = true,
            textStyle = TextStyle(
                color = Color.Black, fontSize = 20.sp
            ),
            trailingIcon = {
                if (!isTextFieldEmpty) {
                    Icon(
                        imageVector = Icons.Default.Clear,
                        contentDescription = "Clear Text",
                        tint = Color.DarkGray,
                        modifier = Modifier
                            .clickable {
                                state.value = TextFieldValue("")
                                isTextFieldEmpty = true
                            }
                            .padding(12.dp)
                    )
                }
            }
        )
    }
}
@Composable
fun ChildListScreen(navController : NavHostController, instructorID: String){

    val userViewModel : UserViewModel = hiltViewModel()
    var childs by remember { mutableStateOf<List<Child?>>(emptyList()) }
    var instructor by remember { mutableStateOf<Instructor?>(null) }
    var myKids by remember { mutableStateOf<List<Child?>>(emptyList()) }


    var user by remember { mutableStateOf<String?>("") }

    val documentExists = remember { mutableStateOf("-1") }
    var result by remember { mutableStateOf<String?>("") }



    LaunchedEffect(userViewModel) {
        try {
            childs = userViewModel.getAllChildren()
            instructor = userViewModel.getCurrentInstructorById(instructorID)
            myKids = userViewModel.getChildByInstructorId(instructorID)
            user = userViewModel.getCurrentUser()
            result = user?.let { userViewModel.findDocument(it) }

            if (result != null) {
                documentExists.value = result.toString()
            }

        } catch (e: Exception) {
        }
    }
        Box(
            modifier = Modifier.fillMaxSize(),
        ) {

            Column(modifier = Modifier.fillMaxHeight()) {

                Column(horizontalAlignment = Alignment.CenterHorizontally){
                    Text(
                        text = "Lista de niños",
                        style = TextStyle(
                            fontSize = 30.sp,
                            fontFamily = FontFamily(Font(R.font.annie_use_your_telescope)),
                            fontWeight = FontWeight(400),
                            color = Color(0xFF000000),
                            letterSpacing = 0.9.sp,
                        ), textAlign = TextAlign.Center
                    )


                    val textState = remember { mutableStateOf(TextFieldValue(""))}

                    SearchView(state= textState, placeHolder= "Buscar aqui...", modifier = Modifier)

                    val searchedText = textState.value.text


                    LazyColumn(modifier = Modifier.padding(10.dp)) {
                        items(items = childs.filter {
                            it?.fullName?.contains(searchedText, ignoreCase = true) ?: false
                        }, key = { it?.id ?: "" }) { item ->
                            if (item != null) {
                                Greeting(navController = navController, item)
                            }
                        }
                    }




                }

            }

        }

    }








@SuppressLint("UnrememberedMutableState")
@Composable
fun MyChildren(navController: NavController, child: Child) {

    val signUpViewModel: SignUpViewModel = hiltViewModel()
    val userViewModel: UserViewModel = hiltViewModel()
    var isChecked by remember { mutableStateOf(child.rollCall?.contains(LocalDate.now().toString()) == true) }



    var myKids by remember { mutableStateOf<List<Child?>>(emptyList()) }
    var instructorID by remember { mutableStateOf("") }

    var user by remember { mutableStateOf<String?>("") }
    var userType by remember { mutableStateOf<String>("")}



    LaunchedEffect(userViewModel) {
        try {
            instructorID = userViewModel.getCurrentUser().toString()

            myKids = instructorID?.let { userViewModel.getChildByInstructorId(it) }!!


            user = userViewModel.getCurrentUser()



            userType = user?.let { userViewModel.getUserType(it).toString() }.toString()
        } catch (e: Exception) {
        }
    }


    val imageUrlState = remember { mutableStateOf("") }
    LaunchedEffect(Unit) {
        userViewModel.getProfileEspecificImage(child.id.lowercase() + "@youthconnect.com",
            onSuccess = { url ->
                imageUrlState.value = url
            },
            onFailure = { _ ->
                // Manejar el error, por ejemplo, mostrar un mensaje
            }
        )
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                navController.navigate("child_profile_screen/${child.id}")
            }
            .padding(4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {


            AsyncImage(
                model = imageUrlState.value,
                contentDescription = "Profile Picture",
                modifier = Modifier
                    .size(50.dp)
                    .border(
                        BorderStroke(4.dp, SolidColor(if (child.goOutAlone) Green else Red)),
                        CircleShape
                    )
                    .padding(4.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )

            Text(
                text = child.fullName,
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                ),
                modifier = Modifier
                    .padding(start = 16.dp, end = 16.dp)
                    .weight(1f),
                textAlign = TextAlign.Start
            )


            val currentRoute = navController.currentBackStackEntry?.destination?.route


            if (currentRoute != NavScreen.ChildList.name +"/{instructorID}" && userType == "Instructor") {

                    Checkbox(
                        checked = isChecked,
                        onCheckedChange = { newCheckedState ->
                            isChecked = newCheckedState
                            if (instructorID != null) {
                                signUpViewModel.rollCall(child, newCheckedState)
                            }
                        },
                        modifier = Modifier.padding(end = 8.dp)
                    )
                }




        }
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Greeting(navController : NavController, child : Child, modifier: Modifier = Modifier) {
    val expanded = remember { mutableStateOf(false) }
    val extraPadding = if (expanded.value) 10.dp else 0.dp


    val userViewModel: UserViewModel = hiltViewModel()



    var myKids by remember { mutableStateOf<List<Child?>>(emptyList()) }
    var instructorID by remember { mutableStateOf("") }

    val documentExists = remember { mutableStateOf("-1") }
    var result by remember { mutableStateOf<String?>("") }
    var user by remember { mutableStateOf<String?>("") }
    var instructor by remember { mutableStateOf<Instructor?>(null) }
    var instructorsList by remember { mutableStateOf<List<Instructor?>>(emptyList()) }



    LaunchedEffect(userViewModel) {
        try {
            instructorID = userViewModel.getCurrentUser().toString()

            myKids = instructorID?.let { userViewModel.getChildByInstructorId(it) }!!
            // myKids = UserViewModel.getAllChildren()

            user = userViewModel.getCurrentUser()
            result = user?.let { userViewModel.findDocument(it) }

            if (result != null) {
                documentExists.value = result.toString()
            }
            instructor = userViewModel.getInstructorByChildId(child.id)
            instructorsList = userViewModel.getAllInstructors()
        } catch (e: Exception) {
        }
    }

    val imageUrlState = remember { mutableStateOf("") }
    LaunchedEffect(Unit) {
        userViewModel.getProfileEspecificImage(child.id.lowercase() + "@youthconnect.com",
            onSuccess = { url ->
                imageUrlState.value = url
            },
            onFailure = { _ ->
                // Manejar el error, por ejemplo, mostrar un mensaje
            }
        )
    }

    Surface(
        modifier = modifier.padding(vertical = 4.dp, horizontal = 8.dp),
        onClick = {navController.navigate("child_profile_screen/${child.id}")}
    ) {

        Row(modifier = Modifier.padding(24.dp)) {

            AsyncImage(
                model = imageUrlState.value,
                contentDescription = "Profile Picture",
                modifier = Modifier
                    .size(50.dp)
                    .border(
                        BorderStroke(4.dp, SolidColor(if (child.goOutAlone) Green else Red)),
                        CircleShape
                    )
                    .padding(4.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(bottom = extraPadding, start = 10.dp)
            ) {
                val faithGroupsState = rememberUpdatedState(child.faithGroups)

                val nameColor = if (faithGroupsState.value && child.instructorId.isNullOrEmpty()) Blue else Color.Black

                Text(text = child.fullName, fontWeight = FontWeight.Bold, color = nameColor)


                if (expanded.value) {


                        Text(text = "Animador: ", fontWeight = FontWeight.Bold )

                        if(child.instructorId.isNullOrEmpty()){
                                CustomDropdownMenu(instructorsList, "", Color.DarkGray, onSelected = { _ ->
                                    userViewModel.changeInstructor(child, instructorID)
                                    navController.navigate(NavScreen.ChildList.name + "/${instructorID}")
                                })

                        }else{
                            instructor?.let {
                                CustomDropdownMenu(instructorsList, it.fullName, Color.DarkGray, onSelected = { _ ->
                                    userViewModel.changeInstructor(child, instructorID)
                                    navController.navigate(NavScreen.ChildList.name + "/${instructorID}")
                                })
                            }
                        }




                    if(child.faithGroups){
                        Text(text = "Pertenece a grupos de fe")
                    }else{
                        Text(text = "No pertenece a grupos de fe")
                    }

                    if(child.belongsToSchool){
                        Text(text = "Pertenece al colegio")
                    }else{
                        Text(text = "No pertenece al colegio")
                    }

                    Text(text = "Obervaciones: ", fontWeight = FontWeight.Bold )

                    if(child.observations.isNullOrEmpty()){
                        Text("No hay observaciones")
                    }else{
                        child.observations?.let { Text(text = it) }
                    }


                }
            }
            IconButton(
                onClick = { expanded.value = !expanded.value }
            ) {
                Icon(
                    if (expanded.value) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                    contentDescription = if (expanded.value) "Mostrar menos" else "Mostrar mas"
                )
            }
        }
    }
}


