package com.example.youthconnect.ViewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.youthconnect.Model.Firebase.Firestore.FirestoreRepository
import com.example.youthconnect.Model.Object.Child
import com.example.youthconnect.Model.Object.Instructor
import com.example.youthconnect.Model.Object.Parent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val firestoreRepository : FirestoreRepository
) : ViewModel() {
    private var document: String? = null

    private var allChild: List<Child?> = emptyList()
    private var searchedChild : List<Child?> = emptyList()
    private var child : Child? = null

    private var allParents: List<Parent?> = emptyList()
    private var parent: Parent? = null
    private var allInstructor: List<Instructor?> = emptyList()
    private var instructor: Instructor? = null

  //  var child by mutableStateOf(Child())

    suspend fun getCurrentUser() : String? {
        try {
            document = firestoreRepository.getCurrentUser()
            return document
        } catch (e: Exception) {
            Log.e("Firestore", "Error en getCurrentUser", e)
            return null
        }
    }

    suspend fun findDocument(userId : String) : String?{
        Log.e("USER", userId)
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

    suspend fun getChildByInstructorId(instructorId: String): List<Child?> {
        try {


            allChild = firestoreRepository.getChildByInstructorId(instructorId)

            return allChild

        } catch (e: Exception) {
            Log.e("Firestore", "Error en getChildByInstructorId", e)
            return emptyList()
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

}