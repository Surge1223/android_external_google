package com.google.android.gtalkservice;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.Parcelable;
import com.android.settings.R;

public final class ConnectionError implements Parcelable
{
    public static final Parcelable.Creator<ConnectionError> CREATOR;
    private int mError;
    
    static {
        CREATOR = (Parcelable.Creator)new Parcelable.Creator<ConnectionError>() {
            public ConnectionError createFromParcel(final Parcel parcel) {
                return new ConnectionError(parcel);
            }
            
            public ConnectionError[] newArray(final int n) {
                return new ConnectionError[n];
            }
        };
    }
    
    public ConnectionError(final Parcel parcel) {
        this.mError = parcel.readInt();
    }
    
    public static final String toString(final int n) {
        switch (n) {
            default: {
                return "NO ERROR";
            }
            case 10: {
                return "UNKNOWN";
            }
            case 8: {
                return "SERVER REJECT - RATE LIMIT";
            }
            case 7: {
                return "SERVER FAILED";
            }
            case 6: {
                return "HEARTBEAT TIMEOUT";
            }
            case 5: {
                return "AUTH EXPIRED";
            }
            case 4: {
                return "AUTH FAILED";
            }
            case 3: {
                return "UNKNOWN HOST";
            }
            case 2: {
                return "CONNECTION FAILED";
            }
            case 1: {
                return "NO NETWORK";
            }
        }
    }
    
    public int describeContents() {
        return 0;
    }
    
    @Override
    public final String toString() {
        return toString(this.mError);
    }
    
    public void writeToParcel(final Parcel parcel, final int n) {
        parcel.writeInt(this.mError);
    }
}
