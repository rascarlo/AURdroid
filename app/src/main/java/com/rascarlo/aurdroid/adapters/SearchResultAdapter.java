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

package com.rascarlo.aurdroid.adapters;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.rascarlo.aurdroid.api.model.SearchResult;
import com.rascarlo.aurdroid.callbacks.SearchResultAdapterCallback;
import com.rascarlo.aurdroid.databinding.SearchResultItemBinding;
import com.rascarlo.aurdroid.viewholders.SearchResultAdapterViewHolder;

public class SearchResultAdapter extends ListAdapter<SearchResult, SearchResultAdapterViewHolder> {
    private final SearchResultAdapterCallback searchResultAdapterCallback;

    public SearchResultAdapter(SearchResultAdapterCallback searchResultAdapterCallback) {
        super(DIFF_CALLBACK);
        this.searchResultAdapterCallback = searchResultAdapterCallback;
    }

    private static final DiffUtil.ItemCallback<SearchResult> DIFF_CALLBACK = new DiffUtil.ItemCallback<SearchResult>() {
        @Override
        public boolean areItemsTheSame(@NonNull SearchResult searchResult, @NonNull SearchResult t1) {
            return TextUtils.equals(searchResult.getiD(), t1.getiD());
        }

        @Override
        public boolean areContentsTheSame(@NonNull SearchResult searchResult, @NonNull SearchResult t1) {
            return searchResult.equals(t1);
        }
    };

    @NonNull
    @Override
    public SearchResultAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        SearchResultItemBinding searchResultItemBinding = SearchResultItemBinding.inflate(LayoutInflater.from(viewGroup.getContext()), viewGroup, false);
        searchResultItemBinding.setSearchResultAdapterCallback(searchResultAdapterCallback);
        return new SearchResultAdapterViewHolder(searchResultItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchResultAdapterViewHolder searchResultAdapterViewHolder, int i) {
        if (getItem(i) != null) {
            SearchResult searchResult = getItem(i);
            searchResultAdapterViewHolder.bindResult(searchResult);
        }
    }
}
