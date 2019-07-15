package com.google.android.settings.external.specialcase;

import android.database.MatrixCursor;
import com.google.android.settings.external.ExternalSettingsContract;
import android.app.Fragment;
import com.android.settings.datausage.DataUsageSummary;
import android.database.Cursor;
import com.android.settings.datausage.DataSaverBackend;
import com.android.settings.datausage.DataUsageUtils;
import android.content.Context;
import com.google.android.settings.external.Queryable;

public class DataSaverSetting implements Queryable
{
    private int getAvailability(final Context context) {
        int n;
        if (DataUsageUtils.hasMobileData(context)) {
            n = 0;
        }
        else {
            n = 2;
        }
        return n;
    }
    
    private int getCurrentValue(final DataSaverBackend dataSaverBackend) {
        return dataSaverBackend.isDataSaverEnabled() ? 1 : 0;
    }
    
    private int getIconResource() {
        return 2131231121;
    }
    
    private String getScreenTitle(final Context context) {
        return context.getString(2131887285);
    }
    
    private void validateInput(final int n) {
        if (n != 1 && n != 0) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Unexpected value for data saver. Expected 0 or 1, but found: ");
            sb.append(n);
            throw new IllegalArgumentException(sb.toString());
        }
    }
    
    public Cursor getAccessCursor(final Context context) {
        final int currentValue = this.getCurrentValue(new DataSaverBackend(context));
        final int availability = this.getAvailability(context);
        final String intentString = this.getIntentString(context, "data_saver", DataUsageSummary.class, this.getScreenTitle(context));
        final int iconResource = this.getIconResource();
        final MatrixCursor matrixCursor = new MatrixCursor(ExternalSettingsContract.EXTERNAL_SETTINGS_QUERY_COLUMNS);
        matrixCursor.newRow().add("existing_value", (Object)currentValue).add("availability", (Object)availability).add("intent", (Object)intentString).add("icon", (Object)iconResource);
        return (Cursor)matrixCursor;
    }
    
    @Override
    public Cursor getUpdateCursor(final Context context, final int n) {
        this.validateInput(n);
        final DataSaverBackend dataSaverBackend = new DataSaverBackend(context);
        final int currentValue = this.getCurrentValue(dataSaverBackend);
        final int availability = this.getAvailability(context);
        final String intentString = this.getIntentString(context, "data_saver", DataUsageSummary.class, this.getScreenTitle(context));
        final int iconResource = this.getIconResource();
        int n2 = currentValue;
        if (this.shouldChangeValue(availability, currentValue, n)) {
            boolean dataSaverEnabled = true;
            if (n != 1) {
                dataSaverEnabled = false;
            }
            dataSaverBackend.setDataSaverEnabled(dataSaverEnabled);
            n2 = n;
        }
        final MatrixCursor matrixCursor = new MatrixCursor(ExternalSettingsContract.EXTERNAL_SETTINGS_UPDATE_COLUMNS);
        matrixCursor.newRow().add("newValue", (Object)n2).add("existing_value", (Object)currentValue).add("availability", (Object)availability).add("intent", (Object)intentString).add("icon", (Object)iconResource);
        return (Cursor)matrixCursor;
    }
}
