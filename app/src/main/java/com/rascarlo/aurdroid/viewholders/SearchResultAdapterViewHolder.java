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

package com.rascarlo.aurdroid.viewholders;

import android.support.v7.widget.RecyclerView;

import com.rascarlo.aurdroid.api.model.SearchResult;
import com.rascarlo.aurdroid.databinding.SearchResultItemBinding;

public class SearchResultAdapterViewHolder extends RecyclerView.ViewHolder {
    private final SearchResultItemBinding aurSearchResultItemBinding;

    public SearchResultAdapterViewHolder(SearchResultItemBinding searchResultItemBinding) {
        super(searchResultItemBinding.getRoot());
        this.aurSearchResultItemBinding = searchResultItemBinding;
    }

    public void bindResult(SearchResult searchResult) {
        aurSearchResultItemBinding.setSearchResult(searchResult);
        aurSearchResultItemBinding.executePendingBindings();
    }
}
