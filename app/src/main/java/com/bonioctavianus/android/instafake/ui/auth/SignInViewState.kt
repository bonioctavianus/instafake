package com.bonioctavianus.android.instafake.ui.auth

import com.bonioctavianus.android.instafake.base.MviViewState

sealed class SignInViewState : MviViewState {
    object InFlight : SignInViewState()
    object Success : SignInViewState()
    data class Error(val throwable: Throwable) : SignInViewState()
}