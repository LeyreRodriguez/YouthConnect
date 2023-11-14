package com.example.youthconnect.Model

import android.util.Log
import com.example.youthconnect.Model.Users.Child
import com.example.youthconnect.Model.Users.Parent
import com.google.firebase.Firebase
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.storage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import java.util.concurrent.CompletableFuture

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

    fun addChildAccount(child: Child) {


        val documentRef: DocumentReference = db.collection("Child").document(child.ID)

        // Realiza la consulta para obtener el documento
        documentRef.get()
            .addOnSuccessListener { document ->
                if (!document.exists()) {
                    auth.createUserWithEmailAndPassword(
                        child.ID + "@youthconnect.com",
                        child.Password
                    )


                }
            }
    }

    fun getImage(): List<String> = runBlocking {
        val imageUrls = mutableListOf<String>()

        try {
            val listResult = folderReference.listAll().await()
            val deferredUrls = listResult.items.map { item ->
                async(Dispatchers.IO) {
                    try {
                        val uri = item.downloadUrl.await()
                        val imageUrl = uri.toString()
                        imageUrls.add(imageUrl)
                        println("URL de la imagen: $imageUrl")
                        imageUrl
                    } catch (e: Exception) {
                        // Manejar errores al obtener la URL de la imagen
                        println("Error al obtener la URL de la imagen: $e")
                        throw e
                    }
                }
            }

            deferredUrls.awaitAll()
        } catch (e: Exception) {
            // Manejar errores al listar las imágenes
            println("Error al recuperar imágenes: $e")
            emptyList<String>()
        }
    }




}
