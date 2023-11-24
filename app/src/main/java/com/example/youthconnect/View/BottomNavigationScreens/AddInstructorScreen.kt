package com.example.youthconnect.View.BottomNavigationScreens

import android.content.Intent
import android.util.Patterns
import android.widget.Toast
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Password
import androidx.compose.material.icons.filled.PermIdentity
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.youthconnect.CoursesSelectionScreen
import com.example.youthconnect.CustomOutlinedTextField
import com.example.youthconnect.CustomRadioButton
import com.example.youthconnect.FirstSignupFormScreen
import com.example.youthconnect.MainActivity
import com.example.youthconnect.Model.DataBase
import com.example.youthconnect.Model.Users.Child
import com.example.youthconnect.Model.Users.Instructor
import com.example.youthconnect.Model.Users.Parent
import com.example.youthconnect.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddInstructorScreen(){

    var InstructorFullName by remember { mutableStateOf("") }
    var InstructorID  by remember { mutableStateOf("") }
    var InstructorPassword  by remember { mutableStateOf("") }

    var isChildPasswordVisible by rememberSaveable { mutableStateOf(false) }

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


        Column(modifier = Modifier.align(Alignment.Center).padding(15.dp)) {

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

                    val dataBase = DataBase()
                    dataBase.addInstructor(instructor)
                    dataBase.addInstructorAccount(instructor)
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






@Preview(showBackground = true)
@Composable
fun AddInstructorScreenPreview(){
    AddInstructorScreen()
}