package com.github.mahendranv.podroom

object TestResources {

    private fun testAssetUrl(relativePath: String): String {
        return this.javaClass.classLoader!!.getResource(relativePath).toString()
    }

    val FEED_SVK = testAssetUrl("feeds/svk_20230220.xml")
    val FEED_SVK_APR_2023 = testAssetUrl("feeds/svk_20230421.xml")
    val FEED_SVK_APR_30_2023 = testAssetUrl("feeds/svk_20230430.xml")

    val FEED_FRAGMENTED = testAssetUrl("feeds/fragmented.xml")

    // https://www.podcast.co/resources/our-podcasts
    val REMOTE_URL_PODCAST_DOT_CO = "https://feed.pod.co/the-podcast-lab"
}