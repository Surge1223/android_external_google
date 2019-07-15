package com.google.android.settings.external.specialcase;

import android.database.MatrixCursor;
import com.google.android.settings.external.ExternalSettingsContract;
import android.app.Fragment;
import com.android.settings.location.LocationSettings;
import android.database.Cursor;
import android.content.Intent;
import android.os.UserManager;
import android.os.UserHandle;
import android.content.ContentResolver;
import android.provider.Settings;
import android.app.ActivityManager;
import android.content.Context;
import com.google.android.settings.external.Queryable;

public class LocationSetting implements Queryable
{
    private int getAvailability(final Context context) {
        if (this.isRestricted(context)) {
            return 6;
        }
        return 0;
    }
    
    private int getCurrentValue(final Context context) {
        final ContentResolver contentResolver = context.getContentResolver();
        final int currentUser = UserHandle.getUserHandleForUid(android.os.Process.myUid()).describeContents();
        int n = 0;

        return n;
    }
    
    private int getIconResource() {
        return 2131231151;
    }
    
    private String getScreenTitle(final Context context) {
        return context.getString(2131888059);
    }
    
    private boolean isRestricted(final Context context) {
        return ((UserManager)context.getSystemService("user")).hasUserRestriction("no_share_location");
    }
    
    private boolean updateLocationMode(final Context context, final int n, final int n2) {
        final Intent intent = new Intent("com.android.settings.location.MODE_CHANGING");
        intent.putExtra("CURRENT_MODE", n);
        intent.putExtra("NEW_MODE", n2);
        context.sendBroadcast(intent, "android.permission.WRITE_SECURE_SETTINGS");
        return Settings.Secure.putInt(context.getContentResolver(), "location_mode", n2);
    }
    
    private void validateInput(final int n) {
        if (n != 1 && n != 0) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Unexpected value for location toggle. Expected 0 or 1, but found: ");
            sb.append(n);
            throw new IllegalArgumentException(sb.toString());
        }
    }
    
    public Cursor getAccessCursor(final Context context) {
        final int currentValue = this.getCurrentValue(context);
        final int availability = this.getAvailability(context);
        final String intentString = this.getIntentString(context, "location", LocationSettings.class, this.getScreenTitle(context));
        final int iconResource = this.getIconResource();
        final MatrixCursor matrixCursor = new MatrixCursor(ExternalSettingsContract.EXTERNAL_SETTINGS_QUERY_COLUMNS);
        matrixCursor.newRow().add("existing_value", (Object)currentValue).add("availability", (Object)availability).add("intent", (Object)intentString).add("icon", (Object)iconResource);
        return (Cursor)matrixCursor;
    }
    
    @Override
    public Cursor getUpdateCursor(final Context context, final int n) {
        this.validateInput(n);
        final int currentValue = this.getCurrentValue(context);
        final int availability = this.getAvailability(context);
        final String intentString = this.getIntentString(context, "location", LocationSettings.class, this.getScreenTitle(context));
        final int iconResource = this.getIconResource();
        int n2 = currentValue;
        if (this.shouldChangeValue(availability, currentValue, n)) {
            n2 = n2;
            if (this.updateLocationMode(context, currentValue, n)) {
                n2 = n;
            }
        }
        final MatrixCursor matrixCursor = new MatrixCursor(ExternalSettingsContract.EXTERNAL_SETTINGS_UPDATE_COLUMNS);
        matrixCursor.newRow().add("newValue", (Object)n2).add("existing_value", (Object)currentValue).add("availability", (Object)availability).add("intent", (Object)intentString).add("icon", (Object)iconResource);
        return (Cursor)matrixCursor;
    }
}
