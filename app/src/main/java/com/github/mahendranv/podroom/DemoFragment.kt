package com.github.mahendranv.podroom

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat

class DemoFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
    }
}