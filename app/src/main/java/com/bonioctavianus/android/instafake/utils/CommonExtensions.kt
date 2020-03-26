package com.bonioctavianus.android.instafake.utils

import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide

fun Context.showToast(message: String?) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun Fragment.showToast(message: String?) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}

fun View.makeVisible() {
    visibility = View.VISIBLE
}

fun View.makeInvisible() {
    visibility = View.INVISIBLE
}

fun View.makeGone() {
    visibility = View.GONE
}

fun ImageView.loadImage(uri: String?, @DrawableRes placeholder: Int) {
    uri ?: return
    Glide.with(context)
        .load(uri)
        .fitCenter()
        .placeholder(placeholder)
        .into(this)
}