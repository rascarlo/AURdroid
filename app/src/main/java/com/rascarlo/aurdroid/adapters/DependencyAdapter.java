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

import com.rascarlo.aurdroid.databinding.DependencyItemBinding;
import com.rascarlo.aurdroid.viewholders.DependencyAdapterViewHolder;

public class DependencyAdapter extends ListAdapter<String, DependencyAdapterViewHolder> {

    public DependencyAdapter() {
        super(DIFF_CALLBACK);
    }

    private static final DiffUtil.ItemCallback<String> DIFF_CALLBACK = new DiffUtil.ItemCallback<String>() {
        @Override
        public boolean areItemsTheSame(@NonNull String s, @NonNull String t1) {
            return TextUtils.equals(s.trim(), t1.trim());
        }

        @Override
        public boolean areContentsTheSame(@NonNull String s, @NonNull String t1) {
            return t1.equals(s);
        }
    };

    @NonNull
    @Override
    public DependencyAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        DependencyItemBinding binding = DependencyItemBinding.inflate(LayoutInflater.from(viewGroup.getContext()), viewGroup, false);
        return new DependencyAdapterViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull DependencyAdapterViewHolder dependencyAdapterViewHolder, int i) {
        if (getItem(i) != null) {
            String s = getItem(i);
            dependencyAdapterViewHolder.bindString(s);
        }
    }
}