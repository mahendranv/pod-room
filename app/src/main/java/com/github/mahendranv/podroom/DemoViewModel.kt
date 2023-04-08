package com.github.mahendranv.podroom

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import com.github.mahendranv.podroom.entity.Episode
import com.github.mahendranv.podroom.utils.ioOperation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn

class DemoViewModel(application: Application) : AndroidViewModel(application) {

    private val podRoom: PodRoom by lazy {
        PodRoom.getInstance(application)
    }

    val podcasts =
        podRoom.getPodcastDao().fetchAll()
            .flowOn(Dispatchers.IO)

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
        val result = podRoom.syncPodcast(url)
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

    companion object {
        private const val TAG = "DemoViewModel"

        const val CHANNEL_ID_ALL = -1L
    }
}