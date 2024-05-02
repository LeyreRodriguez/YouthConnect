package com.example.youthconnect.Model.Firebase.Authentication

import com.example.youthconnect.Model.Object.Instructor
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthRecentLoginRequiredException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import kotlinx.coroutines.tasks.await

class EmailAuthUiClient (
    private val auth: FirebaseAuth
){

    suspend fun registerUser(email: String, password: String): Result<String> {
        return try {
            auth.createUserWithEmailAndPassword("$email@youthconnect.com", password).await()
            Result.success("Registro exitoso")
        } catch (e: FirebaseAuthUserCollisionException) {
            Result.failure(Exception("El usuario ya está registrado. Por favor, inicie sesión."))
        } catch (e: FirebaseAuthWeakPasswordException) {
            Result.failure(Exception("La contraseña es demasiado débil. Por favor, elija una contraseña más segura."))
        } catch (e: Exception) {
            Result.failure(Exception("Error al registrar el usuario: ${e.message}"))
        }
    }

    suspend fun unregisterUser(email: String, password: String): Result<String> {
        return try {
            val credentials = EmailAuthProvider.getCredential("$email@youthconnect.com", password)
            auth.signInWithCredential(credentials).await()
            auth.currentUser?.delete()?.await()
            Result.success("Usuario eliminado exitosamente")
        } catch (e: FirebaseAuthInvalidCredentialsException) {
            Result.failure(Exception("Credenciales inválidas. Por favor, revise su correo electrónico y contraseña."))
        } catch (e: FirebaseAuthRecentLoginRequiredException) {
            Result.failure(Exception("Se requiere una nueva autenticación. Por favor, inicie sesión nuevamente."))
        } catch (e: Exception) {
            Result.failure(Exception("Error al eliminar el usuario: ${e.message}"))
        }
    }
}