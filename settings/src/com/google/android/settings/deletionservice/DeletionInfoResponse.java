package com.google.android.settings.deletionservice;

import android.os.Parcel;
import android.os.Bundle;
import android.os.Parcelable.Creator;
import android.os.Parcelable;

public class DeletionInfoResponse implements Parcelable {
	    public static Creator<DeletionInfoResponse> CREATOR = new Creator<DeletionInfoResponse>() {
        public DeletionInfoResponse createFromParcel(Parcel in) {
            return new DeletionInfoResponse(in);
        }

        public DeletionInfoResponse[] newArray(int size) {
            return new DeletionInfoResponse[size];
        }
    };

    public Bundle extraArgs;
    public int numItems;
    public long size;
    
    
    private DeletionInfoResponse(Parcel parcel) {
        this.size = parcel.readLong();
        this.numItems = parcel.readInt();
        this.extraArgs = parcel.readBundle();
    }
    
    public int describeContents() {
        return 0;
    }
    
    public void writeToParcel(Parcel parcel, int n) {
        parcel.writeLong(this.size);
        parcel.writeInt(this.numItems);
        parcel.writeBundle(this.extraArgs);
    }
}
