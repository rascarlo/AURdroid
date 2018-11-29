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

package com.rascarlo.aurdroid.api.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Search implements Parcelable {

    @SerializedName("version")
    @Expose
    private String version;

    @SerializedName("type")
    @Expose
    private String type;

    @SerializedName("resultcount")
    @Expose
    private String resultcount;

    @SerializedName("results")
    @Expose
    private List<SearchResult> results = null;

    public String getVersion() {
        return version;
    }

    public String getType() {
        return type;
    }

    public String getResultcount() {
        return resultcount;
    }

    public List<SearchResult> getResults() {
        return results;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.version);
        dest.writeString(this.type);
        dest.writeString(this.resultcount);
        dest.writeTypedList(this.results);
    }

    public Search() {
    }

    private Search(Parcel in) {
        this.version = in.readString();
        this.type = in.readString();
        this.resultcount = in.readString();
        this.results = in.createTypedArrayList(SearchResult.CREATOR);
    }

    public static final Creator<Search> CREATOR = new Creator<Search>() {
        @Override
        public Search createFromParcel(Parcel source) {
            return new Search(source);
        }

        @Override
        public Search[] newArray(int size) {
            return new Search[size];
        }
    };
}