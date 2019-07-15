package com.google.android.settings.external.specialcase;

import android.database.MatrixCursor;
import com.google.android.settings.external.ExternalSettingsContract;
import android.app.Fragment;
import com.android.settings.connecteddevice.ConnectedDeviceDashboardFragment;
import android.database.Cursor;
import com.android.settings.bluetooth.Utils;
import com.android.settingslib.bluetooth.LocalBluetoothAdapter;
import com.android.settings.bluetooth.BluetoothEnabler;
import com.android.settings.bluetooth.RestrictionUtils;
import android.content.Context;
import com.google.android.settings.external.Queryable;
import com.android.settings.R;

public class BluetoothSetting implements Queryable
{
    private final int OFF;
    private final int ON;
    
    public BluetoothSetting() {
        this.ON = 1;
        this.OFF = 0;
    }
    
    private int getAvailability(final Context context) {
        if (BluetoothEnabler.getEnforcedAdmin(new RestrictionUtils(), context) != null) {
            return 6;
        }
        return 0;
    }
    
    private int getIconResource() {
        return 2131231040;
    }    
    
    private LocalBluetoothAdapter getLocalBluetoothAdapter(final Context context) {
        return Utils.getLocalBtManager(context).getBluetoothAdapter();
    }
    
    private String getScreenTitle(final Context context) {
        return context.getString(2131886894);
    }
    
    public Cursor getAccessCursor(final Context context) {
        int n;
        if (this.getLocalBluetoothAdapter(context).isEnabled()) {
            n = 1;
        }
        else {
            n = 0;
        }
        final int availability = this.getAvailability(context);
        final String intentString = this.getIntentString(context, "bluetooth_toggle_key", ConnectedDeviceDashboardFragment.class, this.getScreenTitle(context));
        final int iconResource = this.getIconResource();
        final MatrixCursor matrixCursor = new MatrixCursor(ExternalSettingsContract.EXTERNAL_SETTINGS_QUERY_COLUMNS);
        matrixCursor.newRow().add("existing_value", (Object)n).add("availability", (Object)availability).add("intent", (Object)intentString).add("icon", (Object)iconResource);
        return (Cursor)matrixCursor;
    }
    
    @Override
    public Cursor getUpdateCursor(final Context context, final int n) {
        final LocalBluetoothAdapter localBluetoothAdapter = this.getLocalBluetoothAdapter(context);
        int n2;
        if (localBluetoothAdapter.isEnabled()) {
            n2 = 1;
        }
        else {
            n2 = 0;
        }
        final int n3 = n2;
        final int availability = this.getAvailability(context);
        final String intentString = this.getIntentString(context, "bluetooth_toggle_key", ConnectedDeviceDashboardFragment.class, this.getScreenTitle(context));
        final int iconResource = this.getIconResource();
        int n4 = n3;
        Label_0104: {
            if (this.shouldChangeValue(availability, n2, n)) {
                if (n != 1 || !localBluetoothAdapter.enable()) {
                    n4 = n3;
                    if (n != 0) {
                        break Label_0104;
                    }
                    n4 = n3;
                    if (!localBluetoothAdapter.disable()) {
                        break Label_0104;
                    }
                }
                n4 = n;
            }
        }
        final MatrixCursor matrixCursor = new MatrixCursor(ExternalSettingsContract.EXTERNAL_SETTINGS_UPDATE_COLUMNS);
        matrixCursor.newRow().add("newValue", (Object)n4).add("existing_value", (Object)n2).add("availability", (Object)availability).add("intent", (Object)intentString).add("icon", (Object)iconResource);
        return (Cursor)matrixCursor;
    }
}
