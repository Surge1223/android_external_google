package com.google.android.settings.gestures.assist;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.os.SystemClock;
import android.os.UserHandle;
import android.os.UserManager;
import android.provider.Settings.Secure;
import android.support.v7.preference.Preference;
import android.support.v7.preference.Preference.OnPreferenceChangeListener;
import android.support.v7.preference.PreferenceScreen;
import android.text.TextUtils;
import android.util.Log;
import com.android.settings.R;

import com.android.settings.core.PreferenceControllerMixin;
import com.android.settings.gestures.AssistGestureFeatureProvider;
import com.android.settings.overlay.FeatureFactory;
import com.android.settings.widget.SeekBarPreference;
import com.android.settingslib.RestrictedLockUtils;
import com.android.settingslib.RestrictedLockUtils.EnforcedAdmin;
import com.android.settingslib.core.AbstractPreferenceController;
import com.android.settingslib.core.lifecycle.Lifecycle;
import com.android.settingslib.core.lifecycle.LifecycleObserver;
import com.android.settingslib.core.lifecycle.events.OnPause;
import com.android.settingslib.core.lifecycle.events.OnResume;
import com.google.android.settings.gestures.assist.AssistGestureHelper.GestureListener;
import com.google.android.settings.gestures.assist.bubble.AssistGestureBubbleActivity;

public class AssistGestureSensitivityPreferenceController extends AbstractPreferenceController
        implements PreferenceControllerMixin,
        OnPreferenceChangeListener,
        LifecycleObserver,
        OnPause,
        OnResume {
    private AssistGestureFeatureProvider mFeatureProvider;
    private GestureListener mGestureListener = new C10281();
    private SettingObserver mSettingObserver;
    private UserManager mUserManager;
    private AssistGestureHelper mAssistGestureHelper;
    private EnforcedAdmin mFunDisallowedAdmin;
    private boolean mFunDisallowedBySystem;
    private long[] mHits = new long[3];
    private SeekBarPreference mPreference;
    private Handler mHandler =
            new Handler(Looper.getMainLooper()) {
                public void handleMessage(Message message) {
                    switch (message.what) {
                        case 1:
                            AssistGestureSensitivityPreferenceController.this.mPreference.setShouldBlink(true);
                            return;
                        default:
                            return;
                    }
                }
            };
    private boolean mWasListening;

    public AssistGestureSensitivityPreferenceController(Context context, Lifecycle lifecycle) {
        super(context);
        mFeatureProvider = FeatureFactory.getFactory(context).getAssistGestureFeatureProvider();
        mSettingObserver = new SettingObserver();
        mAssistGestureHelper = new AssistGestureHelper(context);
        mUserManager = (UserManager) context.getSystemService(Context.USER_SERVICE);
        if (lifecycle != null) {
            lifecycle.addObserver(this);
        }
    }

    public static int convertSensitivityFloatToInt(Context context, float f) {
        return Math.round(((float) getMaxSensitivityResourceInteger(context)) * f);
    }

    public static float convertSensitivityIntToFloat(Context context, int i) {
        return 1.0f - (((float) i) / ((float) getMaxSensitivityResourceInteger(context)));
    }

    public static int getMaxSensitivityResourceInteger(Context context) {
        int maximum;
        maximum = context.getResources().getInteger(R.integer.gesture_assist_sensitivity_max);
        return maximum;
    }

    public static float getSensitivity(Context context) {
        float f = Secure.getFloat(context.getContentResolver(), "assist_gesture_sensitivity", 0.5f);
        if (f < 0.0f || f > 1.0f) {
            f = 0.5f;
        }
        return 1.0f - f;
    }

    public static int getSensitivityInt(Context context) {
        return convertSensitivityFloatToInt(context, getSensitivity(context));
    }

    public static boolean isAvailable(
            Context context, AssistGestureFeatureProvider assistGestureFeatureProvider) {
        return assistGestureFeatureProvider.isSensorAvailable(context);
    }

    private void updateGestureListenerState(boolean mWasListening) throws RemoteException {
        if (mWasListening == mWasListening) {
            return;
        }
        if (mWasListening) {
            mAssistGestureHelper.setListener(mGestureListener);
        } else {
            mAssistGestureHelper.setListener(null);
        }
        mWasListening = mWasListening;
    }

    private void updatePreference() {
        if (mPreference != null) {
            mPreference.setProgress(getSensitivityInt(mContext));
            boolean z =
                    Secure.getInt(mContext.getContentResolver(), "assist_gesture_enabled", 1) != 0;
            boolean z2 =
                    Secure.getInt(
                            mContext.getContentResolver(), "assist_gesture_silence_alerts_enabled", 1)
                            != 0;
            if (mFeatureProvider.isSupported(mContext) && (z || z2)) {
                mPreference.setEnabled(true);
            } else if (mFeatureProvider.isSensorAvailable(mContext) && z2) {
                mPreference.setEnabled(true);
            } else {
                mPreference.setEnabled(false);
            }
            if (z && mFeatureProvider.isSupported(mContext)) {
                z2 = true;
            }
            try {
                updateGestureListenerState(z2);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void displayPreference(PreferenceScreen preferenceScreen) {
        mPreference = (SeekBarPreference)preferenceScreen.findPreference(getPreferenceKey());
        if (!mFeatureProvider.isSupported(mContext)) {
            setVisible(preferenceScreen, "gesture_assist_video", false);
        }
        else {
            setVisible(preferenceScreen, "gesture_assist_video_silence", false);
        }
        super.displayPreference(preferenceScreen);
    }

    public String getPreferenceKey() {
        return "gesture_assist_sensitivity";
    }

    public boolean handlePreferenceTreeClick(Preference preference) {
        if (!TextUtils.equals(preference.getKey(), "gesture_assist_sensitivity")) {
            return false;
        }
        System.arraycopy(mHits, 1, mHits, 0, mHits.length - 1);
        mHits[mHits.length - 1] = SystemClock.uptimeMillis();
        if (mHits[0] < SystemClock.uptimeMillis() - 500) {
            return false;
        }
        if (!mUserManager.hasUserRestriction("no_fun")) {
            Intent intent = new Intent(mContext, AssistGestureBubbleActivity.class);
            try {
                mContext.startActivity(intent);
                return true;
            } catch (Exception e) {
                Log.e(
                        "AssistGestureSensitivityPreferenceController",
                        "Unable to start activity " + intent.toString());
                return false;
            }
        } else if (mFunDisallowedAdmin == null) {
            return false;
        } else {
            RestrictedLockUtils.sendShowAdminSupportDetailsIntent(
                    mContext, mFunDisallowedAdmin);
            return false;
        }
    }

    public boolean isSliceable() {
        return TextUtils.equals((CharSequence)getPreferenceKey(), (CharSequence)"gesture_assist_sensitivity");
    }

    public boolean isAvailable() {
        return isAvailable(mContext, mFeatureProvider);
    }

    public void onPause() {
        try {
            updateGestureListenerState(false);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        mAssistGestureHelper.unbindFromElmyraServiceProxy();
        mSettingObserver.unregister();
    }

    public boolean onPreferenceChange(Preference preference, Object obj) {
        Secure.putFloat(
                mContext.getContentResolver(),
                "assist_gesture_sensitivity",
                convertSensitivityIntToFloat(mContext, ((Integer) obj).intValue()));
        return true;
    }

    public void onResume() {
        mAssistGestureHelper.bindToElmyraServiceProxy();
        mSettingObserver.register();
        updatePreference();
        mFunDisallowedAdmin =
                RestrictedLockUtils.checkIfRestrictionEnforced(
                        mContext, "no_fun", UserHandle.myUserId());
        mFunDisallowedBySystem =
                RestrictedLockUtils.hasBaseUserRestriction(mContext, "no_fun", UserHandle.myUserId());
    }

    public void updateState(Preference preference) {
        super.updateState(preference);
        updatePreference();
    }

    class C10281 implements GestureListener {
        C10281() {}

        public void onGestureDetected() {
            AssistGestureSensitivityPreferenceController.this.mHandler.obtainMessage(1).sendToTarget();
        }

        public void onGestureProgress(float f, int i) {}
    }

    class SettingObserver extends ContentObserver {
        private Uri ASSIST_GESTURE_ENABLED_URI = Secure.getUriFor("assist_gesture_enabled");
        private Uri ASSIST_GESTURE_SENSITIVITY_URI =
                Secure.getUriFor("assist_gesture_sensitivity");
        private Uri ASSIST_GESTURE_SILENCE_PHONE_ENABLED_URI =
                Secure.getUriFor("assist_gesture_silence_alerts_enabled");

        SettingObserver() {
            super(AssistGestureSensitivityPreferenceController.this.mHandler);
        }

        public void onChange(boolean z) {
            AssistGestureSensitivityPreferenceController.this.updatePreference();
        }

        public void register() {
            ContentResolver contentResolver =
                    AssistGestureSensitivityPreferenceController.this.mContext.getContentResolver();
            contentResolver.registerContentObserver(ASSIST_GESTURE_ENABLED_URI, false, this);
            contentResolver.registerContentObserver(
                    ASSIST_GESTURE_SILENCE_PHONE_ENABLED_URI, false, this);
            contentResolver.registerContentObserver(ASSIST_GESTURE_SENSITIVITY_URI, false, this);
        }

        public void unregister() {
            AssistGestureSensitivityPreferenceController.this
                    .mContext
                    .getContentResolver()
                    .unregisterContentObserver(this);
        }
    }
}

