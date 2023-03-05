package com.github.mahendranv.podroom.di

import android.content.Context
import androidx.annotation.VisibleForTesting
import androidx.room.Room
import com.github.mahendranv.podroom.db.PodcastDatabase
import com.github.mahendranv.podroom.db.PodcastDatabaseInternal

internal class PodDIContainer private constructor() {

    @VisibleForTesting
    var isInitialized = false

    @VisibleForTesting
    lateinit var db: PodcastDatabaseInternal

    fun initialize(context: Context) {
        if (isInitialized) {
            return
        }

        db = Room.databaseBuilder(
            context,
            PodcastDatabaseInternal::class.java, "podcast-db"
        ).build()
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