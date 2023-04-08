package com.github.mahendranv.podroom.sdk

import com.github.mahendranv.podroom.dao.EpisodeDao
import com.github.mahendranv.podroom.dao.PlayerDao
import com.github.mahendranv.podroom.entity.PlaybackPosition

/**
 * Persistence front for player related operations.
 * 1. Queue
 * 2. Playback positions
 */
class PlayerStore(
    private val prefStore: PrefStore,
    private val playerDao: PlayerDao,
    private val episodeDao: EpisodeDao
) {

    fun getCurrentEpisodeId() = prefStore.currentEpisode

    fun playNext(episodeId: Long) {
        playerDao.playNext(episodeId, getCurrentEpisodeId())
    }

    fun play(episodeId: Long) {
        playerDao.play(episodeId)
        prefStore.currentEpisode = episodeId
    }

    fun enqueue(episodeId: Long) {
        playerDao.enqueue(episodeId)
    }

    fun removeFromQueue(episodeId: Long) {
        playerDao.delete(episodeId)
    }

    /**
     * Marks episode complete.
     */
    fun markAsPlayed(episodeId: Long) {
        val episode = episodeDao.getEpisodeById(episodeId)
        if (episode != null) {
            val entry =
                PlaybackPosition(id = episodeId, playbackPosition = episode.durationInSeconds)
            playerDao.setPlaybackPosition(entry)
        }
    }

    /**
     * Stores player position of the episode for resume purpose.
     */
    fun setPlaybackPosition(episodeId: Long, position: Long) {
        val entry =
            PlaybackPosition(id = episodeId, playbackPosition = position)
        playerDao.setPlaybackPosition(entry)
    }

    fun clearAll() {
        playerDao.clearAll()
    }

    companion object {

        const val POINTER_NONE = -1L
    }
}