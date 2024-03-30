package com.example.youthconnect.View.BottomNavigationScreens

import android.annotation.SuppressLint
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.PopupProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.youthconnect.Model.Enum.NavScreen
import com.example.youthconnect.Model.Object.Child
import com.example.youthconnect.Model.Object.Instructor
import com.example.youthconnect.R
import com.example.youthconnect.ViewModel.UserViewModel
import com.example.youthconnect.ViewModel.signUpViewModel
import com.example.youthconnect.ui.theme.Green
import com.example.youthconnect.ui.theme.Red


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
                .weight(1f) // Para que el TextField ocupe todo el espacio disponible
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

    val UserViewModel : UserViewModel = hiltViewModel()
    var childs by remember { mutableStateOf<List<Child?>>(emptyList()) }
    var instructor by remember { mutableStateOf<Instructor?>(null) }
    var myKids by remember { mutableStateOf<List<Child?>>(emptyList()) }


    var user by remember { mutableStateOf<String?>("") }

    val documentExists = remember { mutableStateOf("-1") }
    var result by remember { mutableStateOf<String?>("") }



    LaunchedEffect(UserViewModel) {
        try {
            childs = UserViewModel.getAllChildren()
            instructor = UserViewModel.getCurrentInstructorById(instructorID)
            myKids = UserViewModel.getChildByInstructorId(instructorID)
            user = UserViewModel.getCurrentUser()
            result = user?.let { UserViewModel.findDocument(it) }

            if (result != null) {
                documentExists.value = result.toString()
            }

        } catch (e: Exception) {
            Log.e("Firestore", "Error en ChildList", e)
        }
    }
        Box(
            modifier = Modifier.fillMaxSize(),
        ) {
            Canvas(
                modifier = Modifier.fillMaxSize(),
                onDraw = {
                    // Dibuja un rectángulo blanco como fondo
                    drawRect(Color.White)

                    // Define el pincel para el borde con el gradiente del Brush
                    val borderBrush = Brush.horizontalGradient(
                        listOf(
                            Color(0xFFE15554),
                            Color(0xFF3BB273),
                            Color(0xFFE1BC29),
                            Color(0xFF4D9DE0)
                        )
                    )

                    // Dibuja el borde con el pincel definido
                    drawRect(
                        brush = borderBrush,
                        topLeft = Offset(0f, 0f),
                        size = Size(size.width, size.height),
                        style = Stroke(width = 15.dp.toPx()) // Ancho del borde
                    )
                }
            )


            Column(modifier = Modifier.fillMaxHeight()) {

                Row (modifier = Modifier
                    .fillMaxWidth()
                    .padding(30.dp),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically){

                    if (user.toString().isNotEmpty()) {
                        userImage(user = user.toString(), navController = navController , documentExists.value)
                    }


                }
                Column(horizontalAlignment = Alignment.CenterHorizontally){
                    Text(
                        text = "Children list",
                        style = TextStyle(
                            fontSize = 30.sp,
                            fontFamily = FontFamily(Font(R.font.annie_use_your_telescope)),
                            fontWeight = FontWeight(400),
                            color = Color(0xFF000000),
                            letterSpacing = 0.9.sp,
                        ), textAlign = TextAlign.Center
                    )


                    val textState = remember { mutableStateOf(TextFieldValue(""))}

                    SearchView(state= textState, placeHolder= "Search here...", modifier = Modifier)

                    val searchedText = textState.value.text


                    LazyColumn(modifier = Modifier.padding(10.dp)) {
                        items(items = childs.filter {
                            it?.FullName?.contains(searchedText, ignoreCase = true) ?: false
                        }, key = { it?.ID ?: "" }) { item ->
                            if (item != null) {
                               // MyChildren(navController = navController, item)
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

    val SignUpViewModel: signUpViewModel = hiltViewModel()
    val UserViewModel: UserViewModel = hiltViewModel()
    var isChecked by remember { mutableStateOf(false) }



    var myKids by remember { mutableStateOf<List<Child?>>(emptyList()) }
    var instructorID by remember { mutableStateOf("") }

    val documentExists = remember { mutableStateOf("-1") }
    var result by remember { mutableStateOf<String?>("") }
    var showDialog by remember { mutableStateOf(false)  }
    var user by remember { mutableStateOf<String?>("") }



    LaunchedEffect(UserViewModel) {
        try {
            instructorID = UserViewModel.getCurrentUser().toString()

            myKids = instructorID?.let { UserViewModel.getChildByInstructorId(it) }!!
           // myKids = UserViewModel.getAllChildren()

            user = UserViewModel.getCurrentUser()
            result = user?.let { UserViewModel.findDocument(it) }

            if (result != null) {
                documentExists.value = result.toString()
            }
        } catch (e: Exception) {
            Log.e("Firestore", "Error en ChildList", e)
        }
    }

//
  //  LaunchedEffect(key1 = child.ID, key2 = myKids) {
    //    isChecked = myKids.any { it?.ID == child.ID }
    //}


    val imageUrlState = remember { mutableStateOf("") }
    LaunchedEffect(Unit) {
        UserViewModel.getProfileEspecificImage(child.ID.lowercase() + "@youthconnect.com",
            onSuccess = { url ->
                imageUrlState.value = url
            },
            onFailure = { exception ->
                // Manejar el error, por ejemplo, mostrar un mensaje
            }
        )
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                navController.navigate("child_profile_screen/${child.ID}")
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
                        BorderStroke(4.dp, SolidColor(if (child.GoOutAlone) Green else Red)),
                        CircleShape
                    )
                    .padding(4.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )

            Text(
                text = child.FullName,
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

            if (documentExists.value == "0" && currentRoute != NavScreen.ChildList.name +"/{instructorID}") {

                Checkbox(
                    checked = isChecked,
                    onCheckedChange = { newCheckedState ->
                        isChecked = newCheckedState
                        if (instructorID != null) {
                            SignUpViewModel.rollCall(child, newCheckedState)
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

    val SignUpViewModel: signUpViewModel = hiltViewModel()
    val UserViewModel: UserViewModel = hiltViewModel()
    var isChecked by remember { mutableStateOf(false) }

    var expandedMenu by remember { mutableStateOf(false) }

    var myKids by remember { mutableStateOf<List<Child?>>(emptyList()) }
    var instructorID by remember { mutableStateOf("") }

    val documentExists = remember { mutableStateOf("-1") }
    var result by remember { mutableStateOf<String?>("") }
    var showDialog by remember { mutableStateOf(false)  }
    var user by remember { mutableStateOf<String?>("") }
    var instructor by remember { mutableStateOf<Instructor?>(null) }
    var instructorsList by remember { mutableStateOf<List<Instructor?>>(emptyList()) }



    LaunchedEffect(UserViewModel) {
        try {
            instructorID = UserViewModel.getCurrentUser().toString()

            myKids = instructorID?.let { UserViewModel.getChildByInstructorId(it) }!!
            // myKids = UserViewModel.getAllChildren()

            user = UserViewModel.getCurrentUser()
            result = user?.let { UserViewModel.findDocument(it) }

            if (result != null) {
                documentExists.value = result.toString()
            }
            instructor = UserViewModel.getInstructorByChildId(child.ID)
            instructorsList = UserViewModel.getAllInstructors()
        } catch (e: Exception) {
            Log.e("Firestore", "Error en ChildList", e)
        }
    }

    val imageUrlState = remember { mutableStateOf("") }
    LaunchedEffect(Unit) {
        UserViewModel.getProfileEspecificImage(child.ID.lowercase() + "@youthconnect.com",
            onSuccess = { url ->
                imageUrlState.value = url
            },
            onFailure = { exception ->
                // Manejar el error, por ejemplo, mostrar un mensaje
            }
        )
    }

    Surface(
        //color = MaterialTheme.colorScheme.primary,
        modifier = modifier.padding(vertical = 4.dp, horizontal = 8.dp),
        onClick = {navController.navigate("child_profile_screen/${child.ID}")}
    ) {

        Row(modifier = Modifier.padding(24.dp)) {

            AsyncImage(
                model = imageUrlState.value,
                contentDescription = "Profile Picture",
                modifier = Modifier
                    .size(50.dp)
                    .border(
                        BorderStroke(4.dp, SolidColor(if (child.GoOutAlone) Green else Red)),
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
                Text(text = child.FullName,  fontWeight = FontWeight.Bold)
                if (expanded.value) {
                    Text(text = "Instructor: ")
                    val mcontext = LocalContext.current
                    instructor?.let {
                        CustomDropdownMenu(instructorsList, it.FullName, Color.DarkGray, onSelected = { selectedOption ->
                            UserViewModel.changeInstructor(child, instructorID)
                        })
                    }
                    if(child.FaithGroups){
                        Text(text = "Belongs to faith groups")
                    }else{
                        Text(text = "Does not belong to a faith group")
                    }

                    if(child.BelongsToSchool){
                        Text(text = "Belongs to school")
                    }else{
                        Text(text = "Does not belong to school")
                    }
                    child.Observations?.let { Text(text = it) }

                }
            }
            IconButton(
                onClick = { expanded.value = !expanded.value }
            ) {
                Icon(
                    if (expanded.value) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                    contentDescription = if (expanded.value) "Show less" else "Show more"
                )
            }
        }
    }
}




@Composable
fun CustomDropdownMenu(
    list: List<Instructor?>, // Menu Options
    defaultSelected: String, // Default Selected Option on load
    color: Color, // Color
    modifier: Modifier = Modifier, // Modifier
    onSelected: (String) -> Unit, // Pass the Selected Option
) {
    var selectedIndex by remember { mutableStateOf(0) }
    var expand by remember { mutableStateOf(false) }
    var stroke by remember { mutableStateOf(1) }
    var selectedOption by remember(defaultSelected) { mutableStateOf(defaultSelected) }

    Box(
        modifier = modifier
            .padding(8.dp)
            .border(
                BorderStroke(stroke.dp, color),
                shape = RoundedCornerShape(4.dp)
            )
            .clickable {
                expand = !expand
                stroke = if (expand) 2 else 1
            },
        contentAlignment = Alignment.Center
    ) {

        Text(
            text = selectedOption,
            color = color,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp)
        )

        DropdownMenu(
            expanded = expand,
            onDismissRequest = {
                expand = false
                stroke = 1
            },
            properties = PopupProperties(
                focusable = false,
                dismissOnBackPress = true,
                dismissOnClickOutside = true,
            ),
            modifier = Modifier
                .background(Color.White)
                .padding(2.dp)
                .fillMaxWidth(.4f)
        ) {
            list.forEachIndexed { index, item ->
                DropdownMenuItem(
                    text = {
                        if (item != null) {
                            Text(
                                text = item.FullName,
                                color = color,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    },
                    onClick = {
                        selectedIndex = index
                        if (item != null) {
                            selectedOption = item.FullName
                        }
                        onSelected(selectedOption)
                        expand = false
                        stroke = 1
                    })

            }
        }
    }
}