package com.bonioctavianus.android.instafake

import com.bonioctavianus.android.instafake.service.StorageService
import com.bonioctavianus.android.instafake.service.UserService
import com.bonioctavianus.android.instafake.ui.home.HomeIntent
import com.bonioctavianus.android.instafake.ui.home.HomeInteractor
import com.bonioctavianus.android.instafake.ui.home.HomeViewState
import com.bonioctavianus.android.instafake.usecase.Result
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.Observable
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class HomeInteractorTest {

    private val mUserService: UserService = mockk()
    private val mStorageService: StorageService = mockk()
    private val mInteractor = HomeInteractor(mUserService, mStorageService)

    @Test
    fun `compose() for Logout intent - should return correct state`() {
        every { mUserService.signOut() } returns
                Observable.just(
                    Result.InFlight,
                    Result.Success(Unit)
                )

        Observable.just(
            HomeIntent.Logout
        )
            .compose(mInteractor.compose())
            .test()
            .assertResult(
                HomeViewState.Logout.InFlight,
                HomeViewState.Logout.Success
            )
            .dispose()

        verify {
            mUserService.signOut()
        }
    }

    @Test
    fun `compose() for Logout intent when there is exception - should return error state`() {
        val exception = RuntimeException()

        every { mUserService.signOut() } returns
                Observable.just(
                    Result.InFlight,
                    Result.Error(exception)
                )

        Observable.just(
            HomeIntent.Logout
        )
            .compose(mInteractor.compose())
            .test()
            .assertResult(
                HomeViewState.Logout.InFlight,
                HomeViewState.Logout.Error(exception)
            )
            .dispose()

        verify {
            mUserService.signOut()
        }
    }
}