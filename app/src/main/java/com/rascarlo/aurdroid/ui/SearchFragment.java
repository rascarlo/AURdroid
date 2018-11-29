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
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
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
    private RadioButton radioButtonSearchNameOrDescription;
    private RadioButton radioButtonSearchName;
    private RadioButton radioButtonSearchMaintainer;
    private RadioButton radioButtonSearchDepends;
    private RadioButton radioButtonSearchMakeDepends;
    private RadioButton radioButtonSearchOptDepends;
    private RadioButton radioButtonSearchCheckDepends;
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
        EditText editText = fragmentSearchBinding.fragmentSearchKeywordsLayout.searchKeywordsTextInputEditText;
        editText.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        editText.setOnEditorActionListener((editTextView, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                if (editTextView.getEditableText() != null
                        && !TextUtils.isEmpty(editTextView.getEditableText().toString().trim())) {
                    if (searchFragmentCallback != null) {
                        searchFragmentCallback.onSearchFragmentCallbackOnFabClicked(getField(),
                                getSort(),
                                editTextView.getEditableText().toString());
                    }
                }
                InputMethodManager inputMethodManager = (InputMethodManager) editTextView.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                if (inputMethodManager != null)
                    inputMethodManager.hideSoftInputFromWindow(editTextView.getWindowToken(), 0);
                return true;
            }
            return false;
        });
        // fields
        radioButtonSearchNameOrDescription = fragmentSearchBinding.fragmentSearchFieldLayout.searchFieldRadioButtonNameOrDescription;
        radioButtonSearchName = fragmentSearchBinding.fragmentSearchFieldLayout.searchFieldRadioButtonName;
        radioButtonSearchMaintainer = fragmentSearchBinding.fragmentSearchFieldLayout.searchFieldRadioButtonMaintainer;
        radioButtonSearchDepends = fragmentSearchBinding.fragmentSearchFieldLayout.searchFieldRadioButtonDepends;
        radioButtonSearchMakeDepends = fragmentSearchBinding.fragmentSearchFieldLayout.searchFieldRadioButtonMakeDepends;
        radioButtonSearchOptDepends = fragmentSearchBinding.fragmentSearchFieldLayout.searchFieldRadioButtonOptDepends;
        radioButtonSearchCheckDepends = fragmentSearchBinding.fragmentSearchFieldLayout.searchFieldRadioButtonCheckDepends;
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
        switch (item.getItemId()) {
            case R.id.menu_search_settings:
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
            String sharedPreferenceField = AurdroidSharedPreferences.getSharedPreferenceString(context,
                    getString(R.string.key_search_field), getString(R.string.key_search_field_default_value));
            if (TextUtils.equals(sharedPreferenceField, getString(R.string.key_search_field_name_or_description))) {
                radioButtonSearchNameOrDescription.setChecked(true);
            } else if (TextUtils.equals(sharedPreferenceField, getString(R.string.key_search_field_name))) {
                radioButtonSearchName.setChecked(true);
            } else if (TextUtils.equals(sharedPreferenceField, getString(R.string.key_search_field_maintainer))) {
                radioButtonSearchMaintainer.setChecked(true);
            } else if (TextUtils.equals(sharedPreferenceField, getString(R.string.key_search_field_depends))) {
                radioButtonSearchDepends.setChecked(true);
            } else if (TextUtils.equals(sharedPreferenceField, getString(R.string.key_search_field_make_depends))) {
                radioButtonSearchMakeDepends.setChecked(true);
            } else if (TextUtils.equals(sharedPreferenceField, getString(R.string.key_search_field_opt_depends))) {
                radioButtonSearchOptDepends.setChecked(true);
            } else if (TextUtils.equals(sharedPreferenceField, getString(R.string.key_search_field_check_depends))) {
                radioButtonSearchCheckDepends.setChecked(true);
            } else {
                radioButtonSearchNameOrDescription.setChecked(true);
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

    private int getField() {
        if (radioButtonSearchNameOrDescription.isChecked()) {
            return AurdroidConstants.SEARCH_PARAMETER_NAME_OR_DESCRIPTION;
        } else if (radioButtonSearchName.isChecked()) {
            return AurdroidConstants.SEARCH_PARAMETER_NAME;
        } else if (radioButtonSearchMaintainer.isChecked()) {
            return AurdroidConstants.SEARCH_PARAMETER_MAINTAINER;
        } else if (radioButtonSearchDepends.isChecked()) {
            return AurdroidConstants.SEARCH_PARAMETER_DEPENDS;
        } else if (radioButtonSearchMakeDepends.isChecked()) {
            return AurdroidConstants.SEARCH_PARAMETER_MAKE_DEPENDS;
        } else if (radioButtonSearchOptDepends.isChecked()) {
            return AurdroidConstants.SEARCH_PARAMETER_OPT_DEPENDS;
        } else if (radioButtonSearchCheckDepends.isChecked()) {
            return AurdroidConstants.SEARCH_PARAMETER_CHECK_DEPENDS;
        } else {
            return AurdroidConstants.SEARCH_PARAMETER_NAME_OR_DESCRIPTION;
        }
    }

    private int getSort() {
        if (radioButtonSortPackageName.isChecked()) {
            return AurdroidConstants.SEARCH_RESULT_SORT_BY_PACKAGE_NAME;
        } else if (radioButtonSortVotes.isChecked()) {
            return AurdroidConstants.SEARCH_RESULT_SORT_BY_VOTES;
        } else if (radioButtonSortPopularity.isChecked()) {
            return AurdroidConstants.SEARCH_RESULT_SORT_BY_POPULARITY;
        } else if (radioButtonSortLastUpdated.isChecked()) {
            return AurdroidConstants.SEARCH_RESULT_SORT_BY_LAST_UPDATED;
        } else if (radioButtonSortFirstSubmitted.isChecked()) {
            return AurdroidConstants.SEARCH_RESULT_SORT_BY_FIRST_SUBMITTED;
        } else {
            return AurdroidConstants.SEARCH_RESULT_SORT_BY_PACKAGE_NAME;
        }
    }
}
