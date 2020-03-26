package com.bonioctavianus.android.instafake.base

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment

abstract class BaseFragment<I : MviIntent, S : MviViewState> : Fragment(), MviView<I, S> {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindIntent(intents())
        observeState(state())
        observeEvent(event())
    }
}
