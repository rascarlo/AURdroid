
package com.rascarlo.aurdroid.api.model.aur.search;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AurSearchObject implements Parcelable {

    @SerializedName("version")
    @Expose
    public Integer version;
    @SerializedName("type")
    @Expose
    public String type;
    @SerializedName("resultcount")
    @Expose
    public Integer resultcount;
    @SerializedName("error")
    @Expose
    public String error;
    @SerializedName("results")
    @Expose
    public List<AurSearchResult> aurSearchResults = null;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.version);
        dest.writeString(this.type);
        dest.writeValue(this.resultcount);
        dest.writeString(this.error);
        dest.writeTypedList(this.aurSearchResults);
    }

    public AurSearchObject() {
    }

    protected AurSearchObject(Parcel in) {
        this.version = (Integer) in.readValue(Integer.class.getClassLoader());
        this.type = in.readString();
        this.resultcount = (Integer) in.readValue(Integer.class.getClassLoader());
        this.error = in.readString();
        this.aurSearchResults = in.createTypedArrayList(AurSearchResult.CREATOR);
    }

    public static final Parcelable.Creator<AurSearchObject> CREATOR = new Parcelable.Creator<AurSearchObject>() {
        @Override
        public AurSearchObject createFromParcel(Parcel source) {
            return new AurSearchObject(source);
        }

        @Override
        public AurSearchObject[] newArray(int size) {
            return new AurSearchObject[size];
        }
    };

    @Override
    public String toString() {
        return "AurSearchObject{" +
                "version=" + version +
                ", type='" + type + '\'' +
                ", resultcount=" + resultcount +
                ", error='" + error + '\'' +
                ", aurSearchResults=" + aurSearchResults +
                '}';
    }
}
