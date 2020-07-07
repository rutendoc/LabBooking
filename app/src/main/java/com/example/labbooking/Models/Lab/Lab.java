package com.example.labbooking.Models.Lab;

import android.os.Parcel;
import android.os.Parcelable;

public class Lab implements Parcelable {
    private String name, labID;

    private Long seats;

    public Lab() {
    }

    protected Lab(Parcel in) {
        name = in.readString();
        labID = in.readString();
        if (in.readByte() == 0) {
            seats = null;
        } else {
            seats = in.readLong();
        }
    }

    public static final Creator<Lab> CREATOR = new Creator<Lab>() {
        @Override
        public Lab createFromParcel(Parcel in) {
            return new Lab(in);
        }

        @Override
        public Lab[] newArray(int size) {
            return new Lab[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getSeats() {
        return seats;
    }

    public void setSeats(Long seats) {
        this.seats = seats;
    }

    public String getLabID() {
        return labID;
    }

    public void setLabID(String labID) {
        this.labID = labID;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(labID);
        if (seats == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(seats);
        }
    }
}
