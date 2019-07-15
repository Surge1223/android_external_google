package com.google.android.settings.external.specialcase;

import android.content.ContentResolver;
import android.database.MatrixCursor;
import com.google.android.settings.external.ExternalSettingsContract;
import android.app.Fragment;
import com.android.settings.accessibility.AccessibilitySettings;
import android.provider.Settings;
import android.database.Cursor;
import android.content.Context;
import android.view.accessibility.AccessibilityManager;
import com.google.android.settings.external.Queryable;

public class MagnificationSetting implements Queryable
{
    private final String mKey;
    private final String mSecureKey;
    
    public MagnificationSetting(final String mKey, final String mSecureKey) {
        this.mKey = mKey;
        this.mSecureKey = mSecureKey;
    }
    
    private int getAvailability() {
        if (this.mKey != "magnify_gesture" && !AccessibilityManager.isAccessibilityButtonSupported()) {
            return 2;
        }
        return 0;
    }
    
    private int getIconResource() {
        return 0;
    }
    
    private String getScreenTitle(final Context context) {
        return context.getString(2131886188);
    }
    
    private void validateInput(final int n) {
        if (n != 1 && n != 0) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Unexpected value for magnification settings. Expected 0 or 1, but found: ");
            sb.append(n);
            throw new IllegalArgumentException(sb.toString());
        }
    }
    
    public Cursor getAccessCursor(final Context context) {
        final int int1 = Settings.Secure.getInt(context.getContentResolver(), this.mSecureKey, 0);
        final int availability = this.getAvailability();
        final String intentString = this.getIntentString(context, "magnification_preference_screen", AccessibilitySettings.class, this.getScreenTitle(context));
        final int iconResource = this.getIconResource();
        final MatrixCursor matrixCursor = new MatrixCursor(ExternalSettingsContract.EXTERNAL_SETTINGS_QUERY_COLUMNS);
        matrixCursor.newRow().add("existing_value", (Object)int1).add("availability", (Object)availability).add("intent", (Object)intentString).add("icon", (Object)iconResource);
        return (Cursor)matrixCursor;
    }
    
    @Override
    public Cursor getUpdateCursor(final Context context, final int n) {
        this.validateInput(n);
        final ContentResolver contentResolver = context.getContentResolver();
        final int int1 = Settings.Secure.getInt(contentResolver, this.mSecureKey, 0);
        final int availability = this.getAvailability();
        final String intentString = this.getIntentString(context, "magnification_preference_screen", AccessibilitySettings.class, this.getScreenTitle(context));
        final int iconResource = this.getIconResource();
        int n2 = int1;
        if (this.shouldChangeValue(availability, int1, n)) {
            n2 = n2;
            if (Settings.Secure.putInt(contentResolver, this.mSecureKey, n)) {
                n2 = n;
            }
        }
        final MatrixCursor matrixCursor = new MatrixCursor(ExternalSettingsContract.EXTERNAL_SETTINGS_UPDATE_COLUMNS);
        matrixCursor.newRow().add("newValue", (Object)n2).add("existing_value", (Object)int1).add("availability", (Object)availability).add("intent", (Object)intentString).add("icon", (Object)iconResource);
        return (Cursor)matrixCursor;
    }
}
