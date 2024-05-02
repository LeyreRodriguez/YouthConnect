import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.navigation.NavController
import com.example.youthconnect.Model.Object.Question
import com.example.youthconnect.View.Authentication.CustomOutlinedTextField
import com.example.youthconnect.ViewModel.QuizViewModel

@Composable
fun ModifyQuestion(item : Question, navController : NavController, onDismiss: () -> Unit) {
    val quizViewModel: QuizViewModel = hiltViewModel()

    var editedQuestion by remember { mutableStateOf(item.question) }
    var editedOptionA by remember { mutableStateOf(item.optionA) }
    var editedOptionB by remember { mutableStateOf(item.optionB) }
    var editedOptionC by remember { mutableStateOf(item.optionC) }
    var editedOptionD by remember { mutableStateOf(item.optionD) }
    var editedAnswer by remember { mutableStateOf(item.answer) }

    AlertDialog(
        onDismissRequest = onDismiss,
        icon = { Icons.Outlined.Quiz },
        title = { Text(text = "Inserte una nueva noticia") },
        text = {

            ModifyQuestionContent(
                editedQuestion = editedQuestion,
                editedOptions = listOf(editedOptionA, editedOptionB, editedOptionC, editedOptionD),
                editedAnswer = editedAnswer,
                onQuestionChange = { editedQuestion = it },
                onOptionChange = { index, value -> when (index) {
                    0 -> editedOptionA = value
                    1 -> editedOptionB = value
                    2 -> editedOptionC = value
                    3 -> editedOptionD = value
                    else -> throw IllegalArgumentException("Invalid index")
                }},
                onAnswerChange = { editedAnswer = it }
            )

        },
        confirmButton = {
            TextButton(
                onClick = {
                    quizViewModel.updateQuestion(
                        item.copy(
                            id = item.id,
                            answer = editedAnswer,
                            optionA = editedOptionA,
                            optionB = editedOptionB,
                            optionC = editedOptionC,
                            optionD = editedOptionD,
                            question = editedQuestion
                        )
                    )
                    navController.navigate("DecideScreen")
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


@Composable
private fun ModifyQuestionContent(
    editedQuestion: String,
    editedOptions: List<String>,
    editedAnswer: String,
    onQuestionChange: (String) -> Unit,
    onOptionChange: (Int, String) -> Unit,
    onAnswerChange: (String) -> Unit
) {
    LazyColumn {
        item {
            Text("Escriba primero la pregunta y seguidamente las posibles respuestas, no olvide marcar cual es la respuesta correcta")
            CustomOutlinedTextField(
                value = editedQuestion,
                onValueChange = onQuestionChange,
                label = "Pregunta",
                leadingIconImageVector = Icons.Default.QuestionMark
            )
            val options = listOf("A", "B", "C", "D")
            options.forEachIndexed { index, option ->
                Row {
                    Checkbox(
                        checked = editedAnswer == option,
                        onCheckedChange = { isChecked ->
                            if (isChecked) {
                                onAnswerChange(option)
                            } else {
                                onAnswerChange("")
                            }
                        }
                    )
                    CustomOutlinedTextField(
                        value = editedOptions[index],
                        onValueChange = { onOptionChange(index, it) },
                        label = "Opción $option",
                        leadingIconImageVector = Icons.Default.QuestionAnswer
                    )
                }
            }
        }
    }
}