package com.github.mahendranv.podroom

import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.github.mahendranv.podroom.screens.EpisodesListScreen

class DemoFragment : PreferenceFragmentCompat(), Preference.OnPreferenceChangeListener,
    Preference.OnPreferenceClickListener {

    private val viewModel by viewModels<DemoViewModel>()

    private val keys = arrayOf(
        R.string.add_podcast
    )

    private val clickables = arrayOf(
        R.string.view_podcasts,
        R.string.view_episodes,
        R.string.add_preset_podcasts,
        R.string.view_queue,
        R.string.clear_queue,
        R.string.view_downloads
    )

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)

        findPreference<Preference>("version")?.setSummaryProvider {
            PodRoom.Version + "/" + PodRoom.VersionCode
        }

        for (k in keys) {
            findPreference<Preference>(getString(k))?.onPreferenceChangeListener = this
        }

        for (k in clickables) {
            findPreference<Preference>(getString(k))?.onPreferenceClickListener = this
        }
    }

    override fun onPreferenceChange(preference: Preference, newValue: Any?): Boolean {
        if (newValue == null) return false
        when (preference.key) {
            getString(R.string.add_podcast) -> {
                viewModel.addPodcast(newValue as String)
            }
        }
        return false
    }

    override fun onPreferenceClick(preference: Preference): Boolean {
        when (preference.key) {
            getString(R.string.view_podcasts) -> {
                findNavController().navigate(R.id.action_demoFragment_to_podcastsListFragment)
            }
            getString(R.string.view_episodes) -> {
                findNavController().navigate(
                    R.id.action_demoFragment_to_episodesListScreen,
                    EpisodesListScreen.prepareArgs()
                )
            }
            getString(R.string.add_preset_podcasts) -> {
                Toast.makeText(requireContext(), "Adding podcasts", Toast.LENGTH_SHORT).show()
                arrayOf(
                    "https://feed.pod.co/the-podcast-lab",
                    "https://anchor.fm/s/e337170/podcast/rss",
                    "https://fragmentedpodcast.com/feed/"
                ).forEach {
                    viewModel.addPodcast(it)
                }
            }
            getString(R.string.clear_queue) -> {
                viewModel.clearAll()
            }
            getString(R.string.view_queue) -> {
                findNavController().navigate(R.id.action_demoFragment_to_playerQueueScreen)
            }
            getString(R.string.view_downloads) -> {
                findNavController().navigate(R.id.action_demoFragment_to_downloadsListScreen)
            }
        }
        return true
    }
}