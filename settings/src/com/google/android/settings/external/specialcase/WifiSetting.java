package com.google.android.settings.external.specialcase;

import android.database.MatrixCursor;
import com.google.android.settings.external.ExternalSettingsContract;
import android.app.Fragment;
import com.android.settings.wifi.WifiSettings;
import android.net.wifi.WifiManager;
import android.database.Cursor;
import android.content.Context;
import com.google.android.settings.external.Queryable;

public class WifiSetting implements Queryable
{
    private int getIconResource() {
        return 2131231146;
    }
    
    private String getScreenTitle(final Context context) {
        return context.getString(2131890112);
    }
    
    public Cursor getAccessCursor(final Context context) {
        int n;
        if (((WifiManager)context.getSystemService("wifi")).isWifiEnabled()) {
            n = 1;
        }
        else {
            n = 0;
        }
        final String intentString = this.getIntentString(context, "master_wifi_toggle", WifiSettings.class, this.getScreenTitle(context));
        final int iconResource = this.getIconResource();
        final MatrixCursor matrixCursor = new MatrixCursor(ExternalSettingsContract.EXTERNAL_SETTINGS_QUERY_COLUMNS);
        matrixCursor.newRow().add("existing_value", (Object)n).add("availability", (Object)0).add("intent", (Object)intentString).add("icon", (Object)iconResource);
        return (Cursor)matrixCursor;
    }
    
    @Override
    public Cursor getUpdateCursor(final Context context, final int n) {
        final WifiManager wifiManager = (WifiManager)context.getSystemService("wifi");
        final boolean wifiEnabled = wifiManager.isWifiEnabled();
        boolean wifiEnabled2 = false;
        int n2;
        if (wifiEnabled) {
            n2 = 1;
        }
        else {
            n2 = 0;
        }
        final String intentString = this.getIntentString(context, "master_wifi_toggle", WifiSettings.class, this.getScreenTitle(context));
        final int iconResource = this.getIconResource();
        int n3 = n2;
        if (this.shouldChangeValue(0, n2, n)) {
            if (n == 1) {
                wifiEnabled2 = true;
            }
            n3 = n3;
            if (wifiManager.setWifiEnabled(wifiEnabled2)) {
                n3 = n;
            }
        }
        final MatrixCursor matrixCursor = new MatrixCursor(ExternalSettingsContract.EXTERNAL_SETTINGS_UPDATE_COLUMNS);
        matrixCursor.newRow().add("newValue", (Object)n3).add("existing_value", (Object)n2).add("availability", (Object)0).add("intent", (Object)intentString).add("icon", (Object)iconResource);
        return (Cursor)matrixCursor;
    }
}
