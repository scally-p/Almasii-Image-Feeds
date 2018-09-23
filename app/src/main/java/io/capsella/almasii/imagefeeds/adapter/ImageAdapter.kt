package io.capsella.almasii.imagefeeds.adapter

import android.app.Dialog
import android.content.Context
import android.graphics.Typeface
import android.support.constraint.ConstraintLayout
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import io.capsella.almasii.imagefeeds.R
import io.capsella.almasii.imagefeeds.application.MyApplication
import io.capsella.almasii.imagefeeds.model.Image
import io.capsella.almasii.imagefeeds.util.HelperFunctions

class ImageAdapter(var context: Context, var itemArrayList: MutableList<Image>?) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val TAG = ImageAdapter::class.java.simpleName

    private val proximaNovaSemiBold = Typeface.createFromAsset(context.assets, "Proxima Nova SemiBold.ttf")

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

        val params: ConstraintLayout.LayoutParams = ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, HelperFunctions.getDisplayMetrics(context).heightPixels / 3)
        itemViewHolder.image.layoutParams = params

        MyApplication().setImageWithGlide(context, image.url, holder.image)
        itemViewHolder.timestamp.text = HelperFunctions.getFormattedDateWithDayAndTimeString(HelperFunctions.getDateFromMilliSeconds(image.createdAtMillis.toLong()), true, true)
        itemViewHolder.timestamp.typeface = proximaNovaSemiBold

        itemViewHolder.mainLayout.setOnClickListener {
            Log.d(TAG, "Image Name: ${image.name}")
            showMoreOptionsDialog(image)
        }
    }

    private fun showMoreOptionsDialog(image: Image) {

        var view = LayoutInflater.from(context).inflate(R.layout.dialog_image_preview, null)
        val rootLayout: ConstraintLayout = view.findViewById(R.id.root_layout)
        val close: ImageView = view.findViewById(R.id.close)
        val imagePreview: ImageView = view.findViewById(R.id.image)

        MyApplication().setImageWithGlide(context, image.url, imagePreview)

        val dialog = Dialog(context, R.style.MaterialSearch)
        dialog.setContentView(view)
        dialog.setCancelable(true)
        dialog.window!!.setLayout(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.MATCH_PARENT)
        dialog.window!!.setGravity(Gravity.CENTER)
        dialog.show()

        val viewTreeObserver = rootLayout.viewTreeObserver
        if (viewTreeObserver.isAlive) {
            viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    HelperFunctions.circularRevealView(1, 300, rootLayout)
                    rootLayout.viewTreeObserver.removeGlobalOnLayoutListener(this)
                }
            })
        }

        close.setOnClickListener {
            HelperFunctions.circularConcealView(1, 300, rootLayout, null, dialog)
        }
    }
}