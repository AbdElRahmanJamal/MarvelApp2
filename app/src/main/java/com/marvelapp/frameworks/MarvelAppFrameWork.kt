package com.marvelapp.frameworks

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.marvelapp.R

fun downloadImage(context: Context, url: String, imageView: ImageView) {
    GlideApp.with(context)
        .load(url)
        .centerCrop()
        .placeholder(R.drawable.ic_the_avengers)
        .error(R.drawable.ic_the_avengers)
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .into(imageView)

}