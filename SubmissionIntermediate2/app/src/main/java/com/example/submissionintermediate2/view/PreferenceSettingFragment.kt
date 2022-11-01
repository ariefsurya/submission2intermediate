package com.example.submissionintermediate2.view

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.example.submissionintermediate2.R

class PreferenceSettingFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(bundle: Bundle?, s: String?) {
        addPreferencesFromResource(R.xml.preferences)
    }
}