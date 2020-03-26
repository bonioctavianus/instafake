package com.bonioctavianus.android.instafake.di.ui

import com.bonioctavianus.android.instafake.HomeActivity
import com.bonioctavianus.android.instafake.SplashActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityModule {

    @ContributesAndroidInjector
    abstract fun contributeSplashActivity(): SplashActivity

    @ContributesAndroidInjector
    abstract fun contributeHomeActivity(): HomeActivity
}