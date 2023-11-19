package com.example.youthconnect.ViewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.youthconnect.Model.DataBase
import com.example.youthconnect.Model.News
import com.example.youthconnect.Model.Users.Child
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class ChildViewModel : ViewModel() {
    private val firestore = FirebaseFirestore.getInstance()
    private val _childState = MutableStateFlow<List<Child>>(emptyList())
    val childState: Flow<List<Child>> = _childState.asStateFlow()

    fun getCurrentUserById(childId : String ){

        Log.i("ID", childId)
        viewModelScope.launch {
            firestore.collection("Child")
                .whereEqualTo("id", childId)
                .get()
                .addOnSuccessListener { documents ->
                    var foundChild: Child? = null
                    for (document in documents) {


                        val childObject = Child(
                            FullName = document.getString("fullName") ?: "",
                            ID = document.getString("id") ?: "",
                            Course = document.getString("course") ?: "",
                            Password = document.getString("password") ?: "",
                            BelongsToSchool = document.getBoolean("belongsToSchool") ?: false,
                            FaithGroups = document.getBoolean("faithGroups") ?: false,
                            GoOutAlone = document.getBoolean("goOutAlone") ?:false,
                            Observations = document.getString("observations") ?: "",
                            ParentID = document.get("parentID") as? List<String> ?: emptyList(),
                            InstructorID = document.getString("instructorID") ?: ""

                        )

                        foundChild = childObject
                        break // Termina el bucle después de encontrar la primera noticia
                    }

                    if (foundChild != null) {
                        _childState.value = listOf(foundChild!!)
                    } else {
                        // No se encontró ninguna noticia con esa ID
                        // Puedes manejar el caso estableciendo el estado con un valor nulo o un indicador de ausencia
                        _childState.value = emptyList() // Por ejemplo, aquí se establece una lista vacía
                    }
                }
                .addOnFailureListener { exception ->
                    // Manejar errores aquí
                }
        }

        }






    }

