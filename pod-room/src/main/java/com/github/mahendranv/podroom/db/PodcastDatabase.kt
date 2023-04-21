package com.github.mahendranv.podroom.db

import com.github.mahendranv.model.Channel
import com.github.mahendranv.podroom.di.PodDIContainer
import com.github.mahendranv.podroom.entity.Episode
import com.github.mahendranv.podroom.entity.toPodcast

class PodcastDatabase {

    val db: PodcastDatabaseInternal = PodDIContainer.getInstance().getDatabase()

    private val podcastDao = db.getPodcastDao()

    private fun upsertChannel(channel: Channel): Long {
        val podcast = channel.toPodcast()
        return podcastDao.insertPodcast(podcast)
    }

    suspend fun addChannel(channel: Channel): Long {
        val channelId = upsertChannel(channel)
        val episodes = channel.items.filter { it.isValid }.map { item ->
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
                streamUrl = item.audioEnclosure.url,
                streamType = item.audioEnclosure.type,
                streamSize = item.audioEnclosure.length.toInt(),
                channelId = channelId
            )
        }
        db.getEpisodeDao().insertAll(episodes)
        return channelId
    }
}