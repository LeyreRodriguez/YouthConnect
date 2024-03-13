package com.example.youthconnect.Model.Firebase.Firestore

import android.util.Log
import com.example.youthconnect.Model.Constants
import com.example.youthconnect.Model.Object.Child
import com.example.youthconnect.Model.Object.Instructor
import com.example.youthconnect.Model.Object.News
import com.example.youthconnect.Model.Object.Parent
import com.example.youthconnect.Model.Object.Question
import com.example.youthconnect.Model.Object.UserData
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.util.UUID
import java.util.concurrent.ExecutionException
import javax.inject.Inject
import kotlinx.coroutines.async



class FirestoreRepositoryImpl @Inject constructor(private val firebaseFirestore: FirebaseFirestore):
    FirestoreRepository {
    override val dataBase: FirebaseFirestore?
        get() = firebaseFirestore

    val auth: FirebaseAuth = FirebaseAuth.getInstance()
    override suspend fun getCurrentUser() : String? {
        var user = auth.currentUser;
        if (user != null) {
            val email = user.email

            val numeroConvertido = email?.substringBefore("@").toString().dropLast(1) + email?.substringBefore("@").toString().takeLast(1).uppercase()
            return  numeroConvertido

        } else {
            return ""
        }
    }


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
                InstructorID = document.getString("instructorID") ?: "",
                State = document.getBoolean("state") ?: false,
                Score = document.getLong("Score")?.toInt() ?: null
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
                        InstructorID = document.getString("instructorID") ?: "",
                        State = document.getBoolean("state") ?: false,
                        Score = document.getLong("Score")?.toInt() ?: null
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
                        InstructorID = document.getString("instructorID") ?: "",
                        State = document.getBoolean("state") ?: false,
                        Score = document.getLong("Score")?.toInt() ?: null
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
                        InstructorID = document.getString("instructorID") ?: "",
                        State = document.getBoolean("state") ?: false,
                        Score = document.getLong("Score")?.toInt() ?: null
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
                InstructorID = document.getString("instructorID") ?: "",
                State = document.getBoolean("state") ?: false,
                Score = document.getLong("Score")?.toInt() ?: null
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

    override suspend fun addNews(news : News) {
        val randomDocumentId = UUID.randomUUID().toString()

        val documentRef: DocumentReference = firebaseFirestore.collection("News").document(randomDocumentId)

        // Realiza la consulta para obtener el documento
        documentRef.get()
            .addOnSuccessListener { document ->

                    firebaseFirestore.collection("News")
                        .document(randomDocumentId)
                        .set(news)


            }

    }

    override suspend fun findDocument(userId: String): String? {
        Log.e("OWO", userId)
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
                PhoneNumber = document.getString("phoneNumber") ?: "",
                Score = document.getLong("Score")?.toInt() ?: null
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
                            PhoneNumber = document.getString("phoneNumber") ?: "",
                            Score = document.getLong("Score")?.toInt() ?: null
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
                Score = document.getLong("Score")?.toInt() ?: null
            )
          //  document.toObject(Instructor::class.java)
        } catch (e: Exception) {
            Log.e("FirestoreRepository", "getChild failed with $e")
            null
        }
    }

    override suspend fun addChild(child: Child) {
        val documentRef: DocumentReference = firebaseFirestore.collection("Child").document(child.ID)

        // Realiza la consulta para obtener el documento
        documentRef.get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val currentParentIds = document.get("parentID") as? List<String> ?: emptyList()
                    val newParentsId = currentParentIds + child.ParentID

                    val updates = hashMapOf<String, Any>(
                        "parentID" to newParentsId
                        // Puedes agregar más campos según sea necesario
                    )

                    // Realiza la actualización del documento
                    documentRef.update(updates)
                        .addOnSuccessListener {
                            // La actualización fue exitosa
                            Log.i("Actualizacion", "completada")
                        }
                        .addOnFailureListener { exception ->
                            Log.i("Actualizacion", "no completada")
                        }

                    val data = document.data
                    // Haz algo con los datos obtenidos
                } else {
                    // El documento no existe

                    firebaseFirestore.collection("Child")
                        .document(child.ID)
                        .set(child)

                }
            }

    }

    override suspend fun addParent(parent: Parent) {



        val documentRef: DocumentReference = firebaseFirestore.collection("Parents").document(parent.ID)

        // Realiza la consulta para obtener el documento
        documentRef.get()
            .addOnSuccessListener { document ->
                if (document.exists()) {

                } else {
                    // El documento no existe

                    firebaseFirestore.collection("Parents")
                        .document(parent.ID)
                        .set(parent)

                }
            }


    }

    override suspend fun addInstructor( instructor: Instructor) {
        firebaseFirestore.collection("Instructor")
            .document(instructor.ID)
            .set(instructor)

    }

    override suspend fun changeState(childId: String) {
        try {
            val childDocumentRef = firebaseFirestore.collection("Child").document(childId)
            val childDocument = childDocumentRef.get().await()

            val currentState = childDocument.getBoolean("state") ?: false

            // Cambiar el valor de State al opuesto del estado actual
            childDocumentRef.update("state", !currentState).await()

        } catch (e: Exception) {
            Log.e("FirestoreRepository", "toggleChildState failed with $e")
        }
    }

    override suspend fun addInstructorToChild(child: Child, instructorID: String) {
        val childDocument = firebaseFirestore.collection("Child").document(child.ID)
        // Actualizar el valor de instructorId en el documento
        childDocument.update("instructorID", instructorID)

    }

    override suspend fun removeInstructorFromChild(child: Child, instructorID: String) {
        val childDocument = firebaseFirestore.collection("Child").document(child.ID)
        // Actualizar el valor de instructorId en el documento
        childDocument.update("instructorID", "")
    }

    override suspend fun getUser(): UserData? {
        return auth.currentUser?.run {
            UserData(
                userId = uid,
                userName = displayName,
                profilePictureUrl = photoUrl?.toString()
            )

        }

    }

    override suspend fun getAllUser(): List<UserData> {
        val allUsers: MutableList<UserData> = mutableListOf()

        try {
            val childQuerySnapshot = firebaseFirestore.collection("Child").get().await()
            val instructorQuerySnapshot = firebaseFirestore.collection("Instructor").get().await()
            val parentQuerySnapshot = firebaseFirestore.collection("Parents").get().await()

            allUsers.addAll(childQuerySnapshot.documents.map { document ->
                UserData(
                    userId = document.getString("id") ?: "",
                    userName = document.getString("fullName") ?: ""
                    // profilePictureUrl = photoUrl?.toString()
                )
            })

            allUsers.addAll(instructorQuerySnapshot.documents.map { document ->
                UserData(
                    userId = document.getString("id") ?: "",
                    userName = document.getString("fullName") ?: ""
                    // profilePictureUrl = photoUrl?.toString()
                )
            })

            allUsers.addAll(parentQuerySnapshot.documents.map { document ->
                UserData(
                    userId = document.getString("id") ?: "",
                    userName = document.getString("fullName") ?: ""
                    // profilePictureUrl = photoUrl?.toString()
                )
            })
        } catch (e: Exception) {
            Log.e("FirestoreRepository", "getAllUsers failed with $e")
        }

        return allUsers
    }

    override suspend fun getChatId(chatId : String): String? {
        return try {
            val querySnapshot = firebaseFirestore.collection(Constants.MESSAGES).get().await()

            for (document in querySnapshot.documents) {
                val documentChatId = document.getString("chatId")
                if (documentChatId == chatId) {
                    return documentChatId
                }
            }

            // Si no se encuentra el campo chatID en ningún documento
            return null
        } catch (e: Exception) {
            Log.e("FirestoreRepository", "getChatId failed with $e")
            null
        }
    }

    override suspend fun getQuestions(): List<Question?> {
        return try {
            firebaseFirestore.collection("Quiz")
                .get()
                .await()
                .documents
                .map { document ->
                    Question(
                        answer = document.getString("answer") ?: "",
                        optionA = document.getString("optionA") ?: "",
                        optionB = document.getString("optionB") ?: "",
                        optionC = document.getString("optionC") ?: "",
                        optionD = document.getString("optionD") ?: "",
                        question = document.getString("question") ?: "",
                    )
                }
        } catch (e: Exception) {
            Log.e("FirestoreRepository", "getNews failed with $e")
            throw e
        }


    }

    override fun addNewQuestion(question: Question ) {
        val quizCollectionRef = firebaseFirestore.collection("Quiz")

        // Agregar un nuevo documento con ID automático
        quizCollectionRef.add(question)
            .addOnSuccessListener { documentReference ->
                // Éxito al agregar el documento
                println("Documento agregado con ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                // Error al agregar el documento
                println("Error al agregar documento: $e")
            }

    }




    override fun updateScore(collection: String, documentId: String) {
        val db = FirebaseFirestore.getInstance()
        val docRef = db.collection(collection).document(documentId)

        docRef.get().addOnSuccessListener { documentSnapshot ->
            if (documentSnapshot.exists()) {
                // Obtener el valor actual de Score
                val scoreActual = documentSnapshot.getLong("score") ?: 0

                // Incrementar el valor de Score
                val nuevoScore = scoreActual + 1

                // Actualizar el documento con el nuevo valor de Score
                docRef.update("score", nuevoScore)
                    .addOnSuccessListener {
                        // Éxito al actualizar el documento
                        println("Score incrementado con éxito a $nuevoScore en la colección $collection con ID $documentId")
                    }
                    .addOnFailureListener { e ->
                        // Manejar cualquier error al actualizar el documento
                        println("Error al incrementar el Score en la colección $collection con ID $documentId: $e")
                    }
            } else {
                // Manejar el caso en que el documento no exista
                println("El documento con ID $documentId no existe en la colección $collection")
            }
        }
    }


    override fun resetScore(collection: String, documentId: String) {
        val db = FirebaseFirestore.getInstance()
        val docRef = db.collection(collection).document(documentId)

        docRef.update("score", 0)
            .addOnSuccessListener {
                // Éxito al actualizar el documento
                println("Score reset successfully to 0 in collection $collection with ID $documentId")
            }
            .addOnFailureListener { e ->
                // Manejar cualquier error al actualizar el documento
                println("Error resetting score in collection $collection with ID $documentId: $e")
            }
    }




    override suspend fun findUserType(id: String): String {
        val colecciones = listOf("Child", "Instructor", "Parents")

        for (coleccion in colecciones) {
            try {
                val snapshot = firebaseFirestore.collection(coleccion).document(id).get().await()
                if (snapshot.exists()) {

                    return coleccion
                }
            } catch (e: FirebaseFirestoreException) {
                // Manejar excepciones si es necesario
                e.printStackTrace()
            }
        }

        // Si no se encuentra en ninguna colección
        return ""
    }


    override fun getScore(coleccion: String, idDocumento: String): String {

        val docRef = firebaseFirestore.collection(coleccion).document(idDocumento)

        try {
            // Obtener el documento de manera sincrónica
            val document = Tasks.await(docRef.get())

            if (document.exists()) {
                // Obtener el valor del campo "score" como entero
                val score = document.getLong("score")
                if (score != null) {
                    return score.toString() // Convertir el score a String
                } else {
                    throw NullPointerException("El campo 'score' no existe o es nulo.")
                }
            } else {
                throw IllegalStateException("El documento no existe.")
            }
        } catch (e: ExecutionException) {
            // Manejar excepciones
            throw e.cause ?: Exception("Error desconocido al obtener el documento.")
        } catch (e: InterruptedException) {
            // Manejar excepciones
            throw Exception("Interrupción mientras se esperaba el resultado.")
        }
    }











}
