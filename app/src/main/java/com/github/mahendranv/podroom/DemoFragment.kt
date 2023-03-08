package com.github.mahendranv.podroom

import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat

class DemoFragment : PreferenceFragmentCompat(), Preference.OnPreferenceChangeListener,
    Preference.OnPreferenceClickListener {

    private val viewModel by viewModels<DemoViewModel>()

    private val keys = arrayOf(
        "add_podcast"
    )

    private val clickables = arrayOf(
        "view_podcasts",
        "view_episodes"
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
                findNavController().navigate(R.id.action_demoFragment_to_episodesListScreen)
            }
        }
        return true
    }
}