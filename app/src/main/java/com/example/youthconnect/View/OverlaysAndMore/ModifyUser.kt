package com.example.youthconnect.View.OverlaysAndMore

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Password
import androidx.compose.material.icons.filled.PermIdentity
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.youthconnect.Model.Object.Child
import com.example.youthconnect.Model.Object.Instructor
import com.example.youthconnect.Model.Object.Parent
import com.example.youthconnect.View.Authentication.CoursesSelectionScreen
import com.example.youthconnect.View.Authentication.CustomOutlinedTextField
import com.example.youthconnect.View.Authentication.CustomRadioButton
import com.example.youthconnect.ViewModel.UserViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModifyUsers(onDismiss: () -> Unit, item : Any, navController : NavController){

    val userViewModel: UserViewModel = hiltViewModel()

    var FullName by remember { mutableStateOf("") }



    var ParentFullName by remember { mutableStateOf("") }


    var ChildFullName by remember { mutableStateOf("") }

    var Course  by remember { mutableStateOf("") }
    var BelongsToSchool  by remember { mutableStateOf(false) }
    var FaithGroups  by remember { mutableStateOf(false) }
    var GoOutAlone  by remember { mutableStateOf(false) }
    var Observations  by remember { mutableStateOf("") }
    var Telephone by remember { mutableStateOf("")}
    var isPasswordVisible by rememberSaveable { mutableStateOf(false) }
    val listState = rememberLazyListState()

    if (item is Instructor){
         FullName = item.FullName

    } else if (item is Child){
         ChildFullName = item.FullName

         Course = item.Course
         BelongsToSchool = item.BelongsToSchool
         FaithGroups  = item.FaithGroups
         GoOutAlone = item.GoOutAlone
         Observations  = item.Observations.toString()
    } else if (item is Parent){
         ParentFullName =item.FullName

         Telephone = item.PhoneNumber

    }


    //INSTRUCTOR

    var editedFullName by remember {
        mutableStateOf((item as? Instructor)?.FullName ?: "")
    }
    var editedPassword by remember {
        mutableStateOf((item as? Instructor)?.Password ?: "")
    }

    //PARENTS

    var editedParentsFullName by remember {
        mutableStateOf((item as? Parent)?.FullName ?: "")
    }
    var editedParentsPassword by remember {
        mutableStateOf((item as? Parent)?.Password ?: "")
    }
    var editedParentsPhoneNumber by remember {
        mutableStateOf((item as? Parent)?.PhoneNumber ?: "")
    }

    //CHILD

    var editedChildFullName by remember {
        mutableStateOf((item as? Child)?.FullName ?: "")
    }
    var editedChildPassword by remember {
        mutableStateOf((item as? Child)?.Password ?: "")
    }
    var editedFaithGroups by remember {
        mutableStateOf((item as? Child)?.FaithGroups ?: false)
    }
    var editedBelongsToSchool by remember {
        mutableStateOf((item as? Child)?.BelongsToSchool ?: false)
    }
    var editedGoOutAlone by remember {
        mutableStateOf((item as? Child)?.GoOutAlone ?: false)
    }
    var editedObservations by remember {
        mutableStateOf((item as? Child)?.Observations ?: "")
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        icon = {
            Icons.Outlined.Edit
        },
        title = {
            Text(text = "Edita el usuario")
        },
        text = {

            LazyColumn(state = listState) {
                item{


                    when(item){
                        is Instructor ->{
                            CustomOutlinedTextField(
                                value = editedFullName ?: "",
                                onValueChange = { editedFullName = it },

                                label = "Nombre completo",
                                leadingIconImageVector = Icons.Default.PermIdentity

                            )



                        }
                        is Child -> {
                            CustomOutlinedTextField(
                                value = editedChildFullName ?: "",
                                onValueChange = { editedChildFullName = it },

                                label = "Nombre completo",
                                leadingIconImageVector = Icons.Default.PermIdentity

                            )


                            CoursesSelectionScreen(Course, { Course = it.name})

                            CustomRadioButton(editedBelongsToSchool, onBelongsToSchool = { editedBelongsToSchool = it }, "¿Pertenece al colegio?")

                            CustomRadioButton(editedFaithGroups,  { editedFaithGroups = it }, "¿Quieres apuntar a \n tu hijo/hija en grupos de fe?")

                            CustomRadioButton(editedGoOutAlone, { editedGoOutAlone = it }, "¿Puede tu hijo/hija salir solo del centro?")
                            OutlinedTextField(
                                value = editedObservations ?: "",
                                onValueChange = {editedObservations = it },
                                label = { Text("Observaciones") },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 10.dp)
                            )

                        } is Parent ->{
                        CustomOutlinedTextField(
                            value = editedParentsFullName ?: "",
                            onValueChange = { editedParentsFullName = it },

                            label = "Nombre completo",
                            leadingIconImageVector = Icons.Default.PermIdentity

                        )


                        CustomOutlinedTextField(
                            value = editedParentsPhoneNumber ?: "",
                            onValueChange = { editedParentsPhoneNumber = it },

                            label = "Numero de teléfono",
                            leadingIconImageVector = Icons.Default.Phone

                        )
                        }
                    }
                }



            }

        },

        confirmButton = {

            TextButton(
                onClick = {
                    when (item) {
                        is Instructor -> {

                            userViewModel.updateUser(
                                item.copy(
                                    FullName = editedFullName
                                )

                            )

                            navController.navigate("instructor_profile_screen/${item.ID}")

                        }
                        is Parent -> {
                            userViewModel.updateUser(
                                item.copy(
                                    FullName = editedParentsFullName,
                                    PhoneNumber = editedParentsPhoneNumber
                                )
                            )

                            navController.navigate("parent_profile_screen/${item.ID}")

                        }
                        is Child -> {
                            userViewModel.updateUser(
                                item.copy(
                                    FullName = editedChildFullName,
                                    BelongsToSchool = editedBelongsToSchool,
                                    GoOutAlone = editedGoOutAlone,
                                    FaithGroups = editedFaithGroups,
                                    Observations = editedObservations
                                )
                            )

                            navController.navigate(" child_profile_screen/${item.ID}")

                        }
                    }

                    onDismiss()
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