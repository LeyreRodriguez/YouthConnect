package com.example.youthconnect.Model.Firebase.Firestore

import com.example.youthconnect.Model.Object.Child
import com.example.youthconnect.Model.Object.Instructor
import com.example.youthconnect.Model.Object.News
import com.example.youthconnect.Model.Object.Parent
import com.google.firebase.firestore.FirebaseFirestore


interface FirestoreRepository {
    val dataBase: FirebaseFirestore?

    suspend fun getChild(childId: String): Child?
    suspend fun getAllChildren(): List<Child?>

    suspend fun getChildByInstructorId(instructorID: String) : List<Child?>

    suspend fun getCurrentChildById(childId : String) : Child?

    suspend fun getChildByParentsId(parentID : String) : List<Child?>
    suspend fun getAllNews() : List<News?>
    suspend fun getNewsById(newsId: String) : News?

    suspend fun findDocument(userId : String) : String?

    suspend fun getCurrentUserById(parentID : String) : Parent?

    suspend fun getParentsByParentsID(parentsID : List<String>) : List<Parent?>

    suspend fun getCurrentInstructorById( instructorID : String) : Instructor?

    suspend fun addChild(child: Child)
    suspend fun addParent(parent: Parent)
    suspend fun addInstructor(instructor: Instructor)


}