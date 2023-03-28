package com.github.mahendranv.podroom

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.github.mahendranv.podroom.entity.Episode
import com.github.mahendranv.podroom.entity.Podcast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

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

    fun addPodcast(url: String) = viewModelScope.launch(Dispatchers.IO) {
        val result = podRoom.syncPodcast(url)
        Log.d(TAG, "addPodcast: $url = $result")
    }

    companion object {
        private const val TAG = "DemoViewModel"

        const val CHANNEL_ID_ALL = -1L
    }
}