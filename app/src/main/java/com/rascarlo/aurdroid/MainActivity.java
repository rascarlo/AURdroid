/*
 *     Copyright (C) rascarlo  rascarlo@gmail.com
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.rascarlo.aurdroid;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.rascarlo.aurdroid.callbacks.InfoResultFragmentCallback;
import com.rascarlo.aurdroid.callbacks.SearchFragmentCallback;
import com.rascarlo.aurdroid.callbacks.SearchResultFragmentCallback;
import com.rascarlo.aurdroid.ui.InfoResultFragment;
import com.rascarlo.aurdroid.ui.SearchFragment;
import com.rascarlo.aurdroid.ui.SearchResultFragment;
import com.rascarlo.aurdroid.ui.SettingsFragment;
import com.rascarlo.aurdroid.util.AurdroidSharedPreferences;

public class MainActivity extends AppCompatActivity
        implements SearchFragmentCallback,
        SearchResultFragmentCallback,
        InfoResultFragmentCallback,
        SharedPreferences.OnSharedPreferenceChangeListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setAppTheme(false);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (savedInstanceState != null) {
            return;
        }
        SearchFragment searchFragment = new SearchFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.content_main_frame_container, searchFragment);
        fragmentTransaction.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        PreferenceManager.getDefaultSharedPreferences(MainActivity.this).registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        PreferenceManager.getDefaultSharedPreferences(MainActivity.this).unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        switch (getAppTheme()) {
            case R.style.AppThemeDark:
                menu.findItem(R.id.menu_main_action_theme_dark).setChecked(true);
                break;
            case R.style.AppThemeBlack:
                menu.findItem(R.id.menu_main_action_theme_black).setChecked(true);
                break;
            case R.style.AppThemeLight:
                menu.findItem(R.id.menu_main_action_theme_light).setChecked(true);
                break;
            default:
                menu.findItem(R.id.menu_main_action_theme_dark).setChecked(true);
                break;
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_main_action_theme_dark:
                AurdroidSharedPreferences.setSharedPreferenceString(MainActivity.this,
                        getString(R.string.key_theme),
                        getString(R.string.key_theme_dark));
                break;
            case R.id.menu_main_action_theme_black:
                AurdroidSharedPreferences.setSharedPreferenceString(MainActivity.this,
                        getString(R.string.key_theme),
                        getString(R.string.key_theme_black));
                break;
            case R.id.menu_main_action_theme_light:
                AurdroidSharedPreferences.setSharedPreferenceString(MainActivity.this,
                        getString(R.string.key_theme),
                        getString(R.string.key_theme_light));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key != null && !TextUtils.isEmpty(key)) {
            if (TextUtils.equals(getString(R.string.key_theme), key)) {
                setAppTheme(true);
            }
        }
    }

    @Override
    public void onSearchFragmentCallbackOnFabClicked(int field, int sort, String query) {
        if (query != null && !TextUtils.isEmpty(query)) {
            SearchResultFragment searchResultFragment = SearchResultFragment.newInstance(field, sort, query);
            replaceFragment(searchResultFragment);
        }
    }

    @Override
    public void onSearchFragmentCallbackOnMenuSettingsClicked() {
        SettingsFragment settingsFragment = new SettingsFragment();
        replaceFragment(settingsFragment);
    }

    @Override
    public void onSearchResultFragmentCallbackOnResultClicked(String pkgname) {
        InfoResultFragment detailsFragment = InfoResultFragment.newInstance(pkgname);
        replaceFragment(detailsFragment);
    }

    @Override
    public void onInfoResultFragmentCallbackViewPkgbuildClicked(Uri viewPkgbuildUri) {
        if (viewPkgbuildUri != null && !TextUtils.isEmpty(viewPkgbuildUri.toString()))
            startIntentViewUri(viewPkgbuildUri);
    }

    @Override
    public void onInfoResultFragmentCallbackViewChangesClicked(Uri viewChangesUri) {
        if (viewChangesUri != null && !TextUtils.isEmpty(viewChangesUri.toString()))
            startIntentViewUri(viewChangesUri);
    }

    @Override
    public void onInfoResultFragmentCallbackOpenInBrowserClicked(Uri infoResultUri) {
        if (infoResultUri != null && !TextUtils.isEmpty(infoResultUri.toString()))
            startIntentViewUri(infoResultUri);
    }

    @Override
    public void onInfoResultFragmentCallbackShareClicked(Uri infoResultUri) {
        if (infoResultUri != null && !TextUtils.isEmpty(infoResultUri.toString())) {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_SEND);
            intent.putExtra(Intent.EXTRA_TEXT, infoResultUri.toString());
            intent.setType("text/plain");
            try {
                MainActivity.this.startActivity(intent);
            } catch (Exception e) {
                Toast.makeText(MainActivity.this, getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.content_main_frame_container, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void startIntentViewUri(Uri uri) {
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        try {
            MainActivity.this.startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(MainActivity.this, getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
        }
    }

    private void setAppTheme(boolean recreate) {
        setTheme(getAppTheme());
        if (recreate) recreate();
    }

    private int getAppTheme() {
        String sharedPreferenceTheme = AurdroidSharedPreferences.getSharedPreferenceString(MainActivity.this,
                getString(R.string.key_theme),
                getString(R.string.key_theme_default_value));
        if (TextUtils.equals(getString(R.string.key_theme_dark), sharedPreferenceTheme)) {
            return R.style.AppThemeDark;
        } else if (TextUtils.equals(getString(R.string.key_theme_black), sharedPreferenceTheme)) {
            return R.style.AppThemeBlack;
        } else if (TextUtils.equals(getString(R.string.key_theme_light), sharedPreferenceTheme)) {
            return R.style.AppThemeLight;
        } else {
            return R.style.AppThemeDark;
        }
    }
}
