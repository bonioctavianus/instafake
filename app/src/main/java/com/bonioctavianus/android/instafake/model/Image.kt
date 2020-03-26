package com.bonioctavianus.android.instafake.model

data class Image(
    val userId: String,
    val userEmail: String,
    val description: String,
    val uri: String,
    val source: ImageSource,
    val creationTimeInMillis: Long = 0
) {

    enum class ImageSource {
        CAMERA,
        GALLERY,
        CLOUD
    }
}