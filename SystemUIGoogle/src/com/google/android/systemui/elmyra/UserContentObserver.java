package com.google.android.systemui.elmyra;

import android.app.ActivityManager;
import android.app.SynchronousUserSwitchObserver;
import android.content.Context;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.os.RemoteException;
import android.util.Log;

import java.util.function.Consumer;

public class UserContentObserver extends ContentObserver {
    public Consumer<Uri> mCallback;
    private Context mContext;
    private static Uri mSettingsUri = null;
    private SynchronousUserSwitchObserver mUserSwitchCallback;

    public UserContentObserver(Context context, Uri uri, Consumer<Uri> consumer) {
        this(context, uri, consumer, true);
    }

    public UserContentObserver(Context context, Uri uri, Consumer<Uri> consumer, boolean enabled) {
        super(new Handler(context.getMainLooper()));
        mUserSwitchCallback = new SynchronousUserSwitchObserver() {
            public void onUserSwitching(int i) throws RemoteException {
                updateContentObserver();
                mCallback.accept(UserContentObserver.mSettingsUri);
            }
        };
        mContext = context;
        mSettingsUri = uri;
        mCallback = consumer;
        if (enabled) {
            activate();
        }
    }

    private void updateContentObserver() {
        mContext.getContentResolver().unregisterContentObserver(this);
        mContext.getContentResolver().registerContentObserver(mSettingsUri, false, this, -2);
    }
    public void activate() {
        updateContentObserver();
        try {
            ActivityManager.getService().registerUserSwitchObserver(mUserSwitchCallback, "Elmyra/UserContentObserver");
        } catch (RemoteException e) {
            Log.e("Elmyra/UserContentObserver", "Failed to register user switch observer", e);
        }
    }

    public void deactivate() {
        mContext.getContentResolver().unregisterContentObserver(this);
        try {
            ActivityManager.getService().unregisterUserSwitchObserver(mUserSwitchCallback);
        } catch (RemoteException e) {
            Log.e("Elmyra/UserContentObserver", "Failed to unregister user switch observer", e);
        }
    }

    public void updateContentObserver() {
        mContext.getContentResolver().unregisterContentObserver(this);
        mContext.getContentResolver().registerContentObserver(mSettingsUri, false, this);
    }

    public void onChange(boolean enabled, Uri uri) {
        mCallback.accept(uri);
    }
}
