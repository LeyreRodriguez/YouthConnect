package com.example.youthconnect.ViewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.youthconnect.Model.Users.Child
import com.example.youthconnect.Model.Users.Parent
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ParentViewModel : ViewModel(){
    private val firestore = FirebaseFirestore.getInstance()
    private val _parentState = MutableStateFlow<List<Parent>>(emptyList())
    val parentState: Flow<List<Parent>> = _parentState.asStateFlow()
    fun getParentByParentsID(parentsID : List<String>) {
       // val numeroConvertido = parentsID.dropLast(1) + parentsID.takeLast(1).uppercase()

        viewModelScope.launch {
            for (id in parentsID){
                firestore.collection("Parents")
                    .whereEqualTo("id", id)
                    .get()
                    .addOnSuccessListener { documents ->
                        var foundParent: Parent? = null
                        for (document in documents) {
                            val parent = Parent(
                                FullName = document.getString("fullName") ?: "",
                                ID = document.getString("id") ?: "",
                                Password = document.getString("password") ?: "",
                                PhoneNumber = document.getString("phoneNumber") ?: ""

                            )
                            Log.i("AJA", parent.FullName)
                            foundParent = parent
                            break // Termina el bucle después de encontrar la primera noticia
                        }

                        if (foundParent != null) {

                            _parentState.value = listOf(foundParent!!)
                        } else {
                            // No se encontró ninguna noticia con esa ID
                            // Puedes manejar el caso estableciendo el estado con un valor nulo o un indicador de ausencia
                            _parentState.value = emptyList() // Por ejemplo, aquí se establece una lista vacía
                        }
                    }
                    .addOnFailureListener { exception ->
                        Log.i("AJA", parentsID.toString())
                        // Manejar errores aquí
                    }
            }

        }

    }








}

