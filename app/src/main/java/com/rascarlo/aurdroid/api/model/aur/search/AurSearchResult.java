
package com.rascarlo.aurdroid.api.model.aur.search;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AurSearchResult implements Parcelable {

    @SerializedName("ID")
    @Expose
    public Integer iD;
    @SerializedName("Name")
    @Expose
    public String name;
    @SerializedName("PackageBaseID")
    @Expose
    public Integer packageBaseID;
    @SerializedName("PackageBase")
    @Expose
    public String packageBase;
    @SerializedName("Version")
    @Expose
    public String version;
    @SerializedName("Description")
    @Expose
    public String description;
    @SerializedName("URL")
    @Expose
    public String uRL;
    @SerializedName("NumVotes")
    @Expose
    public Integer numVotes;
    @SerializedName("Popularity")
    @Expose
    public Double popularity;
    @SerializedName("OutOfDate")
    @Expose
    public String outOfDate;
    @SerializedName("Maintainer")
    @Expose
    public String maintainer;
    @SerializedName("FirstSubmitted")
    @Expose
    public Integer firstSubmitted;
    @SerializedName("LastModified")
    @Expose
    public Integer lastModified;
    @SerializedName("URLPath")
    @Expose
    public String uRLPath;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.iD);
        dest.writeString(this.name);
        dest.writeValue(this.packageBaseID);
        dest.writeString(this.packageBase);
        dest.writeString(this.version);
        dest.writeString(this.description);
        dest.writeString(this.uRL);
        dest.writeValue(this.numVotes);
        dest.writeValue(this.popularity);
        dest.writeString(this.outOfDate);
        dest.writeString(this.maintainer);
        dest.writeValue(this.firstSubmitted);
        dest.writeValue(this.lastModified);
        dest.writeString(this.uRLPath);
    }

    public AurSearchResult() {
    }

    private AurSearchResult(Parcel in) {
        this.iD = (Integer) in.readValue(Integer.class.getClassLoader());
        this.name = in.readString();
        this.packageBaseID = (Integer) in.readValue(Integer.class.getClassLoader());
        this.packageBase = in.readString();
        this.version = in.readString();
        this.description = in.readString();
        this.uRL = in.readString();
        this.numVotes = (Integer) in.readValue(Integer.class.getClassLoader());
        this.popularity = (Double) in.readValue(Double.class.getClassLoader());
        this.outOfDate = in.readString();
        this.maintainer = in.readString();
        this.firstSubmitted = (Integer) in.readValue(Integer.class.getClassLoader());
        this.lastModified = (Integer) in.readValue(Integer.class.getClassLoader());
        this.uRLPath = in.readString();
    }

    public static final Parcelable.Creator<AurSearchResult> CREATOR = new Parcelable.Creator<AurSearchResult>() {
        @Override
        public AurSearchResult createFromParcel(Parcel source) {
            return new AurSearchResult(source);
        }

        @Override
        public AurSearchResult[] newArray(int size) {
            return new AurSearchResult[size];
        }
    };

    @Override
    public String toString() {
        return "AurSearchResult{" +
                "iD=" + iD +
                ", name='" + name + '\'' +
                ", packageBaseID=" + packageBaseID +
                ", packageBase='" + packageBase + '\'' +
                ", version='" + version + '\'' +
                ", description='" + description + '\'' +
                ", uRL='" + uRL + '\'' +
                ", numVotes=" + numVotes +
                ", popularity=" + popularity +
                ", outOfDate='" + outOfDate + '\'' +
                ", maintainer='" + maintainer + '\'' +
                ", firstSubmitted=" + firstSubmitted +
                ", lastModified=" + lastModified +
                ", uRLPath='" + uRLPath + '\'' +
                '}';
    }
}
