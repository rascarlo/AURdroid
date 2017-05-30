
package com.rascarlo.aurdroid.api.model.aur.info;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AurInfoResult implements Parcelable {

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
    @SerializedName("Depends")
    @Expose
    public List<String> depends = null;
    @SerializedName("MakeDepends")
    @Expose
    public List<String> makeDepends = null;
    @SerializedName("OptDepends")
    @Expose
    public List<String> optDepends = null;
    @SerializedName("Conflicts")
    @Expose
    public List<String> conflicts = null;
    @SerializedName("Provides")
    @Expose
    public List<String> provides = null;
    @SerializedName("License")
    @Expose
    public List<String> license = null;
    @SerializedName("Keywords")
    @Expose
    public List<String> keywords = null;

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
        dest.writeStringList(this.depends);
        dest.writeStringList(this.makeDepends);
        dest.writeStringList(this.optDepends);
        dest.writeStringList(this.conflicts);
        dest.writeStringList(this.provides);
        dest.writeStringList(this.license);
        dest.writeStringList(this.keywords);
    }

    public AurInfoResult() {
    }

    private AurInfoResult(Parcel in) {
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
        this.depends = in.createStringArrayList();
        this.makeDepends = in.createStringArrayList();
        this.optDepends = in.createStringArrayList();
        this.conflicts = in.createStringArrayList();
        this.provides = in.createStringArrayList();
        this.license = in.createStringArrayList();
        this.keywords = in.createStringArrayList();
    }

    public static final Parcelable.Creator<AurInfoResult> CREATOR = new Parcelable.Creator<AurInfoResult>() {
        @Override
        public AurInfoResult createFromParcel(Parcel source) {
            return new AurInfoResult(source);
        }

        @Override
        public AurInfoResult[] newArray(int size) {
            return new AurInfoResult[size];
        }
    };

    @Override
    public String toString() {
        return "AurInfoResult{" +
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
                ", depends=" + depends +
                ", makeDepends=" + makeDepends +
                ", optDepends=" + optDepends +
                ", conflicts=" + conflicts +
                ", provides=" + provides +
                ", license=" + license +
                ", keywords=" + keywords +
                '}';
    }
}
