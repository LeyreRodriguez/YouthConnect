package com.example.youthconnect.ViewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.youthconnect.Model.FirestoreRepository
import com.example.youthconnect.Model.Object.News
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(
    private val firestoreRepository: FirestoreRepository,

): ViewModel(){

    private var allNews: List<News?> = emptyList()
    private var searchedNews : List<News?> = emptyList()

    private var News : News? = null
    suspend fun getAllNews(): List<News?> {
        try {

            if (allNews.isEmpty()){
                allNews = firestoreRepository.getAllNews()
            }
            searchedNews = allNews
            return searchedNews

        } catch (e: Exception) {
            Log.e("Firestore", "Error en getBooksStringMatch", e)
            return emptyList()
        }
    }

    suspend fun getNewsById(newsId: String): News? {
        try {

            News = firestoreRepository.getNewsById(newsId)

            //searchedNews = allNews
            return News

        } catch (e: Exception) {
            Log.e("Firestore", "Error en getBooksStringMatch", e)
            return null
        }
    }
}
