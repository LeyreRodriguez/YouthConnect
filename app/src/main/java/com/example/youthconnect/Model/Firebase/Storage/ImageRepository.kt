package com.example.youthconnect.Model.Firebase.Storage
import android.net.Uri
import com.example.youthconnect.Model.Object.Instructor
import com.example.youthconnect.Model.Object.News
import com.example.youthconnect.Model.Sealed.Response
import com.google.firebase.firestore.FirebaseFirestore

typealias AddImageToStorageResponse = Response<Uri>
typealias AddImageUrlToFirestoreResponse = Response<Boolean>
typealias GetImageFromFirestoreResponse = Response<String>

