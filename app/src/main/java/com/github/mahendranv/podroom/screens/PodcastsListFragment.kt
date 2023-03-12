package com.github.mahendranv.podroom.screens

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.github.mahendranv.podroom.DemoViewModel
import com.github.mahendranv.podroom.entity.Podcast
import com.github.mahendranv.podroom.list.GenericListFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

class PodcastsListFragment : GenericListFragment() {

    val viewModel by viewModels<DemoViewModel>()

    override fun getTitle() = "Podcasts"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launch {
            viewModel.podcasts
                .flowOn(Dispatchers.Main)
                .flowWithLifecycle(lifecycle)
                .collect {
                    updateItems(it)
                }
        }
    }

    override fun onItemClick(item: Any?) {
        // no-op
    }

    override fun getActions(): List<String> = listOf(
        "Sync",
        "Delete"
    )

    override fun onActionClicked(position: Int, item: Any) {
        val podcast = item as? Podcast? ?: return
        when (position) {
            0 -> {
                Log.i(TAG, "onActionClicked: Sync podcast $podcast")
            }
            1 -> {
                Log.i(TAG, "onActionClicked: Delete podcast $podcast")
            }
        }
    }

    private val TAG = "PodcastsListFragment"
}