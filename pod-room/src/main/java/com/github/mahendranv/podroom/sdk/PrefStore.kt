package com.github.mahendranv.podroom.sdk

import android.content.Context
import android.content.SharedPreferences

class PrefStore(
    private val context: Context
) {

    private val prefs = context.getSharedPreferences("pod_room", Context.MODE_PRIVATE)

    private val editor: SharedPreferences.Editor
        get() = prefs.edit()


    /**
     * Pointer to entry in player queue.
     */
    var currentEpisode: Long
        set(value) = editor.putLong(KEY_CURRENT_EPISODE_ID, value).apply()
        get() = prefs.getLong(KEY_CURRENT_EPISODE_ID, PlayerStore.POINTER_NONE)

    companion object {

        private const val KEY_CURRENT_EPISODE_ID = "current_episode"
    }
}