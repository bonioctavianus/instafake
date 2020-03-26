package com.bonioctavianus.android.instafake.di

import com.bonioctavianus.android.instafake.firebase.FirebaseUserService
import com.bonioctavianus.android.instafake.service.UserService
import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
object UserModule {

    @JvmStatic
    @Singleton
    @Provides
    fun provideUserService(firebaseAuth: FirebaseAuth): UserService {
        return FirebaseUserService(firebaseAuth)
    }
}