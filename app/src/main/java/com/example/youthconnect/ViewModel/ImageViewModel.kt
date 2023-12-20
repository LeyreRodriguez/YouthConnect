package com.example.youthconnect.ViewModel

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.youthconnect.Model.Firebase.Storage.ImageRepository
import com.example.youthconnect.Model.Object.Instructor
import com.example.youthconnect.Model.Object.News
import com.example.youthconnect.Model.Sealed.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ImageViewModel @Inject constructor(
    private val repo: ImageRepository
): ViewModel() {

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
        addImageToDatabaseResponse = repo.addNewsImageUrlToFirestore(downloadUrl, news)
    }

    fun getNewsImageFromDatabase() = viewModelScope.launch {
        getImageFromDatabaseResponse = Response.Loading
        getImageFromDatabaseResponse = repo.getNewsImageUrlFromFirestore()
    }


    fun addUserPhotoToStorage(imageUri : Uri, id : String) = viewModelScope.launch {
        addImageToStorageResponse = Response.Loading
        addImageToStorageResponse = repo.addNewsImageToFirebaseStorage(imageUri, id)
    }


}