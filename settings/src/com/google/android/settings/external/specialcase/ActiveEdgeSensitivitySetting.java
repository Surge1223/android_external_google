package com.google.android.settings.external.specialcase;

import java.io.ObjectInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import android.provider.Settings;
import android.database.MatrixCursor;
import com.google.android.settings.external.ExternalSettingsContract;
import android.app.Fragment;
import com.android.settings.gestures.AssistGestureSettings;
import android.database.Cursor;
import com.google.android.settings.gestures.assist.AssistGestureSensitivityPreferenceController;
import com.android.settings.overlay.FeatureFactory;
import android.content.Context;
import com.google.android.settings.external.Queryable;

public class ActiveEdgeSensitivitySetting implements Queryable
{
    private static int getAvailability(final Context context) {
        int n;
        if (AssistGestureSensitivityPreferenceController.isAvailable(context, FeatureFactory.getFactory(context).getAssistGestureFeatureProvider())) {
            n = 0;
        }
        else {
            n = 2;
        }
        return n;
    }
    
    private int getIconResource() {
        return 0;
    }
    
    private String getScreenTitle(final Context context) {
        return context.getString(2131886441);
    }
    
    private String getSupportedValues(final Context context) {
        final int maxSensitivityResourceInteger = AssistGestureSensitivityPreferenceController.getMaxSensitivityResourceInteger(context);
        if (maxSensitivityResourceInteger < 0) {
            return "";
        }
        final StringBuilder sb = new StringBuilder();
        for (int i = 0; i <= maxSensitivityResourceInteger; ++i) {
            sb.append(i);
            sb.append(",");
        }
        sb.setLength(sb.length() - 1);
        return sb.toString();
    }
    
    private void validateInput(final Context context, final int n) {
        final int maxSensitivityResourceInteger = AssistGestureSensitivityPreferenceController.getMaxSensitivityResourceInteger(context);
        if (n >= 0 && n <= maxSensitivityResourceInteger) {
            return;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Requested sensitivity rating out of bounds. Expected between 0 and ");
        sb.append(maxSensitivityResourceInteger);
        sb.append(", but found: ");
        sb.append(n);
        throw new IllegalArgumentException(sb.toString());
    }
    
    public Cursor getAccessCursor(final Context context) {
        final int sensitivityInt = AssistGestureSensitivityPreferenceController.getSensitivityInt(context);
        final String intentString = this.getIntentString(context, "assist_sensitivity", AssistGestureSettings.class, this.getScreenTitle(context));
        final int iconResource = this.getIconResource();
        final MatrixCursor matrixCursor = new MatrixCursor(ExternalSettingsContract.EXTERNAL_SETTINGS_QUERY_COLUMNS_WITH_SUPPORTED_VALUES);
        matrixCursor.newRow().add("existing_value", (Object)sensitivityInt).add("availability", (Object)getAvailability(context)).add("intent", (Object)intentString).add("icon", (Object)iconResource).add("supported_values", (Object)this.getSupportedValues(context));
        return (Cursor)matrixCursor;
    }
    
    @Override
    public Cursor getUpdateCursor(final Context context, final int n) {
        this.validateInput(context, n);
        final int sensitivityInt = AssistGestureSensitivityPreferenceController.getSensitivityInt(context);
        final int availability = getAvailability(context);
        final String intentString = this.getIntentString(context, "assist_sensitivity", AssistGestureSettings.class, this.getScreenTitle(context));
        final int iconResource = this.getIconResource();
        final int n2 = sensitivityInt;
        final float convertSensitivityIntToFloat = AssistGestureSensitivityPreferenceController.convertSensitivityIntToFloat(context, n);
        int n3 = n2;
        if (this.shouldChangeValue(availability, sensitivityInt, n)) {
            n3 = n2;
            if (Settings.Secure.putFloat(context.getContentResolver(), "assist_gesture_sensitivity", convertSensitivityIntToFloat)) {
                n3 = n;
            }
        }
        final MatrixCursor matrixCursor = new MatrixCursor(ExternalSettingsContract.EXTERNAL_SETTINGS_UPDATE_COLUMNS);
        matrixCursor.newRow().add("newValue", (Object)n3).add("existing_value", (Object)sensitivityInt).add("availability", (Object)availability).add("intent", (Object)intentString).add("icon", (Object)iconResource);
        return (Cursor)matrixCursor;
    }
}
