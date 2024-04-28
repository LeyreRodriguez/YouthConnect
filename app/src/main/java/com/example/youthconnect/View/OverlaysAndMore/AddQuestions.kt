package com.example.youthconnect.View.OverlaysAndMore

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.QuestionAnswer
import androidx.compose.material.icons.filled.QuestionMark
import androidx.compose.material.icons.outlined.Quiz
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.youthconnect.Model.Object.Question
import com.example.youthconnect.View.Authentication.CustomOutlinedTextField
import com.example.youthconnect.ViewModel.QuizViewModel
import java.util.UUID

@Composable
fun AddQuestions(navController : NavHostController, onDismiss: () -> Unit) {
    val quizViewModel = hiltViewModel<QuizViewModel>()
    val questionData = remember { mutableStateOf(QuestionData()) }

    AlertDialog(
        onDismissRequest = onDismiss,
        icon = { Icons.Outlined.Quiz },
        title = { Text("Inserte una nueva pregunta") },
        text = { QuestionForm(questionData) },
        confirmButton = {

            TextButton(onClick = {
                val id = UUID.randomUUID().toString()


                if (questionData.value.selectedAnswer.isNotEmpty()) {
                    quizViewModel.addNewQuestion(
                        Question(
                            id = id,
                            answer = questionData.value.selectedAnswer,
                            optionA = questionData.value.optionA,
                            optionB = questionData.value.optionB,
                            optionC = questionData.value.optionC,
                            optionD = questionData.value.optionD,
                            question = questionData.value.question
                        )
                    )

                }

                questionData.value = QuestionData()

                navController.navigate("DecideScreen")
            }) {
                Text("Confirmar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}

@Composable
fun QuestionForm(questionData: MutableState<QuestionData>) {

    val listState = rememberLazyListState()
    LazyColumn(state = listState) {
        item {
            Text("Escriba primero la pregunta y seguidamente las posibles respuestas, no olvide marcar cual es la respuesta correcta")
            CustomOutlinedTextField(
                value = questionData.value.question,
                onValueChange = { questionData.value = questionData.value.copy(question = it) },
                label = "Pregunta",
                leadingIconImageVector = Icons.Default.QuestionMark
            )
            OptionsForm(questionData)
        }
    }
}

@Composable
fun OptionsForm(questionData: MutableState<QuestionData>) {
    val options = listOf("A", "B", "C", "D")
    options.forEachIndexed { _, option ->
        Row {
            Checkbox(
                checked = questionData.value.selectedAnswer == option,
                onCheckedChange = { isChecked ->
                    if (isChecked) {
                        questionData.value = questionData.value.copy(selectedAnswer = option)
                    } else {
                        questionData.value = questionData.value.copy(selectedAnswer = "")
                    }
                }
            )
            CustomOutlinedTextField(
                value = when (option) {
                    "A" -> questionData.value.optionA
                    "B" -> questionData.value.optionB
                    "C" -> questionData.value.optionC
                    "D" -> questionData.value.optionD
                    else -> ""
                },
                onValueChange = {
                    when (option) {
                        "A" -> questionData.value = questionData.value.copy(optionA = it)
                        "B" -> questionData.value = questionData.value.copy(optionB = it)
                        "C" -> questionData.value = questionData.value.copy(optionC = it)
                        "D" -> questionData.value = questionData.value.copy(optionD = it)
                    }
                },
                label = "Opción ${option} ",
                leadingIconImageVector = Icons.Default.QuestionAnswer
            )
        }
    }
}

data class QuestionData(
    var question: String = "",
    var optionA: String = "",
    var optionB: String = "",
    var optionC: String = "",
    var optionD: String = "",
    var selectedAnswer: String = ""
)