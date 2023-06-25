package com.github.mahendranv.podroom

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import com.github.mahendranv.podroom.entity.Download
import com.github.mahendranv.podroom.entity.Episode
import com.github.mahendranv.podroom.model.PodResult
import com.github.mahendranv.podroom.utils.ioOperation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlin.random.Random

class DemoViewModel(application: Application) : AndroidViewModel(application) {

    private val random = Random(2435)

    private val ioDispatcher = Dispatchers.IO

    ///////////////////////////
    // Entry point to the SDK
    // Getting an [PodRoom] instance
    ///////////////////////////
    private val podRoom: PodRoom by lazy {
        PodRoom.getInstance(context = application)
    }

    val podcasts =
        podRoom.getPodcastDao().fetchAll()
            .flowOn(ioDispatcher)

    val downloads = podRoom.getDownloads().fetchAll().flowOn(ioDispatcher)

    ///////////////////////
    // Podcast operations
    //////////////////////

    /**
     * Add podcast by RSS xml URL.
     */
    fun addPodcast(url: String) = ioOperation {
        val result = podRoom.getSyncer().syncPodcast(url)
        Log.d(TAG, "addPodcast: $url = $result")

        when (result) {
            is PodResult.Success -> {
                val podcastId = result.data
                Log.d(TAG, "addPodcast: success podcastId = $podcastId")
            }

            is PodResult.Failure -> {
                Log.d(TAG, "addPodcast: failed - ${result.errorCode}")
            }

            else -> {
                // No-op
            }
        }
    }

    /**
     * Sync podcast after insertion.
     */
    fun syncPodcast(id: Long) = ioOperation {
        podRoom.getSyncer().syncPodcast(id)
    }

    /**
     * Deletes podcast and its episodes.
     */
    fun deletePodcast(id: Long) = ioOperation {
        podRoom.getSyncer().deletePodcast(id)
    }

    /////////////////////////
    // Player updates
    /////////////////////////

    /**
     * Enqueues an item to be played next in the player queue.
     */
    fun playNext(id: Long) = ioOperation {
        podRoom.getPlayer().playNext(id)
    }

    /**
     * Moves the given item to the front of the player queue. Pushes down other items
     * if any.
     */
    fun play(id: Long) = ioOperation {
        podRoom.getPlayer().play(id)
    }

    /**
     * Adds an item to the end of the player queue.
     */
    fun enqueue(id: Long) = ioOperation {
        podRoom.getPlayer().enqueue(id)
    }

    /**
     * Clears all items from the player queue.
     */
    fun clearAll() = ioOperation {
        Log.d(TAG, "clearAll: invoked")
        podRoom.getPlayer().clearAll()
    }

    /**
     * Sets seek position to the end and marks episode played.
     */
    fun markAsPlayed(episodeId: Long) = ioOperation {
        podRoom.getPlayer().markAsPlayed(episodeId)
    }

    ///////////////////////////////////////////////
    // Downloads
    ///////////////////////////////////////////////

    /**
     * Download sets download progress to zero. And creates entry in downloads table.
     */
    fun download(id: Long) {
        updateDownloadProgress(id = id, progress = 0)
    }

    /**
     * Used for updating the download progress of the file.
     *
     * @param id episode id
     * @param progress download progress
     */
    fun updateDownloadProgress(id: Long, progress: Int = random.nextInt(0, 100)) = ioOperation {
        podRoom.getDownloads().saveProgress(Download(id = id, progress = progress))
    }

    /**
     * Updates download status to complete and updates local file URI for future reference.
     */
    fun markDownloadComplete(id: Long) = ioOperation {
        podRoom.getDownloads().markAsComplete(id = id, filePath = "file_${random.nextInt()}")
    }

    /**
     * Deletes download entry for file.
     */
    fun deleteDownload(id: Long) = ioOperation {
        podRoom.getDownloads().delete(id)
    }

    //////////////////////////////////
    // WIP: Episode operations
    //////////////////////////////////

    fun getEpisodes(channelId: Long): Flow<List<Episode>> {
        val dao = podRoom.getEpisodeDao()
        val flow = if (channelId == CHANNEL_ID_ALL) {
            dao.getAllEpisodes()
        } else {
            dao.getEpisodesByChannelId(channelId)
        }
        return flow.flowOn(Dispatchers.IO)
    }


    companion object {
        private const val TAG = "DemoViewModel"

        const val CHANNEL_ID_ALL = -1L
    }
}