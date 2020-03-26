package com.bonioctavianus.android.instafake.service

import com.bonioctavianus.android.instafake.model.User
import com.bonioctavianus.android.instafake.usecase.Result
import io.reactivex.Observable

interface UserService {

    fun isSignedIn(): Observable<Boolean>
    fun getUser(): Observable<User>
    fun signIn(email: String, password: String): Observable<Result>
    fun signOut(): Observable<Result>
}