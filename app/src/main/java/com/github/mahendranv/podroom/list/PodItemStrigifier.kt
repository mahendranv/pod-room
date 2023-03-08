package com.github.mahendranv.podroom.list

import com.github.mahendranv.podroom.entity.Episode
import java.text.SimpleDateFormat
import java.util.Date

object PodItemStrigifier {

    val DATE_FORMAT = SimpleDateFormat("MMM dd, yy")

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
            else -> item.toString()
        }
    }

    fun readableDate(timeMillis: Long) = DATE_FORMAT.format(Date(timeMillis))

}