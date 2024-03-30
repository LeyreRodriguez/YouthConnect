package com.example.youthconnect.ViewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.youthconnect.Model.Firebase.Firestore.FirestoreRepository
import com.example.youthconnect.Model.Firebase.Storage.FirebaseStorageRepository
import com.example.youthconnect.Model.Object.Question
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.util.UUID
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class QuizViewModel @Inject constructor(
    private val firestoreRepository: FirestoreRepository, private val repo: FirebaseStorageRepository

): ViewModel() {

    private var allQuestions: List<Question?> = emptyList()
    private var searchedQuestion : List<Question?> = emptyList()

    private var Question : Question? = null
    suspend fun getAllQuestions(): List<Question?> {
        try {
            if (allQuestions.isEmpty()){
                allQuestions = firestoreRepository.getQuestions()
            }
            searchedQuestion = allQuestions.shuffled()
            return searchedQuestion

        } catch (e: Exception) {
            Log.e("Firestore", "Error en getBooksStringMatch", e)
            return emptyList()
        }
    }

     fun addNewQuestion(question: Question) {
        try {
            firestoreRepository.addNewQuestion(question)

        } catch (e: Exception) {
            // Maneja cualquier excepción aquí, por ejemplo, registrándola o lanzándola
        }

    }



     fun updateScore(userID: String){

             val collection = runBlocking {
                 firestoreRepository.findUserType(userID)
             }

             if (collection != null) {

                 firestoreRepository.updateScore(collection, userID)
             }


    }


    fun resetScore(userID: String){

        val collection = runBlocking {
            firestoreRepository.findUserType(userID)
        }

        if (collection != null) {

            firestoreRepository.resetScore(collection, userID)
        }


    }

    suspend fun getScore(userID: String): String? = withContext(Dispatchers.IO) {
        val collection = firestoreRepository.findUserType(userID)
        if (collection != null) {
            return@withContext firestoreRepository.getScore(collection, userID)
        }
        return@withContext ""
    }
}