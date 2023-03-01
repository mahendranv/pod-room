package com.github.mahendranv.podroom.db

import android.content.Context
import androidx.room.Room

class PodcastDatabase private constructor(appContext: Context) {

    val db = Room.databaseBuilder(
        appContext,
        PodcastDatabaseInternal::class.java, "podcast-db"
    ).build()


    companion object {
        private var instance: PodcastDatabase? = null

        @Synchronized
        fun getInstance(context: Context): PodcastDatabase {
            if (instance == null) {
                instance = PodcastDatabase(context)
            }
            return instance as PodcastDatabase
        }
    }
}