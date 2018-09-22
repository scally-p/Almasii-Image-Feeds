package io.capsella.almasii.imagefeeds.view

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Typeface
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.annotation.IdRes
import android.support.constraint.ConstraintLayout
import android.view.View
import android.widget.TextView
import io.capsella.almasii.imagefeeds.util.HelperFunctions
import com.agrawalsuneet.dotsloader.loaders.LazyLoader
import io.capsella.almasii.imagefeeds.R
import io.capsella.almasii.imagefeeds.dao.ImageDao
import io.capsella.almasii.imagefeeds.util.Constants


class SplashActivity : AppCompatActivity() {

    private val TAG = SplashActivity::class.java.simpleName

    private lateinit var rootLayout: ConstraintLayout
    private lateinit var titleTxt: TextView
    private lateinit var statusTxt: TextView
    private lateinit var lazyLoader: LazyLoader

    private lateinit var proximaNovaRegular: Typeface

    private lateinit var dataFetchCompletedBroadcastReceiver: DataFetchCompletedBroadcastReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        proximaNovaRegular = Typeface.createFromAsset(assets, "Proxima Nova Regular.ttf")

        dataFetchCompletedBroadcastReceiver = DataFetchCompletedBroadcastReceiver()

        initViews()
        ImageDao(this).fetchData()
    }

    private fun <T : View> Activity.bind(@IdRes res: Int): T {
        @Suppress("UNCHECKED_CAST")
        return findViewById<T>(res)
    }

    public override fun onResume() {
        super.onResume()
        registerReceiver(dataFetchCompletedBroadcastReceiver, IntentFilter(Constants.Broadcast_DATA_FETCH_COMPLETED))
    }

    public override fun onPause() {
        super.onPause()
        unregisterReceiver(dataFetchCompletedBroadcastReceiver)
    }

    private fun initViews() {
        rootLayout = bind(R.id.root_layout)
        titleTxt = bind(R.id.title)
        statusTxt = bind(R.id.status)
        lazyLoader = bind(R.id.loader)

        titleTxt.typeface = proximaNovaRegular
        statusTxt.typeface = proximaNovaRegular

        if (ImageDao(this).getImagesCount() > 0) {
            startActivity(Intent(this@SplashActivity, MainActivity::class.java))
            HelperFunctions.circularConcealView(1, 500, rootLayout, this@SplashActivity as AppCompatActivity, null)
        } else {
            if (HelperFunctions.isConnectedToInternet(this)) {
                lazyLoader.visibility = View.VISIBLE
                statusTxt.text = resources.getString(R.string.initializing)
            } else {
                lazyLoader.visibility = View.GONE
                statusTxt.text = resources.getString(R.string.no_internet_connection)
            }
        }
    }

    inner class DataFetchCompletedBroadcastReceiver : BroadcastReceiver() {

        override fun onReceive(context: Context, intent: Intent) {
            startActivity(Intent(this@SplashActivity, MainActivity::class.java))
            HelperFunctions.circularConcealView(1, 500, rootLayout, this@SplashActivity as AppCompatActivity, null)
        }
    }
}