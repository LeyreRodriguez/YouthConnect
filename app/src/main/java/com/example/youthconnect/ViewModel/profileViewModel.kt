package com.example.youthconnect.ViewModel

import android.content.Context
import android.net.Uri
import android.os.Environment
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.youthconnect.Model.Firebase.Firestore.FirestoreRepository
import com.example.youthconnect.Model.Firebase.Storage.FirebaseStorageRepository
import com.example.youthconnect.Model.Object.UserData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class profileViewModel @Inject constructor(
    private val firestoreRepository: FirestoreRepository,
    private val firebaseStorage: FirebaseStorageRepository
): ViewModel(
) {


    private var _userData = MutableStateFlow<UserData?>(null)
    val userData = _userData.asStateFlow()


    private var _logoutCompleted = MutableStateFlow(false)
    var logoutCompleted = _logoutCompleted.asStateFlow()

    init {
        viewModelScope.launch {
            _userData.value = firestoreRepository.getUser()
        }

    }

    // Función para subir imagen
    fun uploadProfileImage(imageUri: Uri, onSuccess: (String) -> Unit, onFailure: (Exception) -> Unit) {
        viewModelScope.launch {
            try {
                firebaseStorage.uploadImageToFirebase(imageUri, onSuccess, onFailure)
            } catch (e: Exception) {
                onFailure(e)
            }
        }
    }

    fun getProfileImage(onSuccess: (String) -> Unit, onFailure: (Exception) -> Unit) {
        viewModelScope.launch {
            val userId = firebaseStorage.authConection?.currentUser?.uid
            if (userId != null) {
                try {
                    val url = firebaseStorage.getProfileImageUrl(userId)
                    onSuccess(url)
                } catch (e: Exception) {
                    val url = "https://i.imgur.com/w3UEu8o.jpeg"
                    onSuccess(url)
                }
            }
        }
    }


    fun createImageUri(context: Context): Uri? {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val storageDir: File? = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return try {
            val file = File.createTempFile(
                "JPEG_${timeStamp}_", /* prefix */
                ".jpg", /* suffix */
                storageDir /* directory */
            )

            // Para compatibilidad con Android N y posteriores, usas FileProvider
            val authority = "${context.packageName}.provider"
            FileProvider.getUriForFile(context, authority, file)
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

}