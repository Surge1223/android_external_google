package com.google.android.gtalkservice;

import java.util.ArrayList;
import android.os.Parcel;
import java.util.List;
import android.os.Parcelable.Creator;
import android.os.Parcelable;
import com.android.settings.R;

public final class Presence implements Parcelable
{
    public static final Parcelable.Creator<Presence> CREATOR;
    public static final Presence OFFLINE;
    private boolean mAllowInvisibility;
    private boolean mAvailable;
    private int mCapabilities;
    private List<String> mDefaultStatusList;
    private List<String> mDndStatusList;
    private boolean mInvisible;
    private Show mShow;
    private String mStatus;
    private int mStatusListContentsMax;
    private int mStatusListMax;
    private int mStatusMax;
    
    static {
        OFFLINE = new Presence();
        CREATOR = (Parcelable.Creator)new Parcelable.Creator<Presence>() {
            public Presence createFromParcel(final Parcel parcel) {
                return new Presence(parcel);
            }
            
            public Presence[] newArray(final int n) {
                return new Presence[n];
            }
        };
    }
    
    public Presence() {
        this(false, Show.NONE, null, 8);
    }
    
    public Presence(final Parcel parcel) {
        this.setStatusMax(parcel.readInt());
        this.setStatusListMax(parcel.readInt());
        this.setStatusListContentsMax(parcel.readInt());
        final int int1 = parcel.readInt();
        final boolean b = false;
        this.setAllowInvisibility(int1 != 0);
        this.setAvailable(parcel.readInt() != 0);
        this.setShow(Enum.valueOf(Show.class, parcel.readString()));
        this.mStatus = parcel.readString();
        boolean invisible = b;
        if (parcel.readInt() != 0) {
            invisible = true;
        }
        this.setInvisible(invisible);
        parcel.readStringList((List)(this.mDefaultStatusList = new ArrayList<String>()));
        parcel.readStringList((List)(this.mDndStatusList = new ArrayList<String>()));
        this.setCapabilities(parcel.readInt());
    }
    
    public Presence(final boolean mAvailable, final Show mShow, final String mStatus, final int mCapabilities) {
        this.mAvailable = mAvailable;
        this.mShow = mShow;
        this.mStatus = mStatus;
        this.mInvisible = false;
        this.mDefaultStatusList = new ArrayList<String>();
        this.mDndStatusList = new ArrayList<String>();
        this.mCapabilities = mCapabilities;
    }
    
    public boolean allowInvisibility() {
        return this.mAllowInvisibility;
    }
    
    public int describeContents() {
        return 0;
    }
    
    public int getCapabilities() {
        return this.mCapabilities;
    }
    
    public int getStatusListContentsMax() {
        return this.mStatusListContentsMax;
    }
    
    public int getStatusListMax() {
        return this.mStatusListMax;
    }
    
    public int getStatusMax() {
        return this.mStatusMax;
    }
    
    public boolean isAvailable() {
        return this.mAvailable;
    }
    
    public boolean isInvisible() {
        return this.mInvisible;
    }
    
    public void setAllowInvisibility(final boolean mAllowInvisibility) {
        this.mAllowInvisibility = mAllowInvisibility;
    }
    
    public void setAvailable(final boolean mAvailable) {
        this.mAvailable = mAvailable;
    }
    
    public void setCapabilities(final int mCapabilities) {
        this.mCapabilities = mCapabilities;
    }
    
    public boolean setInvisible(final boolean mInvisible) {
        this.mInvisible = mInvisible;
        return !mInvisible || this.allowInvisibility();
    }
    
    public void setShow(final Show mShow) {
        this.mShow = mShow;
    }
    
    public void setStatusListContentsMax(final int mStatusListContentsMax) {
        this.mStatusListContentsMax = mStatusListContentsMax;
    }
    
    public void setStatusListMax(final int mStatusListMax) {
        this.mStatusListMax = mStatusListMax;
    }
    
    public void setStatusMax(final int mStatusMax) {
        this.mStatusMax = mStatusMax;
    }
    
    @Override
    public String toString() {
        if (!this.isAvailable()) {
            return "UNAVAILABLE";
        }
        if (this.isInvisible()) {
            return "INVISIBLE";
        }
        final StringBuilder sb = new StringBuilder(40);
        if (this.mShow == Show.NONE) {
            sb.append("AVAILABLE(x)");
        }
        else {
            sb.append(this.mShow.toString());
        }
        if ((this.mCapabilities & 0x8) != 0x0) {
            sb.append(" pmuc-v1");
        }
        if ((this.mCapabilities & 0x1) != 0x0) {
            sb.append(" voice-v1");
        }
        if ((this.mCapabilities & 0x2) != 0x0) {
            sb.append(" video-v1");
        }
        if ((this.mCapabilities & 0x4) != 0x0) {
            sb.append(" camera-v1");
        }
        return sb.toString();
    }
    
    public void writeToParcel(final Parcel parcel, final int n) {
        parcel.writeInt(this.getStatusMax());
        parcel.writeInt(this.getStatusListMax());
        parcel.writeInt(this.getStatusListContentsMax());
        parcel.writeInt((int)(this.allowInvisibility() ? 1 : 0));
        parcel.writeInt((int)(this.mAvailable ? 1 : 0));
        parcel.writeString(this.mShow.toString());
        parcel.writeString(this.mStatus);
        parcel.writeInt((int)(this.mInvisible ? 1 : 0));
        parcel.writeStringList((List)this.mDefaultStatusList);
        parcel.writeStringList((List)this.mDndStatusList);
        parcel.writeInt(this.getCapabilities());
    }
    
    public enum Show
    {
        AVAILABLE, 
        AWAY, 
        DND, 
        EXTENDED_AWAY, 
        NONE;
    }
}
