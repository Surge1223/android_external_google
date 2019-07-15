package com.google.android.settings.external.specialcase;

import android.database.MatrixCursor;
import com.google.android.settings.external.ExternalSettingsContract;
import android.app.Fragment;
import com.android.settings.accessibility.AccessibilitySettings;
import android.database.Cursor;
import android.content.Context;
import android.provider.Settings;
import android.content.ContentResolver;
import com.google.android.settings.external.Queryable;

public class ColorInversionSetting implements Queryable
{
    private int getCurrentValue(final ContentResolver contentResolver) {
        return Settings.Secure.getInt(contentResolver, "accessibility_display_inversion_enabled", 0);
    }
    
    private String getScreenTitle(final Context context) {
        return context.getString(2131886186);
    }
    
    private void validateInput(final int n) {
        if (n != 1 && n != 0) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Unexpected value for display inversion. Expected 0 or 1, but found: ");
            sb.append(n);
            throw new IllegalArgumentException(sb.toString());
        }
    }
    
    public Cursor getAccessCursor(final Context context) {
        final int currentValue = this.getCurrentValue(context.getContentResolver());
        final String intentString = this.getIntentString(context, "color_inversion", AccessibilitySettings.class, this.getScreenTitle(context));
        final MatrixCursor matrixCursor = new MatrixCursor(ExternalSettingsContract.EXTERNAL_SETTINGS_QUERY_COLUMNS);
        matrixCursor.newRow().add("existing_value", (Object)currentValue).add("availability", (Object)0).add("intent", (Object)intentString);
        return (Cursor)matrixCursor;
    }
    
    @Override
    public Cursor getUpdateCursor(final Context context, final int n) {
        this.validateInput(n);
        final ContentResolver contentResolver = context.getContentResolver();
        final int currentValue = this.getCurrentValue(contentResolver);
        final String intentString = this.getIntentString(context, "color_inversion", AccessibilitySettings.class, this.getScreenTitle(context));
        int n2 = currentValue;
        if (this.shouldChangeValue(0, currentValue, n)) {
            n2 = n2;
            if (Settings.Secure.putInt(contentResolver, "accessibility_display_inversion_enabled", n)) {
                n2 = n;
            }
        }
        final MatrixCursor matrixCursor = new MatrixCursor(ExternalSettingsContract.EXTERNAL_SETTINGS_UPDATE_COLUMNS);
        matrixCursor.newRow().add("newValue", (Object)n2).add("existing_value", (Object)currentValue).add("availability", (Object)0).add("intent", (Object)intentString);
        return (Cursor)matrixCursor;
    }
}
