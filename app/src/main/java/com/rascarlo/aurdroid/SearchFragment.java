package com.rascarlo.aurdroid;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.rascarlo.aurdroid.adapters.AurSearchResultAdapter;
import com.rascarlo.aurdroid.api.AurRpcApi;
import com.rascarlo.aurdroid.api.AurRpcService;
import com.rascarlo.aurdroid.api.model.aur.search.AurSearchObject;
import com.rascarlo.aurdroid.api.model.aur.search.AurSearchResult;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchFragment extends Fragment implements Callback<AurSearchObject>, AurSearchResultAdapter.AurSearchResultAdapterCallback {

    private SearchFragmentCallback searchFragmentCallback;
    private static final String LOG_TAG = SearchFragment.class.getSimpleName();
    private static final String BUNDLE_AUR_PACKAGE_MAINTAINER = "bundle_aur_package_maintainer";
    private static final String SAVED_INSTANCE_LINEAR_LAYOUT_MANAGER_STATE = "saved_instance_linear_layout_manager_state";
    private static final String SAVED_INSTANCE_SEARCH_KEYWORD = "saved_instance_search_keyword";
    // sort order
    private static final String SORT_ORDER_PACKAGE_NAME = "sort_order_package_name";
    private static final String SORT_ORDER_VOTES = "sort_order_votes";
    private static final String SORT_ORDER_POPULARITY = "sort_order_popularity";
    private static final String SORT_ORDER_MAINTAINER = "sort_order_maintainer";
    private static final String SORT_ORDER_LAST_MODIFIED = "sort_order_last_modified";
    private static final String SORT_ORDER_FIRST_SUBMITTED = "sort_order_first_submitted";
    private static final String SORT_ORDER_LAST_SUBMITTED = "sort_order_last_submitted";
    // search field
    private static final String SEARCH_BY_NAME_DESCRIPTION = "search_by_name_description";
    private static final String SEARCH_BY_PACKAGE_NAME = "search_by_package_name";
    private static final String SEARCH_BY_MAINTAINER = "search_by_maintainer";
    private Resources resources;
    // sort order
    private String searchSortOrder = SORT_ORDER_PACKAGE_NAME;
    // search by
    private String searchQuerySearchBy = SEARCH_BY_NAME_DESCRIPTION;
    // search keyword
    private String searchQueryKeyword;
    // retrofit
    private AurRpcApi aurRpcApi;
    private Call<AurSearchObject> call;
    private List<AurSearchResult> responseAurSearchResultList;
    // views
    private ProgressBar progressBar;
    private RecyclerView recyclerView;
    private TextView footerTextView;
    private ScrollView wikiLinearLayout;
    private LinearLayoutManager linearLayoutManager;
    private AurSearchResultAdapter aurSearchResultAdapter;
    private AurDroidUtils aurDroidUtils;
    private AurSearchObject aurSearchObject;
    private String bundleAurPackageMaintainer;

    public SearchFragment() {
    }

    /**
     * a new instance of the fragment
     * instantiated by the package details fragment
     * by selecting the menu item maintainer
     *
     * @param bundleAurPackageMaintainer: the keyword
     * @return a new search by maintainer with keyword bundleAurPackageMaintainer
     */
    public static SearchFragment newInstanceSearchByMaintainer(String bundleAurPackageMaintainer) {
        SearchFragment searchFragment = new SearchFragment();
        Bundle bundle = new Bundle();
        bundle.putString(BUNDLE_AUR_PACKAGE_MAINTAINER, bundleAurPackageMaintainer);
        searchFragment.setArguments(bundle);
        return searchFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            bundleAurPackageMaintainer = getArguments().getString(BUNDLE_AUR_PACKAGE_MAINTAINER);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setRetainInstance(true);
        setHasOptionsMenu(true);
        Context context = getActivity();
        resources = context.getResources();
        aurRpcApi = AurRpcService.getAurRpcApi();
        aurDroidUtils = new AurDroidUtils(context);
        View rootView = inflater.inflate(R.layout.fragment_search, container, false);
        progressBar = rootView.findViewById(R.id.fragment_search_progress_bar);
        recyclerView = rootView.findViewById(R.id.fragment_search_recycler_view);
        footerTextView = rootView.findViewById(R.id.fragment_search_footer);
        wikiLinearLayout = rootView.findViewById(R.id.fragment_search_wiki_main_container);
        linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                if (parent.getChildAdapterPosition(view) != parent.getAdapter().getItemCount() - 1) {
                    outRect.bottom = (int) (16 * Resources.getSystem().getDisplayMetrics().density);
                }
                outRect.top = (int) (0 * Resources.getSystem().getDisplayMetrics().density);
                outRect.left = (int) (0 * Resources.getSystem().getDisplayMetrics().density);
                outRect.right = (int) (0 * Resources.getSystem().getDisplayMetrics().density);
            }
        });
        return rootView;
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
    public void onDetach() {
        super.onDetach();
        searchFragmentCallback = null;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        if (responseAurSearchResultList == null) {
            responseAurSearchResultList = new ArrayList<>();
        }
        if (aurSearchResultAdapter == null) {
            aurSearchResultAdapter = new AurSearchResultAdapter(responseAurSearchResultList, this);
        }
        if (savedInstanceState == null) {
            if (bundleAurPackageMaintainer != null && !TextUtils.isEmpty(bundleAurPackageMaintainer)) {
                searchQueryKeyword = bundleAurPackageMaintainer;
                searchQuerySearchBy = SEARCH_BY_MAINTAINER;
                searchAurPackage();
            }
        }
        setUpFooter();
        setUpWikiLayout();
        recyclerView.setAdapter(aurSearchResultAdapter);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(SAVED_INSTANCE_LINEAR_LAYOUT_MANAGER_STATE, linearLayoutManager.onSaveInstanceState());
        outState.putString(SAVED_INSTANCE_SEARCH_KEYWORD, searchQueryKeyword);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            if (savedInstanceState.getParcelable(SAVED_INSTANCE_LINEAR_LAYOUT_MANAGER_STATE) != null) {
                linearLayoutManager.onRestoreInstanceState(savedInstanceState.getParcelable(SAVED_INSTANCE_LINEAR_LAYOUT_MANAGER_STATE));
            }
            if (savedInstanceState.getString(SAVED_INSTANCE_SEARCH_KEYWORD) != null) {
                searchQueryKeyword = savedInstanceState.getString(SAVED_INSTANCE_SEARCH_KEYWORD);
            }
        }
        super.onViewStateRestored(savedInstanceState);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        // search view
        MenuItem searchViewMenuItem = menu.findItem(R.id.menu_search_action_search_view);
        final SearchView searchView = (SearchView) searchViewMenuItem.getActionView();
        if (searchQueryKeyword != null && !searchQueryKeyword.isEmpty()) {
            searchView.setQuery(searchQueryKeyword, false);
        }
        // sort
        MenuItem sortPackageName = menu.findItem(R.id.menu_search_action_sort_package_name);
        MenuItem sortVotes = menu.findItem(R.id.menu_search_action_sort_votes);
        MenuItem sortPopularity = menu.findItem(R.id.menu_search_action_sort_popularity);
        MenuItem sortMaintainer = menu.findItem(R.id.menu_search_action_sort_maintainer);
        MenuItem sortLastModified = menu.findItem(R.id.menu_search_action_sort_last_modified);
        MenuItem sortFirstSubmitted = menu.findItem(R.id.menu_search_action_sort_first_submitted);
        MenuItem sortLastSubmitted = menu.findItem(R.id.menu_search_action_sort_last_submitted);
        switch (searchSortOrder) {
            case SORT_ORDER_PACKAGE_NAME:
                sortPackageName.setChecked(true);
                break;
            case SORT_ORDER_VOTES:
                sortVotes.setChecked(true);
                break;
            case SORT_ORDER_POPULARITY:
                sortPopularity.setChecked(true);
                break;
            case SORT_ORDER_MAINTAINER:
                sortMaintainer.setChecked(true);
                break;
            case SORT_ORDER_LAST_MODIFIED:
                sortLastModified.setChecked(true);
                break;
            case SORT_ORDER_FIRST_SUBMITTED:
                sortFirstSubmitted.setChecked(true);
                break;
            case SORT_ORDER_LAST_SUBMITTED:
                sortLastSubmitted.setChecked(true);
                break;
            default:
                sortPackageName.setChecked(true);
                break;
        }
        // search field
        MenuItem searchNameDescription = menu.findItem(R.id.menu_search_action_search_by_name_and_description);
        MenuItem searchPackageName = menu.findItem(R.id.menu_search_action_search_by_package_name);
        MenuItem searchMaintainer = menu.findItem(R.id.menu_search_action_search_by_maintainer);
        switch (searchQuerySearchBy) {
            case SEARCH_BY_NAME_DESCRIPTION:
                searchNameDescription.setChecked(true);
                break;
            case SEARCH_BY_PACKAGE_NAME:
                searchPackageName.setChecked(true);
                break;
            case SEARCH_BY_MAINTAINER:
                searchMaintainer.setChecked(true);
                break;
        }
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_search_result, menu);
        // search view
        MenuItem searchViewMenuItem = menu.findItem(R.id.menu_search_action_search_view);
        final SearchView searchView = (SearchView) searchViewMenuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchQueryKeyword = query;
                searchAurPackage();
                searchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.length() == 0 && TextUtils.equals(searchQuerySearchBy, SEARCH_BY_MAINTAINER)) {
                    onQueryTextSubmit(newText);
                }
                return false;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        /*
          SORT
         */
        // sort package name
        if (id == R.id.menu_search_action_sort_package_name) {
            searchSortOrder = SORT_ORDER_PACKAGE_NAME;
            processResponseAurSearchResultList();
            return true;
            // sort votes
        } else if (id == R.id.menu_search_action_sort_votes) {
            searchSortOrder = SORT_ORDER_VOTES;
            processResponseAurSearchResultList();
            return true;
            // sort popularity
        } else if (id == R.id.menu_search_action_sort_popularity) {
            searchSortOrder = SORT_ORDER_POPULARITY;
            processResponseAurSearchResultList();
            return true;
            // sort maintainer
        } else if (id == R.id.menu_search_action_sort_maintainer) {
            searchSortOrder = SORT_ORDER_MAINTAINER;
            processResponseAurSearchResultList();
            return true;
            // sort last modified
        } else if (id == R.id.menu_search_action_sort_last_modified) {
            searchSortOrder = SORT_ORDER_LAST_MODIFIED;
            processResponseAurSearchResultList();
            return true;
            // sort first submitted
        } else if (id == R.id.menu_search_action_sort_first_submitted) {
            searchSortOrder = SORT_ORDER_FIRST_SUBMITTED;
            processResponseAurSearchResultList();
            return true;
            // sort last submitted
        } else if (id == R.id.menu_search_action_sort_last_submitted) {
            searchSortOrder = SORT_ORDER_LAST_SUBMITTED;
            processResponseAurSearchResultList();
            return true;
            /*
              SEARCH
             */
            // search name description
        } else if (id == R.id.menu_search_action_search_by_name_and_description) {
            searchQuerySearchBy = SEARCH_BY_NAME_DESCRIPTION;
            searchAurPackage();
            return true;
            // search package name
        } else if (id == R.id.menu_search_action_search_by_package_name) {
            searchQuerySearchBy = SEARCH_BY_PACKAGE_NAME;
            searchAurPackage();
            return true;
            // search maintainer
        } else if (id == R.id.menu_search_action_search_by_maintainer) {
            searchQuerySearchBy = SEARCH_BY_MAINTAINER;
            searchAurPackage();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setUpFooter() {
        if (aurSearchObject != null && aurSearchObject.resultcount != null) {
            footerTextView.setText(resources.getQuantityString(R.plurals.plurals_formatted_packages_found,
                    aurSearchObject.resultcount,
                    aurSearchObject.resultcount));
        }
    }

    private void setUpWikiLayout() {
        wikiLinearLayout.setVisibility(responseAurSearchResultList != null && responseAurSearchResultList.isEmpty() ?
                View.VISIBLE : View.GONE);
    }

    private void searchAurPackage() {
        // API: split multi words strings into HashMap to avoid multi words search query
        List<String> stringList = new ArrayList<>();
        if (!TextUtils.isEmpty(searchQueryKeyword)) {
            Collections.addAll(stringList, searchQueryKeyword.split("\\s"));
        } else {
            stringList.add("");
        }
        if (aurDroidUtils.isNetworkInfoConnected()) {
            // show progress bar
            progressBar.setVisibility(View.VISIBLE);
            // cancel any pending call
            if (call != null) {
                call.cancel();
            }
            switch (searchQuerySearchBy) {
                case SEARCH_BY_NAME_DESCRIPTION:
                    call = aurRpcApi.searchAurResultByNameDesc(stringList);
                    break;
                case SEARCH_BY_PACKAGE_NAME:
                    call = aurRpcApi.searchAurResultByName(stringList);
                    break;
                case SEARCH_BY_MAINTAINER:
                    call = aurRpcApi.searchAurResultByMaintainer(stringList);
                    break;
                default:
                    call = aurRpcApi.searchAurResultByNameDesc(stringList);
                    break;
            }
            call.enqueue(this);
        } else {
            if (searchFragmentCallback != null) {
                searchFragmentCallback.onSearchFragmentNetworkInfoNotConnected(searchQueryKeyword, searchQuerySearchBy);
            }
        }
    }

    private void processResponseAurSearchResultList() {
        // show progress bar
        progressBar.setVisibility(View.VISIBLE);
        // sort package name
        switch (searchSortOrder) {
            case SORT_ORDER_PACKAGE_NAME:
                Collections.sort(responseAurSearchResultList, new Comparator<AurSearchResult>() {
                    @Override
                    public int compare(AurSearchResult o1, AurSearchResult o2) {
                        return o1.name.compareToIgnoreCase(o2.name);
                    }
                });
                // sort votes
                break;
            case SORT_ORDER_VOTES:
                Collections.sort(responseAurSearchResultList, new Comparator<AurSearchResult>() {
                    @Override
                    public int compare(AurSearchResult o1, AurSearchResult o2) {
                        return o1.numVotes.compareTo(o2.numVotes);
                    }
                });
                Collections.reverse(responseAurSearchResultList);
                // sort popularity
                break;
            case SORT_ORDER_POPULARITY:
                Collections.sort(responseAurSearchResultList, new Comparator<AurSearchResult>() {
                    @Override
                    public int compare(AurSearchResult o1, AurSearchResult o2) {
                        return o1.popularity.compareTo(o2.popularity);
                    }
                });
                Collections.reverse(responseAurSearchResultList);
                // sort maintainer
                break;
            case SORT_ORDER_MAINTAINER:
                Collections.sort(responseAurSearchResultList, new Comparator<AurSearchResult>() {
                    @Override
                    public int compare(AurSearchResult o1, AurSearchResult o2) {
                        return o1.maintainer.compareToIgnoreCase(o2.maintainer);
                    }
                });
                // sort last modified
                break;
            case SORT_ORDER_LAST_MODIFIED:
                Collections.sort(responseAurSearchResultList, new Comparator<AurSearchResult>() {
                    @Override
                    public int compare(AurSearchResult o1, AurSearchResult o2) {
                        return o1.lastModified.compareTo(o2.lastModified);
                    }
                });
                Collections.reverse(responseAurSearchResultList);
                // sort first submitted
                break;
            case SORT_ORDER_FIRST_SUBMITTED:
                Collections.sort(responseAurSearchResultList, new Comparator<AurSearchResult>() {
                    @Override
                    public int compare(AurSearchResult o1, AurSearchResult o2) {
                        return o1.firstSubmitted.compareTo(o2.firstSubmitted);
                    }
                });
                // sort last submitted
                break;
            case SORT_ORDER_LAST_SUBMITTED:
                Collections.sort(responseAurSearchResultList, new Comparator<AurSearchResult>() {
                    @Override
                    public int compare(AurSearchResult o1, AurSearchResult o2) {
                        return o1.firstSubmitted.compareTo(o2.firstSubmitted);
                    }
                });
                Collections.reverse(responseAurSearchResultList);
                break;
            default:
                Collections.sort(responseAurSearchResultList, new Comparator<AurSearchResult>() {
                    @Override
                    public int compare(AurSearchResult o1, AurSearchResult o2) {
                        return o1.name.compareToIgnoreCase(o2.name);
                    }
                });
                break;
        }
        // wipe the adapter
        aurSearchResultAdapter.clearAll();
        // add new objects
        for (AurSearchResult aurSearchResult : responseAurSearchResultList) {
            aurSearchResultAdapter.addAurSearchResult(aurSearchResult);
        }
        // smooth scroll to position
        recyclerView.smoothScrollToPosition(0);
        // hide progress bar
        progressBar.setVisibility(View.GONE);
        // update footer
        setUpFooter();
        // invalidate wiki
        setUpWikiLayout();
    }

    @Override
    public void onResponse(@NonNull Call<AurSearchObject> call, @NonNull Response<AurSearchObject> response) {
        if (response.isSuccessful() && response.code() == 200) {
            AurSearchObject aurSearchObject = response.body();
            if (aurSearchObject != null) {
                if (aurSearchObject.aurSearchResults != null) {
                    if (TextUtils.equals(aurSearchObject.type, resources.getString(R.string.search).toLowerCase())
                            && aurSearchObject.resultcount > 0
                            && !aurSearchObject.aurSearchResults.isEmpty()) {
                        this.aurSearchObject = aurSearchObject;
                        responseAurSearchResultList = aurSearchObject.aurSearchResults;
                        processResponseAurSearchResultList();
                    } else if (TextUtils.equals(aurSearchObject.type, resources.getString(R.string.error).toLowerCase())) {
                        onResponseError(aurSearchObject);
                    } else {
                        onResponseNotSuccessful();
                    }
                } else {
                    onResponseEmptyResult();
                }
            } else {
                onResponseNullObject();
            }
        } else {
            onResponseNotSuccessful();
        }
    }

    private void onResponseEmptyResult() {
        Log.e(LOG_TAG, "onResponseEmptyResult");
        // hide progress bar
        progressBar.setVisibility(View.GONE);
        // callback to activity
        if (searchFragmentCallback != null) {
            searchFragmentCallback.onSearchFragmentOnResponseEmptyResults(searchQueryKeyword, searchQuerySearchBy);
        }
    }

    private void onResponseError(AurSearchObject aurSearchObject) {
        Log.e(LOG_TAG, "onResponseError");
        // hide progress bar
        progressBar.setVisibility(View.GONE);
        // callback to activity
        if (searchFragmentCallback != null) {
            searchFragmentCallback.onSearchFragmentOnResponseError(searchQueryKeyword, searchQuerySearchBy, aurSearchObject.error);
        }
    }

    private void onResponseNotSuccessful() {
        Log.e(LOG_TAG, "onResponseNotSuccessful");
        // hide progress bar
        progressBar.setVisibility(View.GONE);
        // callback to activity
        if (searchFragmentCallback != null) {
            searchFragmentCallback.onSearchFragmentOnResponseNotSuccessful(searchQueryKeyword, searchQuerySearchBy);
        }
    }

    private void onResponseNullObject() {
        Log.e(LOG_TAG, "onResponseNullObject");
        // hide progress bar
        progressBar.setVisibility(View.GONE);
        // callback to activity
        if (searchFragmentCallback != null) {
            searchFragmentCallback.onSearchFragmentOnResponseNotSuccessful(searchQueryKeyword, searchQuerySearchBy);
        }
    }

    @Override
    public void onFailure(@NonNull Call<AurSearchObject> call, @NonNull Throwable t) {
        Log.e(LOG_TAG, "onFailure");
        // hide progress bar
        progressBar.setVisibility(View.GONE);
        // callback to activity
        if (searchFragmentCallback != null) {
            searchFragmentCallback.onSearchFragmentOnResponseOnFailure(searchQueryKeyword, searchQuerySearchBy, t.getMessage());
        }
    }

    @Override
    public void onItemClicked(String packageName) {
        if (searchFragmentCallback != null) {
            searchFragmentCallback.onSearchFragmentOnItemClicked(packageName);
        }
    }

    interface SearchFragmentCallback {
        void onSearchFragmentNetworkInfoNotConnected(String searchKeyword, String searchQuerySearchBy);

        void onSearchFragmentOnItemClicked(String packageName);

        void onSearchFragmentOnResponseNotSuccessful(String searchKeyword, String searchQuerySearchBy);

        void onSearchFragmentOnResponseEmptyResults(String searchKeyword, String searchQuerySearchBy);

        void onSearchFragmentOnResponseOnFailure(String searchKeyword, String searchQuerySearchBy, String message);

        void onSearchFragmentOnResponseError(String searchKeyword, String searchQuerySearchBy, String message);
    }
}
