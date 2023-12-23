package com.example.youthconnect.Model.Firebase.Storage

import android.net.Uri
import com.example.youthconnect.Model.Object.News
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

interface FirebaseStorageRepository {
    val authConection: FirebaseAuth?
    val storageDataBase: FirebaseStorage?
    val firebase: FirebaseFirestore?
    suspend fun uploadImageToFirebase(imageUri: Uri, onSuccess: (String) -> Unit, onFailure: (Exception) -> Unit)
    suspend fun getProfileImageUrl(userId: String): String

    suspend fun addNewsImageToFirebaseStorage(imageUri: Uri,  id : String) : AddImageToStorageResponse
    suspend fun addNewsImageUrlToFirestore(download : Uri, news : News) : AddImageUrlToFirestoreResponse

    suspend fun getNewsImageUrlFromFirestore(): GetImageFromFirestoreResponse

}