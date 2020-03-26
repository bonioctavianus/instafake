package com.bonioctavianus.android.instafake

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bonioctavianus.android.instafake.service.UserService
import com.bonioctavianus.android.instafake.ui.navigation.Navigator
import dagger.android.AndroidInjection
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class SplashActivity : AppCompatActivity() {

    @Inject
    lateinit var mUserService: UserService
    @Inject
    lateinit var mNavigator: Navigator

    private val mDisposables = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        init()
    }

    private fun init() {
        mDisposables.add(mUserService.isSignedIn()
            .subscribe(
                { result ->
                    if (result) {
                        navigateToMainActivity()
                    } else {
                        navigateToAuthActivity()
                    }
                },
                { }
            )
        )
    }

    private fun navigateToAuthActivity() {
        mNavigator.navigateToAuthActivity(this)
    }

    private fun navigateToMainActivity() {
        mNavigator.navigateToMainActivity(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        mDisposables.dispose()
    }
}
