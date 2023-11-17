package com.example.youthconnect.ViewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.youthconnect.Model.DataBase
import com.example.youthconnect.Model.News
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

class NewsViewModel : ViewModel() {
    private val firestore = FirebaseFirestore.getInstance()
    private val _newsState = MutableStateFlow<List<News>>(emptyList())
    val newsState: Flow<List<News>> = _newsState.asStateFlow()

    // Función para obtener datos de Firestore y convertirlos en objetos News
    fun getNews() {
        viewModelScope.launch {
            firestore.collection("News")
                .get()
                .addOnSuccessListener { documents ->
                    val newsList = mutableListOf<News>()
                    for (document in documents) {
                        val id = document.getString("id") ?: ""
                        val title = document.getString("Title") ?: ""
                        val description = document.getString("Description") ?: ""
                        val image = document.getString("Image") ?: ""


                        val news = News(id,title, description, image)
                        newsList.add(news)
                    }
                    _newsState.value = newsList
                }
                .addOnFailureListener { exception ->
                    // Manejar errores aquí
                }


        }


    }

    fun getNewsById(newsId: String) {
        viewModelScope.launch {
            firestore.collection("News")
                .whereEqualTo("id", newsId)
                .get()
                .addOnSuccessListener { documents ->
                    var foundNews: News? = null
                    for (document in documents) {
                        val id = document.getString("id") ?: ""
                        val title = document.getString("Title") ?: ""
                        val description = document.getString("Description") ?: ""
                        val image = document.getString("Image") ?: ""

                        val news = News(id, title, description, image)
                        foundNews = news
                        break // Termina el bucle después de encontrar la primera noticia
                    }

                    if (foundNews != null) {
                        _newsState.value = listOf(foundNews!!)
                    } else {
                        // No se encontró ninguna noticia con esa ID
                        // Puedes manejar el caso estableciendo el estado con un valor nulo o un indicador de ausencia
                        _newsState.value = emptyList() // Por ejemplo, aquí se establece una lista vacía
                    }
                }
                .addOnFailureListener { exception ->
                    // Manejar errores aquí
                }
        }
    }


}