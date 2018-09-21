package io.capsella.almasii.imagefeeds

import android.app.Activity
import android.graphics.Typeface
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.annotation.IdRes
import android.view.View
import android.widget.TextView

class MainActivity : AppCompatActivity() {

    private lateinit var titleTxt: TextView

    private lateinit var proximaNovaBold: Typeface
    private lateinit var proximaNovaSemiBold: Typeface
    private lateinit var proximaNovaRegular: Typeface

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        proximaNovaBold = Typeface.createFromAsset(assets, "Proxima Nova Bold.ttf")
        proximaNovaSemiBold = Typeface.createFromAsset(assets, "Proxima Nova SemiBold.ttf")
        proximaNovaRegular = Typeface.createFromAsset(assets, "Proxima Nova Regular.ttf")

        initViews()
    }

    private fun <T : View> Activity.bind(@IdRes res: Int): T {
        @Suppress("UNCHECKED_CAST")
        return findViewById<T>(res)
    }

    private fun initViews(){
        titleTxt = bind(R.id.title)
        titleTxt.typeface = proximaNovaBold
    }
}
