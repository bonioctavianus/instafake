package com.bonioctavianus.android.instafake.di

import com.bonioctavianus.android.instafake.InstaApp
import com.bonioctavianus.android.instafake.android.AndroidImageService
import com.bonioctavianus.android.instafake.service.ImageService
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
object ImageModule {

    @JvmStatic
    @Singleton
    @Provides
    fun provideImageService(application: InstaApp): ImageService {
        return AndroidImageService(application)
    }
}