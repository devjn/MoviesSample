package com.github.devjn.moviessample.utils

import android.widget.ImageView
import androidx.databinding.BindingAdapter


/**
 * Created by @author Jahongir on 23-Aug-18
 * devjn@jn-arts.com
 * AndroidUtils
 */
@BindingAdapter(":loadImage")
fun ImageView.loadImage(url: String) {
    GlideApp.with(this).load(url).into(this)
}