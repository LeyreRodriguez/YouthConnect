import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.QuestionAnswer
import androidx.compose.material.icons.filled.QuestionMark
import androidx.compose.material.icons.filled.Title
import androidx.compose.material.icons.outlined.Newspaper
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.youthconnect.Model.Object.News
import com.example.youthconnect.Model.Object.Question
import com.example.youthconnect.View.Authentication.CustomOutlinedTextField
import com.example.youthconnect.View.OverlaysAndMore.OptionsForm
import com.example.youthconnect.ViewModel.NewsViewModel
import com.example.youthconnect.ViewModel.QuizViewModel

@Composable
fun ModifyQuestion(item : Question, navController : NavController, onDismiss: () -> Unit) {
    val quizViewModel: QuizViewModel = hiltViewModel()

    var question by remember { mutableStateOf("") }
    var optionA by remember { mutableStateOf("") }
    var optionB by remember { mutableStateOf("") }
    var optionC by remember { mutableStateOf("") }
    var optionD by remember { mutableStateOf("") }
    var selected by remember { mutableStateOf("") }




    question = item.question
    optionA = item.optionA
    optionB = item.optionB
    optionC = item.optionC
    optionD = item.optionD
    selected = item.answer

    var editedQuestion by remember {
        mutableStateOf((item as? Question)?.question ?: "")
    }
    var editedOptionA by remember {
        mutableStateOf((item as? Question)?.optionA ?: "")
    }
    var editedOptionB by remember {
        mutableStateOf((item as? Question)?.optionB ?: "")
    }

    var editedOptionC by remember {
        mutableStateOf((item as? Question)?.optionC ?: "")
    }

    var editedOptionD by remember {
        mutableStateOf((item as? Question)?.optionD ?: "")
    }

    var editedAnswer by remember {
        mutableStateOf((item as? Question)?.answer ?: "")
    }



    val listState = rememberLazyListState()
    val mcontext = LocalContext.current

    AlertDialog(
        onDismissRequest = onDismiss,
        icon = { Icons.Outlined.Quiz },
        title = { Text(text = "Inserte una nueva noticia") },
        text = {

            LazyColumn {
                item {
                    Text("Escriba primero la pregunta y seguidamente las posibles respuestas, no olvide marcar cual es la respuesta correcta")
                    CustomOutlinedTextField(
                        value = editedQuestion,
                        onValueChange = {editedQuestion = it },
                        label = "Pregunta",
                        leadingIconImageVector = Icons.Default.QuestionMark
                    )
                    val options = listOf("A", "B", "C", "D")
                    options.forEachIndexed { _, option ->
                        Row {
                            Checkbox(
                                checked = editedAnswer == option,
                                onCheckedChange = { isChecked ->
                                    if (isChecked) {
                                        editedAnswer = option
                                    } else {
                                        editedAnswer = ""
                                    }
                                }
                            )
                            CustomOutlinedTextField(
                                value = when (option) {
                                    "A" -> editedOptionA
                                    "B" -> editedOptionB
                                    "C" -> editedOptionC
                                    "D" -> editedOptionD
                                    else -> ""
                                },
                                onValueChange = {
                                    when (option) {
                                        "A" -> editedOptionA = it
                                        "B" -> editedOptionB= it
                                        "C" -> editedOptionC = it
                                        "D" -> editedOptionD = it
                                    }
                                },
                                label = "Opción ${option} ",
                                leadingIconImageVector = Icons.Default.QuestionAnswer
                            )
                        }
                    }
                }
            }

        },
        confirmButton = {
            TextButton(
                onClick = {

                    quizViewModel.updateQuestion(item.copy(id = item.id,answer = editedAnswer, optionA = editedOptionA, optionB = editedOptionB, optionC = editedOptionC, optionD = editedOptionD, question = editedQuestion))


                    navController.navigate("DecideScreen")
                    //onDismiss()
                }
            ) {
                Text("Confirmar")
            }
        },
        dismissButton = {
            TextButton(
                onClick = { onDismiss() }
            ) {
                Text("Cancelar")
            }
        }
    )
}