package com.google.android.settings.gestures.assist;

import android.content.pm.PermissionInfo;
import android.content.res.Resources;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager;
import android.os.RemoteException;
import android.util.Log;

import com.android.internal.widget.ILockSettings;
import com.android.internal.widget.ILockSettings.Stub;
import android.os.ServiceManager;
import android.content.ContentResolver;
import android.provider.Settings;
import android.content.ComponentName;
import android.os.UserHandle;
import com.android.internal.app.AssistUtils;
import android.content.Context;
import com.android.settings.gestures.AssistGestureFeatureProviderImpl;

import com.android.settings.R;

public class AssistGestureFeatureProviderGoogleImpl extends AssistGestureFeatureProviderImpl
{
    private static boolean hasAssistGestureSensor(final Context context) {
        return context.getPackageManager().hasSystemFeature("android.hardware.sensor.assist");
    }

    private static boolean isGsaCurrentAssistant(final Context context) {
    final String string = "gesture_assist_component";
        final ComponentName assistComponentForUser = new AssistUtils(context).getAssistComponentForUser(UserHandle.myUserId());
        return assistComponentForUser != null && assistComponentForUser.flattenToString().equals(string);
    }

    private static boolean isOpaEligible(final Context context) {
        final ContentResolver contentResolver = context.getContentResolver();
        boolean b = false;
        if (Settings.Secure.getIntForUser(contentResolver, "systemui.google.opa_enabled", 0, -2) != 0) {
            b = true;
        }
        return b;
    }

    public static boolean isOpaEnabled(final Context context) {
        try {
            return ILockSettings.Stub.asInterface(ServiceManager.getService("lock_settings")).getBoolean("systemui.google.opa_user_enabled", false, -2);
        }
        catch (RemoteException ex) {
            Log.e("AssistGestureFeatureProviderGoogleImpl", "isOpaEnabled RemoteException", (Throwable)ex);
            return false;
        }
    }

    public boolean isDeskClockSupported(Context context) {
        PackageManager packageManager = context.getPackageManager();
        Resources resources = context.getResources();
        try {
            PermissionInfo permissionInfo =
                    packageManager.getPermissionInfo(
                            "com.google.android.deskclock.permission.RECEIVE_ALERT_BROADCASTS", 0);
            return permissionInfo != null
                    && permissionInfo.packageName.equals("com.google.android.deskclock");
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    @Override
    public boolean isSensorAvailable(final Context context) {
        return hasAssistGestureSensor(context);
    }

    @Override
    public boolean isSupported(final Context context) {
        return hasAssistGestureSensor(context) && isGsaCurrentAssistant(context) && isOpaEligible(context) && isOpaEnabled(context);
    }
}

