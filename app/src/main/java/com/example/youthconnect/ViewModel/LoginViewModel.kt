package com.example.libraryapp.viewModel

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.libraryapp.model.firebaseAuth.SignInResult
import com.example.libraryapp.model.firebaseAuth.SignInState
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.concurrent.CancellationException

class LoginViewModel: ViewModel() {

    var userEmail: String = ""
    var userPassword: String = ""

    private val _state = MutableStateFlow(SignInState())
    val state = _state.asStateFlow()

    private val auth: FirebaseAuth = Firebase.auth //esto debería estar en el model?

    fun onSignInResult(result: SignInResult) {
        _state.update { it.copy(
            isSignInSuccessful = result.data != null,
            signInError = result.errorMessage
        ) }
    }



    fun resetState() {
        _state.update { SignInState() }
    }


    fun signInWithEmail(email: String, passworld: String, home: () -> Unit) = viewModelScope.launch{
        try {
            auth.signInWithEmailAndPassword(email, passworld)
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

    fun signOut() = viewModelScope.launch{
        try {


            auth.signOut()
        }
        catch (e: Exception){
            Log.d("Login Email", "${e.message}")
        }
    }

}