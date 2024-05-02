package com.example.youthconnect.Model.Sealed

sealed class AuthError {
    object ConnectionError : AuthError()
    object InvalidCredentials : AuthError()
    object Timeout : AuthError()
}