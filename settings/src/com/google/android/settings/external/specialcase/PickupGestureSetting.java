package com.google.android.settings.external.specialcase;

import android.provider.Settings;
import android.database.MatrixCursor;
import com.google.android.settings.external.ExternalSettingsContract;
import android.app.Fragment;
import com.android.settings.gestures.PickupGestureSettings;
import android.database.Cursor;
import android.content.Context;
import android.os.UserHandle;
import com.android.internal.hardware.AmbientDisplayConfiguration;
import com.google.android.settings.external.Queryable;

public class PickupGestureSetting implements Queryable
{
    private int getAvailability(final AmbientDisplayConfiguration ambientDisplayConfiguration) {
        int n;
        if (ambientDisplayConfiguration.pulseOnPickupAvailable()) {
            if (ambientDisplayConfiguration.pulseOnPickupCanBeModified(UserHandle.getUserHandleForUid(android.os.Process.myUid()).describeContents())) {
                n = 0;
            }
            else {
                n = 1;
            }
        }
        else {
            n = 2;
        }
        return n;
    }
    
    private int getCurrentValue(final AmbientDisplayConfiguration ambientDisplayConfiguration) {
        return ambientDisplayConfiguration.pulseOnPickupEnabled(UserHandle.getUserHandleForUid(android.os.Process.myUid()).describeContents()) ? 1 : 0;
    }
    
    private String getScreenTitle(final Context context) {
        return context.getString(2131886301);
    }
    
    private void validateInput(final int n) {
        if (n != 1 && n != 0) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Unexpected value for pickup gesture. Expected 0 or 1, but found: ");
            sb.append(n);
            throw new IllegalArgumentException(sb.toString());
        }
    }
    
    public Cursor getAccessCursor(final Context context) {
        final AmbientDisplayConfiguration ambientDisplayConfiguration = new AmbientDisplayConfiguration(context);
        final int currentValue = this.getCurrentValue(ambientDisplayConfiguration);
        final int availability = this.getAvailability(ambientDisplayConfiguration);
        final String intentString = this.getIntentString(context, "gesture_pick_up", PickupGestureSettings.class, this.getScreenTitle(context));
        final int iconResource = this.getIconResource();
        final MatrixCursor matrixCursor = new MatrixCursor(ExternalSettingsContract.EXTERNAL_SETTINGS_QUERY_COLUMNS);
        matrixCursor.newRow().add("existing_value", (Object)currentValue).add("availability", (Object)availability).add("intent", (Object)intentString).add("icon", (Object)iconResource).add("dependent_setting", (Object)"ambient_display_always_on");
        return (Cursor)matrixCursor;
    }
    
    public int getIconResource() {
        return 0;
    }
    
    @Override
    public Cursor getUpdateCursor(final Context context, final int n) {
        this.validateInput(n);
        final AmbientDisplayConfiguration ambientDisplayConfiguration = new AmbientDisplayConfiguration(context);
        final int currentValue = this.getCurrentValue(ambientDisplayConfiguration);
        final int availability = this.getAvailability(ambientDisplayConfiguration);
        final String intentString = this.getIntentString(context, "gesture_pick_up", PickupGestureSettings.class, this.getScreenTitle(context));
        final int iconResource = this.getIconResource();
        int n2 = currentValue;
        if (this.shouldChangeValue(availability, currentValue, n)) {
            Settings.Secure.putInt(context.getContentResolver(), "doze_pulse_on_pick_up", n);
            n2 = n;
        }
        final MatrixCursor matrixCursor = new MatrixCursor(ExternalSettingsContract.EXTERNAL_SETTINGS_UPDATE_COLUMNS);
        matrixCursor.newRow().add("newValue", (Object)n2).add("existing_value", (Object)currentValue).add("availability", (Object)availability).add("intent", (Object)intentString).add("icon", (Object)iconResource).add("dependent_setting", (Object)"ambient_display_always_on");
        return (Cursor)matrixCursor;
    }
}
