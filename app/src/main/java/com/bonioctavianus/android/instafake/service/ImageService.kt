package com.bonioctavianus.android.instafake.service

import androidx.fragment.app.Fragment

interface ImageService {

    fun requestPictureFromCamera(fragment: Fragment, requestCode: Int): String?
    fun requestPictureFromGallery(fragment: Fragment, requestCode: Int): Boolean
    fun scanMediaForImage(uri: String)
}