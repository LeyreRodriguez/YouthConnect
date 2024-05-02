package com.example.youthconnect.ViewModel

import android.content.Context
import android.net.Uri
import android.os.Environment
import android.util.Log
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.youthconnect.Model.Constants
import com.example.youthconnect.Model.Firebase.Firestore.FirestoreRepository
import com.example.youthconnect.Model.Firebase.Storage.FirebaseStorageRepository
import com.example.youthconnect.Model.Object.Child
import com.example.youthconnect.Model.Object.Instructor
import com.example.youthconnect.Model.Object.Parent
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
class UserViewModel @Inject constructor(
    private val firestoreRepository: FirestoreRepository,
    private val firebaseStorage: FirebaseStorageRepository
) : ViewModel() {
    private var document: String? = null

    private var allChild: List<Child?> = emptyList()
    private var searchedChild: List<Child?> = emptyList()
    private var child: Child? = null

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

    suspend fun getCurrentUser(): String? {
        return try {
            document = firestoreRepository.getCurrentUser()
            document
        } catch (e: Exception) {
            null
        }
    }

    suspend fun getUserById(id: String): UserData? {
        return try {
            user = firestoreRepository.getUserById(id)
            user
        } catch (e: Exception) {
            null
        }
    }

    suspend fun findDocument(userId: String): String? {
        return try {
            document = firestoreRepository.findDocument(userId)
            document
        } catch (e: Exception) {
            null
        }
    }

    suspend fun getAllChildren(): List<Child?> {
        return try {
            if (allChild.isEmpty()) {
                allChild = firestoreRepository.getAllChildren()
            }
            searchedChild = allChild
            searchedChild
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun getAllInstructors(): List<Instructor?> {
        return try {
            if (allInstructor.isEmpty()) {
                allInstructor = firestoreRepository.getAllInstructors()
            }
            allInstructor
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun getChildByInstructorId(instructorId: String): List<Child?> {
        return try {
            allChild = firestoreRepository.getChildByInstructorId(instructorId)
            allChild
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun getChildByInstructorIdThatIsInSchool(instructorId: String): List<Child?> {
        return try {
            allChild = firestoreRepository.getChildByInstructorIdThatIsInSchool(instructorId)
            allChild
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun getInstructorByChildId(childId: String): Instructor? {
        return try {
            instructor = firestoreRepository.getInstructorByChildId(childId)
            instructor
        } catch (e: Exception) {
            null
        }
    }

    suspend fun getChildByParentsId(parentID: String): List<Child?> {
        return try {
            if (allChild.isEmpty()) {
                allChild = firestoreRepository.getChildByParentsId(parentID)
            }
            allChild
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun getCurrentChildById(childId: String): Child? {
        return try {
            child = firestoreRepository.getCurrentChildById(childId)
            child
        } catch (e: Exception) {
            null
        }
    }

    suspend fun getCurrentUserById(parentID: String): Parent? {
        return try {
            parent = firestoreRepository.getCurrentUserById(parentID)
            parent
        } catch (e: Exception) {
            null
        }
    }

    suspend fun getParentsByParentsID(parentsID: List<String>): List<Parent?> {
        return try {
            if (allParents.isEmpty()) {
                allParents = firestoreRepository.getParentsByParentsID(parentsID)
            }
            allParents
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun getCurrentInstructorById(instructorID: String): Instructor? {
        return try {
            instructor = firestoreRepository.getCurrentInstructorById(instructorID)
            instructor
        } catch (e: Exception) {
            null
        }
    }

    suspend fun changeState(childId: String) {
        try {
            firestoreRepository.changeState(childId)
        } catch (e: Exception) {
            e.message?.let { Log.e(Constants.ERROR_LOG_TAG, it) }
        }
    }

    fun uploadProfileImage(
        imageUri: Uri,
        onSuccess: (String) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        viewModelScope.launch {
            try {
                firebaseStorage.uploadImageToFirebase(imageUri, onSuccess, onFailure)
            } catch (e: Exception) {
                onFailure(e)
            }
        }
    }

    fun getProfileImage(
        onSuccess: (String) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
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

    fun getProfileEspecificImage(
        email: String,
        onSuccess: (String) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        viewModelScope.launch {
            val userId = email
            if (userId != null) {
                try {
                    onSuccess(firebaseStorage.getProfileImageUrl(userId))
                } catch (e: Exception) {
                    onSuccess(Constants.IMAGE)
                }
            }
        }
    }

    fun createImageUri(context: Context): Uri? {
        val fileName = FileUtil.createUniqueImageFileName()
        return FileUtil.createImageUri(context, fileName)
    }

    suspend fun getAllUsers(): List<UserData?>? {
        return try {
            firestoreRepository.getAllUser()
        } catch (e: Exception) {
            null
        }
    }

    suspend fun getUserType(userID: String): String? {
        return firestoreRepository.findUserType(userID)
    }

    suspend fun getRollState(childId: String): List<String>? {
        return firestoreRepository.getRollCall(childId)
    }

    fun changeInstructor(child: Child, instructorID: String) {
        viewModelScope.launch {
            firestoreRepository.addInstructorToChild(child, instructorID)
        }
    }

    fun updateUser(user: Any) {
        try {
            firestoreRepository.updateUser(user)
        } catch (e: Exception) {
            e.message?.let { Log.e(Constants.ERROR_LOG_TAG, it) }
        }
    }
}

object FileUtil {
    fun createUniqueImageFileName(): String {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        return "JPEG_${timeStamp}_"
    }

    fun createImageUri(context: Context, fileName: String): Uri? {
        val storageDir: File? = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return try {
            val file = File.createTempFile(
                fileName, /* prefix */
                ".jpg", /* suffix */
                storageDir /* directory */
            )

            val authority = "${context.packageName}.provider"
            FileProvider.getUriForFile(context, authority, file)
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }
}
