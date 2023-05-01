package com.github.mahendranv.podroom

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import com.github.mahendranv.podroom.entity.Download
import com.github.mahendranv.podroom.entity.Episode
import com.github.mahendranv.podroom.utils.ioOperation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlin.random.Random

class DemoViewModel(application: Application) : AndroidViewModel(application) {

    private val random = Random(2435)

    private val ioDispatcher = Dispatchers.IO

    private val podRoom: PodRoom by lazy {
        PodRoom.getInstance(application)
    }

    val podcasts =
        podRoom.getPodcastDao().fetchAll()
            .flowOn(ioDispatcher)

    val downloads = podRoom.getDownloads().fetchAll().flowOn(ioDispatcher)

    fun getEpisodes(channelId: Long): Flow<List<Episode>> {
        val dao = podRoom.getEpisodeDao()
        val flow = if (channelId == CHANNEL_ID_ALL) {
            dao.getAllEpisodes()
        } else {
            dao.getEpisodesByChannelId(channelId)
        }
        return flow.flowOn(Dispatchers.IO)
    }

    fun addPodcast(url: String) = ioOperation {
        val result = podRoom.getSyncer().syncPodcast(url)
        Log.d(TAG, "addPodcast: $url = $result")
    }

    fun playNext(id: Long) = ioOperation {
        podRoom.getPlayer().playNext(id)
    }

    fun play(id: Long) = ioOperation {
        podRoom.getPlayer().play(id)
    }

    fun enqueue(id: Long) = ioOperation {
        podRoom.getPlayer().enqueue(id)
    }

    fun clearAll() = ioOperation {
        Log.d(TAG, "clearAll: invoked")
        podRoom.getPlayer().clearAll()
    }

    fun markAsPlayed(episodeId: Long) = ioOperation {
        podRoom.getPlayer().markAsPlayed(episodeId)
    }

    fun download(id: Long) {
        updateDownloadProgress(id = id, progress = 0)
    }

    fun updateDownloadProgress(id: Long, progress: Int = random.nextInt(0, 100)) = ioOperation {
        podRoom.getDownloads().saveProgress(Download(id = id, progress = progress))
    }

    fun markDownloadComplete(id: Long) = ioOperation {
        podRoom.getDownloads().markAsComplete(id = id, filePath = "file_${random.nextInt()}")
    }

    fun deleteDownload(id: Long) = ioOperation {
        podRoom.getDownloads().delete(id)
    }

    fun syncPodcast(id: Long) = ioOperation {
        podRoom.getSyncer().syncPodcast(id)
    }

    fun deletePodcast(id: Long) = ioOperation {
        podRoom.getSyncer().deletePodcast(id)
    }

    companion object {
        private const val TAG = "DemoViewModel"

        const val CHANNEL_ID_ALL = -1L
    }
}