package com.bonioctavianus.android.instafake.di

import com.bonioctavianus.android.instafake.InstaApp
import com.bonioctavianus.android.instafake.di.ui.ActivityModule
import com.bonioctavianus.android.instafake.di.ui.FragmentModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AndroidInjectionModule::class,
        FirebaseModule::class,
        ActivityModule::class,
        FragmentModule::class,
        UserModule::class,
        StorageModule::class,
        ImageModule::class,
        EventModule::class
    ]
)
interface InstaComponent {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance application: InstaApp): InstaComponent
    }

    fun inject(application: InstaApp)
}
