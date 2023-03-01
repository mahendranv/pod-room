package com.github.mahendranv.podroom

import kotlinx.coroutines.runBlocking
import org.junit.Test

class PodRoomTest : BaseTest() {

    @Test
    fun test_fetchPodcast_svk() = runBlocking {
        println("starting test")
        podRoom.fetchPodcast(TestResources.FEED_SVK)
    }

}