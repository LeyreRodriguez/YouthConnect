package com.example.youthconnect.Model.Firebase.Authentication

import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await

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