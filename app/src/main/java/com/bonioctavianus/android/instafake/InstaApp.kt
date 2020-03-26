package com.bonioctavianus.android.instafake

import android.app.Application
import com.bonioctavianus.android.instafake.di.DaggerInstaComponent
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import timber.log.Timber
import javax.inject.Inject

class InstaApp : Application(), HasAndroidInjector {

    @Inject
    lateinit var mAndroidInjector: DispatchingAndroidInjector<Any>

    override fun onCreate() {
        super.onCreate()
        initTimber()
        initDagger()
    }

    private fun initTimber() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

    private fun initDagger() {
        DaggerInstaComponent.factory().create(this).inject(this)
    }

    override fun androidInjector(): AndroidInjector<Any> = mAndroidInjector
}