package com.example.youthconnect.View.OverlaysAndMore

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
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
import com.example.youthconnect.Model.Enum.Course
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

    var fullName by remember { mutableStateOf("") }



    var parentFullName by remember { mutableStateOf("") }


    var childFullName by remember { mutableStateOf("") }

    var course  by remember { mutableStateOf("") }
    var belongsToSchool  by remember { mutableStateOf(false) }
    var faithGroups  by remember { mutableStateOf(false) }
    var goOutAlone  by remember { mutableStateOf(false) }
    var observations  by remember { mutableStateOf("") }
    var telephone by remember { mutableStateOf("")}
    val listState = rememberLazyListState()

    when (item) {
        is Instructor -> {
            fullName = item.fullName

        }

        is Child -> {
            childFullName = item.fullName

            course = item.course
            belongsToSchool = item.belongsToSchool
            faithGroups  = item.faithGroups
            goOutAlone = item.goOutAlone
            observations  = item.observations.toString()
        }

        is Parent -> {
            parentFullName =item.fullName

            telephone = item.phoneNumber

        }
    }




    var editedFullName by remember {
        mutableStateOf((item as? Instructor)?.fullName ?: "")
    }
    var editedPassword by remember {
        mutableStateOf((item as? Instructor)?.password ?: "")
    }

    //PARENTS

    var editedParentsFullName by remember {
        mutableStateOf((item as? Parent)?.fullName ?: "")
    }
    var editedParentsPassword by remember {
        mutableStateOf((item as? Parent)?.password ?: "")
    }
    var editedParentsPhoneNumber by remember {
        mutableStateOf((item as? Parent)?.phoneNumber ?: "")
    }

    //CHILD

    var editedChildFullName by remember {
        mutableStateOf((item as? Child)?.fullName ?: "")
    }
    var editedChildPassword by remember {
        mutableStateOf((item as? Child)?.password ?: "")
    }
    var editedFaithGroups by remember {
        mutableStateOf((item as? Child)?.faithGroups ?: false)
    }
    var editedBelongsToSchool by remember {
        mutableStateOf((item as? Child)?.belongsToSchool ?: false)
    }
    var editedGoOutAlone by remember {
        mutableStateOf((item as? Child)?.goOutAlone ?: false)
    }
    var editedObservations by remember {
        mutableStateOf((item as? Child)?.observations ?: "")
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

                            println(course)
                            println(Course.fromString(course))

                            Course.fromString(course)
                                ?.let { CoursesSelectionScreen(it) { course = it.name } }

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
                                    fullName = editedFullName
                                )

                            )

                            navController.navigate("instructor_profile_screen/${item.id}")

                        }
                        is Parent -> {
                            userViewModel.updateUser(
                                item.copy(
                                    fullName = editedParentsFullName,
                                    phoneNumber = editedParentsPhoneNumber
                                )
                            )

                            navController.navigate("parent_profile_screen/${item.id}")

                        }
                        is Child -> {
                            userViewModel.updateUser(
                                item.copy(
                                    fullName = editedChildFullName,
                                    belongsToSchool = editedBelongsToSchool,
                                    goOutAlone = editedGoOutAlone,
                                    faithGroups = editedFaithGroups,
                                    observations = editedObservations
                                )
                            )

                            navController.navigate(" child_profile_screen/${item.id}")

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