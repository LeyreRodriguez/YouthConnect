package com.example.libraryapp.viewModel

import SignInState
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.libraryapp.model.firebaseAuth.SignInResult
import com.google.firebase.FirebaseException
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

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

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
                Log.d("Login Email", "Authentication failed.")
            }
        } catch (e: FirebaseAuthInvalidCredentialsException) {
            Log.d("Login Email", "Invalid credentials: ${e.message}")
        } catch (e: FirebaseException) {
            Log.d("Login Email", "Firebase error: ${e.message}")
        } catch (e: Exception) {
            Log.d("Login Email", "Error: ${e.message}")
        }
    }

    fun signOut() = viewModelScope.launch {
        try {
            auth.signOut()
        } catch (e: Exception) {
            Log.d("Login Email", "Sign out error: ${e.message}")
        }
    }
}
