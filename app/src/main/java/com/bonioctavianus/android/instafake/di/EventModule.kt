package com.bonioctavianus.android.instafake.di

import dagger.Module
import dagger.Provides
import org.greenrobot.eventbus.EventBus
import javax.inject.Singleton

@Module
object EventModule {

    @JvmStatic
    @Singleton
    @Provides
    fun provideEventBus(): EventBus {
        return EventBus.getDefault()
    }
}