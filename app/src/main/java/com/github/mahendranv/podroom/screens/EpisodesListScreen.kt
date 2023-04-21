package com.github.mahendranv.podroom.screens

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.github.mahendranv.podroom.DemoViewModel
import com.github.mahendranv.podroom.DemoViewModel.Companion.CHANNEL_ID_ALL
import com.github.mahendranv.podroom.entity.Episode
import com.github.mahendranv.podroom.list.GenericListFragment
import com.github.mahendranv.podroom.list.PodItemStringifier
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

class EpisodesListScreen : GenericListFragment<Episode>() {

    private val channelId by lazy {
        arguments?.getLong(ARG_CHANNEL_ID, CHANNEL_ID_ALL) ?: CHANNEL_ID_ALL
    }

    private val viewModel by viewModels<DemoViewModel>()

    override fun getTitle() = "Episodes"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            viewModel.getEpisodes(channelId)
                .flowOn(Dispatchers.Main)
                .collect {
                    updateItems(it)
                }
        }
    }

    override fun onItemClick(item: Episode) {
        // no-op
    }

    override fun getActions(): List<String> = listOf(
        "Play",
        "Play Next",
        "Enqueue",
        "Download",
        "Mark as played"
    )

    override fun onActionClicked(position: Int, episode: Episode) {
        when (position) {
            0 -> {
                Log.i(TAG, "onActionClicked: Play > $episode")
                viewModel.play(episode.id)
            }
            1 -> {
                Log.i(TAG, "onActionClicked: PlayNext > $episode")
                viewModel.playNext(episode.id)
            }
            2 -> {
                Log.i(TAG, "onActionClicked: enqueue > $episode")
                viewModel.enqueue(episode.id)
            }
            3 -> {
                Log.i(TAG, "onActionClicked: Download > $episode")
            }
            4 -> {
                Log.i(TAG, "onActionClicked: Mark as played > ${episode.title}")
                viewModel.markAsPlayed(episodeId = episode.id)
            }
        }
    }

    override fun prepareUiString(item: Episode): CharSequence {
        return """
                S${item.season} E${item.episode} ${item.title}
                ${item.duration} ${
            if (item.explicit) {
                "[E]"
            } else ""
        } ${PodItemStringifier.readableDate(item.pubDate)}
            """.trimIndent()
    }

    companion object {
        private const val TAG = "EpisodesListScreen"
        private const val ARG_CHANNEL_ID = "arg_channel_id"

        fun prepareArgs(channelId: Long = CHANNEL_ID_ALL) = bundleOf(
            ARG_CHANNEL_ID to channelId
        )
    }
}