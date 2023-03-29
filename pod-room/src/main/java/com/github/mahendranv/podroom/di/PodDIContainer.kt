package com.github.mahendranv.podroom.di

import android.content.Context
import androidx.annotation.VisibleForTesting
import androidx.room.Room
import com.ctc.wstx.stax.WstxInputFactory
import com.ctc.wstx.stax.WstxOutputFactory
import com.fasterxml.jackson.dataformat.xml.XmlFactory
import com.github.mahendranv.AnchorParser
import com.github.mahendranv.podroom.db.PodcastDatabaseInternal
import com.github.mahendranv.podroom.sdk.PlayerStore
import com.github.mahendranv.podroom.sdk.PrefStore

internal class PodDIContainer private constructor() {

    @VisibleForTesting
    var isInitialized = false

    @VisibleForTesting
    lateinit var db: PodcastDatabaseInternal

    @VisibleForTesting
    lateinit var xmlFactory: XmlFactory

    @VisibleForTesting
    lateinit var player: PlayerStore

    fun initialize(context: Context) {
        if (isInitialized) {
            return
        }

        xmlFactory = XmlFactory.builder()
            .xmlInputFactory(WstxInputFactory())
            .xmlOutputFactory(WstxOutputFactory())
            .build()
        AnchorParser.setFactory(xmlFactory)

        db = Room.databaseBuilder(
            context,
            PodcastDatabaseInternal::class.java, "podcast-db"
        ).build()

        player = PlayerStore(
            PrefStore(context),
            db.getPlayerDao(),
            db.getEpisodeDao()
        )
        isInitialized = true
    }

    fun getDatabase() = db

    fun releaseResources() {
        if (::db.isInitialized) {
            db.close()
        }
    }

    companion object {

        private var instance: PodDIContainer? = null

        @Synchronized
        fun getInstance(): PodDIContainer {
            if (instance == null) {
                instance = PodDIContainer()
            }
            return instance!!
        }
    }
}