package com.zhufucdev.motion_emulator.ui

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.preference.EditTextPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreferenceCompat
import com.zhufucdev.motion_emulator.R
import com.zhufucdev.motion_emulator.databinding.ActivitySettingsBinding
import com.zhufucdev.motion_emulator.plugin.Plugins

class SettingsActivity : AppCompatActivity(),
    PreferenceFragmentCompat.OnPreferenceStartFragmentCallback {

    private lateinit var binding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.appBarToolbar)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.settings, HeaderFragment())
                .commit()
        } else {
            title = savedInstanceState.getCharSequence("settingsActivityTitle")
        }

        supportFragmentManager.addOnBackStackChangedListener {
            if (supportFragmentManager.backStackEntryCount == 0) {
                supportActionBar?.setTitle(R.string.title_activity_settings)
            }
        }

        binding.appBarToolbar.setNavigationOnClickListener {
            if (supportFragmentManager.backStackEntryCount > 0) {
                supportFragmentManager.popBackStack()
                return@setNavigationOnClickListener
            }
            finish()
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onPreferenceStartFragment(
        caller: PreferenceFragmentCompat,
        pref: Preference
    ): Boolean {
        val args = pref.extras
        val fragment = supportFragmentManager.fragmentFactory
            .instantiate(classLoader, pref.fragment!!)
        fragment.arguments = args
        fragment.setTargetFragment(caller, 0)
        supportFragmentManager.beginTransaction()
            .replace(R.id.settings, fragment)
            .setTransition(0x1003)
            .addToBackStack(null)
            .commit()
        title = pref.title
        return true
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putCharSequence("settingsActivityTitle", title)
    }

    override fun onSupportNavigateUp(): Boolean {
        return if (supportFragmentManager.popBackStackImmediate()) true
        else super.onSupportNavigateUp()
    }

    override fun setTitle(titleId: Int) {
        supportActionBar?.setTitle(titleId)
        super.setTitle(titleId)
    }

    override fun setTitle(title: CharSequence?) {
        supportActionBar?.title = title
        super.setTitle(title)
    }

    // Inner Fragment: Header
    class HeaderFragment : M3PreferenceFragment() {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.header_preferences, rootKey)
        }
    }

    // Inner Fragment: Maps
    class MapsFragment : M3PreferenceFragment() {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.maps_preferences, rootKey)
        }
    }

    // Inner Fragment: Naming
    class NamingFragment : M3PreferenceFragment() {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.naming_preferences, rootKey)
            init()
        }

        private fun init() {
            val customizeTimeFormat = findPreference<SwitchPreferenceCompat>("customize_time_format")!!
            val timeFormat = findPreference<EditTextPreference>("time_format")!!
            timeFormat.isEnabled = customizeTimeFormat.isChecked
            customizeTimeFormat.setOnPreferenceChangeListener { _, newValue ->
                timeFormat.isEnabled = newValue as Boolean
                true
            }
        }
    }

    // Inner Fragment: Emulation
    class EmulationFragment : M3PreferenceFragment() {
        private var changed = false

        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.emulation_preferences, rootKey)
            init()
        }

        private fun init() {
            for (i in 0 until preferenceScreen.preferenceCount) {
                val pref = preferenceScreen.getPreference(i)
                pref.setOnPreferenceClickListener {
                    changed = true
                    true
                }
            }

            val providerPort = findPreference<EditTextPreference>("provider_port")!!
            providerPort.setOnBindEditTextListener { input ->
                input.inputType = android.text.InputType.TYPE_CLASS_NUMBER
                input.addTextChangedListener(object : TextWatcher {
                    override fun afterTextChanged(s: Editable?) {
                        val value = s?.toString()?.toIntOrNull()
                        if (value == null || !isValidPort(value)) {
                            input.error = getString(R.string.text_input_invalid)
                        }
                    }
                    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
                })
            }
            providerPort.setOnPreferenceChangeListener { _, newValue ->
                val port = newValue.toString().toIntOrNull()
                val valid = port != null && isValidPort(port)
                changed = changed || !valid
                valid
            }
        }

        private fun isValidPort(port: Int): Boolean = port in 0x400..0xFFFF

        override fun onDetach() {
            super.onDetach()
            if (changed) {
                Plugins.notifySettingsChanged(requireContext())
            }
        }
    }
}
