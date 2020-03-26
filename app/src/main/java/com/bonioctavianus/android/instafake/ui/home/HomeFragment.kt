package com.bonioctavianus.android.instafake.ui.home

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.bonioctavianus.android.instafake.R
import com.bonioctavianus.android.instafake.base.BaseFragment
import com.bonioctavianus.android.instafake.ui.navigation.Navigator
import com.bonioctavianus.android.instafake.utils.SingleLiveEvent
import dagger.android.support.AndroidSupportInjection
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.fragment_home.*
import javax.inject.Inject

class HomeFragment : BaseFragment<HomeIntent, HomeViewState>() {

    @Inject
    lateinit var mNavigator: Navigator
    @Inject
    lateinit var mViewModel: HomeViewModel

    private val mSubject: PublishSubject<HomeIntent> = PublishSubject.create()

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        home_view.mSignOutSuccessListener = {
            mNavigator.navigateToSplashActivity(activity!!)
        }

        home_view.mCreatePictureSelectedListener = {
            mNavigator.navigateToCreatePostFragment(this)
        }
    }

    override fun intents(): Observable<HomeIntent> {
        return Observable.merge(
            home_view.intents(),
            mSubject.hide()
        )
    }

    override fun bindIntent(intent: Observable<HomeIntent>) {
        mViewModel.bindIntent(intent)
    }

    override fun observeState(state: MutableLiveData<HomeViewState>) {
        state.observe(viewLifecycleOwner, Observer { value ->
            home_view.render(value)
        })
    }

    override fun state(): MutableLiveData<HomeViewState> = mViewModel.state()

    override fun observeEvent(event: SingleLiveEvent<HomeViewState>?) {
        event?.observe(viewLifecycleOwner, Observer { value ->
            home_view.renderEvent(value)
        })
    }

    override fun event(): SingleLiveEvent<HomeViewState>? = mViewModel.event()

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.fragment_home_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_menu_logout) {
            mSubject.onNext(
                HomeIntent.Logout
            )
        }
        return super.onOptionsItemSelected(item)
    }
}
