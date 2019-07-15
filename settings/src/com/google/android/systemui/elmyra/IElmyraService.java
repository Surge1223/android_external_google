package com.google.android.systemui.elmyra;

import android.os.Parcel;
import android.os.Binder;
import android.os.RemoteException;
import android.os.IBinder;
import android.os.IInterface;

public interface IElmyraService extends IInterface
{
    void registerGestureListener( IBinder p0,  IBinder p1) throws RemoteException;
    
    void registerServiceListener( IBinder p0,  IBinder p1) throws RemoteException;
    
    void triggerAction() throws RemoteException;
    
    public abstract static class Stub extends Binder implements IElmyraService
    {
        public static IElmyraService asInterface( IBinder binder) {
            if (binder == null) {
                return null;
            }
             IInterface queryLocalInterface = binder.queryLocalInterface("com.google.android.systemui.elmyra.IElmyraService");
            if (queryLocalInterface != null && queryLocalInterface instanceof IElmyraService) {
                return (IElmyraService)queryLocalInterface;
            }
            return new Proxy(binder);
        }
        
        public boolean onTransact( int n,  Parcel parcel,  Parcel parcel2,  int n2) throws RemoteException {
            if (n == INTERFACE_TRANSACTION) {
                parcel2.writeString("com.google.android.systemui.elmyra.IElmyraService");
                return true;
            }
            switch (n) {
                default: {
                    return super.onTransact(n, parcel, parcel2, n2);
                }
                case 3: {
                    parcel.enforceInterface("com.google.android.systemui.elmyra.IElmyraService");
                    registerServiceListener(parcel.readStrongBinder(), parcel.readStrongBinder());
                    return true;
                }
                case 2: {
                    parcel.enforceInterface("com.google.android.systemui.elmyra.IElmyraService");
                    triggerAction();
                    return true;
                }
                case FLAG_ONEWAY: {
                    parcel.enforceInterface("com.google.android.systemui.elmyra.IElmyraService");
                    registerGestureListener(parcel.readStrongBinder(), parcel.readStrongBinder());
                    return true;
                }
            }
        }
        
        private static class Proxy implements IElmyraService
        {
            private IBinder mRemote;
            
            Proxy( IBinder mRemote) {
                mRemote = mRemote;
            }
            
            public IBinder asBinder() {
                return mRemote;
            }
            
            @Override
            public void registerGestureListener( IBinder binder,  IBinder binder2) throws RemoteException {
                 Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.systemui.elmyra.IElmyraService");
                    obtain.writeStrongBinder(binder);
                    obtain.writeStrongBinder(binder2);
                    mRemote.transact(1, obtain, (Parcel)null, 1);
                }
                finally {
                    obtain.recycle();
                }
            }
            
            @Override
            public void registerServiceListener( IBinder binder,  IBinder binder2) throws RemoteException {
                 Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.systemui.elmyra.IElmyraService");
                    obtain.writeStrongBinder(binder);
                    obtain.writeStrongBinder(binder2);
                    mRemote.transact(3, obtain, (Parcel)null, 1);
                }
                finally {
                    obtain.recycle();
                }
            }
            
            @Override
            public void triggerAction() throws RemoteException {
                 Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.systemui.elmyra.IElmyraService");
                    mRemote.transact(2, obtain, (Parcel)null, 1);
                }
                finally {
                    obtain.recycle();
                }
            }
        }
    }
}
