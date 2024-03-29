package com.github.mahendranv.podroom

import android.content.Context
import androidx.annotation.VisibleForTesting
import com.github.mahendranv.podroom.db.PodcastDatabase
import com.github.mahendranv.podroom.di.PodDIContainer

class PodRoom private constructor(private val appContext: Context) {

    internal val db = PodcastDatabase()

    @VisibleForTesting
    fun getPodcastDao() = db.db.getPodcastDao()
    @VisibleForTesting
    fun getEpisodeDao() = db.db.getEpisodeDao()
    @VisibleForTesting
    fun getPlayerDao() = db.db.getPlayerDao()
    @VisibleForTesting
    fun getDownloadDao() = db.db.getDownloadDao()

    fun getPlayer() = PodDIContainer.getInstance().player
    fun getSyncer() = PodDIContainer.getInstance().syncer
    fun getDownloads() = PodDIContainer.getInstance().downloads
    fun getEpisodeStore() = PodDIContainer.getInstance().episodes

    companion object {
        const val TAG = "PodRoom"
        const val Version = "0.3"
        const val VersionCode = 3

        private var instance: PodRoom? = null


        @Synchronized
        fun getInstance(context: Context): PodRoom {
            PodDIContainer.getInstance().initialize(context)
            if (instance == null) {
                instance = PodRoom(context)
            }
            return instance as PodRoom
        }

        @VisibleForTesting
        fun clearInstance() {
            instance = null
        }
    }
}