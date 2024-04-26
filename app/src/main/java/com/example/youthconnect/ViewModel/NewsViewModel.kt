package com.example.youthconnect.ViewModel

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.youthconnect.Model.Constants
import com.example.youthconnect.Model.Firebase.Firestore.FirestoreRepository
import com.example.youthconnect.Model.Firebase.Storage.FirebaseStorageRepository
import com.example.youthconnect.Model.Object.News
import com.example.youthconnect.Model.Sealed.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(
    private val firestoreRepository: FirestoreRepository,private val repo: FirebaseStorageRepository

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


    var addImageToStorageResponse by mutableStateOf<Response<Uri>>(Response.Success(null))
        private set
    var addImageToDatabaseResponse by mutableStateOf<Response<Boolean>>(Response.Success(null))
        private set

    var getImageFromDatabaseResponse by mutableStateOf<Response<String>>(Response.Success(null))
        private set

    fun addNewsToStorage(imageUri : Uri, id : String) = viewModelScope.launch {
        addImageToStorageResponse = Response.Loading
        addImageToStorageResponse = repo.addNewsImageToFirebaseStorage(imageUri, id)
    }

    fun addNewsToDatabase(downloadUrl : Uri, news : News) = viewModelScope.launch {
        addImageToStorageResponse = Response.Loading
        addImageToDatabaseResponse = repo.addNewsImageUrlToFirestore(downloadUrl.toString(), news)
    }

    fun getNewsImageFromDatabase() = viewModelScope.launch {
        getImageFromDatabaseResponse = Response.Loading
        getImageFromDatabaseResponse = repo.getNewsImageUrlFromFirestore()
    }

    fun updateNews( news : News){
        try {
            firestoreRepository.updateNews(news)

        } catch (e: Exception) {
            e.message?.let { Log.e(Constants.ERROR_LOG_TAG, it) }

        }
    }

    fun deleteNews( newsId : String){
        try {
            firestoreRepository.deleteNews(newsId)

        } catch (e: Exception) {
            e.message?.let { Log.e(Constants.ERROR_LOG_TAG, it) }

        }
    }


}
