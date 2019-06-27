package com.souqbh.utils

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.souqbh.R


/**
 * Sets an Image to an ImageView
 * @param view the ImageView on which to set the Image
 * @param url the url to get the image and set to the ImageView
 */
@BindingAdapter("imageUrl")
fun loadImageUrl(view: ImageView, url: String) {
    Glide.with(view.context).load(url).apply(
        RequestOptions()
            .error(R.mipmap.ic_launcher)
            .placeholder(R.mipmap.ic_launcher)
            .centerCrop()
    )
        .into(view)
}

@BindingAdapter("imageUrl")
fun loadImageUrl(view: ImageView, resourceId: Int) {
    Glide.with(view.context).load(resourceId).apply(
        RequestOptions()
            .error(R.mipmap.ic_launcher)
    )
        .into(view)
}