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

class DataBase(){

    val db = FirebaseFirestore.getInstance()
     val auth: FirebaseAuth = FirebaseAuth.getInstance()

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




}
