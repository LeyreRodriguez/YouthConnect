package com.example.youthconnect.View.BottomNavigationScreens

import android.util.Patterns
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Password
import androidx.compose.material.icons.filled.PermIdentity
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.youthconnect.Model.Enum.Course
import com.example.youthconnect.Model.Enum.NavScreen
import com.example.youthconnect.Model.Object.Child
import com.example.youthconnect.Model.Object.Parent
import com.example.youthconnect.R
import com.example.youthconnect.ViewModel.signUpViewModel
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpView( navController: NavController) {

    val signUpViewModel : signUpViewModel = hiltViewModel()


    //TODO Ver diferencias entre observeAsState() y collectAsState() y entender cual es mejor
    val shouldNavigate by signUpViewModel.navigateToNextScreen.collectAsState()
    val showFirstScreen by signUpViewModel.showFirstScreen2.collectAsState()



    var validateParentFullName by rememberSaveable { mutableStateOf(true) }
    var validateParentID by rememberSaveable { mutableStateOf(true) }
    var validateParentPhoneNumber by rememberSaveable { mutableStateOf(true) }
    var validateParentPassword by rememberSaveable { mutableStateOf(true) }
    var validateChildFullName by rememberSaveable { mutableStateOf(true) }
    var validateChildID by rememberSaveable { mutableStateOf(true) }
    var validateChildPassword by rememberSaveable { mutableStateOf(true) }
    var isChildPasswordVisible by rememberSaveable { mutableStateOf(false) }
    var isParentPasswordVisible by rememberSaveable { mutableStateOf(false) }
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
    val focusManager = LocalFocusManager.current
    val mcontext = LocalContext.current
    val passwordRegex = "(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#%$^&+=]).{8,}".toRegex()
    val IDRegex = "^[0-9]{8}[A-Za-z]$".toRegex()
    validateParentFullName = parentFullName.isNotBlank()
    validateParentID = parentID.matches(IDRegex)
    validateParentPhoneNumber = Patterns.PHONE.matcher(parentPhoneNumber).matches()
    validateParentPassword = password.matches(passwordRegex)
    validateChildFullName = childFullName.isNotBlank()
    validateChildID = childID.matches(IDRegex)
    validateChildPassword = childPassword.matches(passwordRegex)

    val validateParentFullNameError = "Please, input a a valid name"
    val validateParentIDError = "The format of the ID doesn´t seem right"
    val validateParentPhoneNumberError = "The format of the phone number doesn´t seem right"
    val validateParentPasswordError = "Must mix capital and non-capital letters, a number, special character and minimun length of 8"
    val validateChildFullNameError = "Please, input a valid ID"
    val validateChildIDError = "The format of the ID doesn´t seem right"
    val validateChildCourseError = "You have to choose one course"
    val validateChildPasswordError = "Must mix capital and non-capital letters, a number, special character and minimun length of 8"

    fun validateData(parentFullName: String,
                     parentID: String,
                     parentPhoneNumber: String,
                     parentPassword : String,
                     childFullName: String,
                     childCourse: String,
                     childID: String,
                     childPassword : String) : Boolean{
        val passwordRegex = "(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#%$^&+=.]).{8,}".toRegex()

        val IDRegex = "^[0-9]{8}[A-Za-z]$".toRegex()
        validateParentFullName = parentFullName.isNotBlank()
        validateParentID = parentID.matches(IDRegex)
        validateParentPhoneNumber = Patterns.PHONE.matcher(parentPhoneNumber).matches()
        validateParentPassword = parentPassword.matches(passwordRegex)
        validateChildFullName = childFullName.isNotBlank()
        validateChildID = childID.matches(IDRegex)
        validateChildPassword = childPassword.matches(passwordRegex)
        return validateParentFullName && validateParentID && validateParentPhoneNumber && validateParentPassword && validateChildFullName && validateChildID && validateChildPassword
    }
    fun register(parentFullName: String,
                 parentID: String,
                 parentPhoneNumber: String,
                 parentPassword : String,
                 childFullName: String,
                 childCourse: String,
                 childID: String,
                 childPassword : String)
    {
        if (validateData(parentFullName, parentID, parentPhoneNumber, parentPassword, childFullName, childCourse, childID, childPassword)){

            val parentsID : List<String>  = listOf(parentID)
            val parent = Parent(parentFullName,
                parentID,
                parentPhoneNumber,
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
                "",
                false)
            signUpViewModel.registerUser(parentID, password)
            signUpViewModel.registerUser(childID, childPassword)

            signUpViewModel.addParent(parent)
            signUpViewModel.addChild(child)

            navController.navigate(NavScreen.NewsScreen.name)
        }else{
            Toast.makeText(mcontext,"Please, review fields", Toast.LENGTH_SHORT).show()
        }
    }

    LaunchedEffect(shouldNavigate) {
        if (shouldNavigate == true) {
            navController.navigate("login") {
                // Configuraciones adicionales de navegación si las necesitas
                popUpTo("seconScreens") { inclusive = true }
            }
        }
    }

    BackHandler {
        if(showFirstScreen){
            navController.navigate("login") {
                // Configuraciones adicionales de navegación si las necesitas
                popUpTo("signUp") { inclusive = true }
            }
        }else{
            signUpViewModel.changeScreen()
        }

    }

    val brush = Brush.horizontalGradient(
        listOf(
            Color(0xFFE15554),
            Color(0xFF3BB273),
            Color(0xFFE1BC29),
            Color(0xFF4D9DE0)
        ))
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

                    Column {
                        LazyColumn(
                            modifier = Modifier
                                .height(400.dp)
                                .padding(16.dp)
                        ) {
                            item {

                                CustomOutlinedTextField(
                                    value = childFullName,
                                    onValueChange = {childFullName = it},
                                    label = "Child's full name",
                                    showError = !validateChildFullName,
                                    errorMessage = validateChildFullNameError  ,
                                    leadingIconImageVector = Icons.Default.PermIdentity,
                                    keyboardOptions = KeyboardOptions(
                                        keyboardType = KeyboardType.Text,
                                        imeAction = ImeAction.Next
                                    ),
                                    keyboardActions = KeyboardActions(
                                        onNext = {focusManager.moveFocus(FocusDirection.Down)}
                                    )
                                )
                            }
                            item {
                                CoursesSelectionScreen(childCourse, { childCourse = it.name})
                            }
                            item {
                                CustomOutlinedTextField(
                                    value = childID,
                                    onValueChange = {childID = it },
                                    label = "Child's ID",
                                    showError = !validateChildID,
                                    errorMessage = validateChildIDError ,
                                    leadingIconImageVector = Icons.Default.CreditCard ,
                                    keyboardOptions = KeyboardOptions(
                                        keyboardType = KeyboardType.Text,
                                        imeAction = ImeAction.Next
                                    ),
                                    keyboardActions = KeyboardActions(
                                        onNext = {focusManager.moveFocus(FocusDirection.Down)}
                                    )
                                )
                            }
                            item {
                                CustomOutlinedTextField(
                                    value = childPassword,
                                    onValueChange = {childPassword = it },
                                    label = "Child's Password",
                                    showError = !validateChildPassword,
                                    errorMessage =validateChildPasswordError ,
                                    isPasswordField = true,
                                    isPasswordVisible = isChildPasswordVisible,
                                    onVisibilityChange = {isChildPasswordVisible = it},
                                    leadingIconImageVector = Icons.Default.Password ,
                                    keyboardOptions = KeyboardOptions(
                                        keyboardType = KeyboardType.Text,
                                        imeAction = ImeAction.Next
                                    ),
                                    keyboardActions = KeyboardActions(
                                        onNext = {focusManager.moveFocus(FocusDirection.Down)}
                                    )
                                )
                            }
                            item {
                                CustomRadioButton(belongsToSchool, onBelongsToSchool = { belongsToSchool = it }, "Belongs to the school?")
                            }
                            item {
                                CustomOutlinedTextField(
                                    value = parentFullName,
                                    onValueChange = {parentFullName = it},
                                    label = "Parent's full name",
                                    showError = !validateParentFullName,
                                    errorMessage = validateParentFullNameError ,
                                    leadingIconImageVector = Icons.Default.PermIdentity ,
                                    keyboardOptions = KeyboardOptions(
                                        keyboardType = KeyboardType.Text,
                                        imeAction = ImeAction.Next
                                    ),
                                    keyboardActions = KeyboardActions(
                                        onNext = {focusManager.moveFocus(FocusDirection.Down)}
                                    )
                                )
                            }
                            item {
                                CustomOutlinedTextField(
                                    value = parentID,
                                    onValueChange = {parentID = it},
                                    label = "Parent's ID",
                                    showError = !validateParentID,
                                    errorMessage = validateParentIDError ,
                                    leadingIconImageVector = Icons.Default.CreditCard ,
                                    keyboardOptions = KeyboardOptions(
                                        keyboardType = KeyboardType.Text,
                                        imeAction = ImeAction.Next
                                    ),
                                    keyboardActions = KeyboardActions(
                                        onNext = {focusManager.moveFocus(FocusDirection.Down)}
                                    )
                                )
                            }
                            item {
                                CustomOutlinedTextField(
                                    value = parentPhoneNumber,
                                    onValueChange = {parentPhoneNumber = it},
                                    label = "Parent's Phone Number",
                                    showError = !validateParentPhoneNumber,
                                    errorMessage = validateParentPhoneNumberError ,
                                    leadingIconImageVector = Icons.Default.Phone ,
                                    keyboardOptions = KeyboardOptions(
                                        keyboardType = KeyboardType.Phone,
                                        imeAction = ImeAction.Next
                                    ),
                                    keyboardActions = KeyboardActions(
                                        onNext = {focusManager.moveFocus(FocusDirection.Down)}
                                    )
                                )
                            }
                            item {
                                CustomOutlinedTextField(
                                    value = password,
                                    onValueChange = {password = it},
                                    label = "Password",
                                    showError = !validateParentPassword,
                                    errorMessage = validateParentPasswordError ,
                                    isPasswordField = true,
                                    isPasswordVisible = isParentPasswordVisible,
                                    onVisibilityChange = {isParentPasswordVisible = it},
                                    leadingIconImageVector = Icons.Default.Password ,
                                    keyboardOptions = KeyboardOptions(
                                        keyboardType = KeyboardType.Text,
                                        imeAction = ImeAction.Done
                                    ),
                                    keyboardActions = KeyboardActions(
                                        onNext = {focusManager.clearFocus()}
                                    )
                                )
                            }
                            item {
                                CustomRadioButton(faithGroups,  { faithGroups = it }, "Do you want to enroll\nyour child in faith groups?")
                            }
                            item {
                                CustomRadioButton(goOutAlone, { goOutAlone = it }, "Can the child go out alone?")
                            }
                            item {
                                OutlinedTextField(
                                    value = observations ?: "",
                                    onValueChange = {observations = it },
                                    label = { Text("Observations") },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(bottom = 10.dp)
                                )
                            }
                        }


                        Button(
                            onClick = {
                                register(parentFullName, parentID, parentPhoneNumber, password, childFullName,childCourse, childID, childPassword)

                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(20.dp)
                                .shadow(elevation = 14.dp)
                        ) {
                            Text(
                                text = "Sign up",
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
                    }




                }
            }
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.BottomEnd,
            ) {
                Column {
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
fun CustomOutlinedTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String = "",
    leadingIconImageVector: ImageVector,
    leadingIconDescription: String = "",
    isPasswordField: Boolean = false,
    isPasswordVisible: Boolean = false,
    onVisibilityChange: (Boolean) -> Unit = {},
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    showError: Boolean = false,
    errorMessage: String = ""
){
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){
        OutlinedTextField(
            value = value,
            onValueChange = { onValueChange(it)},
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 10.dp),
            label = { Text(label)},
            leadingIcon = {
                Icon(
                    imageVector = leadingIconImageVector,
                    contentDescription = leadingIconDescription,
                    tint = if (showError) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface
                )
            },
            isError = showError,
            trailingIcon = {
                if (showError && !isPasswordField ) Icon(imageVector = Icons.Filled.Error, contentDescription = "Show error icon")
                if (isPasswordField){
                    IconButton(onClick = { onVisibilityChange(!isPasswordVisible) }) {
                        Icon(
                            imageVector = if (isPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = "Toggle password visibility"
                        )
                    }
                }
            },
            visualTransformation = when {
                isPasswordField && isPasswordVisible -> VisualTransformation.None
                isPasswordField -> PasswordVisualTransformation()
                else -> VisualTransformation.None
            },
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            singleLine = true
        )
        if (showError) {
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error ,
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier
                    .padding(start = 8.dp)
                    .offset(y = (-8).dp)
                    .fillMaxWidth(0.9f)
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
        modifier = Modifier
            .fillMaxWidth()
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