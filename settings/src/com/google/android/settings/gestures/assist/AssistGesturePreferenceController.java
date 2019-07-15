package com.google.android.settings.gestures.assist;

import android.text.TextUtils;
import android.content.ContentResolver;
import android.provider.Settings;
import android.support.v7.preference.PreferenceGroup;
import com.android.settings.overlay.FeatureFactory;
import android.content.Context;
import android.support.v7.preference.PreferenceScreen;
import android.support.v7.preference.Preference;
import com.android.settings.gestures.AssistGestureFeatureProvider;
import com.android.internal.annotations.VisibleForTesting;
import com.android.settingslib.core.lifecycle.events.OnResume;
import com.android.settingslib.core.lifecycle.LifecycleObserver;
import com.android.settings.gestures.GesturePreferenceController;
import com.android.settings.R;

public class AssistGesturePreferenceController extends GesturePreferenceController implements LifecycleObserver, OnResume
{
    @VisibleForTesting
    static final int OFF = 0;
    @VisibleForTesting
    static final int ON = 1;
    private static final String PREF_KEY_VIDEO = "gesture_assist_video";
    private static final String SECURE_KEY_ASSIST = "assist_gesture_enabled";
    private final AssistGestureFeatureProvider mFeatureProvider;
    private Preference mPreference;
    private PreferenceScreen mScreen;

    public AssistGesturePreferenceController(final Context context, final String s) {
        super(context, s);
        this.mFeatureProvider = FeatureFactory.getFactory(context).getAssistGestureFeatureProvider();
    }

    private void updatePreference() {
        if (this.mPreference != null && this.mScreen != null) {
            this.setVisible(this.mScreen, this.getPreferenceKey(), this.isAvailable());
        }
    }

    @Override
    public void displayPreference(final PreferenceScreen mScreen) {
        this.mScreen = mScreen;
        this.mPreference = mScreen.findPreference(this.getPreferenceKey());
        super.displayPreference(mScreen);
    }

    @Override
    public int getAvailabilityStatus() {
        int n;
        if (this.mFeatureProvider.isSupported(this.mContext)) {
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
        if (Settings.Secure.getInt(contentResolver, "assist_gesture_enabled", 1) == 0) {
            b = false;
        }
        return b;
    }

    @Override
    public boolean isSliceable() {
        return TextUtils.equals((CharSequence)this.getPreferenceKey(), (CharSequence)"gesture_assist");
    }

    @Override
    public void onResume() {
        this.updatePreference();
    }

    @Override
    public boolean setChecked(final boolean b) {
        return Settings.Secure.putInt(this.mContext.getContentResolver(), "assist_gesture_enabled", (int)(b ? 1 : 0));
    }
}

