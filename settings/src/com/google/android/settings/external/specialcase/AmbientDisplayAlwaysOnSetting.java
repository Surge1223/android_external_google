package com.google.android.settings.external.specialcase;

import android.provider.Settings;
import android.database.MatrixCursor;
import com.google.android.settings.external.ExternalSettingsContract;
import android.app.Fragment;
import com.android.settings.display.AmbientDisplaySettings;
import android.database.Cursor;
import android.content.Context;
import com.android.settings.display.AmbientDisplayAlwaysOnPreferenceController;
import com.android.internal.hardware.AmbientDisplayConfiguration;
import com.google.android.settings.external.Queryable;

public class AmbientDisplayAlwaysOnSetting implements Queryable
{
    private final int OFF;
    private final int ON;
    
    public AmbientDisplayAlwaysOnSetting() {
        this.ON = 1;
        this.OFF = 0;
    }
    
    private int getAvailability(final AmbientDisplayConfiguration ambientDisplayConfiguration) {
        int n;
        if (AmbientDisplayAlwaysOnPreferenceController.accessibilityInversionEnabled(ambientDisplayConfiguration)) {
            n = 1;
        }
        else if (AmbientDisplayAlwaysOnPreferenceController.isAvailable(ambientDisplayConfiguration)) {
            n = 0;
        }
        else {
            n = 2;
        }
        return n;
    }
    
    private int getCurrentValue(final AmbientDisplayConfiguration ambientDisplayConfiguration) {
        return AmbientDisplayAlwaysOnPreferenceController.isAlwaysOnEnabled(ambientDisplayConfiguration) ? 1 : 0;
    }
    
    private int getIconResource() {
        return 0;
    }
    
    private String getScreenTitle(final Context context) {
        return context.getString(2131886304);
    }
    
    private void validateInput(final int n) {
        if (n != 1 && n != 0) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Unexpected value for always on ambient display. Expected 0 or 1, but found: ");
            sb.append(n);
            throw new IllegalArgumentException(sb.toString());
        }
    }
    
    public Cursor getAccessCursor(final Context context) {
        final AmbientDisplayConfiguration ambientDisplayConfiguration = new AmbientDisplayConfiguration(context);
        final int currentValue = this.getCurrentValue(ambientDisplayConfiguration);
        final int availability = this.getAvailability(ambientDisplayConfiguration);
        final String intentString = this.getIntentString(context, "ambient_display_always_on", AmbientDisplaySettings.class, this.getScreenTitle(context));
        final int iconResource = this.getIconResource();
        final MatrixCursor matrixCursor = new MatrixCursor(ExternalSettingsContract.EXTERNAL_SETTINGS_QUERY_COLUMNS);
        matrixCursor.newRow().add("existing_value", (Object)currentValue).add("availability", (Object)availability).add("intent", (Object)intentString).add("icon", (Object)iconResource).add("dependent_setting", (Object)"color_inversion");
        return (Cursor)matrixCursor;
    }
    
    @Override
    public Cursor getUpdateCursor(final Context context, final int n) {
        this.validateInput(n);
        final AmbientDisplayConfiguration ambientDisplayConfiguration = new AmbientDisplayConfiguration(context);
        final int currentValue = this.getCurrentValue(ambientDisplayConfiguration);
        final int availability = this.getAvailability(ambientDisplayConfiguration);
        final String intentString = this.getIntentString(context, "ambient_display_always_on", AmbientDisplaySettings.class, this.getScreenTitle(context));
        final int iconResource = this.getIconResource();
        int n2 = currentValue;
        if (this.shouldChangeValue(availability, currentValue, n)) {
            Settings.Secure.putInt(context.getContentResolver(), "doze_always_on", n);
            n2 = n;
        }
        final MatrixCursor matrixCursor = new MatrixCursor(ExternalSettingsContract.EXTERNAL_SETTINGS_UPDATE_COLUMNS);
        matrixCursor.newRow().add("newValue", (Object)n2).add("existing_value", (Object)currentValue).add("availability", (Object)availability).add("intent", (Object)intentString).add("icon", (Object)iconResource).add("dependent_setting", (Object)"color_inversion");
        return (Cursor)matrixCursor;
    }
}
