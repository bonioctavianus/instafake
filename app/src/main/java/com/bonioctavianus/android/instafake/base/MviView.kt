package com.bonioctavianus.android.instafake.base

import androidx.lifecycle.MutableLiveData
import com.bonioctavianus.android.instafake.utils.SingleLiveEvent
import io.reactivex.Observable

interface MviView<I : MviIntent, S : MviViewState> {
    fun intents(): Observable<I>
    fun bindIntent(intent: Observable<I>)
    fun observeState(state: MutableLiveData<S>)
    fun state(): MutableLiveData<S>
    fun observeEvent(event: SingleLiveEvent<S>?)
    fun event(): SingleLiveEvent<S>?
}