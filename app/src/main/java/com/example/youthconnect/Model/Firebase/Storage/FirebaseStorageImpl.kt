package com.example.youthconnect.Model.Firebase.Storage


import android.net.Uri
import com.example.youthconnect.Model.Constants
import com.example.youthconnect.Model.Object.News
import com.example.youthconnect.Model.Sealed.Response
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageException
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseStorageImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val storage: FirebaseStorage,
    private val db : FirebaseFirestore
): FirebaseStorageRepository{

    override val authConection: FirebaseAuth?
        get() = auth

    override val storageDataBase: FirebaseStorage?
        get() = storage

    override val firebase: FirebaseFirestore?
        get() = db
    override suspend fun uploadImageToFirebase(
        imageUri: Uri,
        onSuccess: (String) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val userId = auth.currentUser?.uid ?: return
        val storageRef = storage.reference.child("users/$userId/profilePicture.jpg")

        storageRef.putFile(imageUri).addOnSuccessListener {
            storageRef.downloadUrl.addOnSuccessListener { uri ->
                onSuccess(uri.toString())
            }
        }.addOnFailureListener { exception ->
            onFailure(exception)
        }
    }


    override suspend fun getProfileImageUrl(userId: String): String {

        val storageRef = storage.reference.child("users/$userId/profilePicture.jpg")
        return try {
            val uri = storageRef.downloadUrl.await()
            uri.toString()
        } catch (exception: StorageException) {

            if (exception.errorCode == StorageException.ERROR_OBJECT_NOT_FOUND) {

                throw exception
            } else {

                throw exception
            }
        }
    }

    override suspend fun addNewsImageToFirebaseStorage(imageUri: Uri,  id : String): AddImageToStorageResponse {
        return try {
            val downloadUrl = storage.reference.child(Constants.NEWS_IMAGES).child(id)
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
                Constants.URL to download,
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
            val imageUrl = db.collection(Constants.NEWS_IMAGES).document(Constants.UID).get().await().getString(
                Constants.URL
            )
            Response.Success(imageUrl)
        }catch (e:Exception){
            Response.Failure(e)
        }
    }


}