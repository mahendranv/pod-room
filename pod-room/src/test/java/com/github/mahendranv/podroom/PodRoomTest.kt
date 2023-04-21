package com.github.mahendranv.podroom

import com.github.mahendranv.podroom.model.PodResult
import java.io.File
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import org.junit.After
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

    @Test
    fun test_episode_amends_in_next_fetch() = runBlocking {
        val file = File("tmp_sync_test.xml")
        file.createNewFile()
        val feedUrl = file.toURI().toURL().toString()

        val testEpisodeUrl =
            "https://anchor.fm/s/e337170/podcast/play/64460253/https%3A%2F%2Fd3ctxlq1ktw2nl.cloudfront.net%2Fproduction%2Fexports%2Fe337170%2F64460253%2F85c1c87e06cb20fc01baea4bdfbe8647.m4a"

        // Simulates content publish v1
        downloadContent(TestResources.FEED_SVK, file)

        // sync v1
        val result1 = podRoom.syncPodcast(feedUrl)
        assertTrue(result1 is PodResult.Success)
        val podcastV1 = podRoom.getPodcastDao().fetchAll().pickFirstElement()
        assertNotNull(podcastV1)
        assertEquals("SVK Feb 2023", podcastV1!!.title)
        val episodeId = podRoom.getEpisodeDao().getIDByStream(testEpisodeUrl)!!
        val episodeTitleV1 = podRoom.getEpisodeDao().getEpisodeById(episodeId)!!.title
        assertEquals("Ayali01", episodeTitleV1)

        // Simulates content publish v2
        downloadContent(TestResources.FEED_SVK_APR_2023, file)

        // sync v2
        val result2 = podRoom.syncPodcast(feedUrl)
        assertTrue(result2 is PodResult.Success)

        val podcastV2 = podRoom.getPodcastDao().fetchAll().pickFirstElement()
        assertNotNull(podcastV2)
        assertEquals("SVK Apr 2023", podcastV2!!.title)

        val episodeTitleV2 = podRoom.getEpisodeDao().getEpisodeById(episodeId)!!.title
        assertEquals("Ayali02", episodeTitleV2)
    }

    @After
    override fun tearDown() {
        val file = File("tmp_sync_test.xml")
        file.delete()
        super.tearDown()
    }
}