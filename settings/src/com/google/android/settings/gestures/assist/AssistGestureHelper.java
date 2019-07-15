package com.google.android.settings.gestures.assist;

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
import android.util.Log;
import com.google.android.systemui.elmyra.IElmyraService;
import com.google.android.systemui.elmyra.IElmyraService.Stub;
import com.google.android.systemui.elmyra.IElmyraServiceGestureListener;

import static android.os.Binder.getCallingPid;
import static android.os.Binder.getCallingUid;

public class AssistGestureHelper {
    private boolean mBoundToService;
    private Context mContext;
    private IElmyraServiceGestureListener mElmyraServiceGestureListener;
    private static GestureListener mGestureListener;
    private PowerManager mPowerManager;
    private IElmyraService mService;
    private ServiceConnection mServiceConnection;
    private IBinder mToken;
    private static String LOG_TAG = "AssistGestureElmyra";

    public AssistGestureHelper(Context context) {
        mContext = context;
        mPowerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        mToken = (IBinder)new Binder();
        mServiceConnection = new ServiceConnection() {
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                mService = Stub.asInterface(iBinder);
                if (mGestureListener != null) {
                    try {
                        mService.registerGestureListener(mToken, (IBinder) mElmyraServiceGestureListener);
                    } catch (Throwable e) {
                        Log.e("AssistGestureHelper", "registerGestureListener()", e);
                    }
                }
            }

            public void onServiceDisconnected(ComponentName componentName) {
                mService = null;
            }
        };
        mElmyraServiceGestureListener = new IElmyraServiceGestureListener.Stub() {
            @Override
            public IBinder asBinder() {
                return null;
            }

            private int mLastStage = 0;


            public void onGestureDetected() {
                if (mGestureListener != null) {
                    mGestureListener.onGestureDetected();
                }
            }

            public void onGestureProgress(float f, int i) {
                if (mGestureListener != null) {
                    mGestureListener.onGestureProgress(f, i);
                }
                if (mLastStage != 2 && i == 2) {
                    mPowerManager.userActivity(SystemClock.uptimeMillis(), 0, 0);
                }
                mLastStage = i;
            }
        };



    }

    public interface GestureListener {
        void onGestureDetected();

        void onGestureProgress(float f, int i);
    }

    public void bindToElmyraServiceProxy() {
        if (mService != null) {
            Log.e("AssistGestureHelper", "bound to ElmyraService");
            return;
        }
        try {
            Intent intent = new Intent();
            intent.setComponent(new ComponentName("com.android.systemui", "com.google.android.systemui.elmyra.ElmyraServiceProxy"));
            mContext.bindServiceAsUser(intent, mServiceConnection, Context.BIND_AUTO_CREATE, UserHandle.getUserHandleForUid(0));
            Log.d(LOG_TAG, "Starting service: " + intent);
            Log.i(LOG_TAG, " caller's uid " + getCallingUid()
                    + ", pid " + getCallingPid());
            mContext.startService(intent);
            mBoundToService = true;
        }
        catch (Throwable ex) {
            Log.e("AssistGestureHelper", "Unable to bind to ElmyraService", ex);
        }
    }


    public void launchAssistant() {
        try {
            mService.triggerAction();
        } catch (Throwable e) {
            Log.e("AssistGestureHelper", "Error invoking triggerAction()", e);
        }
    }

    public void setListener(GestureListener gestureListener) {
        mGestureListener = gestureListener;
        if (mService == null) {
            Log.w("AssistGestureHelper", "Service is null, should try to reconnect");
        } else if (gestureListener != null) {
            try {
                mService.registerGestureListener(mToken, (IBinder) mElmyraServiceGestureListener);
            } catch (Throwable e) {
                Throwable th = e;
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Failed to ");
                stringBuilder.append(gestureListener == null ? "unregister" : "register");
                stringBuilder.append(" listener");
                Log.e("AssistGestureHelper", stringBuilder.toString(), th);
            }
        } else {
            try {
                mService.registerGestureListener(mToken, null);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    public void unbindFromElmyraServiceProxy() {
        if (mBoundToService) {
            mContext.unbindService(mServiceConnection);
            mBoundToService = false;
        }
    }
}

