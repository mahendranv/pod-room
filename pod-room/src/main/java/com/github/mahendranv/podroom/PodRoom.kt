package com.github.mahendranv.podroom

import android.content.Context
import androidx.annotation.VisibleForTesting
import com.github.mahendranv.AnchorParser
import com.github.mahendranv.model.StatusCode
import com.github.mahendranv.podroom.db.PodcastDatabase
import com.github.mahendranv.podroom.di.PodDIContainer
import com.github.mahendranv.podroom.model.PodResult

class PodRoom private constructor(private val appContext: Context) {

    internal val db = PodcastDatabase()

    /**
     * Fetches podcast and episodes and inserts to DB.
     */
    suspend fun syncPodcast(url: String): PodResult<Long> {
        val result = AnchorParser.parse(url)
        return if (result.statusCode == StatusCode.SUCCESS) {
            val channel = result.channel
            val id = db.addChannel(channel)
            PodResult.Success(id)
        } else {
            PodResult.Failure(
                errorCode = result.statusCode.ordinal,
                Exception("Anchor parse failed ${result.statusCode}")
            )
        }
    }

    companion object {
        private var instance: PodRoom? = null

        @Synchronized
        fun getInstance(context: Context): PodRoom {
            if (instance == null) {
                instance = PodRoom(context)
            }
            PodDIContainer.getInstance().initialize(context)
            return instance as PodRoom
        }

        @VisibleForTesting
        fun clearInstance() {
            instance = null
        }
    }
}