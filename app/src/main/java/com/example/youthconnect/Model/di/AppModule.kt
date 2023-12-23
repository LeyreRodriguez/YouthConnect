package com.example.youthconnect.Model.di


import com.example.youthconnect.Model.Firebase.Firestore.FirestoreRepository
import com.example.youthconnect.Model.Firebase.Firestore.FirestoreRepositoryImpl
import com.example.youthconnect.Model.Firebase.Storage.FirebaseStorageImpl
import com.example.youthconnect.Model.Firebase.Storage.FirebaseStorageRepository
import com.example.youthconnect.Model.Object.UserData
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.storage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class AppModule {

    @Provides
    @Singleton
    fun provideFirebaseFirestore(): FirebaseFirestore = FirebaseFirestore.getInstance()

    @Provides
    @Singleton
    fun provideFirestoreRepository(impl: FirestoreRepositoryImpl): FirestoreRepository = impl

    @Provides
    @Singleton
    fun provideFirebaseStorage() = Firebase.storage


    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()
/*
    @Provides
    @Singleton
    fun providesImageRepository(storage: FirebaseStorage, db : FirebaseFirestore): ImageRepository = ImageRepositoryImpl(storage = storage, db = db)
*/
    @Provides
    @Singleton
    fun provideFirebaseStorageRepository(
        firebaseAuth: FirebaseAuth,
        storage: FirebaseStorage,
        db: FirebaseFirestore
    ): FirebaseStorageRepository {
        return FirebaseStorageImpl(firebaseAuth, storage, db)
    }

    @Provides
    @Singleton
    fun provideUserData(): UserData {
        // Obtiene los datos del usuario de alguna fuente
        return UserData("userId", "userName", "userEmail")
    }
}