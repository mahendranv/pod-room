package com.github.mahendranv.podroom

import android.net.Uri

object TestResources {

    private fun testAssetUrl(relativePath: String): String {
        return this.javaClass.classLoader!!.getResource(relativePath).toString()
    }

    val FEED_SVK = testAssetUrl("feeds/svk_20230220.xml")

    val FEED_FRAGMENTED = testAssetUrl("feeds/fragmented.xml")

}