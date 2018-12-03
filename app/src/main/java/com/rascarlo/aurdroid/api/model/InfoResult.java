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

public class InfoResult implements Parcelable {

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
    private String url;

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
    private String urlPath;

    @SerializedName("Depends")
    @Expose
    private List<String> depends = null;

    @SerializedName("MakeDepends")
    @Expose
    private List<String> makeDepends = null;

    @SerializedName("OptDepends")
    @Expose
    private List<String> optDepends = null;

    @SerializedName("Conflicts")
    @Expose
    private List<String> conflicts = null;

    @SerializedName("Provides")
    @Expose
    private List<String> provides = null;

    @SerializedName("License")
    @Expose
    private List<String> license = null;

    @SerializedName("Keywords")
    @Expose
    private List<String> keywords = null;

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

    public String getUrl() {
        return url;
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

    public String getUrlPath() {
        return urlPath;
    }

    public List<String> getDepends() {
        return depends;
    }

    public List<String> getMakeDepends() {
        return makeDepends;
    }

    public List<String> getOptDepends() {
        return optDepends;
    }

    public List<String> getConflicts() {
        return conflicts;
    }

    public List<String> getProvides() {
        return provides;
    }

    public List<String> getLicense() {
        return license;
    }

    public List<String> getKeywords() {
        return keywords;
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
        dest.writeString(this.url);
        dest.writeString(this.numVotes);
        dest.writeString(this.popularity);
        dest.writeString(this.outOfDate);
        dest.writeString(this.maintainer);
        dest.writeString(this.firstSubmitted);
        dest.writeString(this.lastModified);
        dest.writeString(this.urlPath);
        dest.writeStringList(this.depends);
        dest.writeStringList(this.makeDepends);
        dest.writeStringList(this.optDepends);
        dest.writeStringList(this.conflicts);
        dest.writeStringList(this.provides);
        dest.writeStringList(this.license);
        dest.writeStringList(this.keywords);
    }

    public InfoResult() {
    }

    protected InfoResult(Parcel in) {
        this.iD = in.readString();
        this.name = in.readString();
        this.packageBaseID = in.readString();
        this.packageBase = in.readString();
        this.version = in.readString();
        this.description = in.readString();
        this.url = in.readString();
        this.numVotes = in.readString();
        this.popularity = in.readString();
        this.outOfDate = in.readString();
        this.maintainer = in.readString();
        this.firstSubmitted = in.readString();
        this.lastModified = in.readString();
        this.urlPath = in.readString();
        this.depends = in.createStringArrayList();
        this.makeDepends = in.createStringArrayList();
        this.optDepends = in.createStringArrayList();
        this.conflicts = in.createStringArrayList();
        this.provides = in.createStringArrayList();
        this.license = in.createStringArrayList();
        this.keywords = in.createStringArrayList();
    }

    public static final Creator<InfoResult> CREATOR = new Creator<InfoResult>() {
        @Override
        public InfoResult createFromParcel(Parcel source) {
            return new InfoResult(source);
        }

        @Override
        public InfoResult[] newArray(int size) {
            return new InfoResult[size];
        }
    };
}