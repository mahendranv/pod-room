package com.github.mahendranv.podroom.list

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.Date

object PodItemStringifier {

    @SuppressLint("SimpleDateFormat")
    private val DATE_FORMAT = SimpleDateFormat("MMM dd, yy")

    fun readableDate(timeMillis: Long) = DATE_FORMAT.format(Date(timeMillis))

}