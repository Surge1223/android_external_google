package com.google.android.systemui.elmyra;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IElmyraServiceGestureListener extends IInterface
{
    void onGestureDetected() throws RemoteException;
    
    void onGestureProgress(final float p0, final int p1) throws RemoteException;
    
    public abstract static class Stub extends Binder implements IElmyraServiceGestureListener
    {
        public Stub() {
            this.attachInterface((IInterface)this, "com.google.android.systemui.elmyra.IElmyraServiceGestureListener");
        }
        
        public IBinder asBinder() {
            return (IBinder)this;
        }

        // Surge: set case 1 to FLAG_ONEWAY since its (int, Parcel, Parcel, int)      
        public boolean onTransact(final int n, final Parcel parcel, final Parcel parcel2, final int n2) throws RemoteException {
            if (n == INTERFACE_TRANSACTION) {
                parcel2.writeString("com.google.android.systemui.elmyra.IElmyraServiceGestureListener");
                return true;
            }
            switch (n) {
                default: {
                    return super.onTransact(n, parcel, parcel2, n2);
                }
                case 2: {
                    parcel.enforceInterface("com.google.android.systemui.elmyra.IElmyraServiceGestureListener");
                    this.onGestureDetected();
                    return true;
                }
                case FLAG_ONEWAY: {
                    parcel.enforceInterface("com.google.android.systemui.elmyra.IElmyraServiceGestureListener");
                    this.onGestureProgress(parcel.readFloat(), parcel.readInt());
                    return true;
                }
            }
        }
    }
}
