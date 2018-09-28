package com.rascarlo.aurdroid;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity
        implements SearchFragment.SearchFragmentCallback, AurPackageDetailsFragment.AurPackageDetailsFragmentCallback {

    private static final String AUR_DIALOG_FRAGMENT_TAG = "aur_dialog_fragment_tag";
    private static final String AUR_PACKAGE_DETAILS_FRAGMENT_TAG = "aur_package_details_fragment_tag";
    private FragmentManager fragmentManager;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        fragmentManager = getSupportFragmentManager();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        int nightMode = sharedPreferences.getInt(getResources().getString(R.string.key_night_mode),
                AppCompatDelegate.MODE_NIGHT_YES);
        AppCompatDelegate.setDefaultNightMode(nightMode);
        setTheme(R.style.AppTheme_NoActionBar);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (findViewById(R.id.main_fragment_container) != null) {
            if (savedInstanceState != null) {
                return;
            }
            SearchFragment searchFragment = new SearchFragment();
            fragmentManager.beginTransaction()
                    .add(R.id.main_fragment_container, searchFragment)
                    .commit();
        }
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        /*
          app share and rate group
          hide on AurPackageDetailsFragment
         */
        MenuItem menuItem = menu.findItem(R.id.menu_main_app_group);
        Fragment fragmentByTag = fragmentManager.findFragmentByTag(AUR_PACKAGE_DETAILS_FRAGMENT_TAG);
        menuItem.setVisible(fragmentByTag == null);
        // night mode
        switch (AppCompatDelegate.getDefaultNightMode()) {
            case AppCompatDelegate.MODE_NIGHT_AUTO:
                menu.findItem(R.id.menu_main_action_night_mode_auto).setChecked(true);
                break;
            case AppCompatDelegate.MODE_NIGHT_YES:
                menu.findItem(R.id.menu_main_action_night_mode_night).setChecked(true);
                break;
            case AppCompatDelegate.MODE_NIGHT_NO:
                menu.findItem(R.id.menu_main_action_night_mode_day).setChecked(true);
                break;
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // night mode auto
            case R.id.menu_main_action_night_mode_auto:
                storeApplyNightMode(AppCompatDelegate.MODE_NIGHT_AUTO);
                break;
            // night mode night
            case R.id.menu_main_action_night_mode_night:
                storeApplyNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                break;
            // night mode day
            case R.id.menu_main_action_night_mode_day:
                storeApplyNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                break;
            // app rate
            case R.id.menu_main_app_rate:
                Intent intentRate = new Intent(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + getPackageName())));
                try {
                    startActivity(intentRate);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(this, getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
                }
                return true;
            // app share
            case R.id.menu_main_app_share:
                Intent intentShare = new Intent();
                intentShare.setAction(Intent.ACTION_SEND);
                intentShare.putExtra(Intent.EXTRA_TEXT, "http://play.google.com/store/apps/details?id=" + getPackageName());
                intentShare.setType("text/plain");
                try {
                    startActivity(intentShare);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(this, getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
                }
                return true;
            // app source
            case R.id.menu_main_app_source:
                Intent intentSource = new Intent(new Intent(Intent.ACTION_VIEW, Uri.parse(getResources().getString(R.string.github_link))));
                try {
                    startActivity(intentSource);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(this, getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void storeApplyNightMode(int nightMode) {
        if (nightMode != AppCompatDelegate.getDefaultNightMode()) {
            sharedPreferences.edit()
                    .putInt(getResources().getString(R.string.key_night_mode), nightMode)
                    .apply();
            AppCompatDelegate.setDefaultNightMode(nightMode);
            recreate();
        }
    }

    private void inflateDetailsFragment(String packageName) {
        AurPackageDetailsFragment aurPackageDetailsFragment = AurPackageDetailsFragment.newInstance(packageName);
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.fragment_enter,
                R.anim.fragment_exit,
                R.anim.fragment_pop_enter,
                R.anim.fragment_pop_exit);
        fragmentTransaction.replace(R.id.main_fragment_container, aurPackageDetailsFragment, AUR_PACKAGE_DETAILS_FRAGMENT_TAG);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void inflateDialogFragment(String title, String message) {
        AurDroidDialogFragment aurDroidDialogFragment = AurDroidDialogFragment.newInstance(title, message);
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        aurDroidDialogFragment.show(fragmentTransaction, AUR_DIALOG_FRAGMENT_TAG);
    }

    // search fragment callbacks
    @Override
    public void onSearchFragmentNetworkInfoNotConnected(String searchKeyword, String searchQuerySearchBy) {
        inflateDialogFragment(getString(R.string.no_internet_connection),
                String.format(getString(R.string.formatted_keyword_field), searchKeyword, searchQuerySearchBy));
    }

    @Override
    public void onSearchFragmentOnItemClicked(String packageName) {
        inflateDetailsFragment(packageName);
    }

    @Override
    public void onSearchFragmentOnResponseNotSuccessful(String searchKeyword, String searchQuerySearchBy) {
        inflateDialogFragment(getString(R.string.response_not_successful),
                String.format(getString(R.string.formatted_keyword_field), searchKeyword, searchQuerySearchBy));
    }

    @Override
    public void onSearchFragmentOnResponseEmptyResults(String searchKeyword, String searchQuerySearchBy) {
        inflateDialogFragment(getString(R.string.no_packages_found),
                String.format(getString(R.string.formatted_keyword_field), searchKeyword, searchQuerySearchBy));
    }

    @Override
    public void onSearchFragmentOnResponseOnFailure(String searchKeyword, String searchQuerySearchBy, String message) {
        inflateDialogFragment(message, String.format(getString(R.string.formatted_keyword_field), searchKeyword, searchQuerySearchBy));
    }

    @Override
    public void onSearchFragmentOnResponseError(String searchQueryKeyword, String searchQuerySearchBy, String error) {
        inflateDialogFragment(TextUtils.equals(error.substring(error.length() - 1), ".") ?
                        error.substring(0, error.length() - 1)
                        : error,
                String.format(getString(R.string.formatted_keyword_field),
                        searchQueryKeyword,
                        searchQuerySearchBy));
    }

    // details fragment callbacks
    @Override
    public void onAurPackageDetailsFragmentOnResponseNotSuccessful(String responseAurPackageName) {
        inflateDialogFragment(getString(R.string.response_not_successful), responseAurPackageName);
    }

    @Override
    public void onAurPackageDetailsFragmentOnResponseEmptyResult(String responseAurPackageName) {
        inflateDialogFragment(getString(R.string.no_packages_found), responseAurPackageName);
    }

    @Override
    public void onAurPackageDetailsFragmentOnResponseOnFailure(String responseAurPackageName, String throwableMessage) {
        inflateDialogFragment(responseAurPackageName, throwableMessage);
    }

    @Override
    public void onAurPackageDetailsFragmentBrowseMaintainer(String aurPackageMaintainer) {
        SearchFragment searchFragment = SearchFragment.newInstanceSearchByMaintainer(aurPackageMaintainer);
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.fragment_enter,
                R.anim.fragment_exit,
                R.anim.fragment_pop_enter,
                R.anim.fragment_pop_exit);
        fragmentTransaction.replace(R.id.main_fragment_container, searchFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}
