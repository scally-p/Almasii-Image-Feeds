package io.capsella.almasii.imagefeeds.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.support.v7.app.AppCompatActivity
import android.util.DisplayMetrics
import android.util.Log
import java.text.SimpleDateFormat
import java.util.*

class HelperFunctions{

    companion object {

        private val TAG = HelperFunctions::class.java.simpleName

        fun isConnectedToInternet(context: Context): Boolean {

            val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetwork: NetworkInfo? = cm.activeNetworkInfo

            return activeNetwork?.isConnectedOrConnecting == true
        }

        fun getDateTimeInMilliSeconds(date: String): Long {

            val calendar = Calendar.getInstance()
            val parts = date.trim().split(" ").toTypedArray()

            val day = parts[0].split("-")[2]
            val mon = parts[0].split("-")[1]
            val yr = parts[0].split("-")[0]
            val hour = parts[1].split(":")[0]
            val minute = parts[1].split(":")[1]
            val second = parts[1].split(":")[2]

            calendar.set(Integer.parseInt(yr),
                    Integer.parseInt(mon) - 1,
                    Integer.parseInt(day),
                    Integer.parseInt(hour),
                    Integer.parseInt(minute),
                    Integer.parseInt(second))

            return calendar.timeInMillis
        }

        fun getDateFromMilliSeconds(milliSeconds: Long): String {

            val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

            val calendar = Calendar.getInstance()
            calendar.timeInMillis = milliSeconds

            return formatter.format(calendar.time)
        }

        fun getFormattedDateWithDayAndTimeString(date: String?, withDay: Boolean, withTime: Boolean): String {

            Log.d(TAG, "Date provided: $date")

            val calendar = Calendar.getInstance()
            val formatter: SimpleDateFormat

            val parts = date!!.trim().split(" ".toRegex())

            val day = parts[0].split("-".toRegex())[2]
            val mon = parts[0].split("-".toRegex())[1]
            val yr = parts[0].split("-".toRegex())[0]

            calendar.set(Integer.parseInt(yr), Integer.parseInt(mon) - 1, Integer.parseInt(day))

            if (withDay) {
                if (withTime) {
                    formatter = SimpleDateFormat("EE, d MMM yyyy, HH:mm")
                    val hr = parts[1].split(":".toRegex())[0]
                    val min = parts[1].split(":".toRegex())[1]
                    val sec = parts[1].split(":".toRegex())[2]

                    calendar.set(
                            Integer.parseInt(yr),
                            Integer.parseInt(mon) - 1,
                            Integer.parseInt(day),
                            Integer.parseInt(hr),
                            Integer.parseInt(min),
                            Integer.parseInt(sec))
                } else {
                    formatter = SimpleDateFormat("EE, d MMM yyyy")
                }
            } else {
                if (withTime) {
                    formatter = SimpleDateFormat("d MMM yyyy, HH:mm")
                    val hr = parts[1].split(":".toRegex())[0]
                    val min = parts[1].split(":".toRegex())[1]
                    val sec = parts[1].split(":".toRegex())[2]

                    calendar.set(
                            Integer.parseInt(yr),
                            Integer.parseInt(mon) - 1,
                            Integer.parseInt(day),
                            Integer.parseInt(hr),
                            Integer.parseInt(min),
                            Integer.parseInt(sec))
                } else {
                    formatter = SimpleDateFormat("d MMM yyyy")
                }
            }

            return formatter.format(Date(calendar.timeInMillis))
        }

        fun getDisplayMetrics(context: Context): DisplayMetrics {
            val displayMetrics = DisplayMetrics()
            val activity = context as AppCompatActivity
            activity.windowManager.defaultDisplay.getMetrics(displayMetrics)

            val width = displayMetrics.widthPixels
            val height = displayMetrics.heightPixels

            return displayMetrics
        }

        fun dpToPx(context: Context, dp: Int): Int {
            val displayMetrics = context.resources.displayMetrics
            return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT))
        }
    }
}