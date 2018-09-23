package io.capsella.almasii.imagefeeds.application

import android.app.Application
import android.content.Context
import android.util.Log
import android.widget.ImageView
import com.bumptech.glide.Glide


class MyApplication : Application() {

    private val TAG = MyApplication::class.java.simpleName

    init {
        instance = this
    }

    companion object {
        private var instance: Application? = null

        fun applicationContext(): Context {
            return instance!!.applicationContext
        }
    }

    fun setImageWithGlide(context: Context, url: String, imageView: ImageView) {

        Log.d(TAG, "Glide Image Url: $url")

        Glide.with(context)
                .load(url)
                .into(imageView)
    }
}