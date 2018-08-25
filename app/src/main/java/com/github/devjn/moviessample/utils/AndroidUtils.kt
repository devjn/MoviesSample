package com.github.devjn.moviessample.utils

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.github.devjn.moviessample.App
import com.github.devjn.moviessample.R


/**
 * Created by @author Jahongir on 23-Aug-18
 * devjn@jn-arts.com
 * AndroidUtils
 */
@BindingAdapter("loadImage")
fun ImageView.loadImage(url: String) {
    GlideApp.with(this).load(url).placeholder(R.drawable.ic_image)
            .centerCrop().into(this)
}

object AndroidUtils {
    val density = App.appContext.resources.displayMetrics.density

    fun dp(value: Int) = Math.ceil((density * value).toDouble()).toInt()
}