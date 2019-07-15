package com.google.android.settings.external.specialcase;

import android.content.ComponentName;
import android.os.UserHandle;
import android.database.MatrixCursor;
import com.google.android.settings.external.ExternalSettingsContract;
import android.app.Fragment;
import com.android.settings.DisplaySettings;
import android.database.Cursor;
import android.text.TextUtils;
import java.util.Arrays;
import android.app.admin.DevicePolicyManager;
import android.provider.Settings;
import android.content.Context;
import com.google.android.settings.external.Queryable;

public class DisplayTimeoutSetting implements Queryable
{
    private long getCurrentValue(final Context context) {
        return Settings.System.getLong(context.getContentResolver(), "screen_off_timeout", 30000L);
    }
    
    private String getScreenTitle(final Context context) {
        return context.getString(2131887447);
    }
    
    private String[] getSupportedStringValues(final Context context) {
        final String[] stringArray = context.getResources().getStringArray(2130903139);
        final DevicePolicyManager devicePolicyManager = (DevicePolicyManager)context.getSystemService("device_policy");
        if (devicePolicyManager == null) {
            return stringArray;
        }
        final long maxTimeout = this.getMaxTimeout(devicePolicyManager);
        final int n = -1;
        int n2 = stringArray.length - 1;
        int n3;
        while (true) {
            n3 = n;
            if (n2 < 0) {
                break;
            }
            if (Integer.valueOf(stringArray[n2]) <= maxTimeout) {
                n3 = n2 + 1;
                break;
            }
            --n2;
        }
        if (n3 == -1) {
            return new String[0];
        }
        return Arrays.copyOfRange(stringArray, 0, n3);
    }
    
    private String getSupportedTimeouts(final Context context) {
        return TextUtils.join((CharSequence)",", (Object[])this.getSupportedStringValues(context));
    }
    
    public Cursor getAccessCursor(final Context context) {
        final long currentValue = this.getCurrentValue(context);
        final String intentString = this.getIntentString(context, "screen_timeout", DisplaySettings.class, this.getScreenTitle(context));
        final String supportedTimeouts = this.getSupportedTimeouts(context);
        final MatrixCursor matrixCursor = new MatrixCursor(ExternalSettingsContract.EXTERNAL_SETTINGS_QUERY_COLUMNS_WITH_SUPPORTED_VALUES);
        matrixCursor.newRow().add("existing_value", (Object)currentValue).add("availability", (Object)0).add("intent", (Object)intentString).add("icon", (Object)0).add("supported_values", (Object)supportedTimeouts);
        return (Cursor)matrixCursor;
    }
    
    public long getMaxTimeout(final DevicePolicyManager devicePolicyManager) {
        return devicePolicyManager.getMaximumTimeToLock((ComponentName)null);
    }
    
    @Override
    public Cursor getUpdateCursor(final Context context, final int n) {
        final long currentValue = this.getCurrentValue(context);
        final String intentString = this.getIntentString(context, "screen_timeout", DisplaySettings.class, this.getScreenTitle(context));
        long n2 = currentValue;
        if (this.shouldChangeValue(0, currentValue, n)) {
            n2 = n2;
            if (Settings.System.putInt(context.getContentResolver(), "screen_off_timeout", n)) {
                n2 = n;
            }
        }
        final MatrixCursor matrixCursor = new MatrixCursor(ExternalSettingsContract.EXTERNAL_SETTINGS_UPDATE_COLUMNS);
        matrixCursor.newRow().add("newValue", (Object)n2).add("existing_value", (Object)currentValue).add("availability", (Object)0).add("intent", (Object)intentString).add("icon", (Object)0);
        return (Cursor)matrixCursor;
    }
}
