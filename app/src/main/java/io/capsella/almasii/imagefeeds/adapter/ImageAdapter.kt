package io.capsella.almasii.imagefeeds.adapter

import android.content.Context
import android.graphics.Typeface
import android.support.constraint.ConstraintLayout
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import io.capsella.almasii.imagefeeds.R
import io.capsella.almasii.imagefeeds.application.MyApplication
import io.capsella.almasii.imagefeeds.model.Image
import io.capsella.almasii.imagefeeds.util.HelperFunctions

class ImageAdapter(var context: Context, var itemArrayList: MutableList<Image>?) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val TAG = ImageAdapter::class.java.simpleName

    private val proximaNovaRegular = Typeface.createFromAsset(context.assets, "Proxima Nova Regular.ttf")

    class ItemViewHolder(row: View) : RecyclerView.ViewHolder(row) {
        var mainLayout: ConstraintLayout = row.findViewById(R.id.main_layout)
        var timestamp: TextView = row.findViewById(R.id.timestamp)
        var image: ImageView = row.findViewById(R.id.image)
    }

    override fun getItemCount(): Int {
        return itemArrayList!!.size
    }

    private fun getItem(position: Int): Image {
        return itemArrayList!![position]
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.layout_image_item, parent, false)
        return ItemViewHolder(v)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        var image: Image = getItem(position)
        var itemViewHolder = holder as ItemViewHolder

        MyApplication().setImageWithGlide(context, image.url, holder.image)
        itemViewHolder.timestamp.text = HelperFunctions.getFormattedDateWithDayAndTimeString(HelperFunctions.getDateFromMilliSeconds(image.createdAtMillis.toLong()), true, true)
        itemViewHolder.timestamp.typeface = proximaNovaRegular

        itemViewHolder.mainLayout.setOnClickListener {
            Log.d(TAG, "Image Name: ${image.name}")
        }
    }
}