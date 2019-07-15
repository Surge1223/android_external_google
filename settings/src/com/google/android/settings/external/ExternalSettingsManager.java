package com.google.android.settings.external;


import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.os.BadParcelableException;
import android.os.Binder;
import android.util.Log;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import com.android.settings.overlay.FeatureFactory;
import com.android.settings.search.DatabaseIndexingUtils;
import com.android.settings.search.IndexDatabaseHelper;
import com.android.settings.search.InlineListPayload;
import com.android.settings.search.InlinePayload;
import com.android.settings.search.InlineSwitchPayload;
import com.android.settings.search.ResultPayload;
import com.android.settings.search.ResultPayloadUtils;
import com.android.settingslib.core.instrumentation.SharedPreferencesLogger;

public class ExternalSettingsManager {
    private static final String[] SELECT_COLUMNS = {"class_name", "payload_type", "payload", "icon", "screen_title"};
    public static final Set<String> SPECIAL_SETTINGS;

    private static void closeResource(Throwable th, AutoCloseable autoCloseable) {
        if (th != null) {
            try {
                autoCloseable.close();
                return;
            } catch (Throwable th2) {
                th.addSuppressed(th2);
                return;
            }
        }
    }

    static {
        HashSet hashSet = new HashSet();
        hashSet.add("gesture_assist");
        hashSet.add("assist_sensitivity");
        hashSet.add("toggle_airplane");
        hashSet.add("ambient_display_always_on");
        hashSet.add("toggle_lock_screen_rotation_preference");
        hashSet.add("bluetooth_toggle_key");
        hashSet.add("color_inversion");
        hashSet.add("data_saver");
        hashSet.add("screen_timeout");
        hashSet.add("zen_mode");
        hashSet.add("gesture_double_twist");
        hashSet.add("font_size");
        hashSet.add("enable_wifi_ap");
        hashSet.add("location");
        hashSet.add("magnify_gesture");
        hashSet.add("magnify_navbar");
        hashSet.add("mobile_data");
        hashSet.add("nfc");
        hashSet.add("night_display");
        hashSet.add("night_display_temperature");
        hashSet.add("gesture_pick_up");
        hashSet.add("preferred_network_type");
        hashSet.add("screen_zoom");
        hashSet.add("swipe_to_notification");
        hashSet.add("switch_access");
        hashSet.add("system_update");
        hashSet.add("talkback");
        hashSet.add("master_wifi_toggle");
        SPECIAL_SETTINGS = Collections.unmodifiableSet(hashSet);
    }

    private static String buildSearchResultPageIntentString(Context context, String str, String str2, String str3) {
        return DatabaseIndexingUtils.buildSearchResultPageIntent(context, str, str2, str3, 1033).toUri(0);
    }

    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static Cursor getAccessCursorForSpecialSetting(Context context, String str, String str2) {
        char c;
        int i = -1;
        Cursor cursor = null;
        switch (str2.hashCode()) {
            case -2072222422:
                if (str2.equals("swipe_to_notification")) {
                    c = 22;
                    break;
                }
            case -2061057831:
                if (str2.equals("system_update")) {
                    c = 24;
                    break;
                }
            case -1552376189:
                if (str2.equals("gesture_pick_up")) {
                    c = 20;
                    break;
                }
            case -1539906063:
                if (str2.equals("font_size")) {
                    c = 11;
                    break;
                }
            case -1525260053:
                if (str2.equals("ambient_display_always_on")) {
                    c = 3;
                    break;
                }
            case -1314247385:
                if (str2.equals("mobile_data")) {
                    c = 16;
                    break;
                }
            case -1001942051:
                if (str2.equals("toggle_airplane")) {
                    c = 2;
                    break;
                }
            case -610139245:
                if (str2.equals("talkback")) {
                    c = 25;
                    break;
                }
            case -608350689:
                if (str2.equals("gesture_assist")) {
                    c = 0;
                    break;
                }
            case -416443991:
                if (str2.equals("preferred_network_type")) {
                    c = 27;
                    break;
                }
            case -382039141:
                if (str2.equals("night_display")) {
                    c = 18;
                    break;
                }
            case -315259171:
                if (str2.equals("enable_wifi_ap")) {
                    c = 12;
                    break;
                }
            case -42893370:
                if (str2.equals("screen_zoom")) {
                    c = 21;
                    break;
                }
            case 108971:
                if (str2.equals("nfc")) {
                    c = 17;
                    break;
                }
            case 15719777:
                if (str2.equals("master_wifi_toggle")) {
                    c = 26;
                    break;
                }
            case 536948395:
                if (str2.equals("magnify_gesture")) {
                    c = 14;
                    break;
                }
            case 556955191:
                if (str2.equals("color_inversion")) {
                    c = 6;
                    break;
                }
            case 558352831:
                if (str2.equals("zen_mode")) {
                    c = 9;
                    break;
                }
            case 860063886:
                if (str2.equals("screen_timeout")) {
                    c = 8;
                    break;
                }
            case 1043529839:
                if (str2.equals("gesture_double_twist")) {
                    c = 10;
                    break;
                }
            case 1376598223:
                if (str2.equals("switch_access")) {
                    c = 23;
                    break;
                }
            case 1580211920:
                if (str2.equals("night_display_temperature")) {
                    c = 19;
                    break;
                }
            case 1599575662:
                if (str2.equals("magnify_navbar")) {
                    c = 15;
                    break;
                }
            case 1619122624:
                if (str2.equals("data_saver")) {
                    c = 7;
                    break;
                }
            case 1839966665:
                if (str2.equals("assist_sensitivity")) {
                    c = 1;
                    break;
                }
            case 1901043637:
                if (str2.equals("location")) {
                    c = 13;
                    break;
                }
            case 1988786469:
                if (str2.equals("bluetooth_toggle_key")) {
                    c = 5;
                    break;
                }
            case 2056099474:
                if (str2.equals("toggle_lock_screen_rotation_preference")) {
                    c = 4;
                    break;
                }
            default:
                c = 65535;
                break;
        }
        switch (c) {
            case 0:
                cursor = InlineSettings.ACTIVE_EDGE_SETTING.getAccessCursor(context);
                break;
            case 1:
                cursor = InlineSettings.ACTIVE_EDGE_SENSITIVITY_SETTING.getAccessCursor(context);
                break;
            case 2:
                cursor = InlineSettings.AIRPLANE_MODE_SETTING.getAccessCursor(context);
                break;
            case 3:
                cursor = InlineSettings.AMBIENT_DISPLAY_ALWAYS_ON_SETTING.getAccessCursor(context);
                break;
            case 4:
                cursor = InlineSettings.AUTO_ROTATE_SETTING.getAccessCursor(context);
                break;
            case 5:
                cursor = InlineSettings.BLUETOOTH_SETTING.getAccessCursor(context);
                break;
            case 6:
                cursor = InlineSettings.COLOR_INVERSION_SETTING.getAccessCursor(context);
                break;
            case 7:
                cursor = InlineSettings.DATA_SAVER_SETTING.getAccessCursor(context);
                break;
            case 8:
                cursor = InlineSettings.DISPLAY_TIMEOUT_SETTING.getAccessCursor(context);
                break;
            case 9:
                cursor = InlineSettings.DO_NOT_DISTURB_SETTING.getAccessCursor(context);
                break;
            case 10:
                cursor = InlineSettings.DOUBLE_TWIST_GESTURE_SETTING.getAccessCursor(context);
                break;
            case 11:
                cursor = InlineSettings.FONT_SIZE_SETTING.getAccessCursor(context);
                break;
            case 12:
                cursor = InlineSettings.HOTSPOT_SETTING.getAccessCursor(context);
                break;
            case 13:
                cursor = InlineSettings.LOCATION_SETTING.getAccessCursor(context);
                break;
            case 14:
                cursor = InlineSettings.MAGNIFY_GESTURE_SETTING.getAccessCursor(context);
                break;
            case 15:
                cursor = InlineSettings.MAGNIFY_NAVBAR_SETTING.getAccessCursor(context);
                break;
            case 16:
                cursor = InlineSettings.MOBILE_DATA_SETTING.getAccessCursor(context);
                break;
            case 17:
                cursor = InlineSettings.NFC_SETTING.getAccessCursor(context);
                break;
            case 18:
                cursor = InlineSettings.NIGHTDISPLAY_SETTING.getAccessCursor(context);
                break;
            case 19:
                cursor = InlineSettings.NIGHTDISPLAY_INTENSITY_SETTING.getAccessCursor(context);
                break;
            case 20:
                cursor = InlineSettings.PICKUP_GESTURE_SETTING.getAccessCursor(context);
                break;
            case 21:
                cursor = InlineSettings.SCREEN_ZOOM_SETTING.getAccessCursor(context);
                break;
            case 22:
                cursor = InlineSettings.SWIPE_TO_NOTIFICATION_SETTING.getAccessCursor(context);
                break;
            case 23:
                cursor = InlineSettings.SWITCH_ACCESS_SETTING.getAccessCursor(context);
                break;
            case 24:
                cursor = InlineSettings.SYSTEM_UPDATE_SETTING.getAccessCursor(context);
                break;
            case 25:
                cursor = InlineSettings.TALKBACK_SETTING.getAccessCursor(context);
                break;
            case 26:
                cursor = InlineSettings.WIFI_SETTING.getAccessCursor(context);
                break;
            case 27:
                cursor = InlineSettings.MOBILE_NETWORK_SETTINGS.getAccessCursor(context);
                break;
        }
        if (cursor == null || !cursor.moveToFirst()) {
            StringBuilder sb = new StringBuilder();
            sb.append("Invalid access special case key: ");
            sb.append(str2);
            throw new IllegalArgumentException(sb.toString());
        }
        int columnIndex = cursor.getColumnIndex("existing_value");
        if (columnIndex >= 0) {
            i = cursor.getInt(columnIndex);
        }
        logAccessSetting(context, str, str2, i);
        return cursor;
    }

    public static Cursor getAccessCursorFromPayload(Context context, String str, String str2) {
        Throwable th = null;
        Throwable th2 = null;
        Cursor cursorFromUriKey = getCursorFromUriKey(context, str2);
        try {
            String string = cursorFromUriKey.getString(cursorFromUriKey.getColumnIndex("class_name"));
            int i = cursorFromUriKey.getInt(cursorFromUriKey.getColumnIndex("payload_type"));
            byte[] blob = cursorFromUriKey.getBlob(cursorFromUriKey.getColumnIndex("payload"));
            int i2 = cursorFromUriKey.getInt(cursorFromUriKey.getColumnIndex("icon"));
            String string2 = cursorFromUriKey.getString(cursorFromUriKey.getColumnIndex("screen_title"));
            if (cursorFromUriKey != null) {
                ExternalSettingsManager.closeResource(null, cursorFromUriKey);
            }
            if (i != 0) {
                InlinePayload inlinePayload = (InlinePayload) ExternalSettingsManager.getUnmarshalledPayload(blob, i);
                string = ExternalSettingsManager.buildSearchResultPageIntentString(context, string, str2, string2);
                String buildSearchResultPageIntentString = buildSearchResultPageIntentString(context, string, str2, string2);

                int value = inlinePayload.getValue(context);
                int availability = inlinePayload.getAvailability();
                int intValue = Integer.valueOf(str2).intValue();
                int i3 = (availability == 0 && inlinePayload.setValue(context, intValue)) ? intValue : value;

                MatrixCursor matrixCursor = new MatrixCursor(ExternalSettingsContract.EXTERNAL_SETTINGS_UPDATE_COLUMNS);
                matrixCursor.newRow().add("newValue", Integer.valueOf(i3)).add("existing_value", Integer.valueOf(value)).add("availability", Integer.valueOf(availability)).add("intent", string).add("icon", Integer.valueOf(i2));
                logUpdateSetting(context, str, str2, i3);
                return matrixCursor;
            }
        } catch (Throwable th3) {
            th = th3;
            th2 = th;
        }
        if (cursorFromUriKey != null) {
            closeResource(th2, cursorFromUriKey);
        }
        return null;
    }

    public static Cursor getCursorFromUriKey(Context context, String str) {
        ExternalSettingsManager.verifyIndexing(context);
        Cursor query = IndexDatabaseHelper.getInstance(context).getReadableDatabase().query("prefs_index", SELECT_COLUMNS, "data_key_reference like ? ", new String[]{str}, null, null, null);
        if (query.getCount() == 1 && query.moveToFirst()) {
            return query;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Key not found: ");
        stringBuilder.append(str);
        throw new IllegalArgumentException(stringBuilder.toString());
    }

    public static String getNewSettingValueQueryParameter(Uri uri) {
        return uri.getQueryParameter("new_setting_value");
    }

    private static ResultPayload getUnmarshalledPayload(byte[] bArr, int i) {
        if (i == 0) {
            return (ResultPayload) ResultPayloadUtils.unmarshall(bArr, ResultPayload.CREATOR);
        }
        switch (i) {
            case 2:
                return (ResultPayload) ResultPayloadUtils.unmarshall(bArr, InlineSwitchPayload.CREATOR);
            case 3:
                try {
                    return (ResultPayload) ResultPayloadUtils.unmarshall(bArr, InlineListPayload.CREATOR);
                } catch (BadParcelableException e) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("Error creating parcelable: ");
                    stringBuilder.append(e);
                    Log.w("ExternalSettingsManager", stringBuilder.toString());
                    break;
                }
        }
        return null;
    }
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static Cursor getUpdateCursorForSpecialSetting(Context context, String str, String str2, String str3) {
        char c;
        int i = -1;
        Cursor cursor = null;
        switch (str2.hashCode()) {
            case -2072222422:
                if (str2.equals("swipe_to_notification")) {
                    c = 22;
                    break;
                }
            case -2061057831:
                if (str2.equals("system_update")) {
                    c = 24;
                    break;
                }
            case -1552376189:
                if (str2.equals("gesture_pick_up")) {
                    c = 20;
                    break;
                }
            case -1539906063:
                if (str2.equals("font_size")) {
                    c = 11;
                    break;
                }
            case -1525260053:
                if (str2.equals("ambient_display_always_on")) {
                    c = 3;
                    break;
                }
            case -1314247385:
                if (str2.equals("mobile_data")) {
                    c = 16;
                    break;
                }
            case -1001942051:
                if (str2.equals("toggle_airplane")) {
                    c = 2;
                    break;
                }
            case -610139245:
                if (str2.equals("talkback")) {
                    c = 25;
                    break;
                }
            case -608350689:
                if (str2.equals("gesture_assist")) {
                    c = 0;
                    break;
                }
            case -416443991:
                if (str2.equals("preferred_network_type")) {
                    c = 27;
                    break;
                }
            case -382039141:
                if (str2.equals("night_display")) {
                    c = 18;
                    break;
                }
            case -315259171:
                if (str2.equals("enable_wifi_ap")) {
                    c = 12;
                    break;
                }
            case -42893370:
                if (str2.equals("screen_zoom")) {
                    c = 21;
                    break;
                }
            case 108971:
                if (str2.equals("nfc")) {
                    c = 17;
                    break;
                }
            case 15719777:
                if (str2.equals("master_wifi_toggle")) {
                    c = 26;
                    break;
                }
            case 536948395:
                if (str2.equals("magnify_gesture")) {
                    c = 14;
                    break;
                }
            case 556955191:
                if (str2.equals("color_inversion")) {
                    c = 6;
                    break;
                }
            case 558352831:
                if (str2.equals("zen_mode")) {
                    c = 9;
                    break;
                }
            case 860063886:
                if (str2.equals("screen_timeout")) {
                    c = 8;
                    break;
                }
            case 1043529839:
                if (str2.equals("gesture_double_twist")) {
                    c = 10;
                    break;
                }
            case 1376598223:
                if (str2.equals("switch_access")) {
                    c = 23;
                    break;
                }
            case 1580211920:
                if (str2.equals("night_display_temperature")) {
                    c = 19;
                    break;
                }
            case 1599575662:
                if (str2.equals("magnify_navbar")) {
                    c = 15;
                    break;
                }
            case 1619122624:
                if (str2.equals("data_saver")) {
                    c = 7;
                    break;
                }
            case 1839966665:
                if (str2.equals("assist_sensitivity")) {
                    c = 1;
                    break;
                }
            case 1901043637:
                if (str2.equals("location")) {
                    c = 13;
                    break;
                }
            case 1988786469:
                if (str2.equals("bluetooth_toggle_key")) {
                    c = 5;
                    break;
                }
            case 2056099474:
                if (str2.equals("toggle_lock_screen_rotation_preference")) {
                    c = 4;
                    break;
                }
            default:
                c = 65535;
                break;
        }
        switch (c) {
            case 0:
                cursor = InlineSettings.ACTIVE_EDGE_SETTING.getUpdateCursor(context, str3);
                break;
            case 1:
                cursor = InlineSettings.ACTIVE_EDGE_SENSITIVITY_SETTING.getUpdateCursor(context, str3);
                break;
            case 2:
                cursor = InlineSettings.AIRPLANE_MODE_SETTING.getUpdateCursor(context, str3);
                break;
            case 3:
                cursor = InlineSettings.AMBIENT_DISPLAY_ALWAYS_ON_SETTING.getUpdateCursor(context, str3);
                break;
            case 4:
                cursor = InlineSettings.AUTO_ROTATE_SETTING.getUpdateCursor(context, str3);
                break;
            case 5:
                cursor = InlineSettings.BLUETOOTH_SETTING.getUpdateCursor(context, str3);
                break;
            case 6:
                cursor = InlineSettings.COLOR_INVERSION_SETTING.getUpdateCursor(context, str3);
                break;
            case 7:
                cursor = InlineSettings.DATA_SAVER_SETTING.getUpdateCursor(context, str3);
                break;
            case 8:
                cursor = InlineSettings.DISPLAY_TIMEOUT_SETTING.getUpdateCursor(context, str3);
                break;
            case 9:
                cursor = InlineSettings.DO_NOT_DISTURB_SETTING.getUpdateCursor(context, str3);
                break;
            case 10:
                cursor = InlineSettings.DOUBLE_TWIST_GESTURE_SETTING.getUpdateCursor(context, str3);
                break;
            case 11:
                cursor = InlineSettings.FONT_SIZE_SETTING.getUpdateCursor(context, str3);
                break;
            case 12:
                cursor = InlineSettings.HOTSPOT_SETTING.getUpdateCursor(context, str3);
                break;
            case 13:
                cursor = InlineSettings.LOCATION_SETTING.getUpdateCursor(context, str3);
                break;
            case 14:
                cursor = InlineSettings.MAGNIFY_GESTURE_SETTING.getUpdateCursor(context, str3);
                break;
            case 15:
                cursor = InlineSettings.MAGNIFY_NAVBAR_SETTING.getUpdateCursor(context, str3);
                break;
            case 16:
                cursor = InlineSettings.MOBILE_DATA_SETTING.getUpdateCursor(context, str3);
                break;
            case 17:
                cursor = InlineSettings.NFC_SETTING.getUpdateCursor(context, str3);
                break;
            case 18:
                cursor = InlineSettings.NIGHTDISPLAY_SETTING.getUpdateCursor(context, str3);
                break;
            case 19:
                cursor = InlineSettings.NIGHTDISPLAY_INTENSITY_SETTING.getUpdateCursor(context, str3);
                break;
            case 20:
                cursor = InlineSettings.PICKUP_GESTURE_SETTING.getUpdateCursor(context, str3);
                break;
            case 21:
                cursor = InlineSettings.SCREEN_ZOOM_SETTING.getUpdateCursor(context, str3);
                break;
            case 22:
                cursor = InlineSettings.SWIPE_TO_NOTIFICATION_SETTING.getUpdateCursor(context, str3);
                break;
            case 23:
                cursor = InlineSettings.SWITCH_ACCESS_SETTING.getUpdateCursor(context, str3);
                break;
            case 24:
                cursor = InlineSettings.SYSTEM_UPDATE_SETTING.getUpdateCursor(context, str3);
                break;
            case 25:
                cursor = InlineSettings.TALKBACK_SETTING.getUpdateCursor(context, str3);
                break;
            case 26:
                cursor = InlineSettings.WIFI_SETTING.getUpdateCursor(context, str3);
                break;
            case 27:
                cursor = InlineSettings.MOBILE_NETWORK_SETTINGS.getUpdateCursor(context, str3);
                break;
        }
        if (cursor == null || !cursor.moveToFirst()) {
            StringBuilder sb = new StringBuilder();
            sb.append("Invalid update special case key: ");
            sb.append(str2);
            throw new IllegalArgumentException(sb.toString());
        }
        int columnIndex = cursor.getColumnIndex("newValue");
        if (columnIndex >= 0) {
            i = cursor.getInt(columnIndex);
        }
        logUpdateSetting(context, str, str2, i);
        return cursor;
    }
   
    public static Cursor getUpdateCursorFromPayload(Context context, String str, String str2, String str3) {
        Throwable th = null;
        Throwable th2 = null;
        Cursor cursorFromUriKey = getCursorFromUriKey(context, str2);

        try {
            String string = cursorFromUriKey.getString(cursorFromUriKey.getColumnIndex("class_name"));
            int i = cursorFromUriKey.getInt(cursorFromUriKey.getColumnIndex("payload_type"));
            byte[] blob = cursorFromUriKey.getBlob(cursorFromUriKey.getColumnIndex("payload"));
            int i2 = cursorFromUriKey.getInt(cursorFromUriKey.getColumnIndex("icon"));
            String string2 = cursorFromUriKey.getString(cursorFromUriKey.getColumnIndex("screen_title"));
            if (cursorFromUriKey != null) {
                ExternalSettingsManager.closeResource(null, cursorFromUriKey);
            }
            if (i != 0) {
                InlinePayload inlinePayload = (InlinePayload) ExternalSettingsManager.getUnmarshalledPayload(blob, i);
                string = ExternalSettingsManager.buildSearchResultPageIntentString(context, string, str2, string2);
                String buildSearchResultPageIntentString = buildSearchResultPageIntentString(context, string, str2, string2);

                int value = inlinePayload.getValue(context);
                int availability = inlinePayload.getAvailability();
                int intValue = Integer.valueOf(str3).intValue();
                int i3 = (availability == 0 && inlinePayload.setValue(context, intValue)) ? intValue : value;

                MatrixCursor matrixCursor = new MatrixCursor(ExternalSettingsContract.EXTERNAL_SETTINGS_UPDATE_COLUMNS);
                matrixCursor.newRow().add("newValue", Integer.valueOf(i3)).add("existing_value", Integer.valueOf(value)).add("availability", Integer.valueOf(availability)).add("intent", string).add("icon", Integer.valueOf(i2));
                logUpdateSetting(context, str, str2, i3);
                return matrixCursor;
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("No update support for settings key: ");
            stringBuilder.append(str2);
            throw new IllegalArgumentException(stringBuilder.toString());
        } catch (Throwable th3) {
            th = th3;
        }
        if (cursorFromUriKey != null) {
            ExternalSettingsManager.closeResource(th2, cursorFromUriKey);
        }
        return null;
    }

    private static void logAccessSetting(Context context, String str, String str2, int i) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(str);
        stringBuilder.append("/access");
        FeatureFactory.getFactory(context).getMetricsFeatureProvider().count(context, SharedPreferencesLogger.buildCountName(SharedPreferencesLogger.buildPrefKey(stringBuilder.toString(), str2), Integer.valueOf(i)), 1);
    }

    private static void logUpdateSetting(Context context, String str, String str2, int i) {
        FeatureFactory.getFactory(context).getMetricsFeatureProvider().count(context, SharedPreferencesLogger.buildCountName(SharedPreferencesLogger.buildPrefKey(str, str2), Integer.valueOf(i)), 1);
    }

    static void verifyIndexing(Context context) {
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            FeatureFactory.getFactory(context).getSearchFeatureProvider().updateIndex(context);
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }
}
