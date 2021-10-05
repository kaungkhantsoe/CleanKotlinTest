package com.kks.cleankotlintest.extensions

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.snackbar.Snackbar
import com.kks.cleankotlintest.R


fun Context.showImage(
    imageView: ImageView,
    imageUrl: Any,
    errorHandlerView: Int? = null,
    placeHolder: Int? = null
) {

    Glide.with(this)
        .setDefaultRequestOptions(
            RequestOptions
                .placeholderOf(placeHolder ?: R.drawable.placeholder)
                .error(errorHandlerView ?: R.drawable.placeholder)
                .centerCrop()
        )
        .load(imageUrl)
        .into(imageView)

}

fun Context.showToast(string: CharSequence, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, string, duration).show()
}

fun showSnackBar(view: View, string: CharSequence, duration: Int = Snackbar.LENGTH_SHORT) {
    Snackbar.make(
        view,
        string,
        duration
    ).show()
}

fun Context.showRetrySnackBar(
    view: View,
    string: CharSequence,
    listener: SnackBarOnRetryListener,
    duration: Int = Snackbar.LENGTH_INDEFINITE
) {

    Snackbar.make(
        view,
        string,
        duration
    )
        .setAction(this.getString(R.string.retry)) {
            listener.onRetry()
        }.show()
}

interface SnackBarOnRetryListener {
    fun onRetry()
}