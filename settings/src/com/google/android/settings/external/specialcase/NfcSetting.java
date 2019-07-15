package com.google.android.settings.external.specialcase;

import android.database.MatrixCursor;
import com.google.android.settings.external.ExternalSettingsContract;
import android.app.Fragment;
import com.android.settings.connecteddevice.ConnectedDeviceDashboardFragment;
import android.database.Cursor;
import com.android.settings.nfc.NfcPreferenceController;
import android.provider.Settings;
import android.provider.Settings;
import android.nfc.NfcAdapter;
import android.content.Context;
import com.google.android.settings.external.Queryable;

public class NfcSetting implements Queryable
{
    private static int getAvailability(final Context context, final NfcAdapter nfcAdapter) {
        if (nfcAdapter == null) {
            return 2;
        }
        if (Settings.Global.getInt(context.getContentResolver(), "airplane_mode_on", 0) == 1 && !NfcPreferenceController.isToggleableInAirplaneMode(context)) {
            return 1;
        }
        return 0;
    }
    
    private int getIconResource() {
        return 2131231146;
    }
    
    private static int getNfcStatus(final NfcAdapter nfcAdapter) {
        int enabled = 0;
        if (nfcAdapter != null) {
            enabled = (nfcAdapter.isEnabled() ? 1 : 0);
        }
        return enabled;
    }
    
    private String getScreenTitle(final Context context) {
        return context.getString(2131887123);
    }
    
    public Cursor getAccessCursor(final Context context) {
        final NfcAdapter defaultAdapter = NfcAdapter.getDefaultAdapter(context);
        final int nfcStatus = getNfcStatus(defaultAdapter);
        final int availability = getAvailability(context, defaultAdapter);
        final String intentString = this.getIntentString(context, "nfc", ConnectedDeviceDashboardFragment.class, this.getScreenTitle(context));
        final int iconResource = this.getIconResource();
        final MatrixCursor matrixCursor = new MatrixCursor(ExternalSettingsContract.EXTERNAL_SETTINGS_QUERY_COLUMNS);
        matrixCursor.newRow().add("existing_value", (Object)nfcStatus).add("availability", (Object)availability).add("intent", (Object)intentString).add("icon", (Object)iconResource).add("dependent_setting", (Object)"toggle_airplane");
        return (Cursor)matrixCursor;
    }
    
    @Override
    public Cursor getUpdateCursor(final Context context, final int n) {
        final NfcAdapter defaultAdapter = NfcAdapter.getDefaultAdapter(context);
        final int nfcStatus = getNfcStatus(defaultAdapter);
        final int availability = getAvailability(context, defaultAdapter);
        final String intentString = this.getIntentString(context, "nfc", ConnectedDeviceDashboardFragment.class, this.getScreenTitle(context));
        final int iconResource = this.getIconResource();
        int n3;
        final int n2 = n3 = nfcStatus;
        Label_0101: {
            if (this.shouldChangeValue(availability, nfcStatus, n)) {
                n3 = n2;
                if (defaultAdapter != null) {
                    if (n != 1) {
                        n3 = n2;
                        if (n != 0) {
                            break Label_0101;
                        }
                        n3 = n2;
                    }
                    n3 = n;
                }
            }
        }
        final MatrixCursor matrixCursor = new MatrixCursor(ExternalSettingsContract.EXTERNAL_SETTINGS_UPDATE_COLUMNS);
        matrixCursor.newRow().add("newValue", (Object)n3).add("existing_value", (Object)nfcStatus).add("availability", (Object)availability).add("intent", (Object)intentString).add("icon", (Object)iconResource).add("dependent_setting", (Object)"toggle_airplane");
        return (Cursor)matrixCursor;
    }
}
