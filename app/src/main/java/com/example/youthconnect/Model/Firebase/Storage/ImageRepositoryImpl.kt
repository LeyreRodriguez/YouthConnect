package com.example.youthconnect.Model.Firebase.Storage

import android.net.Uri
import android.util.Log
import com.example.youthconnect.Model.Constants.NEWS_IMAGES
import com.example.youthconnect.Model.Constants.USER_IMAGES

import com.example.youthconnect.Model.Constants.UID
import com.example.youthconnect.Model.Constants.URL
import com.example.youthconnect.Model.Object.Instructor
import com.example.youthconnect.Model.Object.News
import com.example.youthconnect.Model.Sealed.Response
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ImageRepositoryImpl @Inject constructor(
    private val storage : FirebaseStorage,
    private val db : FirebaseFirestore
): ImageRepository {
    override suspend fun addNewsImageToFirebaseStorage(imageUri: Uri,  id : String): AddImageToStorageResponse {
        return try {
            val downloadUrl = storage.reference.child(NEWS_IMAGES).child(id)
                .putFile(imageUri).await()
                .storage.downloadUrl.await()
            Response.Success(downloadUrl)
        }
        catch (e:Exception){
            Response.Failure(e)
        }
    }

    override suspend fun addNewsImageUrlToFirestore(download: Uri, news : News): AddImageUrlToFirestoreResponse {
        return try {
            db.collection("News").document(news.id).set(mapOf(
                URL to download,
                "Description" to news.Description,
                "Title" to news.Title,
                "id" to news.id
            )).await()
            Response.Success(true)
        }catch (e:Exception){
            Response.Failure(e)
        }
    }


    override suspend fun getNewsImageUrlFromFirestore(): GetImageFromFirestoreResponse {
        return try {
            val imageUrl = db.collection(NEWS_IMAGES).document(UID).get().await().getString(URL)
            Response.Success(imageUrl)
        }catch (e:Exception){
            Response.Failure(e)
        }
    }
}