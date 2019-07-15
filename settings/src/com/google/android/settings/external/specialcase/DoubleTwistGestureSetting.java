package com.google.android.settings.external.specialcase;

import android.os.UserManager;
import android.database.MatrixCursor;
import com.google.android.settings.external.ExternalSettingsContract;
import android.app.Fragment;
import com.android.settings.gestures.DoubleTwistGestureSettings;
import android.provider.Settings;
import android.database.Cursor;
import com.android.settings.gestures.DoubleTwistPreferenceController;
import android.content.Context;
import com.google.android.settings.external.Queryable;

public class DoubleTwistGestureSetting implements Queryable
{
    private final int OFF;
    private final int ON;
    
    public DoubleTwistGestureSetting() {
        this.ON = 1;
        this.OFF = 0;
    }
    
    private int getAvailability(final Context context) {
        int n;
        if (DoubleTwistPreferenceController.isGestureAvailable(context)) {
            n = 0;
        }
        else {
            n = 2;
        }
        return n;
    }
    
    private int getIconResource() {
        return 0;
    }
    
    private String getScreenTitle(final Context context) {
        return context.getString(2131887499);
    }
    
    private void validateInput(final int n) {
        if (n != 1 && n != 0) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Unexpected value for double twist camera. Expected 0 or 1, but found: ");
            sb.append(n);
            throw new IllegalArgumentException(sb.toString());
        }
    }
    
    public Cursor getAccessCursor(final Context context) {
        final int int1 = Settings.Secure.getInt(context.getContentResolver(), "camera_double_twist_to_flip_enabled", 1);
        final int availability = this.getAvailability(context);
        final String intentString = this.getIntentString(context, "gesture_double_twist", DoubleTwistGestureSettings.class, this.getScreenTitle(context));
        final int iconResource = this.getIconResource();
        final MatrixCursor matrixCursor = new MatrixCursor(ExternalSettingsContract.EXTERNAL_SETTINGS_QUERY_COLUMNS);
        matrixCursor.newRow().add("existing_value", (Object)int1).add("availability", (Object)availability).add("intent", (Object)intentString).add("icon", (Object)iconResource);
        return (Cursor)matrixCursor;
    }
    
    @Override
    public Cursor getUpdateCursor(final Context context, final int n) {
        this.validateInput(n);
        int int1;
        final int n2 = int1 = Settings.Secure.getInt(context.getContentResolver(), "camera_double_twist_to_flip_enabled", 1);
        final int availability = this.getAvailability(context);
        final String intentString = this.getIntentString(context, "gesture_double_twist", DoubleTwistGestureSettings.class, this.getScreenTitle(context));
        final int iconResource = this.getIconResource();
        if (this.shouldChangeValue(availability, n2, n)) {
            DoubleTwistPreferenceController.setDoubleTwistPreference(context, (UserManager)context.getSystemService("user"), n);
            int1 = n;
        }
        final MatrixCursor matrixCursor = new MatrixCursor(ExternalSettingsContract.EXTERNAL_SETTINGS_UPDATE_COLUMNS);
        matrixCursor.newRow().add("newValue", (Object)int1).add("existing_value", (Object)n2).add("availability", (Object)availability).add("intent", (Object)intentString).add("icon", (Object)iconResource);
        return (Cursor)matrixCursor;
    }
}
