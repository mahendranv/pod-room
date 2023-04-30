package com.github.mahendranv.podroom

import com.github.mahendranv.podroom.model.PodResult
import com.github.mahendranv.podroom.sdk.PodcastSyncer
import junit.framework.TestCase.*
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Test
import java.io.File

class PodRoomTest : BaseTest() {

    @Test
    fun test_fetchPodcast_svk() = runBlocking {
        val result = podRoom.getSyncer().syncPodcast(TestResources.FEED_SVK)
        assertTrue(result is PodResult.Success)

        val podcasts = diContainer.db.getPodcastDao().fetchAll().firstOrNull()
        assertEquals(1, podcasts?.size)

        val episodes = diContainer.db.getEpisodeDao().getAllEpisodes().firstOrNull()
        assertEquals(108, episodes?.size)
    }

    @Test
    fun test_fetchPodcast_fragmented() = runBlocking {
        val result = podRoom.getSyncer().syncPodcast(TestResources.FEED_FRAGMENTED)
        assertTrue(result is PodResult.Success)

        val podcasts = diContainer.db.getPodcastDao().fetchAll().firstOrNull()
        assertEquals(1, podcasts?.size)

        val episodes = diContainer.db.getEpisodeDao().getAllEpisodes().firstOrNull()
        assertEquals(242, episodes?.size)
    }

    @Test
    fun test_fetch_remote_podcasts() = runBlocking {
        val result = podRoom.getSyncer().syncPodcast(TestResources.REMOTE_URL_PODCAST_DOT_CO)
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
        var podcastId = -1L

        val testEpisodeUrl =
            "https://anchor.fm/s/e337170/podcast/play/64460253/https%3A%2F%2Fd3ctxlq1ktw2nl.cloudfront.net%2Fproduction%2Fexports%2Fe337170%2F64460253%2F85c1c87e06cb20fc01baea4bdfbe8647.m4a"

        // Simulates content publish v1
        downloadContent(TestResources.FEED_SVK, file)

        // sync v1
        val result1 = podRoom.getSyncer().syncPodcast(feedUrl)
        assertTrue(result1 is PodResult.Success)
        podcastId = (result1 as PodResult.Success).data
        val podcastV1 = podRoom.getPodcastDao().fetchAll().pickFirstElement()
        assertNotNull(podcastV1)
        assertEquals("SVK Feb 2023", podcastV1!!.title)
        val episodeId = podRoom.getEpisodeDao().getIDByStream(testEpisodeUrl)!!
        val episodeTitleV1 = podRoom.getEpisodeDao().getEpisodeById(episodeId)!!.title
        assertEquals("Ayali01", episodeTitleV1)

        // Simulates content publish v2
        downloadContent(TestResources.FEED_SVK_APR_2023, file)

        // sync v2
        val result2 = podRoom.getSyncer().syncPodcast(feedUrl)
        assertTrue(result2 is PodResult.Success)

        val podcastV2 = podRoom.getPodcastDao().fetchAll().pickFirstElement()
        assertNotNull(podcastV2)
        assertEquals("SVK Apr 2023", podcastV2!!.title)

        val episodeTitleV2 = podRoom.getEpisodeDao().getEpisodeById(episodeId)!!.title
        assertEquals("Ayali02", episodeTitleV2)

        // Simulate content update v3
        downloadContent(TestResources.FEED_SVK_APR_30_2023, file)
        // sync v3
        val result3 = podRoom.getSyncer().syncPodcast(podcastId)
        assertTrue(result3 is PodResult.Success)

        val afterSync3 = podRoom.getPodcastDao().getPodcast(podcastId)
        assertEquals("SVK April30 2023", afterSync3!!.title)
    }

    @Test
    fun test_sync_when_unknown_podcast_id_used() = runBlocking {
        val result = podRoom.getSyncer().syncPodcast(10)
        assertTrue(result is PodResult.Failure)
        assertEquals(PodcastSyncer.ERROR_UNKNOWN_PODCAST_ID, (result as PodResult.Failure).errorCode)
    }

    @After
    override fun tearDown() {
        val file = File("tmp_sync_test.xml")
        file.delete()
        super.tearDown()
    }
}