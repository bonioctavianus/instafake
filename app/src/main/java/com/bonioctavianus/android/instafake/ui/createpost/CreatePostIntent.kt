package com.bonioctavianus.android.instafake.ui.createpost

import com.bonioctavianus.android.instafake.base.MviIntent
import com.bonioctavianus.android.instafake.model.Image

sealed class CreatePostIntent : MviIntent {
    object SelectPicture : CreatePostIntent()
    object SelectCamera : CreatePostIntent()
    object SelectGallery : CreatePostIntent()
    data class SavePictureUri(val uri: String, val source: Image.ImageSource) : CreatePostIntent()
    data class ChangeDescription(val message: String) : CreatePostIntent()
    data class Upload(val uri: String?, val description: String?, val source: Image.ImageSource?) : CreatePostIntent()
}