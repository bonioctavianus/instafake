package com.bonioctavianus.android.instafake.di.ui

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import com.bonioctavianus.android.instafake.ui.auth.SignInFragment
import com.bonioctavianus.android.instafake.ui.auth.SignInViewModel
import com.bonioctavianus.android.instafake.ui.auth.SignInViewModelFactory
import dagger.Module
import dagger.Provides

@Module
object SignInModule {

    @Provides
    fun provideViewModelStoreOwner(fragment: SignInFragment): ViewModelStoreOwner {
        return fragment
    }

    @Provides
    fun provideSignInViewModel(
        owner: ViewModelStoreOwner,
        factory: SignInViewModelFactory
    ): SignInViewModel {
        return ViewModelProvider(owner, factory).get(SignInViewModel::class.java)
    }
}