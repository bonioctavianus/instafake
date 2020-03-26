package com.bonioctavianus.android.instafake.ui.createpost

import android.content.Context
import android.util.AttributeSet
import android.view.MenuItem
import android.view.View
import android.widget.FrameLayout
import com.bonioctavianus.android.instafake.R
import com.bonioctavianus.android.instafake.model.Image
import com.bonioctavianus.android.instafake.utils.loadImage
import com.bonioctavianus.android.instafake.utils.makeInvisible
import com.bonioctavianus.android.instafake.utils.makeVisible
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.jakewharton.rxbinding3.view.clicks
import com.jakewharton.rxbinding3.widget.afterTextChangeEvents
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.widget_create_post.view.*
import java.util.concurrent.TimeUnit

class CreatePostView(context: Context, attributeSet: AttributeSet) :
    FrameLayout(context, attributeSet) {

    var mCameraSelectedListener: (() -> Unit)? = null
    var mGallerySelectedListener: (() -> Unit)? = null
    var mSaveSuccessListener: (() -> Unit)? = null
    var mMenuItemSend: MenuItem? = null
    var mCurrentState: CreatePostViewState? = null

    private val mSubject: PublishSubject<CreatePostIntent> = PublishSubject.create()

    init {
        View.inflate(context, R.layout.widget_create_post, this)
    }

    fun render(state: CreatePostViewState) {
        renderData(state.imageUri)

        when {
            state.pictureSourceDialogVisible -> renderPictureSourceDialog()
            state.cameraSelected -> mCameraSelectedListener?.invoke()
            state.gallerySelected -> mGallerySelectedListener?.invoke()
        }

        when (state.saveStatus) {
            CreatePostViewState.SaveStatus.IDLE -> renderSaveIdle()
            CreatePostViewState.SaveStatus.IN_FLIGHT -> renderSaveInFlight()
            CreatePostViewState.SaveStatus.SUCCESS -> renderSaveSuccess()
            CreatePostViewState.SaveStatus.ERROR -> renderSaveFailure()
        }
        mCurrentState = state
    }

    private fun renderData(pictureUri: String?) {
        image_picture.loadImage(pictureUri, R.drawable.ic_add_black)
    }

    private fun renderPictureSourceDialog() {
        MaterialAlertDialogBuilder(context)
            .setTitle(R.string.home_select_picture_source_dialog_title)
            .setPositiveButton(R.string.home_select_picture_source_dialog_camera_option) { _, _ ->
                mSubject.onNext(
                    CreatePostIntent.SelectCamera
                )
            }
            .setNegativeButton(R.string.home_select_picture_source_dialog_gallery_option) { _, _ ->
                mSubject.onNext(
                    CreatePostIntent.SelectGallery
                )
            }
            .show()
    }

    private fun renderSaveIdle() {
        progress_bar.makeInvisible()
        mMenuItemSend?.isEnabled = true
        mMenuItemSend?.isVisible = true
    }

    private fun renderSaveInFlight() {
        progress_bar.makeVisible()
        mMenuItemSend?.isEnabled = false
        mMenuItemSend?.isVisible = false
    }

    private fun renderSaveSuccess() {
        mSaveSuccessListener?.invoke()
    }

    private fun renderSaveFailure() {
        progress_bar.makeInvisible()
        mMenuItemSend?.isEnabled = true
        mMenuItemSend?.isVisible = true
    }

    fun intents(): Observable<CreatePostIntent> {
        return Observable.merge(
            getPictureIntent(),
            getInputMessageIntent(),
            mSubject.hide()
        )
    }

    private fun getPictureIntent(): Observable<CreatePostIntent> {
        return image_picture.clicks()
            .map { CreatePostIntent.SelectPicture }
    }

    private fun getInputMessageIntent(): Observable<CreatePostIntent> {
        return input_message.afterTextChangeEvents()
            .skip(2)
            .debounce(250, TimeUnit.MILLISECONDS)
            .map { event ->
                val text = event.editable.toString()
                CreatePostIntent.ChangeDescription(text)
            }
            .cast(CreatePostIntent::class.java)
            .distinctUntilChanged()
    }

    fun getImageUri(): String? {
        return mCurrentState?.imageUri
    }

    fun getImageDescription(): String? {
        return mCurrentState?.imageDescription
    }

    fun getImageSource(): Image.ImageSource? {
        return mCurrentState?.imageSource
    }
}
