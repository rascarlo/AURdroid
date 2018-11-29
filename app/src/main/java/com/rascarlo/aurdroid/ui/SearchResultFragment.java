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

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.rascarlo.aurdroid.R;
import com.rascarlo.aurdroid.adapters.SearchResultAdapter;
import com.rascarlo.aurdroid.api.model.SearchResult;
import com.rascarlo.aurdroid.callbacks.SearchResultFragmentCallback;
import com.rascarlo.aurdroid.databinding.FragmentSearchResultBinding;
import com.rascarlo.aurdroid.util.AurdroidConstants;
import com.rascarlo.aurdroid.viewmodels.SearchViewModel;
import com.rascarlo.aurdroid.viewmodels.SearchViewModelFactory;

import java.util.Collections;
import java.util.List;


public class SearchResultFragment extends Fragment {
    private static final String BUNDLE_PARAMETER_FIELD = "bundle_field_parameter";
    private static final String BUNDLE_PARAMETER_SORT = "bundle_sort_parameter";
    private static final String BUNDLE_PARAMETER__QUERY = "bundle_query_parameter";
    private static final String SAVED_INSTANCE_SORT = "SAVED_INSTANCE_SORT";
    private int bundleField;
    private String bundleQuery;
    private SearchResultAdapter resultAdapter;
    private SearchResultFragmentCallback searchResultFragmentCallback;
    private List<SearchResult> searchResultList;
    private int sortOder;

    public SearchResultFragment() {
        // Required empty public constructor
    }

    public static SearchResultFragment newInstance(int bundleFieldParameter,
                                                   int bundleSortParameter,
                                                   String bundleQuery) {
        SearchResultFragment fragment = new SearchResultFragment();
        Bundle args = new Bundle();
        args.putInt(BUNDLE_PARAMETER_FIELD, bundleFieldParameter);
        args.putInt(BUNDLE_PARAMETER_SORT, bundleSortParameter);
        args.putString(BUNDLE_PARAMETER__QUERY, bundleQuery);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof SearchResultFragmentCallback) {
            searchResultFragmentCallback = (SearchResultFragmentCallback) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement SearchResultFragmentCallback");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            bundleField = getArguments().getInt(BUNDLE_PARAMETER_FIELD);
            int bundleSort = getArguments().getInt(BUNDLE_PARAMETER_SORT);
            bundleQuery = getArguments().getString(BUNDLE_PARAMETER__QUERY);
            if (savedInstanceState == null) {
                sortOder = bundleSort;
            }
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Context context = container.getContext();
        FragmentSearchResultBinding fragmentSearchResultBinding = FragmentSearchResultBinding.inflate(inflater, container, false);
        SearchViewModelFactory searchViewModelFactory = new SearchViewModelFactory(bundleField, bundleQuery);
        SearchViewModel searchViewModel = ViewModelProviders.of(this, searchViewModelFactory).get(SearchViewModel.class);
        resultAdapter = new SearchResultAdapter(searchResult -> {
            if (searchResultFragmentCallback != null) {
                if (searchResult != null && searchResult.getName() != null && !TextUtils.isEmpty(searchResult.getName())) {
                    searchResultFragmentCallback.onSearchResultFragmentCallbackOnResultClicked(searchResult.getName());
                }
            }
        });
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        RecyclerView recyclerView = fragmentSearchResultBinding.fragmentSearchResultRecyclerView;
        recyclerView.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        searchViewModel.getFilesLiveData().observe(this, aurSearch -> {
            if (aurSearch != null) {
                this.searchResultList = aurSearch.getResults();
                submitSearchResultList(false);
            }
        });
        recyclerView.setAdapter(resultAdapter);
        return fragmentSearchResultBinding.getRoot();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        searchResultFragmentCallback = null;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putInt(SAVED_INSTANCE_SORT, sortOder);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        if (savedInstanceState != null && savedInstanceState.getInt(SAVED_INSTANCE_SORT) != -1) {
            sortOder = savedInstanceState.getInt(SAVED_INSTANCE_SORT);
        }
        super.onViewStateRestored(savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_search_result, menu);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        boolean listIsNotEmpty = searchResultList != null && !searchResultList.isEmpty();
        menu.findItem(R.id.menu_search_result_item_sort).setVisible(listIsNotEmpty);
        menu.findItem(R.id.menu_search_result_item_sort_by_package_name).setVisible(listIsNotEmpty);
        menu.findItem(R.id.menu_search_result_item_sort_by_votes).setVisible(listIsNotEmpty);
        menu.findItem(R.id.menu_search_result_item_sort_by_popularity).setVisible(listIsNotEmpty);
        menu.findItem(R.id.menu_search_result_item_sort_by_last_updated).setVisible(listIsNotEmpty);
        menu.findItem(R.id.menu_search_result_item_sort_by_first_submitted).setVisible(listIsNotEmpty);
        switch (sortOder) {
            case AurdroidConstants.SEARCH_RESULT_SORT_BY_PACKAGE_NAME:
                menu.findItem(R.id.menu_search_result_item_sort_by_package_name).setChecked(true);
                break;
            case AurdroidConstants.SEARCH_RESULT_SORT_BY_VOTES:
                menu.findItem(R.id.menu_search_result_item_sort_by_votes).setChecked(true);
                break;
            case AurdroidConstants.SEARCH_RESULT_SORT_BY_POPULARITY:
                menu.findItem(R.id.menu_search_result_item_sort_by_popularity).setChecked(true);
                break;
            case AurdroidConstants.SEARCH_RESULT_SORT_BY_LAST_UPDATED:
                menu.findItem(R.id.menu_search_result_item_sort_by_last_updated).setChecked(true);
                break;
            case AurdroidConstants.SEARCH_RESULT_SORT_BY_FIRST_SUBMITTED:
                menu.findItem(R.id.menu_search_result_item_sort_by_first_submitted).setChecked(true);
                break;
            default:
                menu.findItem(R.id.menu_search_result_item_sort_by_package_name).setChecked(true);
                break;
        }
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_search_result_item_sort_by_package_name:
                sortOder = AurdroidConstants.SEARCH_RESULT_SORT_BY_PACKAGE_NAME;
                submitSearchResultList(true);
                break;
            case R.id.menu_search_result_item_sort_by_votes:
                sortOder = AurdroidConstants.SEARCH_RESULT_SORT_BY_VOTES;
                submitSearchResultList(true);
                break;
            case R.id.menu_search_result_item_sort_by_popularity:
                sortOder = AurdroidConstants.SEARCH_RESULT_SORT_BY_POPULARITY;
                submitSearchResultList(true);
                break;
            case R.id.menu_search_result_item_sort_by_last_updated:
                sortOder = AurdroidConstants.SEARCH_RESULT_SORT_BY_LAST_UPDATED;
                submitSearchResultList(true);
                break;
            case R.id.menu_search_result_item_sort_by_first_submitted:
                sortOder = AurdroidConstants.SEARCH_RESULT_SORT_BY_FIRST_SUBMITTED;
                submitSearchResultList(true);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void submitSearchResultList(boolean notifyItemRangeChanged) {
        if (searchResultList != null && !searchResultList.isEmpty()) {
            resultAdapter.submitList(getSortedList(searchResultList));
            if (notifyItemRangeChanged) {
                resultAdapter.notifyItemRangeChanged(0, resultAdapter.getItemCount());
            }
        }
    }

    private List<SearchResult> getSortedList(List<SearchResult> searchResultList) {
        switch (sortOder) {
            case AurdroidConstants.SEARCH_RESULT_SORT_BY_PACKAGE_NAME:
                Collections.sort(searchResultList, (o1, o2) -> o1.getName().compareToIgnoreCase(o2.getName()));
                break;
            case AurdroidConstants.SEARCH_RESULT_SORT_BY_VOTES:
                Collections.sort(searchResultList, (o1, o2) -> Long.compare(Long.parseLong(o2.getNumVotes()), Long.parseLong(o1.getNumVotes())));
                break;
            case AurdroidConstants.SEARCH_RESULT_SORT_BY_POPULARITY:
                Collections.sort(searchResultList, (o1, o2) -> Double.compare(Double.parseDouble(o2.getPopularity()), Double.parseDouble(o1.getPopularity())));
                break;
            case AurdroidConstants.SEARCH_RESULT_SORT_BY_LAST_UPDATED:
                Collections.sort(searchResultList, (o1, o2) -> Long.compare(Long.parseLong(o2.getLastModified()), Long.parseLong(o1.getLastModified())));
                break;
            case AurdroidConstants.SEARCH_RESULT_SORT_BY_FIRST_SUBMITTED:
                Collections.sort(searchResultList, (o1, o2) -> Long.compare(Long.parseLong(o1.getFirstSubmitted()), Long.parseLong(o2.getFirstSubmitted())));
                break;
            default:
                Collections.sort(searchResultList, (o1, o2) -> o1.getName().compareToIgnoreCase(o2.getName()));
                break;
        }
        return searchResultList;
    }
}
