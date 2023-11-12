package com.example.youthconnect.ViewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import kotlinx.coroutines.launch

class LoginScreenViewModel : ViewModel() {
    private val auth: FirebaseAuth = Firebase.auth
    private val _loading = MutableLiveData(false)

    fun signIn( ID: String, password : String, home: ()-> Unit)
            = viewModelScope.launch {
        auth.signInWithEmailAndPassword(ID, password)
            .addOnCompleteListener{ task->
                if(task.isSuccessful){
                    home()
                }
            }
    }


}