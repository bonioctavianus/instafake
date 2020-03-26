package com.bonioctavianus.android.instafake.ui.home

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.bonioctavianus.android.instafake.R
import com.bonioctavianus.android.instafake.model.Image
import com.bonioctavianus.android.instafake.utils.makeInvisible
import com.bonioctavianus.android.instafake.utils.makeVisible
import com.bonioctavianus.android.instafake.utils.showToast
import com.jakewharton.rxbinding3.view.clicks
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.widget_home.view.*

class HomeView(context: Context, attributeSet: AttributeSet) : FrameLayout(context, attributeSet) {

    var mSignOutSuccessListener: (() -> Unit)? = null
    var mCreatePictureSelectedListener: (() -> Unit)? = null

    private val mSubject: PublishSubject<HomeIntent> = PublishSubject.create()
    private val mAdapter: HomeAdapter = HomeAdapter()

    init {
        View.inflate(context, R.layout.widget_home, this)
        list_image.layoutManager = LinearLayoutManager(context)
        list_image.adapter = mAdapter
    }

    fun render(state: HomeViewState) {
        when (state) {
            is HomeViewState.Logout.Success -> renderLogoutSuccess()
            is HomeViewState.Logout.Error -> renderLogoutError(state.throwable)
            is HomeViewState.GetImages.InFlight -> renderGetImagesInFlight()
            is HomeViewState.GetImages.Success -> renderGetImagesSuccess(state.images)
            is HomeViewState.GetImages.Error -> renderGetImagesError(state.throwable)
        }
    }

    fun renderEvent(state: HomeViewState) {
        when (state) {
            is HomeViewState.CreatePostSelected -> renderCreatePictureSelected()
        }
    }

    private fun renderLogoutSuccess() {
        mSignOutSuccessListener?.invoke()
    }

    private fun renderLogoutError(throwable: Throwable) {
        context.showToast(throwable.message)
    }

    private fun renderGetImagesInFlight() {
        progress_bar.makeVisible()
    }

    private fun renderGetImagesSuccess(items: List<Image>) {
        progress_bar.makeInvisible()
        mAdapter.submitList(items)
    }

    private fun renderGetImagesError(throwable: Throwable) {
        progress_bar.makeInvisible()
        context.showToast(throwable.message)
    }

    private fun renderCreatePictureSelected() {
        mCreatePictureSelectedListener?.invoke()
    }

    fun intents(): Observable<HomeIntent> {
        return Observable.merge(
            Observable.just(
                HomeIntent.GetImages
            ),
            getCreatePictureIntent(),
            mSubject.hide()
        )
    }

    private fun getCreatePictureIntent(): Observable<HomeIntent> {
        return button_create_picture.clicks()
            .map { HomeIntent.SelectCreatePost }
    }
}
