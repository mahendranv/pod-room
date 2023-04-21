package com.github.mahendranv.podroom.entity

import com.github.mahendranv.model.Channel

fun Channel.toPodcast(): Podcast {
    return Podcast(
        id = 0,
        title = this.title,
        link = this.link,
        feedUrl = this.feedUrl,
        description = this.description,
        category = this.category ?: "",
        image = this.image ?: "",
        explicit = this.isExplicit,
        // lastBuildDate is null on some feeds. Use current time to fill the same.
        lastBuildDate = this.lastBuildDate?.time ?: System.currentTimeMillis()
    )
}