package com.bonioctavianus.android.instafake.ui.auth

import com.bonioctavianus.android.instafake.service.UserService
import com.bonioctavianus.android.instafake.usecase.Result
import io.reactivex.ObservableTransformer
import javax.inject.Inject

class SignInInteractor @Inject constructor(
    private val mUserService: UserService
) {

    fun compose(): ObservableTransformer<SignInIntent, SignInViewState> {
        return ObservableTransformer { intents ->
            intents.publish { intent ->
                intent.ofType(SignInIntent.SignIn::class.java)
                    .compose(signIn)
            }
        }
    }

    private val signIn =
        ObservableTransformer<SignInIntent.SignIn, SignInViewState> { intents ->
            intents.flatMap { intent ->
                mUserService.signIn(intent.email, intent.password)
                    .map { result ->
                        when (result) {
                            is Result.InFlight -> SignInViewState.InFlight
                            is Result.Success<*> -> SignInViewState.Success
                            is Result.Error -> SignInViewState.Error(result.throwable)
                        }
                    }
            }
        }
}