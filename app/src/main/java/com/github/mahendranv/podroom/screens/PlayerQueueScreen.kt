package com.github.mahendranv.podroom.screens

import android.util.Log
import android.widget.Toast
import com.github.mahendranv.podroom.list.GenericListFragment

class PlayerQueueScreen : GenericListFragment<Any>() {

    override fun getTitle(): CharSequence = "Player"

    override fun onItemClick(item: Any) {
        Log.i(TAG, "onItemClick: ")
        Toast.makeText(requireContext(), "Playing episode:", Toast.LENGTH_SHORT).show()
    }

    override fun getActions(): List<String> = listOf(
        "Remove"
    )

    override fun prepareUiString(item: Any): CharSequence {
        return item.toString()
    }

    override fun onActionClicked(position: Int, item: Any) {
        // no-op
    }

    companion object {
        private const val TAG = "PlayerQueueScreen"
    }
}