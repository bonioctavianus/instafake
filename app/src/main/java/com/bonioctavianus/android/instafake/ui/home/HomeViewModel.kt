package com.bonioctavianus.android.instafake.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bonioctavianus.android.instafake.base.BaseViewModel
import com.bonioctavianus.android.instafake.utils.SingleLiveEvent
import io.reactivex.Observable
import javax.inject.Inject

class HomeViewModel(
    private val mInteractor: HomeInteractor
) : BaseViewModel<HomeIntent, HomeViewState>() {

    private val mState: MutableLiveData<HomeViewState> = MutableLiveData()
    private val mEvent: SingleLiveEvent<HomeViewState> = SingleLiveEvent()

    override fun bindIntent(intent: Observable<HomeIntent>) {
        addDisposable(
            intent
                .compose(mInteractor.compose())
                .subscribe(
                    { state ->
                        if (state is HomeViewState.CreatePostSelected) {
                            mEvent.postValue(state)
                        } else {
                            mState.postValue(state)
                        }
                    },
                    { }
                )
        )
    }

    override fun state(): MutableLiveData<HomeViewState> = mState

    override fun event(): SingleLiveEvent<HomeViewState>? = mEvent
}

class HomeViewModelFactory @Inject constructor(
    private val mInteractor: HomeInteractor
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(HomeInteractor::class.java)
            .newInstance(mInteractor)
    }
}