package com.example.libraryapp.model.firebaseAuth

import com.example.youthconnect.Model.Object.UserData

data class SignInResult(
    val data: UserData?,
    val errorMessage: String?
)

