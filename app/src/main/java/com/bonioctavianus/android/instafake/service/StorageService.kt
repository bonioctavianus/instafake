package com.bonioctavianus.android.instafake.service

import com.bonioctavianus.android.instafake.model.Image
import com.bonioctavianus.android.instafake.usecase.Result
import io.reactivex.Observable

interface StorageService {

    fun getImages(): Observable<Result>
    fun uploadImage(image: Image): Observable<Result>
}