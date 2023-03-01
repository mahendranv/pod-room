package com.github.mahendranv.podroom

import android.content.Context
import com.github.mahendranv.AnchorParser
import com.github.mahendranv.model.StatusCode
import com.github.mahendranv.podroom.entity.Episode

class PodRoom private constructor(private val appContext: Context) {

    /**
     * Fetches podcast and episodes and inserts to DB.
     */
    suspend fun fetchPodcast(url: String) {
        val result = AnchorParser.parse(url)
        if (result.statusCode == StatusCode.SUCCESS) {
            val channel = result.channel
            channel.items.map { item ->
                Episode(
                    title = item.title,
                    description = item.description,
                    link = item.link,
                    guid = item.guid,
                    season = item.season,
                    episode = item.episode,
                    explicit = item.isExplicit,
                    pubDate = item.pubDate.time,
                    durationInSeconds = item.durationInSeconds,
                    duration = item.printableDuration,
                    streamUrl = item.enclosure.url,
                    streamType = item.enclosure.type,
                    streamSize = item.enclosure.length.toInt(),
                    channelId = 10
                )
            }.forEach {
                println("Item -> $it")
            }
        }
    }

    companion object {
        private var instance: PodRoom? = null

        @Synchronized
        fun getInstance(context: Context): PodRoom {
            if (instance == null) {
                instance = PodRoom(context)
            }
            return instance as PodRoom
        }
    }
}