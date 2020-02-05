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

package com.rascarlo.aurdroid.ui;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.material.textfield.TextInputEditText;
import androidx.fragment.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.RadioButton;

import com.rascarlo.aurdroid.R;
import com.rascarlo.aurdroid.callbacks.SearchFragmentCallback;
import com.rascarlo.aurdroid.databinding.FragmentSearchBinding;
import com.rascarlo.aurdroid.util.AurdroidConstants;
import com.rascarlo.aurdroid.util.AurdroidSharedPreferences;

public class SearchFragment extends Fragment {

    private Context context;
    private SearchFragmentCallback searchFragmentCallback;
    // search by
    private RadioButton radioButtonSearchByNameOrDescription;
    private RadioButton radioButtonSearchByName;
    private RadioButton radioButtonSearchByMaintainer;
    private RadioButton radioButtonSearchByDepends;
    private RadioButton radioButtonSearchByMakeDepends;
    private RadioButton radioButtonSearchByOptDepends;
    private RadioButton radioButtonSearchByCheckDepends;
    // sort
    private RadioButton radioButtonSortPackageName;
    private RadioButton radioButtonSortVotes;
    private RadioButton radioButtonSortPopularity;
    private RadioButton radioButtonSortLastUpdated;
    private RadioButton radioButtonSortFirstSubmitted;

    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        context = container.getContext();
        FragmentSearchBinding fragmentSearchBinding = FragmentSearchBinding.inflate(LayoutInflater.from(context), container, false);
        TextInputEditText textInputEditText = fragmentSearchBinding.fragmentSearchKeywordsLayout.searchKeywordsTextInputEditText;
        textInputEditText.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        textInputEditText.setOnEditorActionListener((editTextView, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                if (editTextView.getEditableText() != null
                        && !TextUtils.isEmpty(editTextView.getEditableText().toString().trim())) {
                    if (searchFragmentCallback != null) {
                        searchFragmentCallback.onSearchFragmentCallbackOnFabClicked(getSearchBy(),
                                getSort(),
                                editTextView.getEditableText().toString().trim());
                    }
                }
                InputMethodManager inputMethodManager = (InputMethodManager) editTextView.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                if (inputMethodManager != null)
                    inputMethodManager.hideSoftInputFromWindow(editTextView.getWindowToken(), 0);
                return true;
            }
            return false;
        });
        // search by
        radioButtonSearchByNameOrDescription = fragmentSearchBinding.fragmentSearchByLayout.searchByRadioButtonNameOrDescription;
        radioButtonSearchByName = fragmentSearchBinding.fragmentSearchByLayout.searchByRadioButtonName;
        radioButtonSearchByMaintainer = fragmentSearchBinding.fragmentSearchByLayout.searchByRadioButtonMaintainer;
        radioButtonSearchByDepends = fragmentSearchBinding.fragmentSearchByLayout.searchByRadioButtonDepends;
        radioButtonSearchByMakeDepends = fragmentSearchBinding.fragmentSearchByLayout.searchByRadioButtonMakeDepends;
        radioButtonSearchByOptDepends = fragmentSearchBinding.fragmentSearchByLayout.searchByRadioButtonOptDepends;
        radioButtonSearchByCheckDepends = fragmentSearchBinding.fragmentSearchByLayout.searchByRadioButtonCheckDepends;
        // sort
        radioButtonSortPackageName = fragmentSearchBinding.fragmentSearchSortLayout.searchRadioButtonPackageName;
        radioButtonSortVotes = fragmentSearchBinding.fragmentSearchSortLayout.searchRadioButtonVotes;
        radioButtonSortPopularity = fragmentSearchBinding.fragmentSearchSortLayout.searchRadioButtonPopularity;
        radioButtonSortLastUpdated = fragmentSearchBinding.fragmentSearchSortLayout.searchRadioButtonLastUpdated;
        radioButtonSortFirstSubmitted = fragmentSearchBinding.fragmentSearchSortLayout.searchRadioButtonFirstSubmitted;
        bindViews();
        return fragmentSearchBinding.getRoot();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof SearchFragmentCallback) {
            searchFragmentCallback = (SearchFragmentCallback) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement SearchFragmentCallback");
        }
    }

    @Override
    public void onResume() {
        bindViews();
        super.onResume();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        searchFragmentCallback = null;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_search, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_search_settings) {
            if (searchFragmentCallback != null) {
                searchFragmentCallback.onSearchFragmentCallbackOnMenuSettingsClicked();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void bindViews() {
        bindKeyWords();
        bindSort();
    }

    private void bindKeyWords() {
        if (context != null) {
            String sharedPreferenceSearchBy = AurdroidSharedPreferences.getSharedPreferenceString(context,
                    getString(R.string.key_search_by), getString(R.string.key_search_by_default_value));
            if (TextUtils.equals(sharedPreferenceSearchBy, getString(R.string.key_search_by_name_or_description))) {
                radioButtonSearchByNameOrDescription.setChecked(true);
            } else if (TextUtils.equals(sharedPreferenceSearchBy, getString(R.string.key_search_by_name))) {
                radioButtonSearchByName.setChecked(true);
            } else if (TextUtils.equals(sharedPreferenceSearchBy, getString(R.string.key_search_by_maintainer))) {
                radioButtonSearchByMaintainer.setChecked(true);
            } else if (TextUtils.equals(sharedPreferenceSearchBy, getString(R.string.key_search_by_depends))) {
                radioButtonSearchByDepends.setChecked(true);
            } else if (TextUtils.equals(sharedPreferenceSearchBy, getString(R.string.key_search_by_make_depends))) {
                radioButtonSearchByMakeDepends.setChecked(true);
            } else if (TextUtils.equals(sharedPreferenceSearchBy, getString(R.string.key_search_by_opt_depends))) {
                radioButtonSearchByOptDepends.setChecked(true);
            } else if (TextUtils.equals(sharedPreferenceSearchBy, getString(R.string.key_search_by_check_depends))) {
                radioButtonSearchByCheckDepends.setChecked(true);
            } else {
                radioButtonSearchByNameOrDescription.setChecked(true);
            }
        }
    }

    private void bindSort() {
        if (context != null) {
            String sharedPreferenceSort = AurdroidSharedPreferences.getSharedPreferenceString(context,
                    getString(R.string.key_sort), getString(R.string.key_sort_default_value));
            if (TextUtils.equals(sharedPreferenceSort, getString(R.string.key_sort_by_package_name))) {
                radioButtonSortPackageName.setChecked(true);
            } else if (TextUtils.equals(sharedPreferenceSort, getString(R.string.key_sort_by_votes))) {
                radioButtonSortVotes.setChecked(true);
            } else if (TextUtils.equals(sharedPreferenceSort, getString(R.string.key_sort_by_popularity))) {
                radioButtonSortPopularity.setChecked(true);
            } else if (TextUtils.equals(sharedPreferenceSort, getString(R.string.key_sort_by_last_updated))) {
                radioButtonSortLastUpdated.setChecked(true);
            } else if (TextUtils.equals(sharedPreferenceSort, getString(R.string.key_sort_by_first_submitted))) {
                radioButtonSortFirstSubmitted.setChecked(true);
            } else {
                radioButtonSortPackageName.setChecked(true);
            }
        }
    }

    private int getSearchBy() {
        if (radioButtonSearchByNameOrDescription.isChecked()) {
            return AurdroidConstants.SEARCH_BY_NAME_OR_DESCRIPTION;
        } else if (radioButtonSearchByName.isChecked()) {
            return AurdroidConstants.SEARCH_BY_NAME;
        } else if (radioButtonSearchByMaintainer.isChecked()) {
            return AurdroidConstants.SEARCH_BY_MAINTAINER;
        } else if (radioButtonSearchByDepends.isChecked()) {
            return AurdroidConstants.SEARCH_BY_DEPENDS;
        } else if (radioButtonSearchByMakeDepends.isChecked()) {
            return AurdroidConstants.SEARCH_BY_MAKE_DEPENDS;
        } else if (radioButtonSearchByOptDepends.isChecked()) {
            return AurdroidConstants.SEARCH_BY_OPT_DEPENDS;
        } else if (radioButtonSearchByCheckDepends.isChecked()) {
            return AurdroidConstants.SEARCH_BY_CHECK_DEPENDS;
        } else {
            return AurdroidConstants.SEARCH_BY_NAME_OR_DESCRIPTION;
        }
    }

    private int getSort() {
        if (radioButtonSortPackageName.isChecked()) {
            return AurdroidConstants.SORT_BY_PACKAGE_NAME;
        } else if (radioButtonSortVotes.isChecked()) {
            return AurdroidConstants.SORT_BY_VOTES;
        } else if (radioButtonSortPopularity.isChecked()) {
            return AurdroidConstants.SORT_BY_POPULARITY;
        } else if (radioButtonSortLastUpdated.isChecked()) {
            return AurdroidConstants.SORT_BY_LAST_UPDATED;
        } else if (radioButtonSortFirstSubmitted.isChecked()) {
            return AurdroidConstants.SORT_BY_FIRST_SUBMITTED;
        } else {
            return AurdroidConstants.SORT_BY_PACKAGE_NAME;
        }
    }
}
