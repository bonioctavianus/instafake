package com.bonioctavianus.android.instafake.ui.auth

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bonioctavianus.android.instafake.base.BaseViewModel
import com.bonioctavianus.android.instafake.utils.SingleLiveEvent
import io.reactivex.Observable
import javax.inject.Inject

class SignInViewModel(
    private val mInteractor: SignInInteractor
) : BaseViewModel<SignInIntent, SignInViewState>() {

    private val mState: MutableLiveData<SignInViewState> = MutableLiveData()

    override fun bindIntent(intent: Observable<SignInIntent>) {
        addDisposable(
            intent
                .compose(mInteractor.compose())
                .subscribe(
                    { mState.postValue(it) },
                    { }
                )
        )
    }

    override fun state(): MutableLiveData<SignInViewState> = mState

    override fun event(): SingleLiveEvent<SignInViewState>? = null
}

class SignInViewModelFactory @Inject constructor(
    private val mInteractor: SignInInteractor
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(SignInInteractor::class.java)
            .newInstance(mInteractor)
    }
}