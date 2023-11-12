package com.example.youthconnect

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.youthconnect.Model.Course
import com.example.youthconnect.Model.DataBase
import com.example.youthconnect.Model.Users.Child
import com.example.youthconnect.Model.Users.Parent
import com.example.youthconnect.ui.theme.YouthconnectTheme

class SignUp : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            YouthconnectTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    SignUpScreen()
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable

fun SignupScreenPreview(){
    YouthconnectTheme {

        var parentFullName by remember { mutableStateOf("") }
        var parentID  by remember { mutableStateOf("") }
        var parentPhoneNumber  by remember { mutableStateOf("") }
        var password  by remember { mutableStateOf("") }

        var childFullName  by remember { mutableStateOf("") }
        var childID  by remember { mutableStateOf("")}
        var childCourse  by remember { mutableStateOf("") }
        var childPassword  by remember { mutableStateOf("") }
        var belongsToSchool  by remember { mutableStateOf(false) }
        var faithGroups  by remember { mutableStateOf(false) }
        var goOutAlone  by remember { mutableStateOf(false) }
        var observations  by remember { mutableStateOf("") }

        FirstSignupFormScreen(
            parentFullName = parentFullName,
            parentID = parentID,
            parentPhoneNumber = parentPhoneNumber,
            password = password,
            childFullName = childFullName,
            childID = childID,
            childCourse = childCourse,
            childPassword = childPassword,
            belongsToSchool = belongsToSchool,
            faithGroups = faithGroups,
            goOutAlone  = goOutAlone,
            observations = observations,
            onParentFullNameChange = { parentFullName = it },
            onParentIDChange = { parentID = it },
            onParentPhoneNumberChange = { parentPhoneNumber = it },
            onPasswordChange = { password = it },
            onChildFullNameChange = { childFullName = it },
            onChildIDChange = { childID = it },
            onChildCourseChange = { childCourse = it.name },
            onChildPasswordChange = { childPassword = it },
            onBelongsToSchool = {belongsToSchool = it},
            onFaithGroups = { faithGroups = it },
            onGoOutAlone = { goOutAlone = it},
            onObservations = { observations = it}
        )

    }
}

@SuppressLint("RememberReturnType")
@Composable
fun SignUpScreen(
    modifier: Modifier = Modifier
        .width(320.dp)
        .height(568.dp)
) {
    val brush = Brush.horizontalGradient(
        listOf(
            Color(0xFFE15554),
            Color(0xFF3BB273),
            Color(0xFFE1BC29),
            Color(0xFF4D9DE0)
        ))



    var parentFullName by remember { mutableStateOf("") }
    var parentID  by remember { mutableStateOf("") }
    var parentPhoneNumber  by remember { mutableStateOf("") }
    var password  by remember { mutableStateOf("") }

    var childFullName  by remember { mutableStateOf("") }
    var childID  by remember { mutableStateOf("")}
    var childCourse  by remember { mutableStateOf("") }
    var childPassword  by remember { mutableStateOf("") }
    var belongsToSchool  by remember { mutableStateOf(false) }
    var faithGroups  by remember { mutableStateOf(false) }
    var goOutAlone  by remember { mutableStateOf(false) }
    var observations  by remember { mutableStateOf("") }

    Box(modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.TopCenter,
    ) {


        Canvas(
            modifier = Modifier.fillMaxSize(), // Llena todo el espacio del Box
            onDraw = {
                drawRect(brush)
            }
        )
        Column(modifier = Modifier.fillMaxSize()){

            Box(modifier = Modifier.wrapContentSize(),
                contentAlignment = Alignment.TopCenter ){
                Image(
                    painter = painterResource(id = R.drawable.background_signup),
                    contentDescription = "image description",
                    contentScale = ContentScale.FillWidth ,
                    modifier = Modifier
                        .fillMaxWidth()
                )

                Column (horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.padding(vertical = 20.dp)){

                    Text(
                        text = "Signup",
                        style = TextStyle(
                            fontSize = 40.sp,
                            fontFamily = FontFamily(Font(R.font.annie_use_your_telescope)),
                            fontWeight = FontWeight(400),
                            color = Color(0xFF000000),
                            letterSpacing = 0.9.sp,
                        ),
                        modifier = Modifier.padding(bottom = 15.dp)
                    )

                    FirstSignupFormScreen(
                        parentFullName = parentFullName,
                        parentID = parentID,
                        parentPhoneNumber = parentPhoneNumber,
                        password = password,
                        childFullName = childFullName,
                        childID = childID,
                        childCourse = childCourse,
                        childPassword = childPassword,
                        belongsToSchool = belongsToSchool,
                        faithGroups = faithGroups,
                        goOutAlone  = goOutAlone,
                        observations = observations,
                        onParentFullNameChange = { parentFullName = it },
                        onParentIDChange = { parentID = it },
                        onParentPhoneNumberChange = { parentPhoneNumber = it },
                        onPasswordChange = { password = it },
                        onChildFullNameChange = { childFullName = it },
                        onChildIDChange = { childID = it },
                        onChildCourseChange = { childCourse = it.name },
                        onChildPasswordChange = { childPassword = it },
                        onBelongsToSchool = {belongsToSchool = it},
                        onFaithGroups = { faithGroups = it },
                        onGoOutAlone = { goOutAlone = it},
                        onObservations = { observations = it}
                    )


                }
            }




            val mcontext = LocalContext.current
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.BottomEnd,
            ) {

                Column {

                    Button(
                        onClick = {
                            val parentsID : List<String>  = listOf(parentID)
                            val parent = Parent(parentFullName,
                                parentID,
                                parentPhoneNumber.toInt(),
                                password)

                            val child = Child(childFullName,
                                childID,
                                childCourse,
                                childPassword,
                                belongsToSchool,
                                faithGroups,
                                goOutAlone,
                                observations,
                                parentsID,
                                "745896H")
                            val dataBase = DataBase()

                            dataBase.addParents(parent)
                            dataBase.addParentAccount(parent)

                            dataBase.addChild(child)
                            dataBase.addChildAccount(child)




                            mcontext.startActivity(Intent(mcontext,MainActivity::class.java))

                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp)
                            .shadow(elevation = 14.dp)
                    ) {
                        Text(
                            text = "Next",
                            style = TextStyle(
                                fontSize = 20.sp,
                                fontFamily = FontFamily.Default,
                                fontWeight = FontWeight(400),
                                color = Color(0xFFFFFFFF),
                                letterSpacing = 0.3.sp,
                            ),
                            color = Color(0xFF000000),
                            textAlign = TextAlign.Center
                        )
                    }

                    Box(modifier = Modifier.wrapContentSize(),
                        contentAlignment = Alignment.BottomCenter ) {

                    }



                    Image(
                        painter = painterResource(id = R.drawable.kids_signup),
                        contentDescription = "image description",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                }

            }
        }




    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FirstSignupFormScreen(
    parentFullName: String,
    parentID: String,
    parentPhoneNumber : String,
    password: String,
    childFullName: String,
    childID: String,
    childCourse: String,
    childPassword: String,
    belongsToSchool : Boolean,
    faithGroups : Boolean,
    goOutAlone : Boolean,
    observations : String,
    onParentFullNameChange: (String) -> Unit,
    onParentIDChange: (String) -> Unit,
    onParentPhoneNumberChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onChildFullNameChange: (String) -> Unit,
    onChildIDChange: (String) -> Unit,
    onChildCourseChange: (Course) -> Unit,
    onChildPasswordChange: (String) -> Unit,
    onBelongsToSchool : (Boolean) -> Unit,
    onFaithGroups : (Boolean) -> Unit,
    onGoOutAlone : (Boolean) -> Unit,
    onObservations : (String) -> Unit
) {
    val mcontext = LocalContext.current

    LazyColumn(
        modifier = Modifier
            .height(400.dp)
            .padding(16.dp)
    ) {
        item {
            OutlinedTextField(
                value = childFullName,
                onValueChange = { onChildFullNameChange(it) },
                label = { Text("Child's full name") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp)
            )
        }

        item {
            CoursesSelectionScreen(childCourse, onChildCourseChange)
        }

        item {
            OutlinedTextField(
                value = childID,
                onValueChange = {onChildIDChange(it)},
                label = { Text("Child's ID") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp)
            )
        }

        item {
            OutlinedTextField(
                value = childPassword,
                onValueChange = {onChildPasswordChange(it)},
                label = { Text("Password") },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Password
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp)
            )
        }

        item {
            CustomRadioButton(belongsToSchool, onBelongsToSchool = onBelongsToSchool, "Belongs to the school?")
        }

        item {
            OutlinedTextField(
                value = parentFullName,
                onValueChange = {onParentFullNameChange(it)},
                label = { Text("Parent's full name") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp)
            )
        }

        item {
            OutlinedTextField(
                value = parentID,
                onValueChange = {onParentIDChange(it)},
                label = { Text("Parent's ID") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp)
            )
        }

        item {
            OutlinedTextField(
                value = parentPhoneNumber,
                onValueChange = {onParentPhoneNumberChange(it) },
                label = { Text("Parent's Phone Number") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp)
            )
        }


        item {
            OutlinedTextField(
                value = password,
                onValueChange = {onPasswordChange(it)},
                label = { Text("Password") },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Password
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp)
            )
        }


        item {
            CustomRadioButton(faithGroups,  onFaithGroups, "Do you want to enroll\nyour child in faith groups?")
        }

        item {
            CustomRadioButton(goOutAlone,  onGoOutAlone, "Can the child go out alone?")
        }

        item {
            OutlinedTextField(
                value = observations,
                onValueChange = {onObservations(it) },
                label = { Text("Observations") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp)
            )
        }




    }
}





@Composable
fun CustomRadioButton(belongsToSchool: Boolean,
                onBelongsToSchool : (Boolean) -> Unit, question : String){

    val yes = remember { mutableStateOf(belongsToSchool) }
    val no = remember { mutableStateOf(!belongsToSchool) }
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxWidth()
    ) {
        val text = question
        Text(
            text = buildAnnotatedString {
                withStyle(style = ParagraphStyle(lineHeight = 20.sp)) {
                    append(text)
                }
            },
            fontSize = 18.sp,
            textAlign = TextAlign.Center,
        )

        Column(
            modifier = Modifier.wrapContentSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            RadioButton(selected = yes.value,
                onClick = {
                    yes.value = true
                    no.value = false
                    onBelongsToSchool(yes.value)
                })
            Text(text = "Yes", fontSize = 18.sp)
        }

        Column(
            modifier = Modifier.wrapContentSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            RadioButton(
                selected = no.value ,
                onClick = {
                    yes.value = false
                    no.value = true
                    onBelongsToSchool(no.value) })
            Text(text = "No", fontSize = 18.sp)
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CoursesSelectionScreen(childCourse: String, onChildCourseChange : (Course) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    var selectedText by remember { mutableStateOf(Course.values()[0]) }

            // Dropdown menu
            OutlinedTextField(
                value = selectedText.name,
                onValueChange = {},
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expanded = true },
                label = { Text("Select a course") },
                readOnly = true,
                trailingIcon = {
                    Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = null,
                        modifier = Modifier.clickable { expanded = true })
                }
            )

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.fillMaxWidth()
                   .clip(RoundedCornerShape(20.dp)) // Asegura que el DropdownMenu ocupe el ancho máximo disponible
            ) {
                Course.values().forEach { course ->
                    DropdownMenuItem(
                        modifier = Modifier
                            .fillMaxWidth(), // Hace que el DropdownMenuItem ocupe el ancho máximo disponible
                        text = { Text(text = course.name) },
                        onClick = {
                            selectedText = course
                            expanded = false
                            onChildCourseChange(course)
                        }
                    )
                }
            }

}

