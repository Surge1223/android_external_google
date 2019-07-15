package com.google.android.settings.external.specialcase;

import android.database.MatrixCursor;
import com.google.android.settings.external.ExternalSettingsContract;
import android.app.Fragment;
import com.android.settings.gestures.AssistGestureSettings;
import android.database.Cursor;
import android.content.ContentResolver;
import android.provider.Settings;
import com.android.settings.overlay.FeatureFactory;
import android.content.Context;
import com.google.android.settings.external.Queryable;
import com.android.settings.R;


public class ActiveEdgeSetting implements Queryable
{
    private final String SECURE_KEY;
    
    public ActiveEdgeSetting() {
        this.SECURE_KEY = "assist_gesture_enabled";
    }
    
    private int getAvailability(final Context context) {
        int n;
        if (FeatureFactory.getFactory(context).getAssistGestureFeatureProvider().isSupported(context)) {
            n = 0;
        }
        else {
            n = 2;
        }
        return n;
    }
    
    private int getCurrentValue(final Context context) {
        final ContentResolver contentResolver = context.getContentResolver();
        int n = 1;
        if (Settings.Secure.getInt(contentResolver, "assist_gesture_enabled", 1) == 0) {
            n = 0;
        }
        return n;
    }
    
    private int getIconResource() {
        return 0;
    }
    
    private String getScreenTitle(final Context context) {
        return context.getString(R.string.assist_gesture_title);
    }
    
    private void validateInput(final int n) {
        if (n != 1 && n != 0) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Unexpected value for Assist gesture. Expected 0 or 1, but found: ");
            sb.append(n);
            throw new IllegalArgumentException(sb.toString());
        }
    }
    
    public Cursor getAccessCursor(final Context context) {
        final int currentValue = this.getCurrentValue(context);
        final int availability = this.getAvailability(context);
        final int iconResource = this.getIconResource();
        final String intentString = this.getIntentString(context, "gesture_assist", AssistGestureSettings.class, this.getScreenTitle(context));
        final MatrixCursor matrixCursor = new MatrixCursor(ExternalSettingsContract.EXTERNAL_SETTINGS_QUERY_COLUMNS);
        matrixCursor.newRow().add("existing_value", (Object)currentValue).add("availability", (Object)availability).add("intent", (Object)intentString).add("icon", (Object)iconResource);
        return (Cursor)matrixCursor;
    }
    
    @Override
    public Cursor getUpdateCursor(final Context context, final int n) {
        this.validateInput(n);
        final int currentValue = this.getCurrentValue(context);
        final int availability = this.getAvailability(context);
        final String intentString = this.getIntentString(context, "gesture_assist", AssistGestureSettings.class, this.getScreenTitle(context));
        final int iconResource = this.getIconResource();
        int n2 = currentValue;
        if (this.shouldChangeValue(availability, currentValue, n)) {
            n2 = n2;
            if (Settings.Secure.putInt(context.getContentResolver(), "assist_gesture_enabled", n)) {
                n2 = n;
            }
        }
        final MatrixCursor matrixCursor = new MatrixCursor(ExternalSettingsContract.EXTERNAL_SETTINGS_UPDATE_COLUMNS);
        matrixCursor.newRow().add("newValue", (Object)n2).add("existing_value", (Object)currentValue).add("availability", (Object)availability).add("intent", (Object)intentString).add("icon", (Object)iconResource);
        return (Cursor)matrixCursor;
    }
}
