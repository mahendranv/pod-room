package com.github.mahendranv.podroom.sdk

import com.github.mahendranv.podroom.dao.EpisodeDao
import com.github.mahendranv.podroom.dao.PlayerDao

/**
 * Persistence front for player related operations.
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

    companion object {

        const val POINTER_NONE = -1L
    }
}