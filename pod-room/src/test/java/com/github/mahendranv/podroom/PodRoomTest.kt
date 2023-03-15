package com.github.mahendranv.podroom

import com.github.mahendranv.podroom.model.PodResult
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import org.junit.Test

class PodRoomTest : BaseTest() {

    @Test
    fun test_fetchPodcast_svk() = runBlocking {
        val result = podRoom.syncPodcast(TestResources.FEED_SVK)
        assertTrue(result is PodResult.Success)

        val podcasts = diContainer.db.getPodcastDao().fetchAll().firstOrNull()
        assertEquals(1, podcasts?.size)

        val episodes = diContainer.db.getEpisodeDao().getAllEpisodes().firstOrNull()
        assertEquals(108, episodes?.size)
    }

    @Test
    fun test_fetchPodcast_fragmented() = runBlocking {
        val result = podRoom.syncPodcast(TestResources.FEED_FRAGMENTED)
        assertTrue(result is PodResult.Success)

        val podcasts = diContainer.db.getPodcastDao().fetchAll().firstOrNull()
        assertEquals(1, podcasts?.size)

        val episodes = diContainer.db.getEpisodeDao().getAllEpisodes().firstOrNull()
        assertEquals(242, episodes?.size)
    }

    @Test
    fun test_fetch_remote_podcasts() = runBlocking {
        val result = podRoom.syncPodcast(TestResources.REMOTE_URL_PODCAST_DOT_CO)
        assertTrue(result is PodResult.Success)

        val podcasts = diContainer.db.getPodcastDao().fetchAll().firstOrNull()
        assertEquals(1, podcasts?.size)

        val episodes = diContainer.db.getEpisodeDao().getAllEpisodes().firstOrNull()
        assertTrue((episodes?.size ?: 0) > 0)
    }
}