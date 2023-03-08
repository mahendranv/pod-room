package com.github.mahendranv.podroom.screens

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.github.mahendranv.podroom.DemoViewModel
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
        TODO("Not yet implemented")
    }

    override fun onItemLongClick(item: Any?): Boolean {
        TODO("Not yet implemented")
    }
}