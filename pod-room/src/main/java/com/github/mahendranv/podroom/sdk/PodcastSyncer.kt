package com.github.mahendranv.podroom.sdk

import android.util.Log
import com.github.mahendranv.AnchorParser
import com.github.mahendranv.model.Channel
import com.github.mahendranv.model.StatusCode
import com.github.mahendranv.podroom.PodRoom
import com.github.mahendranv.podroom.dao.EpisodeDao
import com.github.mahendranv.podroom.dao.PodcastDao
import com.github.mahendranv.podroom.entity.Episode
import com.github.mahendranv.podroom.entity.toPodcast
import com.github.mahendranv.podroom.model.PodResult

/**
 * Coordinates podcast / episode tables to complete sync.
 */
class PodcastSyncer(
    private val podcastDao: PodcastDao,
    private val episodeDao: EpisodeDao
) {

    /**
     * Fetches podcast and episodes and inserts to DB.
     */
    suspend fun syncPodcast(url: String): PodResult<Long> {
        val result = AnchorParser.parse(url)
        return if (result.statusCode == StatusCode.SUCCESS) {
            val channel = result.channel
            val id = addChannel(channel)
            PodResult.Success(id)
        } else {
            PodResult.Failure(
                errorCode = result.statusCode.toErrorCode(),
                Exception("Anchor parse failed ${result.statusCode}")
            )
        }
    }

    /**
     * Sync podcast given podcast-id.
     */
    suspend fun syncPodcast(id: Long): PodResult<Long> {
        val podcast = podcastDao.getPodcast(id = id)
        return if (podcast == null) {
            Log.e(PodRoom.TAG, "syncPodcast: podcast not found (id = $id)")
            PodResult.Failure(
                errorCode = ERROR_UNKNOWN_PODCAST_ID,
                Exception("Podcast with id: $id not found")
            )
        } else {
            syncPodcast(podcast.feedUrl)
        }
    }

    /**
     * Deletes given podcast and cascades delete to other dependents.
     */
    suspend fun deletePodcast(id: Long) : Boolean {
        return podcastDao.deletePodcast(id) == 1
    }

    private suspend fun addChannel(channel: Channel): Long {
        val podcast = channel.toPodcast()
        val channelId = podcastDao.insertPodcast(podcast)
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
        episodeDao.insertAll(episodes)
        return channelId
    }

    companion object {
        const val NO_ERROR = 0
        const val ERROR_IO = -100
        const val ERROR_INVALID_URL = -101
        const val ERROR_PARSER_FAILURE = -102
        const val ERROR_UNKNOWN_PODCAST_ID = -103
        const val UNKNOWN_ERROR = -104

        fun StatusCode.toErrorCode() = when (this) {
            StatusCode.SUCCESS -> NO_ERROR
            StatusCode.IO_EXCEPTION -> ERROR_IO
            StatusCode.INVALID_URL -> ERROR_INVALID_URL
            StatusCode.PARSER_FAILURE -> ERROR_PARSER_FAILURE
            else -> UNKNOWN_ERROR
        }
    }
}