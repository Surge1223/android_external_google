package com.google.android.settings.external.specialcase;

import android.database.MatrixCursor;
import com.google.android.settings.external.ExternalSettingsContract;
import android.app.Fragment;
import com.android.settings.datausage.DataUsageSummary;
import android.database.Cursor;
import android.content.Context;
import com.android.settingslib.net.DataUsageController;
import com.google.android.settings.external.Queryable;

public class MobileDataSetting implements Queryable
{
    private final int OFF;
    private final int ON;
    
    public MobileDataSetting() {
        this.ON = 1;
        this.OFF = 0;
    }
    
    private int getAvailability(final DataUsageController dataUsageController) {
        int n;
        if (dataUsageController.isMobileDataSupported()) {
            n = 0;
        }
        else {
            n = 2;
        }
        return n;
    }
    
    private int getCurrentValue(final DataUsageController dataUsageController) {
        return dataUsageController.isMobileDataEnabled() ? 1 : 0;
    }
    
    private int getIconResource() {
        return 2131231070;
    }
    
    private String getScreenTitle(final Context context) {
        return context.getString(2131887285);
    }
    
    private void validateInput(final int n) {
        if (n != 1 && n != 0) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Unexpected value for mobile data mode. Expected 0 or 1, but found: ");
            sb.append(n);
            throw new IllegalArgumentException(sb.toString());
        }
    }
    
    public Cursor getAccessCursor(final Context context) {
        final DataUsageController dataUsageController = new DataUsageController(context);
        final int currentValue = this.getCurrentValue(dataUsageController);
        final int availability = this.getAvailability(dataUsageController);
        final String intentString = this.getIntentString(context, "mobile_data", DataUsageSummary.class, this.getScreenTitle(context));
        final int iconResource = this.getIconResource();
        final MatrixCursor matrixCursor = new MatrixCursor(ExternalSettingsContract.EXTERNAL_SETTINGS_QUERY_COLUMNS);
        matrixCursor.newRow().add("existing_value", (Object)currentValue).add("availability", (Object)availability).add("intent", (Object)intentString).add("icon", (Object)iconResource);
        return (Cursor)matrixCursor;
    }
    
    @Override
    public Cursor getUpdateCursor(final Context context, final int n) {
        this.validateInput(n);
        final DataUsageController dataUsageController = new DataUsageController(context);
        final int currentValue = this.getCurrentValue(dataUsageController);
        final int availability = this.getAvailability(dataUsageController);
        final String intentString = this.getIntentString(context, "mobile_data", DataUsageSummary.class, this.getScreenTitle(context));
        final int iconResource = this.getIconResource();
        int n2 = currentValue;
        if (this.shouldChangeValue(availability, currentValue, n)) {
            boolean mobileDataEnabled = true;
            if (n != 1) {
                mobileDataEnabled = false;
            }
            dataUsageController.setMobileDataEnabled(mobileDataEnabled);
            n2 = n;
        }
        final MatrixCursor matrixCursor = new MatrixCursor(ExternalSettingsContract.EXTERNAL_SETTINGS_UPDATE_COLUMNS);
        matrixCursor.newRow().add("newValue", (Object)n2).add("existing_value", (Object)currentValue).add("availability", (Object)availability).add("intent", (Object)intentString).add("icon", (Object)iconResource);
        return (Cursor)matrixCursor;
    }
}
