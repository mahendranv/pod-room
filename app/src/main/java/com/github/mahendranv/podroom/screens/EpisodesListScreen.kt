package com.github.mahendranv.podroom.screens

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.github.mahendranv.podroom.DemoViewModel
import com.github.mahendranv.podroom.entity.Episode
import com.github.mahendranv.podroom.list.GenericListFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

class EpisodesListScreen : GenericListFragment() {

    val viewModel by viewModels<DemoViewModel>()

    override fun getTitle() = "Episodes"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            viewModel.allEpisodes
                .flowOn(Dispatchers.Main)
                .collect {
                    updateItems(it)
                }
        }
    }

    override fun onItemClick(item: Any?) {
        // no-op
    }

    override fun getActions(): List<String> = listOf(
        "Play",
        "Download"
    )

    override fun onActionClicked(position: Int, item: Any) {
        val episode = item as? Episode? ?: return
        when (position) {
            0 -> {
                Log.i(TAG, "onActionClicked: Play > $episode")
            }
            1 -> {
                Log.i(TAG, "onActionClicked: Download > $episode")
            }
        }
    }

    private val TAG = "EpisodesListScreen"

}