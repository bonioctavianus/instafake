package com.bonioctavianus.android.instafake.base

import androidx.lifecycle.MutableLiveData
import com.bonioctavianus.android.instafake.utils.SingleLiveEvent
import io.reactivex.Observable

interface MviViewModel<I : MviIntent, S : MviViewState> {
    fun bindIntent(intent: Observable<I>)
    fun state(): MutableLiveData<S>
    fun event(): SingleLiveEvent<S>?
}