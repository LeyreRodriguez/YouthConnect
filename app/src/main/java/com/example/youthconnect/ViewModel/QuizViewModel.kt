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
import kotlinx.coroutines.tasks.await
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
            searchedQuestion = allQuestions
            return searchedQuestion

        } catch (e: Exception) {
            Log.e("Firestore", "Error en getBooksStringMatch", e)
            return emptyList()
        }
    }



     fun updateScore(userID: String){

         GlobalScope.launch(Dispatchers.IO) {
             val documentReference = firestoreRepository.findID(userID)

             if (documentReference != null) {

                 firestoreRepository.updateScore(documentReference)
             }
         }


    }

    fun getScore(userID: String){

        GlobalScope.launch(Dispatchers.IO) {
            val documentReference = firestoreRepository.findID(userID)

            if (documentReference != null) {

                firestoreRepository.getScore(documentReference)
            }
        }


    }
}