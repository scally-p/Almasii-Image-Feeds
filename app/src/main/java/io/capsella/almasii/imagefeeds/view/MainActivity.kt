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
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.widget.TextView
import io.capsella.almasii.imagefeeds.R
import io.capsella.almasii.imagefeeds.adapter.ImageAdapter
import io.capsella.almasii.imagefeeds.dao.ImageDao
import io.capsella.almasii.imagefeeds.model.Image
import io.capsella.almasii.imagefeeds.util.Constants


class MainActivity : AppCompatActivity() {

    private val TAG = MainActivity::class.java.simpleName

    private lateinit var titleTxt: TextView
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var recyclerView: RecyclerView

    private lateinit var proximaNovaBold: Typeface
    private lateinit var proximaNovaSemiBold: Typeface
    private lateinit var proximaNovaRegular: Typeface

    private lateinit var dataFetchCompletedBroadcastReceiver: DataFetchCompletedBroadcastReceiver
    private var images: MutableList<Image>? = null
    private lateinit var imageAdapter: ImageAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        proximaNovaBold = Typeface.createFromAsset(assets, "Proxima Nova Bold.ttf")
        proximaNovaSemiBold = Typeface.createFromAsset(assets, "Proxima Nova SemiBold.ttf")
        proximaNovaRegular = Typeface.createFromAsset(assets, "Proxima Nova Regular.ttf")

        dataFetchCompletedBroadcastReceiver = DataFetchCompletedBroadcastReceiver()
        images = ArrayList()

        initViews()
        displayImages()
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
        titleTxt = bind(R.id.title)
        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout)
        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimary)
        recyclerView = bind(R.id.recycler_view)

        titleTxt.typeface = proximaNovaBold

        swipeRefreshLayout.setOnRefreshListener {
            Log.d(TAG, "===============REFRESH STARTED===============")
            ImageDao(this).fetchData()
        }
    }

    private fun displayImages() {

        images = ImageDao(this).getImages()

        if (images != null) {
            imageAdapter = ImageAdapter(this, images)
            recyclerView.layoutManager = LinearLayoutManager(this)
            recyclerView.adapter = imageAdapter
        }
    }

    inner class DataFetchCompletedBroadcastReceiver : BroadcastReceiver() {

        override fun onReceive(context: Context, intent: Intent) {
            displayImages()
            swipeRefreshLayout.isRefreshing = false
        }
    }
}
