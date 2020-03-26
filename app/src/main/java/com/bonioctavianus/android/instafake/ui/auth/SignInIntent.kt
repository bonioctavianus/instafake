package com.bonioctavianus.android.instafake.ui.auth

import com.bonioctavianus.android.instafake.base.MviIntent

sealed class SignInIntent : MviIntent {
    data class SignIn(val email: String, val password: String) : SignInIntent()
}