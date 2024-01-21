package com.example.youthconnect.Model.Firebase.Firestore


import com.example.youthconnect.Model.Object.Child
import com.example.youthconnect.Model.Object.Instructor
import com.example.youthconnect.Model.Object.News
import com.example.youthconnect.Model.Object.Parent
import com.example.youthconnect.Model.Object.UserData
import com.google.firebase.firestore.FirebaseFirestore


interface FirestoreRepository {
    val dataBase: FirebaseFirestore?

    suspend fun getCurrentUser() : String?
    suspend fun getChild(childId: String): Child?
    suspend fun getAllChildren(): List<Child?>

    suspend fun getChildByInstructorId(instructorID: String) : List<Child?>

    suspend fun getCurrentChildById(childId : String) : Child?

    suspend fun getChildByParentsId(parentID : String) : List<Child?>
    suspend fun getAllNews() : List<News?>
    suspend fun getNewsById(newsId: String) : News?

    suspend fun addNews(news : News)

    suspend fun findDocument(userId : String) : String?

    suspend fun getCurrentUserById(parentID : String) : Parent?

    suspend fun getParentsByParentsID(parentsID : List<String>) : List<Parent?>

    suspend fun getCurrentInstructorById( instructorID : String) : Instructor?

    suspend fun addChild(child: Child)
    suspend fun addParent(parent: Parent)
    suspend fun addInstructor(instructor: Instructor)

    suspend fun changeState(childId : String)

    suspend fun addInstructorToChild(child: Child, instructorID: String)
    suspend fun removeInstructorFromChild(child: Child, instructorID: String)

    suspend fun getUser() : UserData?
    suspend fun getAllUser() : List<UserData?>

    suspend fun getChatId(chatId : String) : String?



}