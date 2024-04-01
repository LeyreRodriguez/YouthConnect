package com.example.youthconnect.View.Authentication

import android.widget.Toast
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Password
import androidx.compose.material.icons.filled.PermIdentity
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.libraryapp.model.firebaseAuth.SignInState
import com.example.libraryapp.viewModel.LoginViewModel
import com.example.youthconnect.Model.Enum.NavScreen
import com.example.youthconnect.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginView(loginViewModel: LoginViewModel = viewModel(), navController: NavController, state : SignInState, onSignInClick: () -> Unit) {

    val mcontext = LocalContext.current
    val focusManager = LocalFocusManager.current


    var ID by remember {mutableStateOf("") }
    var password by remember { mutableStateOf("") }


    var validateUserID by rememberSaveable { mutableStateOf(true) }
    var validateUserPassword by rememberSaveable { mutableStateOf(true) }
    var isPasswordVisible by rememberSaveable { mutableStateOf(false) }




    LaunchedEffect(key1 = state.signInError){
        state.signInError?.let { error ->
            Toast.makeText(
                mcontext,
                error,
                Toast.LENGTH_LONG
            ).show()
        }
    }


    fun validate(ID : String, password : String) : Boolean{
        val passwordRegex = "(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#%$^&+=./\\\\_-]).{8,}".toRegex()
        val IDRegex = "^[0-9]{8}[A-Za-z]$".toRegex()

        validateUserID = ID.matches(IDRegex)
        validateUserPassword = password.matches(passwordRegex)

        return validateUserID && validateUserPassword
    }

    fun register(ID : String, password : String){
        if (validate(ID, password) or (ID == "00000000A" && password == "admin")){
            loginViewModel.signInWithEmail(ID + "@youthconnect.com", password) {
                navController.navigate(NavScreen.NewsScreen.name)
            }
        }else{

            Toast.makeText(mcontext,"Please, review fields", Toast.LENGTH_SHORT).show()
        }
    }

    val brush = Brush.horizontalGradient(
        listOf(
            Color(0xFFE15554),
            Color(0xFF3BB273),
            Color(0xFFE1BC29),
            Color(0xFF4D9DE0)
        ))
    Box(

        contentAlignment = Alignment.BottomCenter, // Alinea los elementos en la parte inferior
    ) {


        Canvas(
            modifier = Modifier.fillMaxSize(), // Llena todo el espacio del Box
            onDraw = {
                drawRect(brush)
            }
        )
        Column {

            Image(
                painter = painterResource(id = R.drawable.kids_login),
                contentDescription = "image description",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp, 0.dp)
            )

            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.TopCenter, // Alinea el texto en la parte superior y central
            ) {
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                        .background(
                            color = Color(0xFFFFFFFF),
                            shape = RoundedCornerShape(
                                topStart = 31.dp, // Radio superior izquierdo
                                topEnd = 31.dp,   // Radio superior derecho
                                bottomStart = 0.dp, // Radio inferior izquierdo (0 para esquina recta)
                                bottomEnd = 0.dp   // Radio inferior derecho (0 para esquina recta)
                            )
                        )
                )
                Column(modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,) {
                    Text(
                        text = "Log-in",
                        style = TextStyle(
                            fontSize = 40.sp,
                            fontFamily = FontFamily(Font(R.font.annie_use_your_telescope)),
                            fontWeight = FontWeight(400),
                            color = Color(0xFF000000),
                            letterSpacing = 0.9.sp,
                        )
                    )



                    LazyColumn(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.SpaceAround,

                    ) {
                        item {
                            CustomOutlinedTextField(
                                value = ID,
                                onValueChange = { ID = it },
                                label = "ID",
                                showError = !validateUserID,
                                errorMessage = "The format of the ID doesn´t seem right",
                                leadingIconImageVector = Icons.Default.PermIdentity,
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Text,
                                    imeAction = ImeAction.Next
                                ),
                                keyboardActions = KeyboardActions(
                                    onNext = { focusManager.moveFocus(FocusDirection.Down) }
                                )
                            )
                        }
                        item {
                            CustomOutlinedTextField(
                                value = password,
                                onValueChange = { password = it },
                                label = "Password",
                                showError = !validateUserPassword,
                                errorMessage = "Must mix capital and non-capital letters, a number, special character and minimun length of 8",
                                isPasswordField = true,
                                isPasswordVisible = isPasswordVisible,
                                onVisibilityChange = { isPasswordVisible = it },
                                leadingIconImageVector = Icons.Default.Password,
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Text,
                                    imeAction = ImeAction.Next
                                ),
                                keyboardActions = KeyboardActions(
                                    onNext = { focusManager.moveFocus(FocusDirection.Down) }
                                )
                            )
                        }

                    }
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.SpaceAround,
                        modifier = Modifier.fillMaxSize())
                    {


                        Button(
                            onClick = {
                                register(ID, password)

                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .align(Alignment.CenterHorizontally)
                                .padding(5.dp)
                                .shadow(elevation = 14.dp)



                        ) {
                            Text(
                                text = "Login",
                                style = TextStyle(
                                    fontSize = 20.sp,
                                    fontFamily = FontFamily(Font(R.font.annie_use_your_telescope)),
                                    fontWeight = FontWeight(400),
                                    color = Color(0xFFFFFFFF),
                                    letterSpacing = 0.3.sp,
                                ),
                                color = Color(0xFF000000),
                                textAlign = TextAlign.Center
                            )
                        }

                        Row (modifier = Modifier.align(Alignment.CenterHorizontally)){
                            Text(
                                text = "Don't you have an account? ",
                                style = TextStyle(
                                    fontSize = 15.sp,
                                    fontFamily = FontFamily(Font(R.font.annie_use_your_telescope)),
                                    fontWeight = FontWeight(400),
                                    color = Color(0xFF000000),
                                    letterSpacing = 0.3.sp,
                                )
                            )

                            ClickableText(
                                text = AnnotatedString("Sign up"),
                                onClick = {
                                    //mcontext.startActivity(Intent(mcontext,SignUp::class.java))
                                    navController.navigate("signup")
                                },
                                style = TextStyle(
                                    fontSize = 15.sp,
                                    fontFamily = FontFamily(Font(R.font.annie_use_your_telescope)),
                                    fontWeight = FontWeight(400),
                                    color = Color(0xFF7768AE),
                                    letterSpacing = 0.3.sp,
                                )
                            )
                        }

                    }
                }




            }


        }

    }

}