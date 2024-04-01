package com.example.youthconnect.Model.Firebase.Authentication

import com.example.libraryapp.model.firebaseAuth.SignInResult
import com.example.youthconnect.Model.Object.UserData
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await
import java.util.concurrent.CancellationException

class EmailAuthUiClient (
    //need a context?
    private val auth: FirebaseAuth
){

    suspend fun registerUser(email: String, password: String): Result<String> {
        return try {
            auth.createUserWithEmailAndPassword(email + "@youthconnect.com", password).await()
            Result.success("Registro exitoso")
        } catch (e: Exception) {
            Result.failure(e)
        }
    }



}