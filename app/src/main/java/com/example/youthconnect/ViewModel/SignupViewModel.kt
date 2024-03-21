package com.example.youthconnect.ViewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.youthconnect.Model.Firebase.Authentication.EmailAuthUiClient
import com.example.youthconnect.Model.Firebase.Firestore.FirestoreRepository
import com.example.youthconnect.Model.Object.Child
import com.example.youthconnect.Model.Object.Instructor
import com.example.youthconnect.Model.Object.Parent
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class signUpViewModel @Inject constructor(
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


    fun selectChild(child :Child, instructorID: String, isChecked:Boolean){


        viewModelScope.launch {
            if (isChecked) {
                Log.d("Checkbox", "isChecked: $isChecked")
                Log.d("instructorID", "instructorID: $instructorID")
                Log.d("child", "child: $child")
                // Si el checkbox está marcado, agrega al instructor al niño
                firestoreRepository.addInstructorToChild(child, instructorID)

           //     _updatedState.value = true
            } else {
                // Si el checkbox está desmarcado, elimina al instructor del niño
                firestoreRepository.removeInstructorFromChild(child, instructorID)

           //     _updatedState.value = false
            }
        }
    }






}