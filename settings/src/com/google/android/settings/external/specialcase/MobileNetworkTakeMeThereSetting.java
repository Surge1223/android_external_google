package com.google.android.settings.external.specialcase;

import android.content.ContentResolver;
import android.provider.Settings;
import com.android.settings.network.MobileNetworkPreferenceController;
import android.database.MatrixCursor.RowBuilder;
import android.database.MatrixCursor;
import com.google.android.settings.external.ExternalSettingsContract;
import android.database.Cursor;
import android.content.Context;
import android.content.Intent;

public class MobileNetworkTakeMeThereSetting extends TakeMeThereSetting
{
    public MobileNetworkTakeMeThereSetting() {
        super(new Intent("android.intent.action.MAIN").setClassName("com.android.phone", "com.android.phone.MobileNetworkSettings").toUri(0));
    }
    
    @Override
    public Cursor getAccessCursor(final Context context) {
        final int availability = this.getAvailability(context);
        final MatrixCursor matrixCursor = new MatrixCursor(ExternalSettingsContract.EXTERNAL_SETTINGS_QUERY_COLUMNS);
        final MatrixCursor.RowBuilder add = matrixCursor.newRow().add("existing_value", (Object)0).add("availability", (Object)availability).add("dependent_setting", (Object)"toggle_airplane");
        String mIntentString;
        if (availability == 5) {
            mIntentString = this.mIntentString;
        }
        else {
            mIntentString = null;
        }
        add.add("intent", (Object)mIntentString);
        return (Cursor)matrixCursor;
    }
    
    public int getAvailability(final Context context) {
        final MobileNetworkPreferenceController mobileNetworkPreferenceController = new MobileNetworkPreferenceController(context);
        final ContentResolver contentResolver = context.getContentResolver();
        boolean b = false;
        final int int1 = Settings.Global.getInt(contentResolver, "airplane_mode_on", 0);
        final boolean b2 = true;
        if (int1 != 0) {
            b = true;
        }
        int n;
        if (mobileNetworkPreferenceController.isUserRestricted()) {
            n = 6;
        }
        else if (b) {
            n = (b2 ? 1 : 0);
        }
        else if (mobileNetworkPreferenceController.isAvailable()) {
            n = 5;
        }
        else {
            n = 2;
        }
        return n;
    }
}
