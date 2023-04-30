package com.github.mahendranv.podroom.db

import com.github.mahendranv.model.Channel
import com.github.mahendranv.podroom.di.PodDIContainer
import com.github.mahendranv.podroom.entity.Episode
import com.github.mahendranv.podroom.entity.toPodcast

class PodcastDatabase {

    val db: PodcastDatabaseInternal = PodDIContainer.getInstance().getDatabase()

    private val podcastDao = db.getPodcastDao()


}