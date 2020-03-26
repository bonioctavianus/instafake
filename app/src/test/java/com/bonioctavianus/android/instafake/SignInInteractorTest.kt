package com.bonioctavianus.android.instafake

import com.bonioctavianus.android.instafake.service.UserService
import com.bonioctavianus.android.instafake.ui.auth.SignInIntent
import com.bonioctavianus.android.instafake.ui.auth.SignInInteractor
import com.bonioctavianus.android.instafake.ui.auth.SignInViewState
import com.bonioctavianus.android.instafake.usecase.Result
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.Observable
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class SignInInteractorTest {

    private val mUserService: UserService = mockk()
    private val mInteractor = SignInInteractor(mUserService)

    @Test
    fun `compose() for SignIn intent - should return correct state`() {
        val email = "boni.oxx@gmail.com"
        val password = "123456"

        every { mUserService.signIn(email, password) } returns
                Observable.just(
                    Result.InFlight,
                    Result.Success(Unit)
                )

        Observable.just(
            SignInIntent.SignIn(email, password)
        )
            .compose(mInteractor.compose())
            .test()
            .assertResult(
                SignInViewState.InFlight,
                SignInViewState.Success
            )
            .dispose()

        verify {
            mUserService.signIn(email, password)
        }
    }

    @Test
    fun `compose() for SignIn intent when there is exception - should return error state`() {
        val email = "boni.oxx@gmail.com"
        val password = "123456"
        val exception = RuntimeException()

        every { mUserService.signIn(email, password) } returns
                Observable.just(
                    Result.InFlight,
                    Result.Error(exception)
                )

        Observable.just(
            SignInIntent.SignIn(email, password)
        )
            .compose(mInteractor.compose())
            .test()
            .assertResult(
                SignInViewState.InFlight,
                SignInViewState.Error(exception)
            )
            .dispose()

        verify {
            mUserService.signIn(email, password)
        }
    }
}