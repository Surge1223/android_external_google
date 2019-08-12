package com.google.android.gtalkservice;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.Parcelable;
import com.android.settings.R;

public final class ConnectionState implements Parcelable
{
    public static final Parcelable.Creator<ConnectionState> CREATOR;
    private volatile int mState;
    
    static {
        CREATOR = (Parcelable.Creator)new Parcelable.Creator<ConnectionState>() {
            public ConnectionState createFromParcel(final Parcel parcel) {
                return new ConnectionState(parcel);
            }
            
            public ConnectionState[] newArray(final int n) {
                return new ConnectionState[n];
            }
        };
    }
    
    public ConnectionState(final Parcel parcel) {
        this.mState = parcel.readInt();
    }
    
    public static final String toString(final int n) {
        switch (n) {
            default: {
                return "IDLE";
            }
            case 4: {
                return "ONLINE";
            }
            case 3: {
                return "AUTHENTICATED";
            }
            case 2: {
                return "CONNECTING";
            }
            case 1: {
                return "RECONNECTION_SCHEDULED";
            }
        }
    }
    
    public int describeContents() {
        return 0;
    }
    
    @Override
    public final String toString() {
        return toString(this.mState);
    }
    
    public void writeToParcel(final Parcel parcel, final int n) {
        parcel.writeInt(this.mState);
    }
}
