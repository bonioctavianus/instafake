package com.bonioctavianus.android.instafake.ui.createpost

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bonioctavianus.android.instafake.base.BaseViewModel
import com.bonioctavianus.android.instafake.utils.SingleLiveEvent
import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import javax.inject.Inject

class CreatePostViewModel(
    private val mInteractor: CreatePostInteractor
) : BaseViewModel<CreatePostIntent, CreatePostViewState>() {

    private val mState: MutableLiveData<CreatePostViewState> = MutableLiveData()

    override fun bindIntent(intent: Observable<CreatePostIntent>) {
        addDisposable(
            intent
                .compose(mInteractor.compose())
                .scan(CreatePostViewState.default(), mReducer)
                .subscribe(
                    { mState.postValue(it) },
                    { }
                )
        )
    }

    override fun state(): MutableLiveData<CreatePostViewState> = mState

    override fun event(): SingleLiveEvent<CreatePostViewState>? = null

    private val mReducer =
        BiFunction { previousState: CreatePostViewState, change: CreatePostPartialState ->
            when (change) {
                is CreatePostPartialState.PictureSelected -> {
                    CreatePostViewState.default()
                        .copy(
                            imageUri = previousState.imageUri,
                            imageSource = previousState.imageSource,
                            imageDescription = previousState.imageDescription,
                            pictureSourceDialogVisible = true
                        )
                }
                is CreatePostPartialState.CameraSelected -> {
                    CreatePostViewState.default()
                        .copy(
                            imageUri = previousState.imageUri,
                            imageSource = previousState.imageSource,
                            imageDescription = previousState.imageDescription,
                            cameraSelected = true
                        )
                }
                is CreatePostPartialState.GallerySelected -> {
                    CreatePostViewState.default()
                        .copy(
                            imageUri = previousState.imageUri,
                            imageSource = previousState.imageSource,
                            imageDescription = previousState.imageDescription,
                            gallerySelected = true
                        )
                }
                is CreatePostPartialState.PictureUriSaved -> {
                    CreatePostViewState.default()
                        .copy(
                            imageUri = change.uri,
                            imageSource = change.source,
                            imageDescription = previousState.imageDescription
                        )
                }
                is CreatePostPartialState.DescriptionChanged -> {
                    CreatePostViewState.default()
                        .copy(
                            imageUri = previousState.imageUri,
                            imageSource = previousState.imageSource,
                            imageDescription = change.message
                        )
                }
                is CreatePostPartialState.Upload.InFlight -> {
                    CreatePostViewState.default()
                        .copy(
                            imageUri = previousState.imageUri,
                            imageSource = previousState.imageSource,
                            imageDescription = previousState.imageDescription,
                            saveStatus = CreatePostViewState.SaveStatus.IN_FLIGHT
                        )
                }
                is CreatePostPartialState.Upload.Success -> {
                    CreatePostViewState.default()
                        .copy(
                            imageUri = previousState.imageUri,
                            imageSource = previousState.imageSource,
                            imageDescription = previousState.imageDescription,
                            saveStatus = CreatePostViewState.SaveStatus.SUCCESS
                        )
                }
                is CreatePostPartialState.Upload.Error -> {
                    CreatePostViewState.default()
                        .copy(
                            imageUri = previousState.imageUri,
                            imageSource = previousState.imageSource,
                            imageDescription = previousState.imageDescription,
                            saveStatus = CreatePostViewState.SaveStatus.ERROR
                        )
                }
                else -> previousState
            }
        }
}

class CreatePostViewModelFactory @Inject constructor(
    private val mInteractor: CreatePostInteractor
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(CreatePostInteractor::class.java)
            .newInstance(mInteractor)
    }
}