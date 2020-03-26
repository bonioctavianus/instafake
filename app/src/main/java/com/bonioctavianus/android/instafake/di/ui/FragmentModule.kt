package com.bonioctavianus.android.instafake.di.ui

import com.bonioctavianus.android.instafake.ui.auth.SignInFragment
import com.bonioctavianus.android.instafake.ui.createpost.CreatePostFragment
import com.bonioctavianus.android.instafake.ui.home.HomeFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentModule {

    @ContributesAndroidInjector(modules = [SignInModule::class])
    abstract fun contributeSignInFragment(): SignInFragment

    @ContributesAndroidInjector(modules = [HomeModule::class])
    abstract fun contributeHomeFragment(): HomeFragment

    @ContributesAndroidInjector(modules = [CreatePostModule::class])
    abstract fun contributeCreatePostFragment(): CreatePostFragment
}