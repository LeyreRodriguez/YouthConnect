package com.example.youthconnect.View.BottomNavigationScreens

import android.annotation.SuppressLint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Password
import androidx.compose.material.icons.filled.PermIdentity
import androidx.compose.material.icons.filled.QuestionAnswer
import androidx.compose.material.icons.filled.QuestionMark
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.PersonAdd
import androidx.compose.material.icons.outlined.Quiz
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.youthconnect.Model.Object.Child
import com.example.youthconnect.Model.Object.Instructor
import com.example.youthconnect.Model.Object.Parent
import com.example.youthconnect.Model.Object.Question
import com.example.youthconnect.R
import com.example.youthconnect.ViewModel.QuizViewModel
import com.example.youthconnect.ViewModel.signUpViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@SuppressLint("SuspiciousIndentation")
@Composable
fun AddInstructor (onDismiss: () -> Unit){

    val signUpViewModel : signUpViewModel = hiltViewModel()
    var InstructorFullName by remember { mutableStateOf("") }
    var InstructorID  by remember { mutableStateOf("") }
    var InstructorPassword  by remember { mutableStateOf("") }
    var isChildPasswordVisible by rememberSaveable { mutableStateOf(false) }
    var showMessage by remember { mutableStateOf(false) }
    val listState = rememberLazyListState()

    AlertDialog(
        onDismissRequest = onDismiss,
        icon = {
            Icons.Outlined.PersonAdd
        },
        title = {
            Text(text = "Insert a new instructor")
        },
        text = {

            LazyColumn(state = listState) {
                item{

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
                }



            }
        },

        confirmButton = {

            TextButton(
                onClick = {
                    val instructor = Instructor(
                        InstructorFullName,
                        InstructorID,
                        InstructorPassword
                    )

                    signUpViewModel.registerUser(InstructorID, InstructorPassword)
                    signUpViewModel.addInstructor(instructor)
                    InstructorFullName  =""
                    InstructorID=""
                    InstructorPassword=""
                }
            ) {
                Text("Confirm")
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onDismiss()
                }
            ) {
                Text("Dismiss")
            }
        }
    )
}

