
package com.rascarlo.aurdroid.api.model.aur.info;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AurInfoObject implements Parcelable {

    @SerializedName("version")
    @Expose
    public Integer version;
    @SerializedName("type")
    @Expose
    public String type;
    @SerializedName("resultcount")
    @Expose
    public Integer resultcount;
    @SerializedName("results")
    @Expose
    public List<AurInfoResult> aurInfoResults = null;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.version);
        dest.writeString(this.type);
        dest.writeValue(this.resultcount);
        dest.writeTypedList(this.aurInfoResults);
    }

    public AurInfoObject() {
    }

    private AurInfoObject(Parcel in) {
        this.version = (Integer) in.readValue(Integer.class.getClassLoader());
        this.type = in.readString();
        this.resultcount = (Integer) in.readValue(Integer.class.getClassLoader());
        this.aurInfoResults = in.createTypedArrayList(AurInfoResult.CREATOR);
    }

    public static final Parcelable.Creator<AurInfoObject> CREATOR = new Parcelable.Creator<AurInfoObject>() {
        @Override
        public AurInfoObject createFromParcel(Parcel source) {
            return new AurInfoObject(source);
        }

        @Override
        public AurInfoObject[] newArray(int size) {
            return new AurInfoObject[size];
        }
    };

    @Override
    public String toString() {
        return "AurInfoObject{" +
                "version=" + version +
                ", type='" + type + '\'' +
                ", resultcount=" + resultcount +
                ", aurInfoResults=" + aurInfoResults +
                '}';
    }
}
