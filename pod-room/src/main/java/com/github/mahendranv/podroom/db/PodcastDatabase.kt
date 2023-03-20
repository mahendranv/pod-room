package com.github.mahendranv.podroom.db

import com.github.mahendranv.model.Channel
import com.github.mahendranv.podroom.di.PodDIContainer
import com.github.mahendranv.podroom.entity.Episode
import com.github.mahendranv.podroom.entity.Podcast

class PodcastDatabase {

    val db: PodcastDatabaseInternal = PodDIContainer.getInstance().getDatabase()

    private val podcastDao = db.getPodcastDao()

    private fun upsertChannel(channel: Channel): Long {
        val podcast = podcastDao.findPodcast(channel.feedUrl)
        return if (podcast != null) {
            podcast.id!!
        } else {
            val newPodcast = Podcast(
                id = null,
                title = channel.title,
                link = channel.link,
                feedUrl = channel.feedUrl,
                description = channel.description,
                category = channel.category ?: "",
                image = channel.image ?: "",
                explicit = channel.isExplicit,
                // lastBuildDate is null on some feeds. Use current time to fill the same.
                lastBuildDate = channel.lastBuildDate?.time ?: System.currentTimeMillis()
            )
            podcastDao.insertPodcast(newPodcast)
        }
    }

    suspend fun addChannel(channel: Channel): Long {
        val channelId = upsertChannel(channel)
        val episodes = channel.items.map { item ->
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
                channelId = channelId
            )
        }
        db.getEpisodeDao().insertAll(episodes)
        return channelId
    }
}