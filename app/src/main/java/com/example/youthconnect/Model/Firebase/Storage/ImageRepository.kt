package com.example.youthconnect.Model.Firebase.Storage
import android.net.Uri
import com.example.youthconnect.Model.Sealed.Response


typealias AddImageToStorageResponse = Response<Uri>
typealias AddImageUrlToFirestoreResponse = Response<Boolean>
typealias GetImageFromFirestoreResponse = Response<String>

