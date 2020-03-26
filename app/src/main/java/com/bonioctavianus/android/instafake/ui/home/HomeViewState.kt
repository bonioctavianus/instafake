package com.bonioctavianus.android.instafake.ui.home

import com.bonioctavianus.android.instafake.base.MviViewState
import com.bonioctavianus.android.instafake.model.Image

sealed class HomeViewState : MviViewState {

    sealed class GetImages : HomeViewState() {
        object InFlight : GetImages()
        data class Success(val images: List<Image>) : GetImages()
        data class Error(val throwable: Throwable) : GetImages()
    }

    sealed class Logout : HomeViewState() {
        object InFlight : Logout()
        object Success : Logout()
        data class Error(val throwable: Throwable) : Logout()
    }

    object CreatePostSelected : HomeViewState()
}