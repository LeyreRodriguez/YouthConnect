package com.example.youthconnect.ViewModel

import android.content.Context
import android.net.Uri
import android.os.Environment
import android.util.Log
import androidx.compose.ui.text.toLowerCase
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.youthconnect.Model.Firebase.Firestore.FirestoreRepository
import com.example.youthconnect.Model.Firebase.Storage.FirebaseStorageRepository
import com.example.youthconnect.Model.Object.Child
import com.example.youthconnect.Model.Object.Instructor
import com.example.youthconnect.Model.Object.Parent
import com.example.youthconnect.Model.Object.UserData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val firestoreRepository : FirestoreRepository,
    private val firebaseStorage: FirebaseStorageRepository
) : ViewModel() {
    private var document: String? = null

    private var allChild: List<Child?> = emptyList()
    private var searchedChild : List<Child?> = emptyList()
    private var child : Child? = null

    private var allParents: List<Parent?> = emptyList()
    private var parent: Parent? = null
    private var allInstructor: List<Instructor?> = emptyList()
    private var instructor: Instructor? = null
    private var user: UserData? = null


    private var _userData = MutableStateFlow<UserData?>(null)
    val userData = _userData.asStateFlow()


    private var _logoutCompleted = MutableStateFlow(false)
    var logoutCompleted = _logoutCompleted.asStateFlow()


    init {
        viewModelScope.launch {
            _userData.value = firestoreRepository.getUser()
        }

    }

    suspend fun getCurrentUser() : String? {
        try {
            document = firestoreRepository.getCurrentUser()
            return document
        } catch (e: Exception) {
            Log.e("Firestore", "Error en getCurrentUser", e)
            return null
        }
    }

    suspend fun getUserById(ID: String): UserData? {
        return try {
            user = firestoreRepository.getUserById(ID)
            user
        } catch (e: Exception) {
            Log.e("Firestore", "Error en getUserById", e)
            null
        }
    }



    suspend fun findDocument(userId : String) : String?{

        try {

            document = firestoreRepository.findDocument(userId)
            return document
        } catch (e: Exception) {
            Log.e("Firestore", "Error en findDocument", e)
            return null
        }
    }

    suspend fun getAllChildren(): List<Child?> {
        try {

            if (allChild.isEmpty()){
                allChild = firestoreRepository.getAllChildren()
            }
            searchedChild = allChild
            return searchedChild

        } catch (e: Exception) {
            Log.e("Firestore", "Error en getAllChildren", e)
            return emptyList()
        }
    }

    suspend fun getAllInstructors(): List<Instructor?> {
        try {

            if (allInstructor.isEmpty()){
                allInstructor = firestoreRepository.getAllInstructors()
            }

            return allInstructor

        } catch (e: Exception) {
            Log.e("Firestore", "Error en getAllChildren", e)
            return emptyList()
        }
    }

    suspend fun getChildByInstructorId(instructorId: String): List<Child?> {
        try {


            allChild = firestoreRepository.getChildByInstructorId(instructorId)

            return allChild

        } catch (e: Exception) {
            Log.e("Firestore", "Error en getChildByInstructorId", e)
            return emptyList()
        }
    }

    suspend fun getChildByInstructorIdThatIsInSchool(instructorId: String): List<Child?> {
        try {

            allChild = firestoreRepository.getChildByInstructorIdThatIsInSchool(instructorId)

            return allChild

        } catch (e: Exception) {
            Log.e("Firestore", "Error en getChildByInstructorId", e)
            return emptyList()
        }
    }


    suspend fun getInstructorByChildId(childId: String): Instructor? {
        try {


            instructor = firestoreRepository.getInstructorByChildId(childId)

            return instructor

        } catch (e: Exception) {
            Log.e("Firestore", "Error en getChildByInstructorId", e)
            return null
        }
    }


    suspend fun getChildByParentsId(parentID: String): List<Child?> {
        try {

            if (allChild.isEmpty()){
                allChild = firestoreRepository.getChildByParentsId(parentID)
            }
            // searchedChild = allChild
            return allChild

        } catch (e: Exception) {
            Log.e("Firestore", "Error en getChildByParentsId", e)
            return emptyList()
        }
    }


    suspend fun getCurrentChildById(childId : String) : Child?{
        try {
            child = firestoreRepository.getCurrentChildById(childId)
            return child
        } catch (e: Exception) {
            Log.e("Firestore", "Error en getCurrentChildById", e)
            return null
        }
    }

    suspend fun getCurrentUserById(parentID : String) : Parent?{
        try {
            parent = firestoreRepository.getCurrentUserById(parentID)
            return parent
        } catch (e: Exception) {
            Log.e("Firestore", "Error en getCurrentUserById", e)
            return null
        }
    }


    suspend fun getParentsByParentsID(parentsID: List<String>): List<Parent?> {
        try {

            if (allParents.isEmpty()){
                allParents = firestoreRepository.getParentsByParentsID(parentsID)
            }
           // searchedChild = allChild
            return allParents

        } catch (e: Exception) {
            Log.e("Firestore", "Error en getParentsByParentsID", e)
            return emptyList()
        }
    }

    suspend fun getCurrentInstructorById(instructorID: String): Instructor? {
        return try {
            instructor = firestoreRepository.getCurrentInstructorById(instructorID)
            return  instructor
        } catch (e: Exception) {
            Log.e("Firestore", "Error en getCurrentInstructorById", e)
            null
        }
    }


    suspend fun changeState(childId : String) {
        try {
            firestoreRepository.changeState(childId)

        } catch (e: Exception) {
            Log.e("Firestore", "Error en changeState", e)

        }
    }


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
            val userId = firebaseStorage.authConection?.currentUser?.email
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

    fun getProfileEspecificImage(email: String, onSuccess: (String) -> Unit, onFailure: (Exception) -> Unit) {
        viewModelScope.launch {

            Log.i("USER", email)
            val userId = email
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

    suspend fun getAllUsers() : List<UserData?>? {
        try {
            return firestoreRepository.getAllUser()
        } catch (e: Exception) {
            Log.e("Firestore", "Error en getCurrentUser", e)
            return null
        }
    }


    suspend fun getUserType(userID: String): String? {
        val collection = firestoreRepository.findUserType(userID)
        return collection
    }


    suspend fun getRollState(childId :String) : List<String>?{
        val collection = firestoreRepository.getRollCall(childId)
        return collection
    }


    fun changeInstructor(child :Child, instructorID: String){

        viewModelScope.launch {
                firestoreRepository.addInstructorToChild(child, instructorID)

        }
    }

    fun updateUser( user : Any){
        try {
            firestoreRepository.updateUser(user)

        } catch (e: Exception) {
            Log.e("Firestore", "Error en changeState", e)

        }
    }


}