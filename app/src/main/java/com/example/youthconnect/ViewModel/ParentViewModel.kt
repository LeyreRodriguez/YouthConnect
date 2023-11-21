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
    fun getParentByParentsID(parentsID: List<String>) {
        val foundParents = mutableListOf<Parent>() // Lista para almacenar todos los padres encontrados

        viewModelScope.launch {
            for (id in parentsID) {
                firestore.collection("Parents")
                    .whereEqualTo("id", id)
                    .get()
                    .addOnSuccessListener { documents ->
                        for (document in documents) {
                            val parent = Parent(
                                FullName = document.getString("fullName") ?: "",
                                ID = document.getString("id") ?: "",
                                Password = document.getString("password") ?: "",
                                PhoneNumber = document.getString("phoneNumber") ?: ""
                            )
                            foundParents.add(parent) // Agregar el padre encontrado a la lista
                        }

                        if (foundParents.isNotEmpty()) {
                            _parentState.value = foundParents.toList() // Actualizar el estado con la lista acumulada de padres
                        } else {
                            _parentState.value = emptyList() // No se encontraron padres, actualizar con una lista vacía
                        }
                    }
                    .addOnFailureListener { exception ->
                        // Manejar errores aquí
                    }
            }
        }
    }








}

