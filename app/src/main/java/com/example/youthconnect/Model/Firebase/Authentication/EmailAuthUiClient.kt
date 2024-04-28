package com.example.youthconnect.Model.Firebase.Authentication

import com.example.youthconnect.Model.Object.Instructor
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await

class EmailAuthUiClient (
    //need a context?
    private val auth: FirebaseAuth
){

    suspend fun registerUser(email: String, password: String): Result<String> {
        println("$email@youthconnect.com")
        println(password)
        return try {
            auth.createUserWithEmailAndPassword("$email@youthconnect.com", password).await()
            Result.success("Registro exitoso")
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun unregisterUser(email: String, password: String): Result<String> {
        println("$email@youthconnect.com")
        println(password)

        return try {
            val credentials = EmailAuthProvider.getCredential("$email@youthconnect.com", password)
            auth.signInWithCredential(credentials).await()
            auth.currentUser?.delete()?.await()
            Result.success("Usuario eliminado exitosamente")
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}