package com.google.android.settings.external.specialcase;

import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.support.annotation.VisibleForTesting;
import com.android.settings.accessibility.AccessibilitySettings;
import com.android.settingslib.display.DisplayDensityUtils;
import com.google.android.settings.external.ExternalSettingsContract;
import com.google.android.settings.external.Queryable;
import com.google.android.settings.external.specialcase.ScreenFunctions.C10251;
import java.util.Arrays;
import java.util.stream.Collectors;

public class ScreenZoomSetting implements Queryable {
    @VisibleForTesting(otherwise = 5)
    public static DisplayDensityUtils sDisplayDensityUtilsForTesting;

    private DisplayDensityUtils getDisplayDensityUtils(Context context) {
        DisplayDensityUtils displayDensityUtils = sDisplayDensityUtilsForTesting;
        return displayDensityUtils == null ? new DisplayDensityUtils(context) : displayDensityUtils;
    }

    private int getIconResource() {
        return 0;
    }

    private String getScreenTitle(Context context) {
        return context.getString(2131886148);
    }

    private static boolean isValidValue(DisplayDensityUtils displayDensityUtils, int i) {
        int[] values = displayDensityUtils.getValues();
        for (int i2 : values) {
            if (i == i2) {
                return true;
            }
        }
        return false;
    }

    public Cursor getAccessCursor(Context context) {
        DisplayDensityUtils displayDensityUtils = getDisplayDensityUtils(context);
        int currentIndex = displayDensityUtils.getCurrentIndex();
        int i = 0;
        int[] values = displayDensityUtils.getValues();
        Object obj = null;
        if (currentIndex < 0) {
            i = 3;
        } else {
            String str = (String) Arrays.stream(values).mapToObj(C10251.INSTANCE).map(ScreenFunctions.INSTANCE).collect(Collectors.joining(","));
        }
        String intentString = getIntentString(context, "screen_zoom", AccessibilitySettings.class, getScreenTitle(context));
        MatrixCursor matrixCursor  = new MatrixCursor(ExternalSettingsContract.EXTERNAL_SETTINGS_QUERY_COLUMNS_WITH_SUPPORTED_VALUES);
        matrixCursor.newRow().add("existing_value", Integer.valueOf(currentIndex)).add("availability", Integer.valueOf(i)).add("intent", intentString).add("icon", Integer.valueOf(getIconResource())).add("supported_values", obj);
        return matrixCursor;
    }

    public Cursor getUpdateCursor(Context context, int i) {
        DisplayDensityUtils displayDensityUtils = getDisplayDensityUtils(context);
        int currentIndex = displayDensityUtils.getCurrentIndex();
        int i2 = currentIndex < 0 ? 3 : 0;
        String intentString = getIntentString(context, "screen_zoom", AccessibilitySettings.class, getScreenTitle(context));
        if (isValidValue(displayDensityUtils, i)) {
            DisplayDensityUtils.setForcedDisplayDensity(0, i);
            MatrixCursor matrixCursor  = new MatrixCursor(ExternalSettingsContract.EXTERNAL_SETTINGS_UPDATE_COLUMNS);
            matrixCursor.newRow().add("newValue", Integer.valueOf(i)).add("existing_value", Integer.valueOf(currentIndex)).add("availability", Integer.valueOf(i2)).add("intent", intentString).add("icon", Integer.valueOf(getIconResource()));
            return matrixCursor;
        }
        throw new IllegalArgumentException("Unexpected value for screen zoom. Expected value in density values, but found: " + i);
    }
}
