package com.example.youthconnect.View.Authentication

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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.youthconnect.Model.Enum.Course
import com.example.youthconnect.Model.Enum.NavScreen
import com.example.youthconnect.Model.Object.Child
import com.example.youthconnect.Model.Object.Parent
import com.example.youthconnect.R
import com.example.youthconnect.ViewModel.SignUpViewModel
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpView( navController: NavController) {

    val signUpViewModel : SignUpViewModel = hiltViewModel()


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


    val validateParentFullNameError = "Por favor, introduce un nombre válido"
    val validateParentIDError =  "El formato del DNI no es adecuado"
    val validateParentPhoneNumberError = "El formato del número de teléfono no es adecuado"
    val validateParentPasswordError = "Debe mezclar letras mayusculas y minusculas, numeros, caracteres especiales y tener una logitud minima de 8 caracteres"
    val validateChildFullNameError = "Por favor, introduce un DNI válido"
    val validateChildIDError = "El formato del DNI no es adecuado"
    val validateChildPasswordError = "Debe mezclar letras mayusculas y minusculas, numeros, caracteres especiales y tener una logitud minima de 8 caracteres"

    fun validateData(parentFullName: String,
                     parentID: String,
                     parentPhoneNumber: String,
                     parentPassword : String,
                     childFullName: String,
                     childID: String,
                     childPassword : String) : Boolean{
        val passwordRegex = "(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#%\$^&+=./\\\\_-]).{8,}".toRegex()

        val idRegex = "^[0-9]{8}[A-Za-z]$".toRegex()

        validateParentFullName = parentFullName.isNotBlank()
        validateParentID = parentID.matches(idRegex)
        validateParentPhoneNumber = Patterns.PHONE.matcher(parentPhoneNumber).matches()
        validateParentPassword = parentPassword.matches(passwordRegex)
        validateChildFullName = childFullName.isNotBlank()
        validateChildID = childID.matches(idRegex)
        validateChildPassword = childPassword.matches(passwordRegex)

        return validateParentFullName && validateParentID && validateParentPhoneNumber && validateParentPassword && validateChildFullName && validateChildID && validateChildPassword
    }
    fun register(parentFullName: String,
                 parentID: String,
                 parentPhoneNumber: String,
                 parentPassword : String,
                 childFullName: String,
                 childID: String,
                 childPassword : String)
    {
        if (validateData(parentFullName, parentID, parentPhoneNumber, parentPassword, childFullName, childID, childPassword)){

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
            signUpViewModel.registerUser(childID, childPassword)
            signUpViewModel.signOut()
            signUpViewModel.registerUser(parentID, password)



            signUpViewModel.addChild(child)
            signUpViewModel.addParent(parent)


            navController.navigate(NavScreen.NewsScreen.name)
        }else{
            Toast.makeText(mcontext,"Por favor, revisa los campos", Toast.LENGTH_SHORT).show()
        }
    }

    LaunchedEffect(shouldNavigate) {
        if (shouldNavigate == true) {
            navController.navigate("login") {
                popUpTo("secondScreens") { inclusive = true }
            }
        }
    }

    BackHandler {
        if(showFirstScreen){
            navController.navigate("login") {
                popUpTo("signUp") { inclusive = true }
            }
        }else{
            signUpViewModel.changeScreen()
        }

    }

    var isPolicy by remember { mutableStateOf(true) }


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
            modifier = Modifier.fillMaxSize(),
            onDraw = {
                drawRect(brush)
            }
        )

        if(isPolicy){
            Policy(navController){
                isPolicy = false
            }
        }

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
                        text = "Registrarse",
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
                                    label = "Nombre completo del niño/niña",
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
                                CoursesSelectionScreen(Course.CuartoEP){  childCourse = it.name }


                            }
                            item {
                                CustomOutlinedTextField(
                                    value = childID,
                                    onValueChange = {childID = it },
                                    label = "DNI del niño/niña",
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
                                    label = "Contraseña del niño/niña",
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
                                CustomRadioButton(belongsToSchool, onBelongsToSchool = { belongsToSchool = it }, "¿Pertenece al colegio?")
                            }
                            item {
                                CustomOutlinedTextField(
                                    value = parentFullName,
                                    onValueChange = {parentFullName = it},
                                    label = "Nombre completo del padre/madre/tutor",
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
                                    label = "DNI del padre/madre/tutor",
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
                                    label = "Número de teléfono del padre/madre/tutor",
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
                                    label = "Contraseña del padre/madre/tutor",
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
                                CustomRadioButton(faithGroups,  { faithGroups = it }, "¿Quieres apuntar a \n tu hijo/hija en grupos de fe?")
                            }
                            item {
                                CustomRadioButton(goOutAlone, { goOutAlone = it }, "¿Puede tu hijo/hija salir solo del centro?")
                            }
                            item {
                                OutlinedTextField(
                                    value = observations ?: "",
                                    onValueChange = {observations = it },
                                    label = { Text("Observaciones") },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(bottom = 10.dp)
                                )
                            }
                        }


                        Button(
                            onClick = {
                                register(parentFullName, parentID, parentPhoneNumber, password, childFullName, childID, childPassword)

                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(20.dp)
                                .shadow(elevation = 14.dp)
                        ) {
                            Text(
                                text = "Registrarse",
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
            label = { Text(label,
                style = TextStyle(color = Color.Black))},
            leadingIcon = {
                Icon(
                    imageVector = leadingIconImageVector,
                    contentDescription = leadingIconDescription,
                    tint = if (showError) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface
                )
            },
            isError = showError,
            trailingIcon = {
                if (showError && !isPasswordField) {
                    Icon(imageVector = Icons.Filled.Error, contentDescription = "Show error icon")
                }
                if (isPasswordField) {
                    PasswordVisibilityIconButton(
                        isPasswordVisible = isPasswordVisible,
                        onVisibilityChange = { onVisibilityChange(!isPasswordVisible) }
                    )
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
            ErrorMessage(errorMessage = errorMessage)
        }
    }
}

@Composable
fun PasswordVisibilityIconButton(
    isPasswordVisible: Boolean,
    onVisibilityChange: () -> Unit
) {
    IconButton(onClick = { onVisibilityChange() }) {
        Icon(
            imageVector = if (isPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
            contentDescription = "Toggle password visibility"
        )
    }
}
@Composable
fun ErrorMessage(errorMessage : String){
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
@Composable
fun CustomRadioButton(belongsToSchool: Boolean,
                      onBelongsToSchool: (Boolean) -> Unit,
                      question: String) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = question,
            fontSize = 18.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Row(verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center) {
            RadioButton(
                selected = belongsToSchool,
                onClick = { onBelongsToSchool(true) }
            )
            Text(
                text = "Si",
                fontSize = 18.sp,
                modifier = Modifier.padding(start = 8.dp)
            )
            RadioButton(
                selected = !belongsToSchool,
                onClick = { onBelongsToSchool(false) }
            )
            Text(
                text = "No",
                fontSize = 18.sp,
                modifier = Modifier.padding(start = 8.dp)
            )
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CoursesSelectionScreen(selectedCourse: Course, onChildCourseChange : (Course) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    var selectedText by remember(selectedCourse) { mutableStateOf(selectedCourse) }
    OutlinedTextField(
        value = selectedText.displayName,
        onValueChange = {},
        modifier = Modifier
            .fillMaxWidth()
            .clickable { expanded = true },
        label = { Text("Selecciona un curso") },
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
            .clip(RoundedCornerShape(20.dp))
    ) {
        Course.values().forEach { course ->
            DropdownMenuItem(
                modifier = Modifier
                    .fillMaxWidth(),
                text = { Text(text = course.displayName) },
                onClick = {
                    selectedText = course
                    expanded = false
                    onChildCourseChange(course)
                }
            )
        }
    }
}