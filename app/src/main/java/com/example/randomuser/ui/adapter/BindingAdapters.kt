package com.example.randomuser.ui.adapter

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import coil.load
import coil.request.CachePolicy
import coil.transform.CircleCropTransformation
import com.example.randomuser.R

/**
 * A method to load a Url to a photo into an ImageView
 */
@BindingAdapter("loadImage")
fun bindDetailImage(imageView: ImageView, url: String?) {
    url?.let {
        imageView.load(it) {
            diskCachePolicy(CachePolicy.ENABLED)
            transformations(CircleCropTransformation())
            placeholder(R.drawable.ic_person_24)
            error(R.drawable.ic_broken_image_24)
        }
    }
}
