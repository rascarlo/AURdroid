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

import androidx.lifecycle.ViewModelProviders;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.rascarlo.aurdroid.R;
import com.rascarlo.aurdroid.adapters.DependencyAdapter;
import com.rascarlo.aurdroid.api.model.InfoResult;
import com.rascarlo.aurdroid.callbacks.InfoResultFragmentCallback;
import com.rascarlo.aurdroid.databinding.FragmentInfoResultBinding;
import com.rascarlo.aurdroid.util.AurdroidConstants;
import com.rascarlo.aurdroid.viewmodels.InfoViewModel;
import com.rascarlo.aurdroid.viewmodels.InfoViewModelFactory;

import java.util.HashMap;
import java.util.List;

public class InfoResultFragment extends Fragment {
    private static final String BUNDLE_PKGNAME = "bundle_pkgname";
    private String bundlePkgname;
    private InfoResult infoResult;
    private InfoResultFragmentCallback infoResultFragmentCallback;

    public InfoResultFragment() {
    }

    public static InfoResultFragment newInstance(String pkgname) {
        InfoResultFragment fragment = new InfoResultFragment();
        Bundle args = new Bundle();
        args.putString(BUNDLE_PKGNAME, pkgname);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof InfoResultFragmentCallback) {
            infoResultFragmentCallback = (InfoResultFragmentCallback) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement InfoResultFragmentCallback");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            bundlePkgname = getArguments().getString(BUNDLE_PKGNAME);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Context context = container.getContext();
        FragmentInfoResultBinding fragmentInfoResultBinding = FragmentInfoResultBinding.inflate(inflater, container, false);
        ProgressBar progressBar = fragmentInfoResultBinding.fragmentInfoResultProgressBar;
        progressBar.setVisibility(View.VISIBLE);
        InfoViewModelFactory infoViewModelFactory = new InfoViewModelFactory(bundlePkgname);
        InfoViewModel infoViewModel = ViewModelProviders.of(this, infoViewModelFactory).get(InfoViewModel.class);
        infoViewModel.getInfoLiveData().observe(this, info -> {
            if (info != null && info.getInfoResultList().get(0) != null) {
                this.infoResult = info.getInfoResultList().get(0);
                fragmentInfoResultBinding.setInfoResult(infoResult);
                fragmentInfoResultBinding.executePendingBindings();
                bindDepends(fragmentInfoResultBinding);
            }
            progressBar.setVisibility(View.GONE);
        });
        infoViewModel.getMessageMutableLiveData().observe(this, s -> {
            if (s != null && !TextUtils.isEmpty(s)) {
                Toast.makeText(context,
                        TextUtils.equals(AurdroidConstants.RETROFIT_FAILURE, s) ? getString(R.string.retrofit_something_went_wrong) : s,
                        Toast.LENGTH_LONG).show();
            }
            progressBar.setVisibility(View.GONE);
        });
        return fragmentInfoResultBinding.getRoot();
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.menu_info_result_view_pkgbuild).setVisible(infoResult != null);
        menu.findItem(R.id.menu_info_result_view_changes).setVisible(infoResult != null);
        menu.findItem(R.id.menu_info_result_maintainer).setVisible(infoResult != null);
        menu.findItem(R.id.menu_info_result_open_in_browser).setVisible(infoResult != null);
        menu.findItem(R.id.menu_info_result_share).setVisible(infoResult != null);
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_info_result, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_info_result_view_pkgbuild:
                if (infoResultFragmentCallback != null && infoResult != null && getViewPkgbuildUri() != null) {
                    infoResultFragmentCallback.onInfoResultFragmentCallbackViewPkgbuildClicked(getViewPkgbuildUri());
                }
                break;
            case R.id.menu_info_result_view_changes:
                if (infoResultFragmentCallback != null && infoResult != null && getViewChangesUri() != null) {
                    infoResultFragmentCallback.onInfoResultFragmentCallbackViewChangesClicked(getViewChangesUri());
                }
                break;
            case R.id.menu_info_result_maintainer:
                if (infoResultFragmentCallback != null && getMaintainer()!=null) {
                    infoResultFragmentCallback.onInfoResultFragmentCallbackMaintainer(getMaintainer());
                }
                break;
                    case R.id.menu_info_result_open_in_browser:
                if (infoResultFragmentCallback != null && infoResult != null && getInfoResultUri() != null) {
                    infoResultFragmentCallback.onInfoResultFragmentCallbackOpenInBrowserClicked(getInfoResultUri());
                }
                break;
            case R.id.menu_info_result_share:
                if (infoResultFragmentCallback != null && infoResult != null && getInfoResultUri() != null) {
                    infoResultFragmentCallback.onInfoResultFragmentCallbackShareClicked(getInfoResultUri());
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        infoResultFragmentCallback = null;
    }

    private void bindDepends(FragmentInfoResultBinding fragmentInfoResultBinding) {
        Context context = fragmentInfoResultBinding.getRoot().getContext();
        if (infoResult != null) {
            HashMap<RecyclerView, List<String>> hashMap = new HashMap<>();
            // dependencies
            if (infoResult.getDepends() != null && !infoResult.getDepends().isEmpty()) {
                hashMap.put(fragmentInfoResultBinding.fragmentInfoResultDependenciesLayout.infoResultDependenciesRecyclerView,
                        infoResult.getDepends());
            }
            // make dependencies
            if (infoResult.getMakeDepends() != null && !infoResult.getMakeDepends().isEmpty()) {
                hashMap.put(fragmentInfoResultBinding.fragmentInfoResultMakeDependenciesLayout.infoResultMakeDependenciesRecyclerView,
                        infoResult.getMakeDepends());
            }
            // opt dependencies
            if (infoResult.getOptDepends() != null && !infoResult.getOptDepends().isEmpty()) {
                hashMap.put(fragmentInfoResultBinding.fragmentInfoResultOptDependenciesLayout.infoResultOptDependenciesRecyclerView,
                        infoResult.getOptDepends());
            }
            // conflicts
            if (infoResult.getConflicts() != null && !infoResult.getConflicts().isEmpty()) {
                hashMap.put(fragmentInfoResultBinding.fragmentInfoResultConflictsLayout.infoResultConflictsRecyclerView,
                        infoResult.getConflicts());
            }
            // provides
            if (infoResult.getProvides() != null && !infoResult.getProvides().isEmpty()) {
                hashMap.put(fragmentInfoResultBinding.fragmentInfoResultProvidesLayout.infoResultProvidesRecyclerView,
                        infoResult.getProvides());
            }
            for (HashMap.Entry<RecyclerView, List<String>> entry : hashMap.entrySet()) {
                populateRecyclerView(context, entry.getKey(), entry.getValue());
            }
        }
    }

    private void populateRecyclerView(Context context, RecyclerView recyclerView, List<String> stringList) {
        if (stringList != null && !stringList.isEmpty()) {
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
            recyclerView.setLayoutManager(linearLayoutManager);
            recyclerView.setHasFixedSize(true);
            recyclerView.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));
            DependencyAdapter dependencyAdapter = new DependencyAdapter();
            recyclerView.setAdapter(dependencyAdapter);
            dependencyAdapter.submitList(stringList);
        }
    }

    private Uri getInfoResultUri() {
        return infoResult != null && infoResult.getName() != null && !TextUtils.isEmpty(infoResult.getName()) ? Uri.parse(AurdroidConstants.AUR_PACKAGES_BASE_URL)
                .buildUpon()
                .appendPath(infoResult.getName())
                .build() : null;
    }

    private Uri getViewPkgbuildUri() {
        return infoResult != null && infoResult.getPackageBase() != null && !TextUtils.isEmpty(infoResult.getPackageBase()) ? Uri.parse(AurdroidConstants.AUR_PACKAGE_PKGBUILD_BASE_URL)
                .buildUpon()
                .appendQueryParameter("h", infoResult.getPackageBase())
                .build() : null;
    }

    private Uri getViewChangesUri() {
        return infoResult != null && infoResult.getPackageBase() != null && !TextUtils.isEmpty(infoResult.getPackageBase()) ? Uri.parse(AurdroidConstants.AUR_PACKAGE_LOG_BASE_URL)
                .buildUpon()
                .appendQueryParameter("h", infoResult.getPackageBase())
                .build() : null;
    }

    private String getMaintainer() {
        return infoResult != null && infoResult.getMaintainer() != null && !TextUtils.isEmpty(infoResult.getMaintainer().trim()) ? infoResult.getMaintainer().trim() : null;
    }
}