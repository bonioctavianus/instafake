package com.bonioctavianus.android.instafake.di

import com.bonioctavianus.android.instafake.firebase.FirebaseStorageService
import com.bonioctavianus.android.instafake.service.StorageService
import com.google.firebase.storage.FirebaseStorage
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
object StorageModule {

    @JvmStatic
    @Singleton
    @Provides
    fun provideStorageService(firebaseStorage: FirebaseStorage): StorageService {
        return FirebaseStorageService(firebaseStorage)
    }
}