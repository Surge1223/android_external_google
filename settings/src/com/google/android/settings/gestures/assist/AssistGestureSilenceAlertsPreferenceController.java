package com.google.android.settings.gestures.assist;

import android.text.TextUtils;
import android.content.ContentResolver;
import android.provider.Settings;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceScreen;
import com.android.settings.overlay.FeatureFactory;
import android.content.Context;
import com.android.settings.gestures.AssistGestureFeatureProvider;
import com.android.settings.gestures.GesturePreferenceController;

public class AssistGestureSilenceAlertsPreferenceController extends GesturePreferenceController
{
    private static final String ASSIST_GESTURE_SILENCE_ALERTS_PREF_KEY = "gesture_assist_silence";
    private static final String PREF_KEY_VIDEO = "gesture_assist_video";
    private final AssistGestureFeatureProvider mFeatureProvider;
    
    public AssistGestureSilenceAlertsPreferenceController(final Context context, final String s) {
        super(context, s);
        this.mFeatureProvider = FeatureFactory.getFactory(context).getAssistGestureFeatureProvider();
    }
    
    @Override
    public void displayPreference(final PreferenceScreen preferenceScreen) {
        if (((AssistGestureFeatureProviderGoogleImpl)this.mFeatureProvider).isDeskClockSupported(this.mContext)) {
            final Preference preference = preferenceScreen.findPreference("gesture_assist_silence");
            if (preference != null) {
                preference.setSummary(2131886433);
            }
        }
        super.displayPreference(preferenceScreen);
    }
    
    @Override
    public int getAvailabilityStatus() {
        int n;
        if (this.mFeatureProvider.isSensorAvailable(this.mContext)) {
            n = 0;
        }
        else {
            n = 2;
        }
        return n;
    }
    
    @Override
    protected String getVideoPrefKey() {
        return "gesture_assist_video";
    }
    
    @Override
    public boolean isChecked() {
        final ContentResolver contentResolver = this.mContext.getContentResolver();
        boolean b = true;
        if (Settings.Secure.getInt(contentResolver, "assist_gesture_silence_alerts_enabled", 1) == 0) {
            b = false;
        }
        return b;
    }
    
    @Override
    public boolean isSliceable() {
        return TextUtils.equals((CharSequence)this.getPreferenceKey(), (CharSequence)"gesture_assist_silence");
    }
    
    @Override
    public boolean setChecked(final boolean b) {
        return Settings.Secure.putInt(this.mContext.getContentResolver(), "assist_gesture_silence_alerts_enabled", (int)(b ? 1 : 0));
    }
}
