package com.github.mahendranv.podroom.screens

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.github.mahendranv.podroom.DemoViewModel
import com.github.mahendranv.podroom.entity.Download
import com.github.mahendranv.podroom.list.GenericListFragment

class DownloadsListScreen : GenericListFragment<Download>() {

    private val viewModel by viewModels<DemoViewModel>()

    override fun getTitle() = "Downloads"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fetchItems(viewModel.downloads)
        Toast.makeText(
            requireContext(),
            "Tap on any item to set random progress.",
            Toast.LENGTH_SHORT
        ).show()
    }

    override fun onItemClick(item: Download) {
        // set some random progress.
        viewModel.updateDownloadProgress(item.id)
    }

    override fun getActions(): List<String> = listOf(
        "Mark complete",
        "Delete"
    )

    override fun prepareUiString(t: Download): CharSequence {
        return "id: ${t.id}\t[${t.progress}%]\n${t.filePath}"
    }

    override fun onActionClicked(position: Int, item: Download) {
        when (position) {
            0 -> {
                viewModel.markDownloadComplete(item.id)
            }
            1 -> {
                viewModel.deleteDownload(item.id)
            }
        }
    }
}