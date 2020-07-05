package com.example.labbooking.Models;

import android.os.Parcel;
import android.os.Parcelable;

public class Building implements Parcelable {
    private String name, buildingID;

    public Building() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBuildingID() {
        return buildingID;
    }

    public void setBuildingID(String buildingID) {
        this.buildingID = buildingID;
    }

    public static Creator<Building> getCREATOR() {
        return CREATOR;
    }

    protected Building(Parcel in) {
        name = in.readString();
        buildingID = in.readString();
    }

    public static final Creator<Building> CREATOR = new Creator<Building>() {
        @Override
        public Building createFromParcel(Parcel in) {
            return new Building(in);
        }

        @Override
        public Building[] newArray(int size) {
            return new Building[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(buildingID);
    }
}
