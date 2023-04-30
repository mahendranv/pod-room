package com.github.mahendranv.podroom.screens

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.github.mahendranv.podroom.DemoViewModel
import com.github.mahendranv.podroom.R
import com.github.mahendranv.podroom.entity.Podcast
import com.github.mahendranv.podroom.list.GenericListFragment
import com.github.mahendranv.podroom.list.PodItemStringifier

class PodcastsListFragment : GenericListFragment<Podcast>() {

    private val viewModel by viewModels<DemoViewModel>()

    override fun getTitle() = "Podcasts"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fetchItems(viewModel.podcasts)
    }

    override fun onItemClick(podcast: Podcast) {
        findNavController().navigate(
            R.id.action_podcastsListFragment_to_episodesListScreen,
            EpisodesListScreen.prepareArgs(channelId = podcast.id)
        )
    }

    override fun getActions(): List<String> = listOf(
        "Sync",
        "Delete"
    )

    override fun onActionClicked(position: Int, item: Podcast) {
        when (position) {
            0 -> {
                Log.i(TAG, "onActionClicked: Sync podcast $item")
                viewModel.syncPodcast(item.id)
            }
            1 -> {
                Log.i(TAG, "onActionClicked: Delete podcast $item")
            }
        }
    }

    override fun prepareUiString(item: Podcast): CharSequence {
        return """
                ${item.title} 
                Last build: ${PodItemStringifier.readableDate(item.lastBuildDate)}
                ${item.description.take(100)}
            """.trimIndent()
    }

    private val TAG = "PodcastsListFragment"
}