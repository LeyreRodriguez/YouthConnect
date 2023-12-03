package com.example.youthconnect.Model

import android.graphics.BitmapFactory
import android.util.Log
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import com.example.youthconnect.Model.Object.Child
import com.example.youthconnect.Model.Object.Instructor
import com.example.youthconnect.Model.Object.Parent
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.storage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.io.IOException

class DataBase(){

    val db = FirebaseFirestore.getInstance()
     val auth: FirebaseAuth = FirebaseAuth.getInstance()

    val storage = Firebase.storage
    val storageReference = storage.reference

    // Especifica la carpeta en Firebase Storage
    val folderReference = storageReference.child("newsImage")




    fun addParents(parent: Parent){

        db.collection("Parents")
            .document(parent.ID)
            .set(parent)

    }




    fun addChild(child: Child){

        val documentRef: DocumentReference = db.collection("Child").document(child.ID)

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

                    db.collection("Child")
                        .document(child.ID)
                        .set(child)

                }
            }




    }

    fun addInstructor(instructor: Instructor){

        db.collection("Instructor")
            .document(instructor.ID)
            .set(instructor)

    }


    fun addParentAccount(parent: Parent){
        auth.createUserWithEmailAndPassword(parent.ID + "@youthconnect.com", parent.Password)
            .addOnCompleteListener {task ->
                if (task.isSuccessful){
                    Log.i("YAY", "Usuario creado exitosamente")
                }else{
                    Log.i("NON", "Error al crear el usuario: ${task.exception?.message}")
                }
            }


    }

    fun addChildAccount(child: Child) {

        auth.createUserWithEmailAndPassword(child.ID + "@youthconnect.com", child.Password)
            .addOnCompleteListener {task ->
                if (task.isSuccessful){
                    Log.i("YAY", "Usuario creado exitosamente")
                }else{
                    Log.i("NON", "Error al crear el usuario: ${task.exception?.message}")
                }
            }

    }

    fun addInstructorAccount(instructor: Instructor) {

        auth.createUserWithEmailAndPassword(instructor.ID + "@youthconnect.com", instructor.Password)
            .addOnCompleteListener {task ->
                if (task.isSuccessful){
                    Log.i("YAY", "Usuario creado exitosamente")
                }else{
                    Log.i("NON", "Error al crear el usuario: ${task.exception?.message}")
                }
            }
    }
    suspend fun buscarDocumento(userId: String): String {
        return withContext(Dispatchers.IO) {
            try {
                val childDocRef = db.collection("Child").document(userId).get().await()
                if (childDocRef.exists()) {
                    return@withContext "2"
                }

                val parentDocRef = db.collection("Parents").document(userId).get().await()
                if (parentDocRef.exists()) {
                    return@withContext "1"
                }

                val instructorDocRef = db.collection("Instructor").document(userId).get().await()
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

    fun getCurrentUserId() : String{
        var user = auth.currentUser;
        if (user != null) {
            val email = user.email

            val numeroConvertido = email?.substringBefore("@").toString().dropLast(1) + email?.substringBefore("@").toString().takeLast(1).uppercase()
            return  numeroConvertido

        } else {
            return ""
        }

    }


    suspend fun getImageFromFirebaseStorage(imagePath: String): ImageBitmap? {
        return try {
            val storage = Firebase.storage
            val storageRef = storage.reference.child(imagePath)

            val maxDownloadSizeBytes: Long = 1024 * 1024 // Tamaño máximo de descarga (1 MB en este ejemplo)
            val imageBytes = storageRef.getBytes(maxDownloadSizeBytes).await()

            val bitmap = imageBytes.inputStream().use {
                BitmapFactory.decodeStream(it)
            }
            bitmap.asImageBitmap()
        } catch (e: IOException) {
            Log.e("FirebaseStorage", "Error al descargar la imagen: ${e.message}")
            null
        }
    }


}
