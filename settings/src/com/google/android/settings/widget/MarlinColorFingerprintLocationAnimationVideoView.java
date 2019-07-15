package com.google.android.settings.widget;

import android.view.ContextThemeWrapper;
import android.os.SystemProperties;
import android.util.AttributeSet;
import android.content.Context;
import com.android.setupwizardlib.view.IllustrationVideoView;

public class MarlinColorFingerprintLocationAnimationVideoView extends IllustrationVideoView
{
    public MarlinColorFingerprintLocationAnimationVideoView(final Context context, final AttributeSet set) {
        super(getDeviceColorTheme(context), set);
    }
    
    private static Context getDeviceColorTheme(final Context context) {
        final String value = SystemProperties.get("ro.boot.hardware.color");
        int n;
        if ("BLU00".equals(value)) {
            n = 2131951822;
        }
        else if ("SLV00".equals(value)) {
            n = 2131951824;
        }
        else if ("GRA00".equals(value)) {
            n = 2131951823;
        }
        else {
            n = 2131951821;
        }
        return (Context)new ContextThemeWrapper(context, n);
    }
}
