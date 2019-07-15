package com.google.android.settings.gestures.assist;

import android.net.Uri;
import android.database.ContentObserver;
import android.text.TextUtils;
import android.content.ContentResolver;
import android.provider.Settings;
import android.support.v7.preference.Preference;
import android.os.Looper;
import com.android.settings.overlay.FeatureFactory;
import android.content.Context;
import android.support.v7.preference.PreferenceScreen;
import android.support.v14.preference.SwitchPreference;
import android.os.Handler;
import com.android.settings.gestures.AssistGestureFeatureProvider;
import com.android.settingslib.core.lifecycle.events.OnResume;
import com.android.settingslib.core.lifecycle.events.OnPause;
import com.android.settingslib.core.lifecycle.LifecycleObserver;
import com.android.settings.gestures.GesturePreferenceController;
import com.android.settings.R;

public class AssistGestureWakePreferenceController extends GesturePreferenceController implements LifecycleObserver, OnPause, OnResume
{
    private static final String PREF_KEY_VIDEO = "gesture_assist_video";
    private final AssistGestureFeatureProvider mFeatureProvider;
    private Handler mHandler;
    private SwitchPreference mPreference;
    private PreferenceScreen mScreen;
    private final SettingObserver mSettingObserver;
    
    public AssistGestureWakePreferenceController(final Context context, final String s) {
        super(context, s);
        this.mFeatureProvider = FeatureFactory.getFactory(context).getAssistGestureFeatureProvider();
        this.mHandler = new Handler(Looper.getMainLooper());
        this.mSettingObserver = new SettingObserver();
    }
    
    private void updatePreference() {
        if (this.mPreference == null) {
            return;
        }
        if (this.mFeatureProvider.isSupported(this.mContext)) {
            if (this.mScreen.findPreference(this.getPreferenceKey()) == null) {
                this.mScreen.addPreference(this.mPreference);
            }
            this.mPreference.setEnabled(this.canHandleClicks());
            return;
        }
        this.mScreen.removePreference(this.mPreference);
    }
    
    @Override
    protected boolean canHandleClicks() {
        final ContentResolver contentResolver = this.mContext.getContentResolver();
        boolean b = true;
        if (Settings.Secure.getInt(contentResolver, "assist_gesture_enabled", 1) == 0) {
            b = false;
        }
        return b;
    }
    
    @Override
    public void displayPreference(final PreferenceScreen mScreen) {
        this.mScreen = mScreen;
        this.mPreference = (SwitchPreference)mScreen.findPreference(this.getPreferenceKey());
        if (!this.mFeatureProvider.isSupported(this.mContext)) {
            this.mScreen.removePreference(this.mPreference);
            return;
        }
        super.displayPreference(mScreen);
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
        if (Settings.Secure.getInt(contentResolver, "assist_gesture_wake_enabled", 1) == 0) {
            b = false;
        }
        return b;
    }
    
    @Override
    public boolean isSliceable() {
        return TextUtils.equals((CharSequence)this.getPreferenceKey(), (CharSequence)"gesture_assist_wake");
    }
    
    @Override
    public void onPause() {
        this.mSettingObserver.unregister();
    }
    
    @Override
    public void onResume() {
        this.mSettingObserver.register();
        this.updatePreference();
    }
    
    @Override
    public boolean setChecked(final boolean b) {
        return Settings.Secure.putInt(this.mContext.getContentResolver(), "assist_gesture_wake_enabled", (int)(b ? 1 : 0));
    }
    
    class SettingObserver extends ContentObserver
    {
        private final Uri ASSIST_GESTURE_ENABLED_URI;
        
        public SettingObserver() {
            super(AssistGestureWakePreferenceController.this.mHandler);
            this.ASSIST_GESTURE_ENABLED_URI = Settings.Secure.getUriFor("assist_gesture_enabled");
        }
        
        public void onChange(final boolean b) {
            AssistGestureWakePreferenceController.this.updatePreference();
        }
        
        public void register() {
            AssistGestureWakePreferenceController.this.mContext.getContentResolver().registerContentObserver(this.ASSIST_GESTURE_ENABLED_URI, false, (ContentObserver)this);
        }
        
        public void unregister() {
            AssistGestureWakePreferenceController.this.mContext.getContentResolver().unregisterContentObserver((ContentObserver)this);
        }
    }
}
