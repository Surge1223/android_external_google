package com.google.android.settings.gestures.assist;


import android.annotation.SystemApi;
import android.annotation.TestApi;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Binder;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.RemoteException;
import android.os.SystemClock;
import android.os.UserHandle;
import android.telecom.Call;
import android.util.Log;

import com.android.internal.util.Preconditions;
import com.google.android.systemui.elmyra.IElmyraService;
import com.google.android.systemui.elmyra.IElmyraServiceGestureListener;
import com.android.settings.gestures.AssistGestureFeatureProvider;

import static android.os.Binder.getCallingPid;
import static android.os.Binder.getCallingUid;
import com.android.settings.R;

public class AssistGestureHelper {

    private boolean mBoundToService;
    private Context mContext;
    private IElmyraServiceGestureListener mElmyraServiceGestureListener;
    private GestureListener mGestureListener;
    private PowerManager mPowerManager;
    private IElmyraService mService;
    private ServiceConnection mServiceConnection;
    private IBinder mToken;

    public AssistGestureHelper( Context mContext) {
        mToken = (IBinder)new Binder();
        mServiceConnection = (ServiceConnection)new ServiceConnection() {
            public void onServiceConnected( ComponentName componentName,  IBinder binder) {
                mService = IElmyraService.Stub.asInterface(binder);
                if (mGestureListener != null) {
                    try {
                        mService.registerGestureListener(mToken, (IBinder)mElmyraServiceGestureListener);
                    }
                    catch (RemoteException ex) {
                        Log.e("AssistGestureHelper", "registerGestureListener()", (Throwable)ex);
                    }
                }
            }

            public void onServiceDisconnected( ComponentName componentName) {
                mService = null;
            }
        };
        mElmyraServiceGestureListener = new IElmyraServiceGestureListener.Stub() {
            private int mLastStage = 0;

            public void onGestureDetected() throws RemoteException {
                if (mGestureListener != null) {
                    mGestureListener.onGestureDetected();
                }
            }

            public void onGestureProgress( float n,  int mLastStage) throws RemoteException {
                if (mGestureListener != null) {
                    mGestureListener.onGestureProgress(n, mLastStage);
                }
                if (mLastStage != 2 && mLastStage == 2) {
                    mPowerManager.userActivity(SystemClock.uptimeMillis(), 0, 0);
                }
                mLastStage = mLastStage;
            }
        };
        mPowerManager = (PowerManager)mContext.getSystemService(Context.POWER_SERVICE);
    }

    public void bindToElmyraServiceProxy() {
        if (mService == null) {
            try {
                Intent intent = new Intent();
                intent.setComponent(new ComponentName("com.android.systemui", "com.google.android.systemui.elmyra.ElmyraServiceProxy"));
            //    mContext.bindServiceAsUser(intent, mServiceConnection, 1, UserHandle.getUserHandleForUid(0));
                mContext.bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);
                mBoundToService = true;
            }
            catch (SecurityException ex) {
                Log.e("AssistGestureHelper", "Unable to bind to ElmyraService", (Throwable)ex);
            }
        }
    }

    public void launchAssistant() {
        try {
            mService.triggerAction();
        }
        catch (RemoteException ex) {
            Log.e("AssistGestureHelper", "Error invoking triggerAction()", (Throwable)ex);
        }
    }

    public void setListener( GestureListener mGestureListener) {
        if (mService == null) {
            Log.w("AssistGestureHelper", "Service is null, should try to reconnect");
            return;
        }
        while (true) {
            if (mGestureListener != null) {
                try {
                    mService.registerGestureListener(mToken, (IBinder)mElmyraServiceGestureListener);
                }
                catch (RemoteException ex) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("Failed to ");
                    String s;
                    if (mGestureListener == null) {
                        s = "unregister";
                    }
                    else {
                        s = "register";
                    }
                    sb.append(s);
                    sb.append(" listener");
                    Log.e("AssistGestureHelper", sb.toString(), (Throwable)ex);
                    try {
                        mService.registerGestureListener(mToken, null);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
                return;
            }
            continue;
        }
    }

    public void unbindFromElmyraServiceProxy() {
        if (mBoundToService) {
            mContext.unbindService(mServiceConnection);
            mBoundToService = false;
        }
    }

    public interface GestureListener
    {
        void onGestureDetected();

        void onGestureProgress( float p0,  int p1);
    }
}
