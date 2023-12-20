package com.example.youthconnect.Model.Firebase.Storage
import android.net.Uri
import com.example.youthconnect.Model.Object.Instructor
import com.example.youthconnect.Model.Object.News
import com.example.youthconnect.Model.Sealed.Response

typealias AddImageToStorageResponse = Response<Uri>
typealias AddImageUrlToFirestoreResponse = Response<Boolean>
typealias GetImageFromFirestoreResponse = Response<String>

interface ImageRepository {

    suspend fun addNewsImageToFirebaseStorage(imageUri: Uri,  id : String) : AddImageToStorageResponse
    suspend fun addNewsImageUrlToFirestore(download : Uri, news : News) : AddImageUrlToFirestoreResponse

    suspend fun getNewsImageUrlFromFirestore(): GetImageFromFirestoreResponse


}