package com.bonioctavianus.android.instafake.ui.home

import com.bonioctavianus.android.instafake.base.MviIntent

sealed class HomeIntent : MviIntent {
    object GetImages : HomeIntent()
    object Logout : HomeIntent()
    object SelectCreatePost : HomeIntent()
}