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

public class Info implements Parcelable {

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
    private List<InfoResult> infoResultList = null;

    public String getVersion() {
        return version;
    }

    public String getType() {
        return type;
    }

    public String getResultcount() {
        return resultcount;
    }

    public List<InfoResult> getInfoResultList() {
        return infoResultList;
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
        dest.writeTypedList(this.infoResultList);
    }

    public Info() {
    }

    private Info(Parcel in) {
        this.version = in.readString();
        this.type = in.readString();
        this.resultcount = in.readString();
        this.infoResultList = in.createTypedArrayList(InfoResult.CREATOR);
    }

    public static final Parcelable.Creator<Info> CREATOR = new Parcelable.Creator<Info>() {
        @Override
        public Info createFromParcel(Parcel source) {
            return new Info(source);
        }

        @Override
        public Info[] newArray(int size) {
            return new Info[size];
        }
    };
}