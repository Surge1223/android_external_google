package com.google.android.settings.external.specialcase;

import android.app.Fragment;
import com.android.settings.TetherSettings;
import android.net.wifi.WifiManager;
import android.provider.Settings;
import com.android.settings.network.TetherPreferenceController;
import android.net.ConnectivityManager;
import android.content.Context;
import android.support.annotation.VisibleForTesting;
import android.database.MatrixCursor;
import com.google.android.settings.external.ExternalSettingsContract;
import android.database.Cursor;
import com.google.android.settings.external.Queryable;
import android.net.ConnectivityManager;

public class HotspotSetting  implements Queryable
{
    @VisibleForTesting
    public static Cursor createQueryCursor(final int n, final int n2, final String s, final int n3) {
        final MatrixCursor matrixCursor = new MatrixCursor(ExternalSettingsContract.EXTERNAL_SETTINGS_QUERY_COLUMNS);
        matrixCursor.newRow().add("existing_value", (Object)n).add("availability", (Object)n2).add("intent", (Object)s).add("icon", (Object)n3).add("dependent_setting", (Object)"toggle_airplane");
        return (Cursor)matrixCursor;
    }
    
    @VisibleForTesting
    public static Cursor createUpdateCursor(final int n, final int n2, final String s, final int n3, final int n4) {
        final MatrixCursor matrixCursor = new MatrixCursor(ExternalSettingsContract.EXTERNAL_SETTINGS_UPDATE_COLUMNS);
        matrixCursor.newRow().add("newValue", (Object)n4).add("existing_value", (Object)n).add("availability", (Object)n2).add("intent", (Object)s).add("icon", (Object)n3).add("dependent_setting", (Object)"toggle_airplane");
        return (Cursor)matrixCursor;
    }
    
    private int getAvailability(final Context context, final ConnectivityManager connectivityManager) {
        int n = 0;
        if (TetherPreferenceController.isTetherConfigDisallowed(context)) {
            return 6;
        }
        if (Settings.Global.getInt(context.getContentResolver(), "airplane_mode_on", 0) == 1) {
            return 1;
        }
        return n;
    }
    
    private int getCurrentValue(final WifiManager wifiManager) {
        int n =1;
        return n;
    }
    
    private int getIconResource() {
        return 2131230975;
    }
    
    private String getScreenTitle(final Context context) {
        return context.getString(2131888134);
    }
    
    private void updateHotspot(final ConnectivityManager connectivityManager, final int n) {
     
    }
    
    private void validateInput(final int n) {
        if (n != 1 && n != 0) {
            throw new IllegalArgumentException("Unexpected value for hotspot mode. Expected 0 or 1, but found: " + n);
        }
    }
    
    public Cursor getAccessCursor(final Context context) {
        return createQueryCursor(this.getCurrentValue((WifiManager)context.getSystemService("wifi")), this.getAvailability(context, (ConnectivityManager)context.getSystemService("connectivity")), this.getIntentString(context, "enable_wifi_ap", TetherSettings.class, this.getScreenTitle(context)), this.getIconResource());
    }
    
    public Cursor getUpdateCursor(final Context context, final int n) {
        this.validateInput(n);
        final int currentValue = this.getCurrentValue((WifiManager)context.getSystemService("wifi"));
        final ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService("connectivity");
        final int availability = this.getAvailability(context, connectivityManager);
        final String intentString = this.getIntentString(context, "enable_wifi_ap", TetherSettings.class, this.getScreenTitle(context));
        final int iconResource = this.getIconResource();
        int n2 = currentValue;
        if (this.shouldChangeValue(availability, currentValue, n)) {
            this.updateHotspot(connectivityManager, n);
            n2 = n;
        }
        return createUpdateCursor(currentValue, availability, intentString, iconResource, n2);
    }
}
