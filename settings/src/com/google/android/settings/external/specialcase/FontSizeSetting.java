package com.google.android.settings.external.specialcase;

import com.android.settings.accessibility.ToggleFontSizePreferenceFragment;
import android.database.MatrixCursor;
import com.google.android.settings.external.ExternalSettingsContract;
import android.app.Fragment;
import com.android.settings.DisplaySettings;
import android.database.Cursor;
import android.provider.Settings;
import android.content.Context;
import com.google.android.settings.external.Queryable;

public class FontSizeSetting implements Queryable
{
    private float getFontScale(final Context context) {
        return Settings.System.getFloat(context.getContentResolver(), "font_scale", 1.0f);
    }
    
    private int getIconResource() {
        return 0;
    }
    
    private String getScreenTitle(final Context context) {
        return context.getString(2131887447);
    }
    
    public Cursor getAccessCursor(final Context context) {
        final float fontScale = this.getFontScale(context);
        final String intentString = this.getIntentString(context, "font_size", DisplaySettings.class, this.getScreenTitle(context));
        final int iconResource = this.getIconResource();
        final String[] stringArray = context.getResources().getStringArray(2130903119);
        final float[] array = new float[stringArray.length];
        for (int i = 0; i < stringArray.length; ++i) {
            array[i] = Float.parseFloat(stringArray[i]);
        }
        final MatrixCursor matrixCursor = new MatrixCursor(ExternalSettingsContract.EXTERNAL_SETTINGS_QUERY_COLUMNS_WITH_SUPPORTED_VALUES);
        matrixCursor.newRow().add("existing_value", (Object)fontScale).add("availability", (Object)0).add("intent", (Object)intentString).add("icon", (Object)iconResource).add("supported_values", (Object)String.join(",", (CharSequence[])stringArray));
        return (Cursor)matrixCursor;
    }
    
    @Override
    public Cursor getUpdateCursor(final Context context, float float1) {
        final float fontScale = this.getFontScale(context);
        final String intentString = this.getIntentString(context, "font_size", DisplaySettings.class, this.getScreenTitle(context));
        final int iconResource = this.getIconResource();
        float n = fontScale;
        final String[] stringArray = context.getResources().getStringArray(2130903119);
        float1 = Float.parseFloat(stringArray[ToggleFontSizePreferenceFragment.fontSizeValueToIndex(float1, stringArray)]);
        if (Settings.System.putFloat(context.getContentResolver(), "font_scale", float1)) {
            n = float1;
        }
        final MatrixCursor matrixCursor = new MatrixCursor(ExternalSettingsContract.EXTERNAL_SETTINGS_UPDATE_COLUMNS);
        matrixCursor.newRow().add("newValue", (Object)n).add("existing_value", (Object)fontScale).add("availability", (Object)0).add("intent", (Object)intentString).add("icon", (Object)iconResource);
        return (Cursor)matrixCursor;
    }
}
