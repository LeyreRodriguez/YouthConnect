package com.example.youthconnect.View.OverlaysAndMore

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.QuestionAnswer
import androidx.compose.material.icons.filled.QuestionMark
import androidx.compose.material.icons.outlined.Quiz
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.youthconnect.Model.Object.Question
import com.example.youthconnect.View.Authentication.CustomOutlinedTextField
import com.example.youthconnect.ViewModel.QuizViewModel

@SuppressLint("SuspiciousIndentation")
@Composable
fun AddQuestions (onDismiss: () -> Unit){

    val quizViewModel : QuizViewModel = hiltViewModel()
    var question  by remember { mutableStateOf("") }
    var optionA  by remember { mutableStateOf("") }
    var optionB  by remember { mutableStateOf("") }
    var optionC  by remember { mutableStateOf("") }
    var optionD  by remember { mutableStateOf("") }
    var selectedAnswer by remember { mutableStateOf("") }
    val listState = rememberLazyListState()

        AlertDialog(
            onDismissRequest = onDismiss,
            icon = {
                Icons.Outlined.Quiz
            },
            title = {
                Text(text = "Inserte una nueva pregunta")
            },
            text = {


                LazyColumn(state = listState) {
                    item{
                        Text(text = "Escriba primero la pregunta y seguidamente las posibles respuestas, no olvide marcar cual es la respuesta correcta")

                        CustomOutlinedTextField(
                            value = question,
                            onValueChange = { question = it },
                            label = "Pregunta",
                            leadingIconImageVector = Icons.Default.QuestionMark
                        )

                        Row{
                            Checkbox(checked = selectedAnswer == "OptionA",
                                onCheckedChange = { isChecked ->
                                    if (isChecked) {
                                        selectedAnswer = "OptionA"
                                    } else {
                                        selectedAnswer = ""
                                    }
                                }
                            )
                            CustomOutlinedTextField(
                                value = optionA,
                                onValueChange = { optionA = it },
                                label = "Primera opcion",
                                leadingIconImageVector = Icons.Default.QuestionAnswer
                            )
                        }
                        Row {
                            Checkbox(checked = selectedAnswer == "OptionB",
                                onCheckedChange = { isChecked ->
                                    if (isChecked) {
                                        selectedAnswer = "OptionB"
                                    } else {
                                        selectedAnswer = ""
                                    }
                                }
                            )

                            CustomOutlinedTextField(
                                value = optionB,
                                onValueChange = { optionB = it },
                                label = "Segunda opcion",
                                leadingIconImageVector = Icons.Default.QuestionAnswer
                            )
                        }
                        Row {
                            Checkbox(checked = selectedAnswer == "OptionC",
                                onCheckedChange = { isChecked ->
                                    if (isChecked) {
                                        selectedAnswer = "OptionC"
                                    } else {
                                        selectedAnswer = ""
                                    }
                                }
                            )
                            CustomOutlinedTextField(
                                value = optionC,
                                onValueChange = { optionC = it },
                                label = "Tercera opcion",
                                leadingIconImageVector = Icons.Default.QuestionAnswer
                            )
                        }
                        Row {
                            Checkbox(checked = selectedAnswer == "OptionD",
                                onCheckedChange = { isChecked ->
                                    if (isChecked) {
                                        selectedAnswer = "OptionD"
                                    } else {
                                        selectedAnswer = ""
                                    }
                                }
                            )
                            CustomOutlinedTextField(
                                value = optionD,
                                onValueChange = { optionD = it },
                                label = "Cuarta opcion",
                                leadingIconImageVector = Icons.Default.QuestionAnswer
                            )
                        }
                    }



                }
            },

            confirmButton = {
                if (selectedAnswer.isNotEmpty()) {

                    quizViewModel.addNewQuestion(Question(
                        answer = selectedAnswer,
                        optionA = optionA,
                        optionB = optionB,
                        optionC = optionC,
                        optionD = optionD,
                        question = question
                    ))

                }
                TextButton(
                    onClick = {
                        question = ""
                        optionA = ""
                        optionB = ""
                        optionC = ""
                        optionD = ""
                        selectedAnswer = ""
                    }
                ) {
                    Text("Confirmar")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        onDismiss()
                    }
                ) {
                    Text("Cancelar")
                }
            }
        )
    }


