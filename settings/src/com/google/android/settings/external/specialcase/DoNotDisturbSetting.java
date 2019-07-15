package com.google.android.settings.external.specialcase;

import android.database.MatrixCursor;
import com.google.android.settings.external.ExternalSettingsContract;
import android.app.Fragment;
import com.android.settings.notification.ZenModeSettings;
import android.database.Cursor;
import android.content.Context;
import android.provider.Settings;
import android.app.NotificationManager;
import com.google.android.settings.external.Queryable;

public class DoNotDisturbSetting implements Queryable
{
    private int convertUserInputToInterruptionFilter(final NotificationManager notificationManager, final int n) {
        final StringBuilder sb = new StringBuilder();
        sb.append("Invalid input for Do Not Disturb. Found: ");
        sb.append(n);
        throw new IllegalArgumentException(sb.toString());
    }
    
    private int getAvailability() {
        return 0;
    }
    
    private int getCurrentMode(final NotificationManager notificationManager) {
		return 0;
    }
    
    private String getScreenTitle(final Context context) {
        return context.getString(2131890376);
    }
    
    public Cursor getAccessCursor(final Context context) {
        final int currentMode = this.getCurrentMode((NotificationManager)context.getSystemService("notification"));
        final int availability = this.getAvailability();
        final String intentString = this.getIntentString(context, "zen_mode", ZenModeSettings.class, this.getScreenTitle(context));
        final MatrixCursor matrixCursor = new MatrixCursor(ExternalSettingsContract.EXTERNAL_SETTINGS_QUERY_COLUMNS);
        matrixCursor.newRow().add("existing_value", (Object)currentMode).add("availability", (Object)availability).add("intent", (Object)intentString);
        return (Cursor)matrixCursor;
    }
    
    @Override
    public Cursor getUpdateCursor(final Context context, final int n) {
        final NotificationManager notificationManager = (NotificationManager)context.getSystemService("notification");
        final int currentMode = this.getCurrentMode(notificationManager);
        final int availability = this.getAvailability();
        final String intentString = this.getIntentString(context, "zen_mode", ZenModeSettings.class, this.getScreenTitle(context));
        if (this.shouldChangeValue(availability, currentMode, n)) {
            notificationManager.setInterruptionFilter(this.convertUserInputToInterruptionFilter(notificationManager, n));
        }
        final MatrixCursor matrixCursor = new MatrixCursor(ExternalSettingsContract.EXTERNAL_SETTINGS_UPDATE_COLUMNS);
        matrixCursor.newRow().add("newValue", (Object)n).add("existing_value", (Object)currentMode).add("availability", (Object)availability).add("intent", (Object)intentString);
        return (Cursor)matrixCursor;
    }
}
