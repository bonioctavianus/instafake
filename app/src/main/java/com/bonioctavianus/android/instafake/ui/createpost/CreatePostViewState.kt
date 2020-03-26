package com.bonioctavianus.android.instafake.ui.createpost

import com.bonioctavianus.android.instafake.base.MviViewState
import com.bonioctavianus.android.instafake.model.Image

data class CreatePostViewState(
    val saveStatus: SaveStatus,
    val imageUri: String?,
    val imageSource: Image.ImageSource,
    val imageDescription: String?,
    val pictureSourceDialogVisible: Boolean,
    val gallerySelected: Boolean,
    val cameraSelected: Boolean
) : MviViewState {

    companion object {
        fun default(): CreatePostViewState {
            return CreatePostViewState(
                saveStatus = SaveStatus.IDLE,
                imageUri = null,
                imageSource = Image.ImageSource.CAMERA,
                imageDescription = null,
                pictureSourceDialogVisible = false,
                gallerySelected = false,
                cameraSelected = false
            )
        }
    }

    enum class SaveStatus {
        IDLE,
        IN_FLIGHT,
        SUCCESS,
        ERROR
    }
}

sealed class CreatePostPartialState {
    object PictureSelected : CreatePostPartialState()
    object CameraSelected : CreatePostPartialState()
    object GallerySelected : CreatePostPartialState()
    data class PictureUriSaved(val uri: String, val source: Image.ImageSource) :
        CreatePostPartialState()

    data class DescriptionChanged(val message: String) : CreatePostPartialState()

    sealed class Upload : CreatePostPartialState() {
        object InFlight : Upload()
        object Success : Upload()
        data class Error(val throwable: Throwable) : Upload()
    }
}