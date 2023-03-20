package com.github.mahendranv.podroom.list

import android.annotation.SuppressLint
import com.github.mahendranv.podroom.entity.Episode
import com.github.mahendranv.podroom.entity.Podcast
import java.text.SimpleDateFormat
import java.util.Date

object PodItemStringifier {

    @SuppressLint("SimpleDateFormat")
    private val DATE_FORMAT = SimpleDateFormat("MMM dd, yy")

    fun stringify(item: Any): CharSequence {
        return when (item) {
            is Episode -> """
                S${item.season} E${item.episode} ${item.title}
                ${item.duration} ${
                if (item.explicit) {
                    "[E]"
                } else ""
            } ${readableDate(item.pubDate)}
            """.trimIndent()
            is Podcast -> """
                ${item.title} 
                Last build: ${readableDate(item.lastBuildDate)}
                ${item.description.take(100)}
            """.trimIndent()
            else -> item.toString()
        }
    }

    fun readableDate(timeMillis: Long) = DATE_FORMAT.format(Date(timeMillis))

}