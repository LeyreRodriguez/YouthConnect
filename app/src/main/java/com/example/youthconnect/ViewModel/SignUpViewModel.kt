package com.example.youthconnect.ViewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.youthconnect.Model.Firebase.Authentication.EmailAuthUiClient
import com.example.youthconnect.Model.Firebase.Firestore.FirestoreRepository
import com.example.youthconnect.Model.Object.Child
import com.example.youthconnect.Model.Object.Instructor
import com.example.youthconnect.Model.Object.Parent
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val firestoreRepository: FirestoreRepository
): ViewModel(){

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    private var _navigateToNextScreen = MutableStateFlow(false)
    var navigateToNextScreen = _navigateToNextScreen.asStateFlow()
    // Estado para UI
    private val _loading = MutableStateFlow(false)
    val loading = _loading.asStateFlow()

    private val _message = MutableStateFlow("")
    val message = _message.asStateFlow()

    private var _showFirstScreen2 = MutableStateFlow(true)
    var showFirstScreen2 = _showFirstScreen2.asStateFlow()

    val emailService by lazy {
        EmailAuthUiClient(auth)
    }

    private val _updatedState = MutableStateFlow<Boolean?>(null)
    val updatedState: StateFlow<Boolean?> = _updatedState.asStateFlow()

    fun changeScreen(){
        _showFirstScreen2.value = !_showFirstScreen2.value
        Log.d("Variable publica: ", "${showFirstScreen2.value}")
        Log.d("Variable privada: ", "${_showFirstScreen2.value}")

    }



    fun registerUser(email: String, password: String) {

        viewModelScope.launch {
            _loading.value = true
            val result = emailService.registerUser(email, password)
            _message.value = result.fold(
                onSuccess = {
                    _navigateToNextScreen.value = true // Indica que el registro fue exitoso y se debe navegar
                    it
                },
                onFailure = {
                    "Error de registro: ${it.message}"
                }
            )
            _loading.value = false
        }
    }


    fun deleteChild(email: String, password: String) {



        viewModelScope.launch {
            _loading.value = true
            val result = emailService.unregisterUser(email.lowercase(), password)
            _message.value = result.fold(
                onSuccess = {
                    _navigateToNextScreen.value = true // Indica que la eliminación fue exitosa y se debe navegar
                    it
                },
                onFailure = {
                    "Error al eliminar usuario: ${it.message}"
                }
            )
            _loading.value = false
        }

        firestoreRepository.deleteChild(email)

    }

    fun deleteParent(email: String, password: String) {



        viewModelScope.launch {
            _loading.value = true
            val result = emailService.unregisterUser(email.lowercase(), password)
            _message.value = result.fold(
                onSuccess = {
                    _navigateToNextScreen.value = true // Indica que la eliminación fue exitosa y se debe navegar
                    it
                },
                onFailure = {
                    "Error al eliminar usuario: ${it.message}"
                }
            )
            _loading.value = false
        }

        firestoreRepository.deleteParent(email)

    }

    fun deleteInstructor(email: String, password: String) {



        viewModelScope.launch {
            _loading.value = true
            val result = emailService.unregisterUser(email.lowercase(), password)
            _message.value = result.fold(
                onSuccess = {
                    _navigateToNextScreen.value = true // Indica que la eliminación fue exitosa y se debe navegar
                    it
                },
                onFailure = {
                    "Error al eliminar usuario: ${it.message}"
                }
            )
            _loading.value = false
        }

        firestoreRepository.deleteInstructor(email)

    }







    fun signOut() = viewModelScope.launch{
        try {
            auth.signOut()
        }
        catch (e: Exception){
            Log.d("Login Email", "${e.message}")
        }
    }


    fun signInWithEmail(email: String, passworld: String, home: () -> Unit) = viewModelScope.launch{
        val realEmail = email+"@youthconnect.com"
        try {
            auth.signInWithEmailAndPassword(realEmail, passworld)
                .addOnCompleteListener {task ->
                    if( task.isSuccessful){
                        home()
                    }
                }
        }
        catch (e: Exception){
            Log.d("Login Email", "${e.message}")

        }
    }

     fun addChild(child: Child){

         viewModelScope.launch {
             firestoreRepository.addChild(child)

         }

    }

     fun addParent(parent: Parent){
        viewModelScope.launch {
            firestoreRepository.addParent(parent)
        }
    }

     fun addInstructor(instructor: Instructor){
        viewModelScope.launch {
            firestoreRepository.addInstructor(instructor)
        }
    }





    fun rollCall(child :Child, isChecked:Boolean){


        viewModelScope.launch {
            if (isChecked) {
                firestoreRepository.rollCall(child)
            }else{
                firestoreRepository.notRollCall(child)
            }
        }
    }






}