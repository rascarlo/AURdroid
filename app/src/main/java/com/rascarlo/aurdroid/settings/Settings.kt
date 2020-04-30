package com.rascarlo.aurdroid.settings

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.rascarlo.aurdroid.R

class Settings : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.settings, rootKey)
    }
}