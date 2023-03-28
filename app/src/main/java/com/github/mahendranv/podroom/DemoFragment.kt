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
        "add_podcast"
    )

    private val clickables = arrayOf(
        "view_podcasts",
        "view_episodes",
        "add_preset_podcasts"
    )

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)

        findPreference<Preference>("version")?.setSummaryProvider {
            PodRoom.Version + "/" + PodRoom.VersionCode
        }

        for (k in keys) {
            findPreference<Preference>(k)?.onPreferenceChangeListener = this
        }

        for (k in clickables) {
            findPreference<Preference>(k)?.onPreferenceClickListener = this
        }
    }

    override fun onPreferenceChange(preference: Preference, newValue: Any?): Boolean {
        if (newValue == null) return false
        when (preference.key) {
            "add_podcast" -> {
                viewModel.addPodcast(newValue as String)
            }
        }
        return false
    }

    override fun onPreferenceClick(preference: Preference): Boolean {
        when (preference.key) {
            "view_podcasts" -> {
                findNavController().navigate(R.id.action_demoFragment_to_podcastsListFragment)
            }
            "view_episodes" -> {
                findNavController().navigate(
                    R.id.action_demoFragment_to_episodesListScreen,
                    EpisodesListScreen.prepareArgs()
                )
            }
            "add_preset_podcasts" -> {
                Toast.makeText(requireContext(), "Adding podcasts", Toast.LENGTH_SHORT).show()
                arrayOf(
                    "https://feed.pod.co/the-podcast-lab",
                    "https://anchor.fm/s/e337170/podcast/rss",
                    "https://fragmentedpodcast.com/feed/"
                ).forEach {
                    viewModel.addPodcast(it)
                }
            }
        }
        return true
    }
}