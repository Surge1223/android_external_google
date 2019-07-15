package com.google.android.settings.external.specialcase;

import android.database.MatrixCursor;
import com.google.android.settings.external.ExternalSettingsContract;
import android.app.Fragment;
import com.android.settings.DisplaySettings;
import android.database.Cursor;
import com.android.internal.view.RotationPolicy;
import android.content.Context;
import com.google.android.settings.external.Queryable;

public class AutoRotateSetting implements Queryable
{
    private int getAvailability(final Context context) {
        int n;
        if (RotationPolicy.isRotationSupported(context)) {
            n = 0;
        }
        else {
            n = 2;
        }
        return n;
    }
    
    private int getCurrentValue(final Context context) {
        return RotationPolicy.isRotationLocked(context) ? 0 : 1;
    }
    
    private int getIconResource() {
        return 2131231050;
    }
    
    private String getScreenTitle(final Context context) {
        return context.getString(2131887447);
    }
    
    private void updateAutoRotate(final Context context, final int n) {
        RotationPolicy.setRotationLock(context, n == 0);
    }
    
    private void validateInput(final int n) {
        if (n != 1 && n != 0) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Unexpected value for auto rotate mode. Expected 0 or 1, but found: ");
            sb.append(n);
            throw new IllegalArgumentException(sb.toString());
        }
    }
    
    public Cursor getAccessCursor(final Context context) {
        final int currentValue = this.getCurrentValue(context);
        final int availability = this.getAvailability(context);
        final String intentString = this.getIntentString(context, "toggle_lock_screen_rotation_preference", DisplaySettings.class, this.getScreenTitle(context));
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
        final String intentString = this.getIntentString(context, "toggle_lock_screen_rotation_preference", DisplaySettings.class, this.getScreenTitle(context));
        final int iconResource = this.getIconResource();
        int n2 = currentValue;
        if (this.shouldChangeValue(availability, currentValue, n)) {
            this.updateAutoRotate(context, n);
            n2 = n;
        }
        final MatrixCursor matrixCursor = new MatrixCursor(ExternalSettingsContract.EXTERNAL_SETTINGS_UPDATE_COLUMNS);
        matrixCursor.newRow().add("newValue", (Object)n2).add("existing_value", (Object)currentValue).add("availability", (Object)availability).add("intent", (Object)intentString).add("icon", (Object)iconResource);
        return (Cursor)matrixCursor;
    }
}
