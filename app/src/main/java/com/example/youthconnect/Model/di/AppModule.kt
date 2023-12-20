package com.example.youthconnect.Model.di

import com.example.youthconnect.Model.Firebase.Firestore.FirestoreRepository
import com.example.youthconnect.Model.Firebase.Firestore.FirestoreRepositoryImpl
import com.example.youthconnect.Model.Firebase.Storage.ImageRepository
import com.example.youthconnect.Model.Firebase.Storage.ImageRepositoryImpl
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.storage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
class AppModule {

    @Provides
    fun provideFirebaseFirestore(): FirebaseFirestore = FirebaseFirestore.getInstance()

    @Provides
    fun provideFirestoreRepository(impl: FirestoreRepositoryImpl): FirestoreRepository = impl

    @Provides
    fun provideFirebaseStorage() = Firebase.storage

    @Provides
    fun providesImageRepository(storage: FirebaseStorage, db : FirebaseFirestore): ImageRepository = ImageRepositoryImpl(storage = storage, db = db)

}