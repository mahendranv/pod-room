package com.github.mahendranv.podroom.fetch

import com.github.mahendranv.podroom.BaseTest
import com.github.mahendranv.podroom.model.PodResult
import com.github.mahendranv.podroom.sdk.PodcastSyncer
import kotlinx.coroutines.runBlocking
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class NegativeCases : BaseTest() {

    @Test
    fun `when not a podcast RSS url`(): Unit = runBlocking {
        val result = podRoom.getSyncer().syncPodcast("http://www.google.com")
        assertIs<PodResult.Failure>(result)
        assertEquals(PodcastSyncer.ERROR_PARSER_FAILURE, result.errorCode)
    }

    @Test
    fun `when random xml url provided`(): Unit = runBlocking {
        val url = "https://www.w3schools.com/xml/plant_catalog.xml"
        val result = podRoom.getSyncer().syncPodcast(url)
        assertIs<PodResult.Failure>(result)
        assertEquals(PodcastSyncer.ERROR_PARSER_FAILURE, result.errorCode)
    }

    @Test
    fun `when invalid url`(): Unit = runBlocking {
        val result = podRoom.getSyncer().syncPodcast("www.invalid#url.com")
        assertIs<PodResult.Failure>(result)
        assertEquals(PodcastSyncer.ERROR_INVALID_URL, result.errorCode)
    }

    @Test
    fun `when podcast invalid podcast id`(): Unit = runBlocking {
        val result = podRoom.getSyncer().syncPodcast(376)
        assertIs<PodResult.Failure>(result)
        assertEquals(PodcastSyncer.ERROR_UNKNOWN_PODCAST_ID, result.errorCode)
    }
}