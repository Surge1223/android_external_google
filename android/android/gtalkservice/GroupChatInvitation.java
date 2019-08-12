package com.google.android.gtalkservice;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.Parcelable;
import com.android.settings.R;

public class GroupChatInvitation implements Parcelable
{
    public static final Parcelable.Creator<GroupChatInvitation> CREATOR;
    private long mGroupContactId;
    private String mInviter;
    private String mPassword;
    private String mReason;
    private String mRoomAddress;
    
    static {
        CREATOR = (Parcelable.Creator)new Parcelable.Creator<GroupChatInvitation>() {
            public GroupChatInvitation createFromParcel(final Parcel parcel) {
                return new GroupChatInvitation(parcel);
            }
            
            public GroupChatInvitation[] newArray(final int n) {
                return new GroupChatInvitation[n];
            }
        };
    }
    
    public GroupChatInvitation(final Parcel parcel) {
        this.mRoomAddress = parcel.readString();
        this.mInviter = parcel.readString();
        this.mReason = parcel.readString();
        this.mPassword = parcel.readString();
        this.mGroupContactId = parcel.readLong();
    }
    
    public int describeContents() {
        return 0;
    }
    
    public void writeToParcel(final Parcel parcel, final int n) {
        parcel.writeString(this.mRoomAddress);
        parcel.writeString(this.mInviter);
        parcel.writeString(this.mReason);
        parcel.writeString(this.mPassword);
        parcel.writeLong(this.mGroupContactId);
    }
}
