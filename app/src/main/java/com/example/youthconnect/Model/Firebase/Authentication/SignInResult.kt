package com.example.libraryapp.model.firebaseAuth

import com.example.youthconnect.Model.Object.UserData
import com.example.youthconnect.Model.Sealed.AuthError

data class SignInResult(
    val data: UserData?,
    val error: AuthError? = null
)

