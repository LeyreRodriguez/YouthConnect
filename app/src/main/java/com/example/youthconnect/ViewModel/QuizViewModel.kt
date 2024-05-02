package com.example.youthconnect.ViewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.youthconnect.Model.Constants
import com.example.youthconnect.Model.Firebase.Firestore.FirestoreRepository
import com.example.youthconnect.Model.Firebase.Storage.FirebaseStorageRepository
import com.example.youthconnect.Model.Object.Question
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class QuizViewModel @Inject constructor(
    private val firestoreRepository: FirestoreRepository,
    private val repo: FirebaseStorageRepository
) : ViewModel() {

    private var allQuestions: List<Question?> = emptyList()
    private var searchedQuestion: List<Question?> = emptyList()

    suspend fun getAllQuestions(): List<Question?> {
        return try {
            if (allQuestions.isEmpty()) {
                allQuestions = firestoreRepository.getQuestions()
            }
            searchedQuestion = allQuestions.shuffled()
            searchedQuestion
        } catch (e: Exception) {
            Log.e("Firestore", "Error en getBooksStringMatch", e)
            emptyList()
        }
    }

    fun addNewQuestion(question: Question) {
        viewModelScope.launch {
            try {
                firestoreRepository.addNewQuestion(question)
            } catch (e: Exception) {
                Log.e(Constants.ERROR_LOG_TAG, "Error adding question: ${e.message}")
            }
        }
    }

    fun updateScore(userID: String) {
        viewModelScope.launch {
            try {
                val collection = firestoreRepository.findUserType(userID)
                if (collection != null) {
                    firestoreRepository.updateScore(collection, userID)
                }
            } catch (e: Exception) {
                Log.e(Constants.ERROR_LOG_TAG, "Error updating score: ${e.message}")
            }
        }
    }

    fun resetScore(userID: String) {
        viewModelScope.launch {
            try {
                val collection = firestoreRepository.findUserType(userID)
                if (collection != null) {
                    firestoreRepository.resetScore(collection, userID)
                }
            } catch (e: Exception) {
                Log.e(Constants.ERROR_LOG_TAG, "Error resetting score: ${e.message}")
            }
        }
    }

    suspend fun getScore(userID: String): String? = withContext(Dispatchers.IO) {
        val collection = firestoreRepository.findUserType(userID)
        if (collection != null) {
            firestoreRepository.getScore(collection, userID)
        } else {
            ""
        }
    }

    fun updateQuestion(question: Question) {
        viewModelScope.launch {
            try {
                firestoreRepository.updateQuestion(question)
            } catch (e: Exception) {
                Log.e(Constants.ERROR_LOG_TAG, "Error updating question: ${e.message}")
            }
        }
    }

    fun deleteQuestion(questionId: String) {
        viewModelScope.launch {
            try {
                firestoreRepository.deleteQuestion(questionId)
            } catch (e: Exception) {
                Log.e(Constants.ERROR_LOG_TAG, "Error deleting question: ${e.message}")
            }
        }
    }
}
