package com.example.youthconnect.Model

import android.content.ContentValues.TAG
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
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import java.util.concurrent.CompletableFuture
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

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

    fun getCurrentUserId() : String{
        var user = auth.currentUser;
        if (user != null) {
            val email = user.email

            Log.i("USER" , email?.substringBefore("@").toString())
            val numeroConvertido = email?.substringBefore("@").toString().dropLast(1) + email?.substringBefore("@").toString().takeLast(1).uppercase()
            return  numeroConvertido

        } else {
            return ""
        }

    }















}
