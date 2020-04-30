package com.rascarlo.aurdroid

import android.content.SharedPreferences
import android.os.Bundle
import android.text.TextUtils
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.PreferenceManager

private lateinit var sharedPreferences: SharedPreferences

class MainActivity : AppCompatActivity(), SharedPreferences.OnSharedPreferenceChangeListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        setAppTheme(false)
        setContentView(R.layout.activity_main)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onResume() {
        super.onResume()
        PreferenceManager.getDefaultSharedPreferences(this)
            .registerOnSharedPreferenceChangeListener(this)
    }

    override fun onPause() {
        super.onPause()
        PreferenceManager.getDefaultSharedPreferences(this)
            .unregisterOnSharedPreferenceChangeListener(this)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        if (null != key && key.isNotEmpty()) {
            if (TextUtils.equals(getString(R.string.key_theme), key)) {
                setAppTheme(true)
            }
        }
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        menu.apply {
            when (AppCompatDelegate.getDefaultNightMode()) {
                AppCompatDelegate.MODE_NIGHT_YES -> this?.findItem(R.id.menu_main_theme_night)?.isChecked =
                    true
                AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM -> this?.findItem(R.id.menu_main_theme_follow_system)?.isChecked =
                    true
                AppCompatDelegate.MODE_NIGHT_NO -> this?.findItem(R.id.menu_main_theme_day)?.isChecked =
                    true
                else -> {
                    this?.findItem(R.id.menu_main_theme_night)?.isChecked = true
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                }
            }
        }
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        sharedPreferences.edit().apply {
            when (item.itemId) {
                R.id.menu_main_theme_night -> {
                    putString(
                        getString(R.string.key_theme),
                        getString(R.string.key_theme_night)
                    ).apply()
                }
                R.id.menu_main_theme_day -> {
                    putString(
                        getString(R.string.key_theme),
                        getString(R.string.key_theme_day)
                    ).apply()
                }
                R.id.menu_main_theme_follow_system -> {
                    putString(
                        getString(R.string.key_theme),
                        getString(R.string.key_theme_follow_system)
                    ).apply()
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setAppTheme(recreate: Boolean) {
        setNightMode(getNightModePreference(), recreate)
    }

    private fun getNightModePreference(): Int {
        val preferenceAppTheme = sharedPreferences.getString(
            getString(R.string.key_theme),
            getString(R.string.key_theme_default_value)
        )
        return when (preferenceAppTheme) {
            getString(R.string.key_theme_night) -> AppCompatDelegate.MODE_NIGHT_YES
            getString(R.string.key_theme_day) -> AppCompatDelegate.MODE_NIGHT_NO
            getString(R.string.key_theme_follow_system) -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
            else -> AppCompatDelegate.MODE_NIGHT_YES
        }
    }

    private fun setNightMode(nightMode: Int, recreate: Boolean) {
        AppCompatDelegate.setDefaultNightMode(nightMode)
        when {
            recreate -> {
                recreate()
            }
        }
    }
}
