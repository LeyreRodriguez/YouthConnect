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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color


import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.youthconnect.Model.Enum.NavScreen
import com.example.youthconnect.Model.Object.Question
import com.example.youthconnect.Model.Object.UserData
import com.example.youthconnect.R
import com.example.youthconnect.ViewModel.QuizViewModel
import com.example.youthconnect.ViewModel.UserViewModel
import com.example.youthconnect.ui.theme.Blue
import com.example.youthconnect.ui.theme.Yellow
import com.example.youthconnect.ui.theme.Green
import com.example.youthconnect.ui.theme.Red
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun QuizScreen( navController: NavHostController,
    modifier : Modifier = Modifier.background(color = Color.White)
) {
    var questions by remember { mutableStateOf<List<Question?>>(emptyList()) }
    val QuizViewModel: QuizViewModel = hiltViewModel()
    val UserViewModel: UserViewModel = hiltViewModel()
    var user by remember { mutableStateOf<String?>("") }
    var currentQuestionIndex by remember { mutableStateOf(0) }


    val configuration = LocalConfiguration.current
    val screenWidthDp = configuration.screenWidthDp.dp
    val buttonWidth = screenWidthDp / 2

    val documentExists = remember { mutableStateOf("-1") }
    var result by remember { mutableStateOf<String?>("") }


    LaunchedEffect(Unit) {
        try {
            withContext(Dispatchers.IO) {
                questions = QuizViewModel.getAllQuestions()
            }

        } catch (e: Exception) {
            Log.e("Firestore", "Error en getAllQuestions", e)
        }
    }






    LaunchedEffect(UserViewModel) {
        try {
            user = UserViewModel.getCurrentUser()
            result = user?.let { UserViewModel.findDocument(it) }

            if (result != null) {
                documentExists.value = result.toString()
            }

        } catch (e: Exception) {
            Log.e("Firestore", "Error en ChildList", e)
        }
    }


    val currentQuestion = if (questions.isNotEmpty() && currentQuestionIndex < questions.size) {
        questions[currentQuestionIndex]
    } else {
        null
    }





    Box(
        modifier = modifier.fillMaxSize(),
    ) {
        /*
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

         */
        var showDialog by remember { mutableStateOf(false) }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp),
            contentAlignment = Alignment.BottomEnd
        ) {


            if (documentExists.value == "0") {

                FloatingButton {
                    showDialog = true
                }


            }

            if (showDialog) {
                AddQuestions(onDismiss = { showDialog = false })
            }



            Column(
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxSize()
            ) {
/*
                Row (modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically){

                    if (user.toString().isNotEmpty()) {
                        userImage(user = user.toString(), navController = navController , documentExists.value)
                    }


                }

 */
                Spacer(modifier = Modifier.height(16.dp)) // Espaciador vertical

                Text(
                    text = "AJ MAJO QUIZ",
                    style = TextStyle(
                        fontSize = 20.sp,
                        fontFamily = FontFamily(Font(R.font.annie_use_your_telescope)),
                        fontWeight = FontWeight(400),
                        color = Color(0xFFFFFFFF),
                        letterSpacing = 0.3.sp,
                    ),
                    color = Color.Black,
                    textAlign = TextAlign.Center

                )


                Column(
                    verticalArrangement = Arrangement.SpaceEvenly,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxSize()

                ) {

                    currentQuestion?.question?.let {
                        Text(
                            text = it,
                            style = TextStyle(
                                fontSize = 20.sp,
                                fontFamily = FontFamily(Font(R.font.annie_use_your_telescope)),
                                fontWeight = FontWeight(400),
                                color = Color(0xFFFFFFFF),
                                letterSpacing = 0.3.sp,
                            ),
                            color = Color.Black,
                            textAlign = TextAlign.Center
                        )
                    }

                    Box() {
                        Column(
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.fillMaxWidth()

                        ) {

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(start = 10.dp, end = 10.dp)
                            ) {

                                Button(
                                    onClick = {

                                        if (currentQuestionIndex == 0) {
                                            QuizViewModel.resetScore(user.toString())
                                        }

                                        if (currentQuestion != null) {
                                            if (currentQuestion.answer.equals("OptionA")) {
                                                user?.let { QuizViewModel.updateScore(it) }
                                            }
                                        }

                                        if (currentQuestionIndex == questions.size - 1) {
                                            navController.navigate("Scores")
                                        }
                                        currentQuestionIndex++

                                    },
                                    modifier = Modifier
                                        .width(buttonWidth)
                                        .height(100.dp)
                                        .background(
                                            color = Red,
                                            shape = RoundedCornerShape(10.dp) // Puedes ajustar el radio del borde aquí
                                        ),
                                    colors = ButtonDefaults.buttonColors(
                                        backgroundColor = Red, // Set the background color as per your requirement

                                    )

                                ) {
                                    currentQuestion?.optionA?.let {
                                        Text(
                                            text = it,
                                            style = TextStyle(
                                                fontSize = 15.sp,
                                                fontFamily = FontFamily(Font(R.font.annie_use_your_telescope)),
                                                fontWeight = FontWeight(400),
                                                color = Color(0xFFFFFFFF),
                                                letterSpacing = 0.3.sp,
                                            ),
                                            textAlign = TextAlign.Center
                                        )
                                    }
                                }

                                Button(
                                    onClick = {

                                        if (currentQuestionIndex == 0) {
                                            QuizViewModel.resetScore(user.toString())
                                        }

                                        if (currentQuestion != null) {
                                            if (currentQuestion.answer.equals("OptionB")) {
                                                user?.let { QuizViewModel.updateScore(it) }
                                            }
                                        }
                                        if (currentQuestionIndex == questions.size - 1) {
                                            navController.navigate("Scores")
                                        }
                                        currentQuestionIndex++
                                    },
                                    modifier = Modifier
                                        .width(buttonWidth)
                                        .height(100.dp)
                                        .background(
                                            color = Green,
                                            shape = RoundedCornerShape(10.dp) // Puedes ajustar el radio del borde aquí
                                        ),
                                    colors = ButtonDefaults.buttonColors(
                                        backgroundColor = Green, // Set the background color as per your requirement

                                    )


                                ) {
                                    currentQuestion?.optionB?.let {
                                        Text(
                                            text = it,
                                            style = TextStyle(
                                                fontSize = 15.sp,
                                                fontFamily = FontFamily(Font(R.font.annie_use_your_telescope)),
                                                fontWeight = FontWeight(400),
                                                color = Color(0xFFFFFFFF),
                                                letterSpacing = 0.3.sp,
                                            ),
                                            textAlign = TextAlign.Center
                                        )
                                    }
                                }
                            }

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(start = 10.dp, end = 10.dp)
                            ) {

                                Button(
                                    onClick = {

                                        if (currentQuestionIndex == 0) {
                                            QuizViewModel.resetScore(user.toString())
                                        }

                                        if (currentQuestion != null) {
                                            if (currentQuestion.answer.equals("OptionC")) {
                                                user?.let { QuizViewModel.updateScore(it) }
                                            }
                                        }
                                        if (currentQuestionIndex == questions.size - 1) {
                                            navController.navigate("Scores")
                                        }
                                        currentQuestionIndex++
                                    },
                                    modifier = Modifier
                                        .width(buttonWidth)
                                        .height(100.dp)
                                        .background(
                                            color = Yellow,
                                            shape = RoundedCornerShape(10.dp) // Puedes ajustar el radio del borde aquí
                                        ),
                                    colors = ButtonDefaults.buttonColors(
                                        backgroundColor = Yellow, // Set the background color as per your requirement

                                    )


                                ) {
                                    currentQuestion?.optionC?.let {
                                        Text(
                                            text = it,
                                            style = TextStyle(
                                                fontSize = 15.sp,
                                                fontFamily = FontFamily(Font(R.font.annie_use_your_telescope)),
                                                fontWeight = FontWeight(400),
                                                color = Color(0xFFFFFFFF),
                                                letterSpacing = 0.3.sp,
                                            ),
                                            textAlign = TextAlign.Center
                                        )
                                    }
                                }

                                Button(
                                    onClick = {

                                        if (currentQuestionIndex == 0) {
                                            QuizViewModel.resetScore(user.toString())
                                        }

                                        if (currentQuestion != null) {
                                            if (currentQuestion.answer.equals("OptionD")) {
                                                user?.let { QuizViewModel.updateScore(it) }
                                            }
                                        }

                                        if (currentQuestionIndex == questions.size - 1) {
                                            navController.navigate("Scores")
                                        }
                                        currentQuestionIndex++
                                    },
                                    modifier = Modifier
                                        .width(buttonWidth)
                                        .height(100.dp)
                                        .background(
                                            color = Blue,
                                            shape = RoundedCornerShape(10.dp) // Puedes ajustar el radio del borde aquí
                                        ),
                                    colors = ButtonDefaults.buttonColors(
                                        backgroundColor = Blue, // Set the background color as per your requirement

                                    )


                                ) {
                                    currentQuestion?.optionD?.let {
                                        Text(
                                            text = it,
                                            style = TextStyle(
                                                fontSize = 15.sp,
                                                fontFamily = FontFamily(Font(R.font.annie_use_your_telescope)),
                                                fontWeight = FontWeight(400),
                                                color = Color(0xFFFFFFFF),
                                                letterSpacing = 0.3.sp,
                                            ),
                                            textAlign = TextAlign.Center
                                        )
                                    }
                                }
                            }
                        }
                    }


                }
            }
        }
    }
}



@Composable
fun Scores( navController: NavHostController,modifier : Modifier = Modifier.background(color = Color.White)){


    var questions by remember { mutableStateOf<List<Question?>>(emptyList()) }
    val QuizViewModel : QuizViewModel = hiltViewModel()
    val UserViewModel : UserViewModel = hiltViewModel()
    var user by remember { mutableStateOf<String?>("") }
    var score by remember { mutableStateOf<String?>("") }

    var allUsers by remember { mutableStateOf<List<UserData?>>(emptyList()) }

    val configuration = LocalConfiguration.current
    val screenWidthDp = configuration.screenWidthDp.dp

    LaunchedEffect(Unit) {
        try {
            withContext(Dispatchers.IO) {
                questions = QuizViewModel.getAllQuestions()
            }

        } catch (e: Exception) {
            Log.e("Firestore", "Error en getAllQuestions", e)
        }
    }

    LaunchedEffect(Unit) {
        try {
            user = UserViewModel.getCurrentUser()
            allUsers = UserViewModel.getAllUsers()!!

        } catch (e: Exception) {
            Log.e("Firestore", "Error en ChildList", e)
        }
    }

    LaunchedEffect(Unit) {
        try {
            score = QuizViewModel.getScore(user.toString()).toString()

        } catch (e: Exception) {
            Log.e("Firestore", "Error en ChildList", e)
        }
    }



    Box(
        modifier = modifier.fillMaxSize(),
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
    }


    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        Spacer(modifier = Modifier.height(26.dp)) // Espaciador vertical

        Text(
            text = "SCORES",
            style = TextStyle(
                fontSize = 30.sp,
                fontFamily = FontFamily(Font(R.font.annie_use_your_telescope)),
                fontWeight = FontWeight(400),
                color = Color(0xFFFFFFFF),
                letterSpacing = 0.3.sp,
            ),
            color = Color.Black,
            textAlign = TextAlign.Center

        )

        Text(
            text =   score + " / " + questions.size.toString() ,
            style = TextStyle(
                fontSize = 30.sp,
                fontFamily = FontFamily(Font(R.font.annie_use_your_telescope)),
                fontWeight = FontWeight(400),
                color = Color(0xFFFFFFFF),
                letterSpacing = 0.3.sp,
            ),
            color = Color.Black,
            textAlign = TextAlign.Center

        )



        val puntuacion = score?.toIntOrNull() ?: 0
        val divisor = questions.size.toFloat()

        val porcentajeAciertos = if (divisor != 0f) {
            puntuacion.toFloat() / divisor
        } else {
            0f // O cualquier otro valor predeterminado que desees en caso de división por cero
        }

        Log.e("porcentajeAciertos", porcentajeAciertos.toString())

        val imagen = when {
            porcentajeAciertos.toDouble() == 1.0 -> R.drawable.mas_ochenta
            porcentajeAciertos < 0.5 -> R.drawable.menos_cincuenta
            porcentajeAciertos >= 0.5 -> R.drawable.aprobado
            else -> throw IllegalArgumentException("El porcentaje de aciertos debe estar entre 0 y 1")
        }


        val text = when {
            porcentajeAciertos.toDouble() == 1.0 -> "PERFECTO"
            porcentajeAciertos < 0.5 -> "INTENTALO DE NUEVO \n A LA PRÓXIMA SALDRÁ MEJOR"
            porcentajeAciertos >= 0.5 ->  "ENHORABUENA, HAS APROBADO"
            else -> throw IllegalArgumentException("El porcentaje de aciertos debe estar entre 0 y 1")
        }


            Text(
                text =    text,
                style = TextStyle(
                    fontSize = 30.sp,
                    fontFamily = FontFamily(Font(R.font.annie_use_your_telescope)),
                    fontWeight = FontWeight(400),
                    color = Color(0xFFFFFFFF),
                    letterSpacing = 0.3.sp,
                ),
                color = Color.Black,
                textAlign = TextAlign.Center

            )

            Image(
                painter = painterResource(id = imagen),
                contentDescription = null,
                modifier = Modifier.padding(16.dp)
            )








        Button(
            onClick = {
                QuizViewModel.resetScore(user.toString())
                navController.navigate(NavScreen.QuizScreen.name)
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .background(
                    color = Blue,
                    shape = RoundedCornerShape(10.dp) // Puedes ajustar el radio del borde aquí
                ),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Blue, // Set the background color as per your requirement

            )


        ) {

                Text(
                    text = "REINICIAR",
                    style = TextStyle(
                        fontSize = 30.sp,
                        fontFamily = FontFamily(Font(R.font.annie_use_your_telescope)),
                        fontWeight = FontWeight(400),
                        color = Color(0xFFFFFFFF),
                        letterSpacing = 0.3.sp,
                    ),
                    textAlign = TextAlign.Center
                )

        }
    }
}




