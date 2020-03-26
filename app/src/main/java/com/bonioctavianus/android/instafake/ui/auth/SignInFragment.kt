package com.bonioctavianus.android.instafake.ui.auth

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.bonioctavianus.android.instafake.R
import com.bonioctavianus.android.instafake.base.BaseFragment
import com.bonioctavianus.android.instafake.ui.navigation.Navigator
import com.bonioctavianus.android.instafake.utils.SingleLiveEvent
import dagger.android.support.AndroidSupportInjection
import io.reactivex.Observable
import kotlinx.android.synthetic.main.fragment_sign_in.*
import javax.inject.Inject

class SignInFragment : BaseFragment<SignInIntent, SignInViewState>() {

    @Inject
    lateinit var mNavigator: Navigator
    @Inject
    lateinit var mViewModel: SignInViewModel

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_sign_in, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sign_in_view.mSignInSuccessListener = {
            mNavigator.navigateToMainActivity(activity!!)
        }
    }

    override fun intents(): Observable<SignInIntent> {
        return sign_in_view.intents()
    }

    override fun bindIntent(intent: Observable<SignInIntent>) {
        mViewModel.bindIntent(intent)
    }

    override fun observeState(state: MutableLiveData<SignInViewState>) {
        state.observe(viewLifecycleOwner, Observer { value ->
            sign_in_view.render(value)
        })
    }

    override fun state(): MutableLiveData<SignInViewState> = mViewModel.state()

    override fun observeEvent(event: SingleLiveEvent<SignInViewState>?) = Unit

    override fun event(): SingleLiveEvent<SignInViewState>? = null
}
