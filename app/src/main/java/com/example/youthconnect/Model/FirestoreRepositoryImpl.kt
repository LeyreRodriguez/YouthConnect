package com.example.youthconnect.Model

import android.util.Log
import com.example.youthconnect.Model.Object.Child
import com.example.youthconnect.Model.Object.Instructor
import com.example.youthconnect.Model.Object.News
import com.example.youthconnect.Model.Object.Parent
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject


class FirestoreRepositoryImpl @Inject constructor(private val firebaseFirestore: FirebaseFirestore): FirestoreRepository {
    override val dataBase: FirebaseFirestore?
        get() = firebaseFirestore

    override suspend fun getChild(childId: String): Child? {
        return try {
            val document = firebaseFirestore.collection("Child").document(childId).get().await()
            Child(
                FullName = document.getString("fullName") ?: "",
                ID = document.getString("id") ?: "",
                Course = document.getString("course") ?: "",
                Password = document.getString("password") ?: "",
                BelongsToSchool = document.getBoolean("belongsToSchool") ?: false,
                FaithGroups = document.getBoolean("faithGroups") ?: false,
                GoOutAlone = document.getBoolean("goOutAlone") ?: false,
                Observations = document.getString("observations") ?: "",
                ParentID = document.get("parentID") as? List<String> ?: emptyList(),
                InstructorID = document.getString("instructorID") ?: ""
            )
        } catch (e: Exception) {
            Log.e("FirestoreRepository", "getChild failed with $e")
            null
        }
    }

    override suspend fun getAllChildren(): List<Child?> {
        return try {
            firebaseFirestore.collection("Child")
                .get()
                .await()
                .documents
                .map { document ->
                    Child(
                        FullName = document.getString("fullName") ?: "",
                        ID = document.getString("id") ?: "",
                        Course = document.getString("course") ?: "",
                        Password = document.getString("password") ?: "",
                        BelongsToSchool = document.getBoolean("belongsToSchool") ?: false,
                        FaithGroups = document.getBoolean("faithGroups") ?: false,
                        GoOutAlone = document.getBoolean("goOutAlone") ?: false,
                        Observations = document.getString("observations") ?: "",
                        ParentID = document.get("parentID") as? List<String> ?: emptyList(),
                        InstructorID = document.getString("instructorID") ?: ""
                    )
                }
        } catch (e: Exception) {
            Log.e("FirestoreRepository", "getAllChilds failed with $e")
            emptyList()
        }
    }

    override suspend fun getChildByInstructorId(instructorId: String): List<Child?> {
        return try {
            firebaseFirestore.collection("Child")
                .whereEqualTo("instructorID", instructorId)
                .get()
                .await()
                .documents
                .map { document ->
                    Child(
                        FullName = document.getString("fullName") ?: "",
                        ID = document.getString("id") ?: "",
                        Course = document.getString("course") ?: "",
                        Password = document.getString("password") ?: "",
                        BelongsToSchool = document.getBoolean("belongsToSchool") ?: false,
                        FaithGroups = document.getBoolean("faithGroups") ?: false,
                        GoOutAlone = document.getBoolean("goOutAlone") ?: false,
                        Observations = document.getString("observations") ?: "",
                        ParentID = document.get("parentID") as? List<String> ?: emptyList(),
                        InstructorID = document.getString("instructorID") ?: ""
                    )
                }
        } catch (e: Exception) {
            Log.e("FirestoreRepository", "getChildByInstructorId failed with $e")
            emptyList()
        }
    }

    override suspend fun getChildByParentsId(parentID : String) : List<Child?>{
        return try {
            firebaseFirestore.collection("Child")
                .whereArrayContains("parentID", parentID)
                .get()
                .await()
                .documents
                .map { document ->
                    Child(
                        FullName = document.getString("fullName") ?: "",
                        ID = document.getString("id") ?: "",
                        Course = document.getString("course") ?: "",
                        Password = document.getString("password") ?: "",
                        BelongsToSchool = document.getBoolean("belongsToSchool") ?: false,
                        FaithGroups = document.getBoolean("faithGroups") ?: false,
                        GoOutAlone = document.getBoolean("goOutAlone") ?: false,
                        Observations = document.getString("observations") ?: "",
                        ParentID = document.get("parentID") as? List<String> ?: emptyList(),
                        InstructorID = document.getString("instructorID") ?: ""
                    )
                }
        } catch (e: Exception) {
            Log.e("FirestoreRepository", "getChildByInstructorId failed with $e")
            emptyList()
        }
    }
    override suspend fun getCurrentChildById(childId: String): Child? {
        return try {
            val document = firebaseFirestore.collection("Child").document(childId).get().await()
            Child(
                FullName = document.getString("fullName") ?: "",
                ID = document.getString("id") ?: "",
                Course = document.getString("course") ?: "",
                Password = document.getString("password") ?: "",
                BelongsToSchool = document.getBoolean("belongsToSchool") ?: false,
                FaithGroups = document.getBoolean("faithGroups") ?: false,
                GoOutAlone = document.getBoolean("goOutAlone") ?: false,
                Observations = document.getString("observations") ?: "",
                ParentID = document.get("parentID") as? List<String> ?: emptyList(),
                InstructorID = document.getString("instructorID") ?: ""
            )
        } catch (e: Exception) {
            Log.e("FirestoreRepository", "getChild failed with $e")
            null
        }
    }


    override suspend fun getAllNews(): List<News?> {

        return try{
            firebaseFirestore.collection("News")
                .get()
                .await()
                .documents
                .map { document ->
                    News(
                        id = document.getString("id") ?: "",
                        Title = document.getString("Title") ?: "",
                        Description = document.getString("Description") ?: "",
                        Image = document.getString("Image") ?: ""
                    )
                }
        }catch (e: Exception) {
            Log.e("FirestoreRepository", "getNews failed with $e")
            emptyList()
        }

    }

    override suspend fun getNewsById(newsId: String): News? {
        return try {
            val document = firebaseFirestore.collection("News").document(newsId).get().await()
            document.toObject(News::class.java)
        } catch (e: Exception) {
            Log.e("FirestoreRepository", "getChild failed with $e")
            null
        }
    }

    override suspend fun findDocument(userId: String): String? {
        return withContext(Dispatchers.IO) {
            try {
                val childDocRef = firebaseFirestore.collection("Child").document(userId).get().await()
                if (childDocRef.exists()) {
                    return@withContext "2"
                }

                val parentDocRef = firebaseFirestore.collection("Parents").document(userId).get().await()
                if (parentDocRef.exists()) {
                    return@withContext "1"
                }

                val instructorDocRef = firebaseFirestore.collection("Instructor").document(userId).get().await()
                if (instructorDocRef.exists()) {
                    return@withContext "0"
                }

                // Si no se encuentra en ninguna colección
                return@withContext "-1" // Puedes usar otro valor representativo si prefieres
            } catch (e: Exception) {
                // Manejar cualquier error que pueda ocurrir al buscar el documento
                e.printStackTrace()
                return@withContext "-1" // Puedes usar otro valor representativo si prefieres
            }
        }
    }

    override suspend fun getCurrentUserById(parentID: String): Parent? {
        return try {
            val document = firebaseFirestore.collection("Parents").document(parentID).get().await()
            Parent(
                FullName = document.getString("fullName") ?: "",
                ID = document.getString("id") ?: "",
                Password = document.getString("password") ?: "",
                PhoneNumber = document.getString("phoneNumber") ?: ""
            )
        } catch (e: Exception) {
            Log.e("FirestoreRepository", "getChild failed with $e")
            null
        }
    }

    override suspend fun getParentsByParentsID(parentsID: List<String>): List<Parent?> {
        return try {
            parentsID.mapNotNull { id ->
                firebaseFirestore.collection("Parents")
                    .whereEqualTo("id", id)
                    .get()
                    .await()
                    .documents
                    .firstOrNull()?.let { document ->
                        Parent(
                            FullName = document.getString("fullName") ?: "",
                            ID = document.getString("id") ?: "",
                            Password = document.getString("password") ?: "",
                            PhoneNumber = document.getString("phoneNumber") ?: ""
                        )
                    }
            }
        } catch (e: Exception) {
            Log.e("FirestoreRepository", "getParentsByParentsID failed with $e")
            emptyList()
        }
    }

    override suspend fun getCurrentInstructorById(instructorID: String): Instructor? {
        return try {
            Log.i("UWU", instructorID)
            val document = firebaseFirestore.collection("Instructor").document(instructorID).get().await()
            Log.i("AWA", document.data.toString())
            Instructor(
                FullName = document.getString("FullName") ?: "",
                ID = document.getString("ID") ?: "",
                Password = document.getString("password") ?: "",
            )
          //  document.toObject(Instructor::class.java)
        } catch (e: Exception) {
            Log.e("FirestoreRepository", "getChild failed with $e")
            null
        }
    }


}
