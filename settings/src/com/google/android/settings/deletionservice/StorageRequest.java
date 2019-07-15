package com.google.android.settings.deletionservice;

import android.os.Parcel;
import android.os.Bundle;
import android.os.Parcelable.Creator;
import android.os.Parcelable;

public class StorageRequest implements Parcelable {
   public static Creator<StorageRequest> CREATOR = new Creator<StorageRequest>() {
        public StorageRequest createFromParcel(Parcel in) {
            return new StorageRequest(in);
        }

        public StorageRequest[] newArray(int daysToRetain) {
            return new StorageRequest[daysToRetain];
        }
    };
    public int daysToRetain;
    public Bundle extraArgs;
    public int type;

    private StorageRequest(Parcel in) {
        this.daysToRetain = in.readInt();
        this.type = in.readInt();
        this.extraArgs = in.readBundle();
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.daysToRetain);
        dest.writeInt(this.type);
        dest.writeBundle(this.extraArgs);
    }

    public int describeContents() {
        return 0;
    }
}
