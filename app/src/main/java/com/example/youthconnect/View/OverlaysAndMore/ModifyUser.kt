package com.example.youthconnect.View.OverlaysAndMore

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Password
import androidx.compose.material.icons.filled.PermIdentity
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.libraryapp.viewModel.LoginViewModel
import com.example.youthconnect.Model.Constants
import com.example.youthconnect.Model.Object.Child
import com.example.youthconnect.Model.Object.Instructor
import com.example.youthconnect.Model.Object.Parent
import com.example.youthconnect.View.Authentication.CustomOutlinedTextField
import com.example.youthconnect.View.Authentication.CustomRadioButton
import com.example.youthconnect.ViewModel.UserViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModifyUsers(onDismiss: () -> Unit, item : Any, navController : NavController){


    val focusManager = LocalFocusManager.current

    val context = LocalContext.current
    var user by remember { mutableStateOf<String?>("") }
    var userType by remember { mutableStateOf<String>("")}


    var isPasswordVisible by rememberSaveable { mutableStateOf(false) }

    val userViewModel: UserViewModel = hiltViewModel()
    val loginViewModel: LoginViewModel = viewModel()

    LaunchedEffect(userViewModel) {
        try {
            user = userViewModel.getCurrentUser()
            userType = user?.let { userViewModel.getUserType(it).toString() }.toString()


        } catch (e: Exception) {
            e.message?.let { Log.e(Constants.ERROR_LOG_TAG, it) }
        }
    }

    var fullName by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    var parentFullName by remember { mutableStateOf("") }
    var parentPassword by remember { mutableStateOf("") }


    var childFullName by remember { mutableStateOf("") }
    var childPassword by remember { mutableStateOf("") }
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
            password = item.password

        }

        is Child -> {
            childFullName = item.fullName
            childPassword = item.password
            course = item.course
            belongsToSchool = item.belongsToSchool
            faithGroups  = item.faithGroups
            goOutAlone = item.goOutAlone
            observations  = item.observations.toString()
        }

        is Parent -> {
            parentFullName =item.fullName
            parentPassword = item.password
            telephone = item.phoneNumber

        }
    }


    var editedFullName by remember {
        mutableStateOf((item as? Instructor)?.fullName ?: "")
    }

    var editedPassword by remember {
        mutableStateOf((item as? Instructor)?.password ?: "")
    }


    var editedParentsFullName by remember {
        mutableStateOf((item as? Parent)?.fullName ?: "")
    }
    var editedParentsPassword by remember {
        mutableStateOf((item as? Parent)?.password ?: "")
    }

    var editedParentsPhoneNumber by remember {
        mutableStateOf((item as? Parent)?.phoneNumber ?: "")
    }


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

                    val name = "Nombre Completo"
                    when(item){
                        is Instructor ->{

                                CustomOutlinedTextField(
                                    value = editedFullName ?: "",
                                    onValueChange = { editedFullName = it },

                                    label = name,
                                    leadingIconImageVector = Icons.Default.PermIdentity

                                )

                                CustomOutlinedTextField(
                                    value = editedPassword,
                                    onValueChange = { editedPassword = it },
                                    label = "Contraseña",
                                    errorMessage = "Debe mezclar letras mayusculas y minusculas, numeros, caracteres especiales y tener una logitud minima de 8 caracteres",
                                    isPasswordField = true,
                                    isPasswordVisible = isPasswordVisible,
                                    onVisibilityChange = { isPasswordVisible = it },
                                    leadingIconImageVector = Icons.Default.Password,
                                    keyboardOptions = KeyboardOptions(
                                        keyboardType = KeyboardType.Text,
                                        imeAction = ImeAction.Next
                                    ),
                                    keyboardActions = KeyboardActions(
                                        onNext = { focusManager.moveFocus(FocusDirection.Down) }
                                    )
                                )









                        }
                        is Child -> {

                            if (userType == "Instructor"){
                                CustomOutlinedTextField(
                                    value = editedChildFullName ?: "",
                                    onValueChange = { editedChildFullName = it },
                                    label = name,
                                    leadingIconImageVector = Icons.Default.PermIdentity

                                )

                                CustomRadioButton(editedBelongsToSchool, onBelongsToSchool = { editedBelongsToSchool = it }, "¿Pertenece al colegio?")

                                CustomRadioButton(editedFaithGroups,  { editedFaithGroups = it }, "¿Quieres apuntar a \n tu hijo/hija en grupos de fe?")

                                CustomRadioButton(editedGoOutAlone, { editedGoOutAlone = it }, "¿Puede tu hijo/hija salir solo del centro?")
                                CustomOutlinedTextField(
                                    value = editedObservations ?: "",
                                    onValueChange = {editedObservations = it },
                                    label = "Observaciones",
                                    leadingIconImageVector = Icons.Default.PermIdentity
                                )
                            }else{
                                CustomOutlinedTextField(
                                    value = editedChildPassword,
                                    onValueChange = { editedChildPassword = it },
                                    label = "Contraseña",
                                    errorMessage = "Debe mezclar letras mayusculas y minusculas, numeros, caracteres especiales y tener una logitud minima de 8 caracteres",
                                    isPasswordField = true,
                                    isPasswordVisible = isPasswordVisible,
                                    onVisibilityChange = { isPasswordVisible = it },
                                    leadingIconImageVector = Icons.Default.Password,
                                    keyboardOptions = KeyboardOptions(
                                        keyboardType = KeyboardType.Text,
                                        imeAction = ImeAction.Next
                                    ),
                                    keyboardActions = KeyboardActions(
                                        onNext = { focusManager.moveFocus(FocusDirection.Down) }
                                    )
                                )
                            }










                        } is Parent ->{

                            if(userType == "Instructor"){
                                CustomOutlinedTextField(
                                    value = editedParentsFullName ?: "",
                                    onValueChange = { editedParentsFullName = it },

                                    label = name,
                                    leadingIconImageVector = Icons.Default.PermIdentity

                                )

                                CustomOutlinedTextField(
                                    value = editedParentsPhoneNumber ?: "",
                                    onValueChange = { editedParentsPhoneNumber = it },

                                    label = "Numero de teléfono",
                                    leadingIconImageVector = Icons.Default.Phone

                                )
                            }else{
                                CustomOutlinedTextField(
                                    value = editedParentsPassword,
                                    onValueChange = { editedParentsPassword = it },
                                    label = "Contraseña",
                                    errorMessage = "Debe mezclar letras mayusculas y minusculas, numeros, caracteres especiales y tener una logitud minima de 8 caracteres",
                                    isPasswordField = true,
                                    isPasswordVisible = isPasswordVisible,
                                    onVisibilityChange = { isPasswordVisible = it },
                                    leadingIconImageVector = Icons.Default.Password,
                                    keyboardOptions = KeyboardOptions(
                                        keyboardType = KeyboardType.Text,
                                        imeAction = ImeAction.Next
                                    ),
                                    keyboardActions = KeyboardActions(
                                        onNext = { focusManager.moveFocus(FocusDirection.Down) }
                                    )
                                )
                            }






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
                                    fullName = editedFullName,
                                    password = editedPassword,

                                    )

                            )

                            loginViewModel.changePassword(password, editedPassword, {
                                Toast.makeText(
                                    context,
                                    "Contraseña actualizada exitosamente",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }, {
                                Toast.makeText(
                                    context,
                                    "Se ha producido un error",
                                    Toast.LENGTH_SHORT
                                ).show()
                            })

                            navController.navigate("instructor_profile_screen/${item.id}")

                        }
                        is Parent -> {
                            userViewModel.updateUser(
                                item.copy(
                                    fullName = editedParentsFullName,
                                    password = editedParentsPassword,

                                    phoneNumber = editedParentsPhoneNumber
                                )
                            )

                            loginViewModel.changePassword(parentPassword, editedParentsPassword, {
                                Toast.makeText(
                                    context,
                                    "Contraseña actualizada exitosamente",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }, {
                                Toast.makeText(
                                    context,
                                    "Se ha producido un error",
                                    Toast.LENGTH_SHORT
                                ).show()
                            })

                            navController.navigate("parent_profile_screen/${item.id}")

                        }
                        is Child -> {
                            userViewModel.updateUser(
                                item.copy(
                                    fullName = editedChildFullName,
                                    password = editedChildPassword,
                                    belongsToSchool = editedBelongsToSchool,
                                    goOutAlone = editedGoOutAlone,
                                    faithGroups = editedFaithGroups,
                                    observations = editedObservations
                                )
                            )

                            loginViewModel.changePassword(childPassword, editedChildPassword, {
                                Toast.makeText(
                                    context,
                                    "Contraseña actualizada exitosamente",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }, {
                                Toast.makeText(
                                    context,
                                    "Se ha producido un error",
                                    Toast.LENGTH_SHORT
                                ).show()
                            })

                            navController.navigate("child_profile_screen/${item.id}")

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