package com.google.android.settings.external.specialcase;

import android.content.ContentResolver;
import android.os.UserHandle;
import android.content.Intent;
import android.database.MatrixCursor;
import com.google.android.settings.external.ExternalSettingsContract;
import android.app.Fragment;
import com.android.settings.network.NetworkDashboardFragment;
import android.provider.Settings;
import android.database.Cursor;
import com.android.settings.network.AirplaneModePreferenceController;
import android.content.Context;
import com.google.android.settings.external.Queryable;

public class AirplaneModeSetting implements Queryable
{
    private int getAvailability(final Context context) {
        int n;
        if (AirplaneModePreferenceController.isAvailable(context)) {
            n = 0;
        }
        else {
            n = 2;
        }
        return n;
    }
    
    private int getIconResource() {
        return 2131230948;
    }
    
    private String getScreenTitle(final Context context) {
        return context.getString(2131888342);
    }
    
    private void validateInput(final int n) {
        if (n != 1 && n != 0) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Unexpected value for airplane mode. Expected 0 or 1, but found: ");
            sb.append(n);
            throw new IllegalArgumentException(sb.toString());
        }
    }
    
    public Cursor getAccessCursor(final Context context) {
        final int int1 = Settings.Global.getInt(context.getContentResolver(), "airplane_mode_on", 0);
        final int availability = this.getAvailability(context);
        final String intentString = this.getIntentString(context, "toggle_airplane", NetworkDashboardFragment.class, this.getScreenTitle(context));
        final int iconResource = this.getIconResource();
        final MatrixCursor matrixCursor = new MatrixCursor(ExternalSettingsContract.EXTERNAL_SETTINGS_QUERY_COLUMNS);
        matrixCursor.newRow().add("existing_value", (Object)int1).add("availability", (Object)availability).add("intent", (Object)intentString).add("icon", (Object)iconResource);
        return (Cursor)matrixCursor;
    }
    
    @Override
    public Cursor getUpdateCursor(final Context context, final int n) {
        this.validateInput(n);
        final ContentResolver contentResolver = context.getContentResolver();
        final int availability = this.getAvailability(context);
        final String intentString = this.getIntentString(context, "toggle_airplane", NetworkDashboardFragment.class, this.getScreenTitle(context));
        final int iconResource = this.getIconResource();
        boolean b = false;
        int int1;
        final int n2 = int1 = Settings.Global.getInt(contentResolver, "airplane_mode_on", 0);
        if (this.shouldChangeValue(availability, n2, n)) {
            int1 = int1;
            if (Settings.Global.putInt(contentResolver, "airplane_mode_on", n)) {
                final Intent intent = new Intent("android.intent.action.AIRPLANE_MODE");
                if (n == 1) {
                    b = true;
                }
                intent.putExtra("state", b);
                context.sendBroadcastAsUser(intent, UserHandle.getUserHandleForUid(-1));
                int1 = n;
            }
        }
        final MatrixCursor matrixCursor = new MatrixCursor(ExternalSettingsContract.EXTERNAL_SETTINGS_UPDATE_COLUMNS);
        matrixCursor.newRow().add("newValue", (Object)int1).add("existing_value", (Object)n2).add("availability", (Object)availability).add("intent", (Object)intentString).add("icon", (Object)iconResource);
        return (Cursor)matrixCursor;
    }
}
