package com.bonioctavianus.android.instafake.ui.home

import com.bonioctavianus.android.instafake.model.Image
import com.bonioctavianus.android.instafake.service.StorageService
import com.bonioctavianus.android.instafake.service.UserService
import com.bonioctavianus.android.instafake.usecase.Result
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import javax.inject.Inject

class HomeInteractor @Inject constructor(
    private val mUserService: UserService,
    private val mStorageService: StorageService
) {

    fun compose(): ObservableTransformer<HomeIntent, HomeViewState> {
        return ObservableTransformer { intents ->
            intents.publish { intent ->
                Observable.mergeArray(
                    intent.ofType(HomeIntent.GetImages::class.java)
                        .compose(getImages),
                    intent.ofType(HomeIntent.Logout::class.java)
                        .compose(logout),
                    intent.ofType(HomeIntent.SelectCreatePost::class.java)
                        .compose(selectCreatePost)
                )
            }
        }
    }

    private val getImages =
        ObservableTransformer<HomeIntent.GetImages, HomeViewState> { intents ->
            intents.flatMap {
                mStorageService.getImages()
                    .map { result ->
                        when (result) {
                            is Result.InFlight -> HomeViewState.GetImages.InFlight
                            is Result.Success<*> -> HomeViewState.GetImages.Success(result.item as List<Image>)
                            is Result.Error -> HomeViewState.GetImages.Error(result.throwable)
                        }
                    }
            }
        }

    private val logout =
        ObservableTransformer<HomeIntent.Logout, HomeViewState> { intents ->
            intents.flatMap {
                mUserService.signOut()
                    .map { result ->
                        when (result) {
                            is Result.InFlight -> HomeViewState.Logout.InFlight
                            is Result.Success<*> -> HomeViewState.Logout.Success
                            is Result.Error -> HomeViewState.Logout.Error(result.throwable)
                        }
                    }
            }
        }

    private val selectCreatePost =
        ObservableTransformer<HomeIntent.SelectCreatePost, HomeViewState> { intents ->
            intents.flatMap {
                Observable.just(
                    HomeViewState.CreatePostSelected
                )
            }
        }
}