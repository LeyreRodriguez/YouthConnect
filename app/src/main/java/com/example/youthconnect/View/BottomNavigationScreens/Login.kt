package com.example.youthconnect.View.BottomNavigationScreens

import android.util.Log
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.youthconnect.Model.Enum.NavScreen
import com.example.youthconnect.R
import com.example.youthconnect.ViewModel.LoginScreenViewModel
import com.example.youthconnect.ui.theme.YouthconnectTheme


@Composable
fun LogInScreen(modifier: Modifier = Modifier, navController: NavController
) {
    //val navController : NavHostController = rememberNavController()
    val brush = Brush.horizontalGradient(
        listOf(
            Color(0xFFE15554),
            Color(0xFF3BB273),
            Color(0xFFE1BC29),
            Color(0xFF4D9DE0)
        ))
    Box(
        modifier = modifier,
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

                    FormLogIn(navController = navController )
                }




            }


        }

    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormLogIn(navController : NavController,
              viewModel : LoginScreenViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    var ID by remember { mutableStateOf(TextFieldValue()) }
    var password by remember { mutableStateOf(TextFieldValue()) }

    val mcontext = LocalContext.current
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceAround,
        modifier = Modifier.fillMaxSize())
    {
        OutlinedTextField(
            value = ID,
            onValueChange = { ID = it },
            label = { Text("Enter username") },
        )

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Enter password") },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
        )
        ClickableText(
            text = AnnotatedString("Forgot your password?"),
            onClick = { offset ->
                Log.d("ClickableText", "$offset -th character is clicked.")
            },
            style = TextStyle(
                fontSize = 15.sp,
                fontFamily = FontFamily(Font(R.font.annie_use_your_telescope)),
                fontWeight = FontWeight(400),
                color = Color(0xFF000000),
                letterSpacing = 0.3.sp,
            ),
            modifier = Modifier.align(Alignment.End)
        )

        Button(
            onClick = {

                viewModel.signIn(ID.text + "@youthconnect.com", password.text){
                    //mcontext.startActivity(Intent(mcontext,MainActivity::class.java))
                    navController.navigate(NavScreen.NewsScreen.name)
                }


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




@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    YouthconnectTheme {
      //  LogInScreen()
    }
}