package com.google.android.settings.external.specialcase;

import android.database.MatrixCursor;
import com.google.android.settings.external.ExternalSettingsContract;
import android.app.Fragment;
import com.android.settings.display.NightDisplaySettings;
import android.database.Cursor;
import com.android.internal.app.ColorDisplayController;
import android.content.Context;
import com.google.android.settings.external.Queryable;

public class NightDisplayIntensitySetting implements Queryable
{
    private int getAvailability(final Context context, final ColorDisplayController colorDisplayController) {
        if (!ColorDisplayController.isAvailable(context)) {
            return 2;
        }
        if (!colorDisplayController.isActivated()) {
            return 1;
        }
        return 0;
    }
    
    private int getColorTempFromIntensity(final int n, final int n2) {
        return n2 - n;
    }
    
    private int getIconResource() {
        return 2131231099;
    }
    
    private int getIntensity(final ColorDisplayController colorDisplayController, final int n) {
        return this.getIntensityFromColorTemp(colorDisplayController.getColorTemperature(), n);
    }
    
    private int getIntensityFromColorTemp(final int n, final int n2) {
        return n2 - n;
    }
    
    private String getScreenTitle(final Context context) {
        return context.getString(2131888405);
    }
    
    private void validateInput(final int n, int n2, final int n3) {
        n2 = n3 - n2;
        if (n >= 0 && n <= n2) {
            return;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Unexpected value for night display intensity. Expected value between: 0 and ");
        sb.append(n2);
        sb.append(" but found: ");
        sb.append(n);
        throw new IllegalArgumentException(sb.toString());
    }
    
    public Cursor getAccessCursor(final Context context) {
        final ColorDisplayController colorDisplayController = new ColorDisplayController(context);
        final int minimumColorTemperature = colorDisplayController.getMinimumColorTemperature();
        final int maximumColorTemperature = colorDisplayController.getMaximumColorTemperature();
        final int intensity = this.getIntensity(colorDisplayController, maximumColorTemperature);
        final int availability = this.getAvailability(context, colorDisplayController);
        final String intentString = this.getIntentString(context, "night_display_temperature", NightDisplaySettings.class, this.getScreenTitle(context));
        final int iconResource = this.getIconResource();
        final MatrixCursor matrixCursor = new MatrixCursor(ExternalSettingsContract.EXTERNAL_SETTINGS_QUERY_RANGE_COLUMNS);
        matrixCursor.newRow().add("existing_value", (Object)intensity).add("availability", (Object)availability).add("intent", (Object)intentString).add("icon", (Object)iconResource).add("min_value", (Object)0).add("max_value", (Object)(maximumColorTemperature - minimumColorTemperature)).add("dependent_setting", (Object)"night_display");
        return (Cursor)matrixCursor;
    }
    
    @Override
    public Cursor getUpdateCursor(final Context context, final int n) {
        final ColorDisplayController colorDisplayController = new ColorDisplayController(context);
        final int minimumColorTemperature = colorDisplayController.getMinimumColorTemperature();
        final int maximumColorTemperature = colorDisplayController.getMaximumColorTemperature();
        this.validateInput(n, minimumColorTemperature, maximumColorTemperature);
        final int intensity = this.getIntensity(colorDisplayController, maximumColorTemperature);
        final int availability = this.getAvailability(context, colorDisplayController);
        final String intentString = this.getIntentString(context, "night_display_temperature", NightDisplaySettings.class, this.getScreenTitle(context));
        final int iconResource = this.getIconResource();
        final int n2 = intensity;
        final int colorTempFromIntensity = this.getColorTempFromIntensity(n, maximumColorTemperature);
        int n3 = n2;
        if (this.shouldChangeValue(availability, intensity, n)) {
            n3 = n2;
            if (colorDisplayController.setColorTemperature(colorTempFromIntensity)) {
                n3 = n;
            }
        }
        final MatrixCursor matrixCursor = new MatrixCursor(ExternalSettingsContract.EXTERNAL_SETTINGS_UPDATE_COLUMNS);
        matrixCursor.newRow().add("newValue", (Object)n3).add("existing_value", (Object)intensity).add("availability", (Object)availability).add("intent", (Object)intentString).add("icon", (Object)iconResource).add("dependent_setting", (Object)"night_display");
        return (Cursor)matrixCursor;
    }
}
