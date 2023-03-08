package com.github.mahendranv.podroom

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.github.mahendranv.podroom.entity.Podcast
import kotlinx.coroutines.Dispatchers
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

    val allEpisodes =
        podRoom.getEpisodeDao().getAllEpisodes()
            .flowOn(Dispatchers.IO)

    fun addPodcast(url: String) = viewModelScope.launch(Dispatchers.IO) {
        val result = podRoom.syncPodcast(url)
        Log.d(TAG, "addPodcast: $url = $result")
    }

    companion object {
        private val TAG = "DemoViewModel"
    }
}