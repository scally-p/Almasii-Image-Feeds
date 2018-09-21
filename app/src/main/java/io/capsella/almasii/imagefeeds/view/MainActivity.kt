package io.capsella.almasii.imagefeeds.view

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.graphics.Typeface
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.annotation.IdRes
import android.support.constraint.ConstraintLayout
import android.view.Gravity
import android.view.View
import android.view.ViewTreeObserver
import android.widget.TextView
import io.capsella.almasii.imagefeeds.R
import io.capsella.almasii.imagefeeds.util.HelperFunctions

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

        showProductsFoundDialog()
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

    private fun showProductsFoundDialog() {

        var view = layoutInflater.inflate(R.layout.dialog_splash, null)
        val rootLayout: ConstraintLayout = view.findViewById(R.id.root_layout)
        val titleTxt: TextView = view.findViewById(R.id.title)
        val initializingTxt: TextView = view.findViewById(R.id.initializing)

        titleTxt.typeface = proximaNovaRegular
        initializingTxt.typeface = proximaNovaRegular

        val dialog = Dialog(this, R.style.MaterialSearch)
        dialog.setContentView(view)
        dialog.setCancelable(false)
        dialog.window!!.setLayout(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.MATCH_PARENT)
        dialog.window!!.setGravity(Gravity.CENTER)
        dialog.show()

        val viewTreeObserver = rootLayout.viewTreeObserver
        if (viewTreeObserver.isAlive) {
            viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    HelperFunctions.circularRevealView(1, 0, rootLayout)
                    rootLayout.viewTreeObserver.removeGlobalOnLayoutListener(this)
                }
            })
        }
    }
}
