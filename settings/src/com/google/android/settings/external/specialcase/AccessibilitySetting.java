package com.google.android.settings.external.specialcase;

import android.database.MatrixCursor;
import com.google.android.settings.external.ExternalSettingsContract;
import android.app.Fragment;
import com.android.settings.accessibility.AccessibilitySettings;
import android.database.Cursor;
import java.util.Set;
import android.content.ContentResolver;
import com.android.settingslib.accessibility.AccessibilityUtils;
import android.provider.Settings;
import java.util.List;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.view.accessibility.AccessibilityManager;
import android.content.Context;
import android.content.ComponentName;
import com.google.android.settings.external.Queryable;

public class AccessibilitySetting implements Queryable
{
    private final ComponentName mComponentName;
    private final String mKey;
    private final String mServiceName;
    
    public AccessibilitySetting(final String mKey, final String mServiceName) {
        this.mServiceName = mServiceName;
        this.mKey = mKey;
        this.mComponentName = ComponentName.unflattenFromString(this.mServiceName);
    }
    
    private int getAvailability(final Context context) {
        final List installedAccessibilityServiceList = ((AccessibilityManager)context.getSystemService("accessibility")).getInstalledAccessibilityServiceList();
        for (int i = 0; i < installedAccessibilityServiceList.size(); ++i) {
            if (this.mComponentName.equals((Object)installedAccessibilityServiceList.get(i))) {
                return 0;
            }
        }
        return 4;
    }
    
    private int getCurrentValue(final Context context) {
        final ContentResolver contentResolver = context.getContentResolver();
        final boolean b = false;
        final boolean b2 = Settings.Secure.getInt(contentResolver, "accessibility_enabled", 0) == 1;
        final Set<ComponentName> enabledServicesFromSettings = AccessibilityUtils.getEnabledServicesFromSettings(context);
        int n = b ? 1 : 0;
        if (b2) {
            n = (b ? 1 : 0);
            if (enabledServicesFromSettings.contains(this.mComponentName)) {
                n = 1;
            }
        }
        return n;
    }
    
    private int getIconResource() {
        return 0;
    }
    
    private String getScreenTitle(final Context context) {
        return context.getString(2131886186);
    }
    
    private void setTalkback(final Context context, final boolean b) {
        AccessibilityUtils.setAccessibilityServiceState(context, this.mComponentName, b);
    }
    
    private void validateInput(final int n) {
        if (n != 1 && n != 0) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Unexpected value for Accessibility:");
            sb.append(this.mKey);
            sb.append(". Expected 0 or 1, but found: ");
            sb.append(n);
            throw new IllegalArgumentException(sb.toString());
        }
    }
    
    public Cursor getAccessCursor(final Context context) {
        final int currentValue = this.getCurrentValue(context);
        final int availability = this.getAvailability(context);
        final int iconResource = this.getIconResource();
        final String intentString = this.getIntentString(context, this.mServiceName, AccessibilitySettings.class, this.getScreenTitle(context));
        final MatrixCursor matrixCursor = new MatrixCursor(ExternalSettingsContract.EXTERNAL_SETTINGS_QUERY_COLUMNS);
        matrixCursor.newRow().add("existing_value", (Object)currentValue).add("availability", (Object)availability).add("intent", (Object)intentString).add("icon", (Object)iconResource);
        return (Cursor)matrixCursor;
    }
    
    @Override
    public Cursor getUpdateCursor(final Context context, final int n) {
        this.validateInput(n);
        final int currentValue = this.getCurrentValue(context);
        final int availability = this.getAvailability(context);
        final String intentString = this.getIntentString(context, this.mServiceName, AccessibilitySettings.class, this.getScreenTitle(context));
        final int iconResource = this.getIconResource();
        int n2 = currentValue;
        if (this.shouldChangeValue(availability, currentValue, n)) {
            boolean b = true;
            if (n != 1) {
                b = false;
            }
            this.setTalkback(context, b);
            n2 = n;
        }
        final MatrixCursor matrixCursor = new MatrixCursor(ExternalSettingsContract.EXTERNAL_SETTINGS_UPDATE_COLUMNS);
        matrixCursor.newRow().add("newValue", (Object)n2).add("existing_value", (Object)currentValue).add("availability", (Object)availability).add("intent", (Object)intentString).add("icon", (Object)iconResource);
        return (Cursor)matrixCursor;
    }
}
