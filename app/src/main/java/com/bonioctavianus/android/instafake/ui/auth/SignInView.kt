package com.bonioctavianus.android.instafake.ui.auth

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import com.bonioctavianus.android.instafake.R
import com.bonioctavianus.android.instafake.utils.makeGone
import com.bonioctavianus.android.instafake.utils.makeInvisible
import com.bonioctavianus.android.instafake.utils.makeVisible
import com.bonioctavianus.android.instafake.utils.showToast
import com.jakewharton.rxbinding3.view.clicks
import io.reactivex.Observable
import kotlinx.android.synthetic.main.widget_sign_in.view.*

class SignInView(context: Context, attributeSet: AttributeSet) :
    FrameLayout(context, attributeSet) {

    var mSignInSuccessListener: (() -> Unit)? = null

    init {
        View.inflate(context, R.layout.widget_sign_in, this)
    }

    fun render(state: SignInViewState) {
        when (state) {
            is SignInViewState.InFlight -> renderInFlight()
            is SignInViewState.Success -> renderSuccess()
            is SignInViewState.Error -> renderError(state.throwable)
        }
    }

    private fun renderInFlight() {
        button_sign_in.makeInvisible()
        progress_bar.makeVisible()
    }

    private fun renderSuccess() {
        mSignInSuccessListener?.invoke()
    }

    private fun renderError(throwable: Throwable) {
        button_sign_in.makeVisible()
        progress_bar.makeInvisible()
        context.showToast(throwable.message)
    }

    fun intents(): Observable<SignInIntent> {
        return button_sign_in.clicks()
            .map {
                SignInIntent.SignIn(
                    email = input_email.text.toString(),
                    password = input_password.text.toString()
                )
            }
    }
}
