package com.example.youthconnect.View.BottomNavigationScreens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Password
import androidx.compose.material.icons.filled.PermIdentity
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.youthconnect.Model.Object.Instructor
import com.example.youthconnect.R
import com.example.youthconnect.ViewModel.signUpViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddInstructorScreen(signUpViewModel: signUpViewModel = viewModel()){

    var InstructorFullName by remember { mutableStateOf("") }
    var InstructorID  by remember { mutableStateOf("") }
    var InstructorPassword  by remember { mutableStateOf("") }

    var isChildPasswordVisible by rememberSaveable { mutableStateOf(false) }
    var showMessage by remember { mutableStateOf(false) }

    val coroutineScope = rememberCoroutineScope()
    val focusManager = LocalFocusManager.current
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


        Column(modifier = Modifier
            .fillMaxSize()
            .padding(15.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceAround
        ) {

            Text(
                text = "Signup",
                style = TextStyle(
                    fontSize = 40.sp,
                    fontFamily = FontFamily(Font(R.font.annie_use_your_telescope)),
                    fontWeight = FontWeight(400),
                    color = Color(0xFF000000),
                    letterSpacing = 0.9.sp,
                ),
                modifier = Modifier.padding(bottom = 15.dp),
                textAlign = TextAlign.Center
            )
            CustomOutlinedTextField(
                value = InstructorFullName,
                onValueChange = { InstructorFullName = it },

                label = "Instructor Full Name",
                leadingIconImageVector = Icons.Default.PermIdentity

            )

            CustomOutlinedTextField(
                value = InstructorID,
                onValueChange = { InstructorID = it },

                label = "Instructor ID",

                leadingIconImageVector = Icons.Default.CreditCard

            )

            CustomOutlinedTextField(
                value = InstructorPassword,
                onValueChange = { InstructorPassword = it },
                isPasswordVisible = isChildPasswordVisible,
                onVisibilityChange = { isChildPasswordVisible = it },
                label = "Instructor Password",
                isPasswordField = true,
                leadingIconImageVector = Icons.Default.Password,

                )


            Button(
                onClick = {

                    val instructor = Instructor(
                        InstructorFullName,
                        InstructorID,
                        InstructorPassword
                    )

                    signUpViewModel.addInstructor(instructor)

                    InstructorFullName = ""
                    InstructorID = ""
                    InstructorPassword = ""

                    showMessage = true

                    coroutineScope.launch {
                        if (showMessage) {
                            delay(3000)
                            showMessage = false
                        }
                    }

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

            if (showMessage) {
                Snackbar(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text("El usuario se ha creado correctamente")
                }
            }
        }

    }
}

@Preview(showBackground = true)
@Composable
fun AddInstructorScreenPreview(){
    AddInstructorScreen()
}