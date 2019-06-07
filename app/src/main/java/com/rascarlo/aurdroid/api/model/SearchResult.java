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
import android.text.TextUtils;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SearchResult implements Parcelable {

    @SerializedName("ID")
    @Expose
    private String iD;

    @SerializedName("Name")
    @Expose
    private String name;

    @SerializedName("PackageBaseID")
    @Expose
    private String packageBaseID;

    @SerializedName("PackageBase")
    @Expose
    private String packageBase;

    @SerializedName("Version")
    @Expose
    private String version;

    @SerializedName("Description")
    @Expose
    private String description;

    @SerializedName("URL")
    @Expose
    private String uRL;

    @SerializedName("NumVotes")
    @Expose
    private String numVotes;

    @SerializedName("Popularity")
    @Expose
    private String popularity;

    @SerializedName("OutOfDate")
    @Expose
    private String outOfDate;

    @SerializedName("Maintainer")
    @Expose
    private String maintainer;

    @SerializedName("FirstSubmitted")
    @Expose
    private String firstSubmitted;

    @SerializedName("LastModified")
    @Expose
    private String lastModified;

    @SerializedName("URLPath")
    @Expose
    private String uRLPath;

    public String getiD() {
        return iD;
    }

    public String getName() {
        return name;
    }

    public String getPackageBaseID() {
        return packageBaseID;
    }

    public String getPackageBase() {
        return packageBase;
    }

    public String getVersion() {
        return version;
    }

    public String getDescription() {
        return description;
    }

    public String getuRL() {
        return uRL;
    }

    public String getNumVotes() {
        return numVotes;
    }

    public String getPopularity() {
        return popularity;
    }

    public String getOutOfDate() {
        return outOfDate;
    }

    public String getMaintainer() {
        return maintainer;
    }

    public String getFirstSubmitted() {
        return firstSubmitted;
    }

    public String getLastModified() {
        return lastModified;
    }

    public String getuRLPath() {
        return uRLPath;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.iD);
        dest.writeString(this.name);
        dest.writeString(this.packageBaseID);
        dest.writeString(this.packageBase);
        dest.writeString(this.version);
        dest.writeString(this.description);
        dest.writeString(this.uRL);
        dest.writeString(this.numVotes);
        dest.writeString(this.popularity);
        dest.writeString(this.outOfDate);
        dest.writeString(this.maintainer);
        dest.writeString(this.firstSubmitted);
        dest.writeString(this.lastModified);
        dest.writeString(this.uRLPath);
    }

    public SearchResult() {
    }

    protected SearchResult(Parcel in) {
        this.iD = in.readString();
        this.name = in.readString();
        this.packageBaseID = in.readString();
        this.packageBase = in.readString();
        this.version = in.readString();
        this.description = in.readString();
        this.uRL = in.readString();
        this.numVotes = in.readString();
        this.popularity = in.readString();
        this.outOfDate = in.readString();
        this.maintainer = in.readString();
        this.firstSubmitted = in.readString();
        this.lastModified = in.readString();
        this.uRLPath = in.readString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SearchResult)) return false;
        SearchResult that = (SearchResult) o;
        return TextUtils.equals(getiD(), (that.getiD())) &&
                TextUtils.equals(getName(), (that.getName())) &&
                TextUtils.equals(getPackageBaseID(), (that.getPackageBaseID())) &&
                TextUtils.equals(getPackageBase(), (that.getPackageBase())) &&
                TextUtils.equals(getVersion(), (that.getVersion())) &&
                TextUtils.equals(getDescription(), (that.getDescription())) &&
                TextUtils.equals(getuRL(), (that.getuRL())) &&
                TextUtils.equals(getNumVotes(), (that.getNumVotes())) &&
                TextUtils.equals(getPopularity(), (that.getPopularity())) &&
                TextUtils.equals(getOutOfDate(), (that.getOutOfDate())) &&
                TextUtils.equals(getMaintainer(), (that.getMaintainer())) &&
                TextUtils.equals(getFirstSubmitted(), (that.getFirstSubmitted())) &&
                TextUtils.equals(getLastModified(), (that.getLastModified())) &&
                TextUtils.equals(getuRLPath(), (that.getuRLPath()));
    }

    public static final Creator<SearchResult> CREATOR = new Creator<SearchResult>() {
        @Override
        public SearchResult createFromParcel(Parcel source) {
            return new SearchResult(source);
        }

        @Override
        public SearchResult[] newArray(int size) {
            return new SearchResult[size];
        }
    };
}