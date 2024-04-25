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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.youthconnect.Model.Enum.NavScreen
import com.example.youthconnect.Model.Object.Question
import com.example.youthconnect.Model.Object.UserData
import com.example.youthconnect.R
import com.example.youthconnect.View.OverlaysAndMore.AddQuestions
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
    val quizViewModel: QuizViewModel = hiltViewModel()
    val userViewModel: UserViewModel = hiltViewModel()
    var user by remember { mutableStateOf<String?>("") }
    var currentQuestionIndex by remember { mutableStateOf(0) }




    val documentExists = remember { mutableStateOf("-1") }
    var result by remember { mutableStateOf<String?>("") }


    LaunchedEffect(Unit) {
        try {
            withContext(Dispatchers.IO) {
                questions = quizViewModel.getAllQuestions()
            }
            user = userViewModel.getCurrentUser()
            result = user?.let { userViewModel.findDocument(it) }

            if (result != null) {
                documentExists.value = result.toString()
            }

        } catch (e: Exception) {
            e.message?.let { Log.e("Error: ", it) }

        }
    }


    val currentQuestion = questions.getOrNull(currentQuestionIndex)


    Box(modifier = modifier.fillMaxSize()) {

        var showDialog by remember { mutableStateOf(false) }

        DialogForQuestions(documentExists.value, showDialog) { showDialog = it }

        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
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

                val colors = listOf(Red, Green, Yellow, Blue).shuffled()
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


                            OptionButton(
                                text = currentQuestion?.optionA ?: "",
                                backgroundColor = colors[0]
                            ) {
                                onClick(currentQuestionIndex, quizViewModel,user.toString(), currentQuestion!!, questions, navController,"A")
                                currentQuestionIndex++
                            }

                            OptionButton(
                                text = currentQuestion?.optionB ?: "",
                                backgroundColor = colors[1]
                            ) {
                                onClick(currentQuestionIndex, quizViewModel,user.toString(), currentQuestion!!, questions, navController, "B")
                                currentQuestionIndex++
                            }
                        }

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 10.dp, end = 10.dp)
                        ) {


                            OptionButton(
                                text = currentQuestion?.optionC ?: "",
                                backgroundColor = colors[2]
                            ) {

                                onClick(currentQuestionIndex, quizViewModel,user.toString(), currentQuestion!!, questions, navController,"C")
                                currentQuestionIndex++
                            }

                            OptionButton(
                                text = currentQuestion?.optionD ?: "",
                                backgroundColor = colors[3]
                            ) {

                                onClick(currentQuestionIndex, quizViewModel,user.toString(), currentQuestion!!, questions, navController,"D")
                                currentQuestionIndex++
                            }
                        }
                    }
                }

            }

        }

    }

}


fun onClick(currentQuestionIndex: Int, quizViewModel: QuizViewModel, user:String, currentQuestion: Question, questions: List<Question?>, navController: NavHostController, option :String){
    if (currentQuestionIndex == 0) {
        quizViewModel.resetScore(user.toString())
    }

    if (currentQuestion != null && currentQuestion.answer.equals(option)) {
        user?.let { quizViewModel.updateScore(it) }
    }

    if (currentQuestionIndex == questions.size - 1) {
        navController.navigate("Scores")
    }

}
@Composable
fun OptionButton(text: String, backgroundColor: Color, onClick: () -> Unit) {

    val configuration = LocalConfiguration.current
    val screenWidthDp = configuration.screenWidthDp.dp
    val buttonWidth = screenWidthDp / 2 - 10.dp

    Button(
        onClick = onClick,
        modifier = Modifier
            .width(buttonWidth)
            .height(100.dp)
            .background(
                color = backgroundColor,
                shape = RoundedCornerShape(10.dp)
            ),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = backgroundColor
        )
    ) {
        Text(
            text = text,
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
@Composable
fun DialogForQuestions(documentExists:String, showDialog  : Boolean, onShowDialog :(Boolean) -> Unit){
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp),
        contentAlignment = Alignment.BottomEnd
    ) {
        if (documentExists == "0") {

            FloatingButton {
                onShowDialog(true)
            }

        }

        if (showDialog) {
            AddQuestions(onDismiss = { onShowDialog(false) })
        }
    }
}

@Composable
fun Scores( navController: NavHostController,modifier : Modifier = Modifier.background(color = Color.White)){

    var questions by remember { mutableStateOf<List<Question?>>(emptyList()) }
    val quizViewModel : QuizViewModel = hiltViewModel()
    val userViewModel : UserViewModel = hiltViewModel()
    var user by remember { mutableStateOf<String?>("") }
    var score by remember { mutableStateOf<String?>("") }

    var allUsers by remember { mutableStateOf<List<UserData?>>(emptyList()) }

    LaunchedEffect(Unit) {
        try {
            withContext(Dispatchers.IO) {
                questions = quizViewModel.getAllQuestions()
            }
            user = userViewModel.getCurrentUser()
            allUsers = userViewModel.getAllUsers()!!
            score = quizViewModel.getScore(user.toString()).toString()
        } catch (e: Exception) {
            e.message?.let { Log.e("Error: ", it) }

        }
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
            text = "Puntuacion",
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
                quizViewModel.resetScore(user.toString())
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



