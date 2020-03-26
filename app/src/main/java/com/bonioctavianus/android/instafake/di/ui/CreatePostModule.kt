package com.bonioctavianus.android.instafake.di.ui

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import com.bonioctavianus.android.instafake.ui.createpost.CreatePostFragment
import com.bonioctavianus.android.instafake.ui.createpost.CreatePostViewModel
import com.bonioctavianus.android.instafake.ui.createpost.CreatePostViewModelFactory
import dagger.Module
import dagger.Provides

@Module
object CreatePostModule {

    @Provides
    fun provideViewModelStoreOwner(fragment: CreatePostFragment): ViewModelStoreOwner {
        return fragment
    }

    @Provides
    fun provideCreatePostViewModel(
        owner: ViewModelStoreOwner,
        factory: CreatePostViewModelFactory
    ): CreatePostViewModel {
        return ViewModelProvider(owner, factory).get(CreatePostViewModel::class.java)
    }
}