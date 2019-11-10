package com.google.android.systemui.elmyra;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class ElmyraServiceProxy extends Service {
    private final static String TAG = "ElmyraServiceProxy";
    private final IElmyraService.Stub mBinder = new IElmyraService.Stub() {
        public void registerGestureListener(IBinder iBinder, IBinder iBinder2) {
            ElmyraServiceProxy.checkPermission();
            try {
                for (int size = ElmyraServiceProxy.mElmyraServiceListeners.size() - 1; size >= 0; size--) {
                    IElmyraServiceListener listener = ElmyraServiceProxy.mElmyraServiceListeners.get(size).getListener();
                    if (listener == null) {
                        ElmyraServiceProxy.mElmyraServiceListeners.remove(size);
                    } else {
                        listener.setListener(iBinder, iBinder2);
                    }
                }
            } catch (RemoteException e) {
                Log.e("Elmyra/ElmyraServiceProxy", "Action isn't connected", e);
            }
        }

        public void triggerAction() {
            ElmyraServiceProxy.checkPermission();
            try {
                for (int size = ElmyraServiceProxy.mElmyraServiceListeners.size() - 1; size >= 0; size--) {
                    IElmyraServiceListener listener = ElmyraServiceProxy.mElmyraServiceListeners.get(size).getListener();
                    if (listener == null) {
                        ElmyraServiceProxy.mElmyraServiceListeners.remove(size);
                    } else {
                        listener.triggerAction();
                    }
                }
            } catch (RemoteException e) {
                Log.e("Elmyra/ElmyraServiceProxy", "Error launching assistant", e);
            }
        }

        public void registerServiceListener(IBinder iBinder, IBinder iBinder2) {
            ElmyraServiceProxy.checkPermission();
            if (iBinder == null) {
                Log.e("Elmyra/ElmyraServiceProxy", "Binder token must not be null");
            } else if (iBinder2 == null) {
                for (int i = 0; i < ElmyraServiceProxy.mElmyraServiceListeners.size(); i++) {
                    if (iBinder.equals(ElmyraServiceProxy.mElmyraServiceListeners.get(i).getToken())) {
                        ElmyraServiceProxy.mElmyraServiceListeners.get(i).unlinkToDeath();
                        ElmyraServiceProxy.mElmyraServiceListeners.remove(i);
                        return;
                    }
                }
            } else {
                ElmyraServiceProxy.mElmyraServiceListeners.add(new ElmyraServiceListener(iBinder, IElmyraServiceListener.Stub.asInterface(iBinder2)));
            }
        }
    };
    public static final List<ElmyraServiceListener> mElmyraServiceListeners = new ArrayList<>();

    private class ElmyraServiceListener implements IBinder.DeathRecipient {
        private IElmyraServiceListener mListener;
        private IBinder mToken;

        ElmyraServiceListener(IBinder iBinder, IElmyraServiceListener iElmyraServiceListener) {
            mToken = iBinder;
            mListener = iElmyraServiceListener;
            linkToDeath();
        }

        public IElmyraServiceListener getListener() {
            return mListener;
        }

        public IBinder getToken() {
            return mToken;
        }

        private void linkToDeath() {
            IBinder iBinder = mToken;
            if (iBinder != null) {
                try {
                    iBinder.linkToDeath(this, START_STICKY_COMPATIBILITY);
                } catch (RemoteException e) {
                    Log.e("Elmyra/ElmyraServiceProxy", "Unable to linkToDeath", e);
                }
            }
        }

        void unlinkToDeath() {
            IBinder iBinder = mToken;
            if (iBinder != null) {
                iBinder.unlinkToDeath(this, START_STICKY_COMPATIBILITY);
            }
        }

        public void binderDied() {
            Log.w("Elmyra/ElmyraServiceProxy", "ElmyraServiceListener binder died");
            mToken = null;
            mListener = null;
        }
    }

    public int onStartCommand(Intent intent, int i, int i2) {
        return START_STICKY_COMPATIBILITY;
    }

    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public static void checkPermission() {
        Log.d(TAG, "Normally, you must have com.google.android.elmyra.permission.CONFIGURE_ASSIST_GESTURE permission");
    }
}
