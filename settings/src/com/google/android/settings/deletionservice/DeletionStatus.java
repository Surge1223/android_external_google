package com.google.android.settings.deletionservice;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class DeletionStatus implements Parcelable {
    public static final Creator<DeletionStatus> CREATOR = new Creator<DeletionStatus>() {
        public DeletionStatus createFromParcel(Parcel in) {
            return new DeletionStatus(in);
        }

        public DeletionStatus[] newArray(int size) {
            return new DeletionStatus[size];
        }
    };
    public final int code;
    public final String message;

    private DeletionStatus(Parcel in) {
        this.code = in.readInt();
        this.message = in.readString();
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.code);
        dest.writeString(this.message);
    }
}
