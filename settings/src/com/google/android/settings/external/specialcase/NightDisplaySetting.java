package com.google.android.settings.external.specialcase;

import android.database.MatrixCursor;
import com.google.android.settings.external.ExternalSettingsContract;
import android.app.Fragment;
import com.android.settings.display.NightDisplaySettings;
import com.android.internal.app.ColorDisplayController;
import android.database.Cursor;
import android.content.Context;
import com.google.android.settings.external.Queryable;

public class NightDisplaySetting implements Queryable
{
    private int getIconResource() {
        return 2131231099;
    }
    
    private String getScreenTitle(final Context context) {
        return context.getString(2131888405);
    }
    
    public Cursor getAccessCursor(final Context context) {
        final boolean activated = new ColorDisplayController(context).isActivated();
        int n = 0;
        int n2;
        if (activated) {
            n2 = 1;
        }
        else {
            n2 = 0;
        }
        if (!ColorDisplayController.isAvailable(context)) {
            n = 2;
        }
        final String intentString = this.getIntentString(context, "night_display", NightDisplaySettings.class, this.getScreenTitle(context));
        final int iconResource = this.getIconResource();
        final MatrixCursor matrixCursor = new MatrixCursor(ExternalSettingsContract.EXTERNAL_SETTINGS_QUERY_COLUMNS);
        matrixCursor.newRow().add("existing_value", (Object)n2).add("availability", (Object)n).add("intent", (Object)intentString).add("icon", (Object)iconResource);
        return (Cursor)matrixCursor;
    }
    
    @Override
    public Cursor getUpdateCursor(final Context context, final int n) {
        final ColorDisplayController colorDisplayController = new ColorDisplayController(context);
        final boolean activated = colorDisplayController.isActivated();
        boolean activated2 = false;
        int n2;
        if (activated) {
            n2 = 1;
        }
        else {
            n2 = 0;
        }
        int n3;
        if (ColorDisplayController.isAvailable(context)) {
            n3 = 0;
        }
        else {
            n3 = 2;
        }
        final String intentString = this.getIntentString(context, "night_display", NightDisplaySettings.class, this.getScreenTitle(context));
        final int iconResource = this.getIconResource();
        final int n4 = n2;
        if (n == 1) {
            activated2 = true;
        }
        int n5 = n4;
        if (this.shouldChangeValue(n3, n2, n)) {
            n5 = n4;
            if (colorDisplayController.setActivated(activated2)) {
                n5 = n;
            }
        }
        final MatrixCursor matrixCursor = new MatrixCursor(ExternalSettingsContract.EXTERNAL_SETTINGS_UPDATE_COLUMNS);
        matrixCursor.newRow().add("newValue", (Object)n5).add("existing_value", (Object)n2).add("availability", (Object)n3).add("intent", (Object)intentString).add("icon", (Object)iconResource);
        return (Cursor)matrixCursor;
    }
}
