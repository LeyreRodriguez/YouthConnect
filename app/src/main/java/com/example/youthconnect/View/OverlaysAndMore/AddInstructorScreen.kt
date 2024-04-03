package com.example.youthconnect.View.OverlaysAndMore

import android.annotation.SuppressLint

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Password
import androidx.compose.material.icons.filled.PermIdentity
import androidx.compose.material.icons.outlined.PersonAdd
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.youthconnect.Model.Object.Instructor
import com.example.youthconnect.View.Authentication.CustomOutlinedTextField
import com.example.youthconnect.ViewModel.signUpViewModel

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
            Text(text = "Inserta un nuevo animador")
        },
        text = {

            LazyColumn(state = listState) {
                item{

                    CustomOutlinedTextField(
                        value = InstructorFullName,
                        onValueChange = { InstructorFullName = it },

                        label = "Nombre completo del animador",
                        leadingIconImageVector = Icons.Default.PermIdentity

                    )

                    CustomOutlinedTextField(
                        value = InstructorID,
                        onValueChange = { InstructorID = it },

                        label = "DNI del animador",

                        leadingIconImageVector = Icons.Default.CreditCard

                    )

                    CustomOutlinedTextField(
                        value = InstructorPassword,
                        onValueChange = { InstructorPassword = it },
                        isPasswordVisible = isChildPasswordVisible,
                        onVisibilityChange = { isChildPasswordVisible = it },
                        label = "Contraseña del animador",
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

                    signUpViewModel.registerUser(instructor.ID, instructor.Password)
                    signUpViewModel.addInstructor(instructor)


                    InstructorFullName  =""
                    InstructorID=""
                    InstructorPassword=""
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

