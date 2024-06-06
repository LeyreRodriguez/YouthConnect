package com.example.libraryapp.viewModel

import SignInState
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.libraryapp.model.firebaseAuth.SignInResult
import com.example.youthconnect.Model.Constants
import com.example.youthconnect.Model.Firebase.Authentication.EmailAuthUiClient
import com.google.firebase.FirebaseException
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class LoginViewModel : ViewModel() {

    private val _state = MutableStateFlow<SignInState>(SignInState.Loading)
    val state = _state.asStateFlow()

    private val _errorState = MutableStateFlow<String?>(null)
    val errorState = _errorState.asStateFlow()

    var auth: FirebaseAuth = FirebaseAuth.getInstance()

    private fun handleError(errorMessage: String?) {
        _errorState.value = errorMessage
    }


    fun onSignInResult(result: SignInResult) {
        _state.update {
            when {
                result.data != null -> SignInState.Success(result.data)
                result.error != null -> SignInState.Error(result.error)
                else -> SignInState.Loading
            }
        }
    }
    fun resetState() {
        _state.value = SignInState.Loading
    }

    fun signInWithEmail(email: String, password: String, home: () -> Unit) = viewModelScope.launch {
        try {
            val authResult = auth.signInWithEmailAndPassword(email, password).await()
            if (authResult != null) {
                home()
            } else {
                Log.d(Constants.EMAIL, "Authentication failed.")
                handleError("Falló la autenticación.")
            }
        } catch (e: FirebaseAuthInvalidCredentialsException) {
            Log.d(Constants.EMAIL, "Invalid credentials: ${e.message}")
            handleError("Credenciales inválidas.")

        } catch (e: FirebaseException) {
            Log.d(Constants.EMAIL, "Firebase error: ${e.message}")
            handleError("Error de Firebase.")
        } catch (e: Exception) {
            Log.d(Constants.EMAIL, "Error: ${e.message}")
            handleError("Error desconocido.")
        }
    }

    fun changePassword(currentPassword: String, newPassword: String, successCallback: () -> Unit, errorCallback: (String) -> Unit) = viewModelScope.launch {
        try {
            val user = auth.currentUser
            if (user != null) {
                println(user.email)
                println(currentPassword)

                val credential = EmailAuthProvider.getCredential(user.email!!, currentPassword)
                user.reauthenticate(credential).addOnCompleteListener { reauthTask ->
                    if (reauthTask.isSuccessful) {
                        user.updatePassword(newPassword).addOnCompleteListener { updatePasswordTask ->
                            if (updatePasswordTask.isSuccessful) {
                                successCallback()
                            } else {
                                errorCallback("Error al cambiar la contraseña: ${updatePasswordTask.exception?.message}")
                                handleError("Error al cambiar la contraseña.")
                            }
                        }
                    } else {
                        errorCallback("Error de autenticación: ${reauthTask.exception?.message}")
                        handleError("Error de autenticación.")
                    }
                }
            } else {
                errorCallback("No hay usuario autenticado.")
                handleError("No hay usuario autenticado.")
            }
        } catch (e: Exception) {
            errorCallback("Error: ${e.message}")
            handleError("Error desconocido.")
        }
    }

    fun signOut() = viewModelScope.launch {
        try {
            auth.signOut()
        } catch (e: Exception) {
            Log.d(Constants.EMAIL, "Sign out error: ${e.message}")
        }
    }
}
