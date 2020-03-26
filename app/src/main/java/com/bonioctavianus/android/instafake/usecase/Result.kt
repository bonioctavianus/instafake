package com.bonioctavianus.android.instafake.usecase

sealed class Result {
    object InFlight : Result()
    data class Success<T>(val item: T) : Result()
    data class Error(val throwable: Throwable) : Result()
}