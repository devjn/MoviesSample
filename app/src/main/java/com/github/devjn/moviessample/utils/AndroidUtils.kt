package com.github.devjn.moviessample.utils

import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
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

@BindingAdapter("imageUrl", "textView")
fun ImageView.loadImage(url: String, textView: TextView) {
    GlideApp.with(this).load(url).placeholder(R.drawable.ic_image)
            .centerCrop().listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                    textView.visibility = View.VISIBLE
                    return false
                }

                override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                    textView.visibility = View.GONE
                    return false
                }
            }).into(this)
}

object AndroidUtils {
    val density = App.appContext.resources.displayMetrics.density

    fun dp(value: Int) = Math.ceil((density * value).toDouble()).toInt()
}