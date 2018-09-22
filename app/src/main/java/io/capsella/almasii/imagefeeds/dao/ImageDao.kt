package io.capsella.almasii.imagefeeds.dao

import android.content.Context
import android.content.Intent
import android.util.Log
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import io.capsella.almasii.imagefeeds.database.Database
import io.capsella.almasii.imagefeeds.model.Image
import io.capsella.almasii.imagefeeds.util.Constants
import org.json.JSONArray

class ImageDao {

    private val TAG = ImageDao::class.java.simpleName
    private var context: Context
    private var database: Database? = null

    constructor(context: Context) {
        this.context = context
        this.database = Database(context)
    }

    fun fetchData() {

        Log.d(TAG, "Url - All Photos: ${Constants.URL_ALL_PHOTOS}")

        val request = object : StringRequest(Request.Method.GET, Constants.URL_ALL_PHOTOS,
                Response.Listener { response ->

                    try {
                        Log.d(TAG, "All Photos JSON \n----------$response")

                        database!!.clear()
                        database!!.saveImages(JSONArray(response))

                        context.sendBroadcast(Intent(Constants.Broadcast_DATA_FETCH_COMPLETED))

                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                },
                Response.ErrorListener { error ->
                    error.printStackTrace()
                }) {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val headers = HashMap<String, String>()
                headers["Content-Type"] = "application/json"
                return headers
            }
        }

//        val request = JsonArrayRequest(Request.Method.GET, Constants.URL_ALL_PHOTOS, null,
//                Response.Listener { response ->
//
//                    try {
//                        Log.d(TAG, "All Photos JSON \n----------$response")
//
//                        database!!.clear()
//                        database!!.saveImages(response)
//
//                        context.sendBroadcast(Intent(Constants.Broadcast_DATA_FETCH_COMPLETED))
//
//                    } catch (e: Exception) {
//                        e.printStackTrace()
//                    }
//                },
//                Response.ErrorListener { error ->
//                    error.printStackTrace()
//                }
//        )

        Volley.newRequestQueue(context).add(request)
    }

    fun getImages(): MutableList<Image>? {

        val images: MutableList<Image> = ArrayList()

        try {
            val orderBy = "${Database.COLUMN_CREATED_AT_MILLIS} DESC"
            val cursor = database!!.open().getSQLiteDatabase().query(Database.TABLE_IMAGES, null, null, null, null, null, orderBy)
            Log.d(TAG, "Images Count: ${cursor.count}")

            while (cursor.moveToNext()) {
                val image = Image(cursor.getInt(cursor.getColumnIndex(Database.COLUMN_ID)),
                        cursor.getString(cursor.getColumnIndex(Database.COLUMN_URL)),
                        cursor.getString(cursor.getColumnIndex(Database.COLUMN_DOWNLOAD_TOKEN)),
                        cursor.getString(cursor.getColumnIndex(Database.COLUMN_NAME)),
                        cursor.getInt(cursor.getColumnIndex(Database.COLUMN_CREATED_AT_MILLIS)))

                images.add(image)
            }
            Log.d(TAG, "Images Count: ${images.size}")

            cursor.close()
            database!!.close()
            return images

        } catch (e: Exception) {
            e.printStackTrace()
            database!!.close()
            return null
        }
    }

    fun getImagesCount(): Int {
        return try {
            val cursor = database!!.open().getSQLiteDatabase().query(Database.TABLE_IMAGES, null, null, null, null, null, null)
            val count = cursor.count
            cursor.close()
            database!!.close()

            count
        } catch (e: Exception) {
            database!!.close()
            e.printStackTrace()
            0
        }
    }
}