package com.example.youthconnect.Model.Sealed

import android.net.Uri

sealed class Response<out T>{
    object Loading: Response<Nothing>()
    data class Success<out T>(
        val data: T?

    ): Response<T>()
    data class Failure(
        val e:Exception
    ): Response<Nothing>()
}
sealed class AddImageToStorageResponse {
    data class Success(val downloadUrl: Uri) : AddImageToStorageResponse()
    data class Failure(val exception: Exception) : AddImageToStorageResponse()
}