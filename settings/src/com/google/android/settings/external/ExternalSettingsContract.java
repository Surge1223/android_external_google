package com.google.android.settings.external;

import android.net.Uri;

public class ExternalSettingsContract
{
    private static Uri CONTENT_BASE_URI;
    public static final String[] DEVICE_SIGNALS_COLUMNS;
    public static final Uri DEVICE_SIGNALS_URI;
    public static final String[] EXTERNAL_SETTINGS_QUERY_COLUMNS;
    public static final String[] EXTERNAL_SETTINGS_QUERY_COLUMNS_WITH_SUPPORTED_VALUES;
    public static final String[] EXTERNAL_SETTINGS_QUERY_RANGE_COLUMNS;
    public static final String[] EXTERNAL_SETTINGS_UPDATE_COLUMNS;
    public static final Uri SETTINGS_MANAGER_URI;
    
    static {
        ExternalSettingsContract.CONTENT_BASE_URI = Uri.parse("content://com.google.android.settings.external");
        final Uri content_BASE_URI = ExternalSettingsContract.CONTENT_BASE_URI;
        SETTINGS_MANAGER_URI = Uri.withAppendedPath(ExternalSettingsContract.CONTENT_BASE_URI, "settings_manager");
        final Uri content_BASE_URI2 = ExternalSettingsContract.CONTENT_BASE_URI;
        DEVICE_SIGNALS_URI = Uri.withAppendedPath(ExternalSettingsContract.CONTENT_BASE_URI, "signals");
        EXTERNAL_SETTINGS_QUERY_COLUMNS = new String[] { "existing_value", "availability", "intent", "icon", "dependent_setting" };
        EXTERNAL_SETTINGS_QUERY_RANGE_COLUMNS = new String[] { "existing_value", "availability", "intent", "min_value", "max_value", "icon", "dependent_setting" };
        EXTERNAL_SETTINGS_QUERY_COLUMNS_WITH_SUPPORTED_VALUES = new String[] { "existing_value", "availability", "intent", "icon", "supported_values", "dependent_setting" };
        EXTERNAL_SETTINGS_UPDATE_COLUMNS = new String[] { "newValue", "existing_value", "availability", "intent", "icon", "dependent_setting" };
        DEVICE_SIGNALS_COLUMNS = new String[] { "signal_key", "signal_value" };
    }
}
