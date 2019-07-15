package com.google.android.settings.gestures.assist;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.view.View;
import android.view.ContextThemeWrapper;
import android.view.WindowManager;
import android.view.ViewGroup;

import com.android.settings.gestures.AssistGestureFeatureProvider;
import com.android.settings.overlay.FeatureFactory;
import com.android.settings.core.InstrumentedActivity;

public abstract class AssistGestureTrainingBase extends InstrumentedActivity implements AssistGestureHelper.GestureListener {

    protected AssistGestureHelper mAssistGestureHelper;
    private AssistGestureIndicatorView mIndicatorView;
    private String mLaunchedFrom;
    private WindowManager mWindowManager;

    protected void clearIndicators() {
        mIndicatorView.onGestureProgress(0.0f);
    }

    protected void fadeIndicators() {
        mIndicatorView.onGestureDetected();
    }

    protected boolean flowTypeAccidentalTrigger() {
        return "accidental_trigger".contentEquals(mLaunchedFrom);
    }

    protected boolean flowTypeDeferredSetup() {
        return "deferred_setup".contentEquals(mLaunchedFrom);
    }

    protected boolean flowTypeSettingsSuggestion() {
        return "settings_suggestion".contentEquals(mLaunchedFrom);
    }

    protected boolean flowTypeSetup() {
        return "setup".contentEquals(mLaunchedFrom);
    }

    protected void handleDoneAndLaunch() {
        setResult(-1);
        mAssistGestureHelper.setListener(null);
        mAssistGestureHelper.launchAssistant();
        finishAndRemoveTask();
    }

    protected void launchAssistGestureSettings() {
        startActivity(new Intent("android.settings.ASSIST_GESTURE_SETTINGS"));
    }

    @Override
    protected void onCreate(final Bundle bundle) {
        super.onCreate(bundle);
        mWindowManager = (WindowManager)getSystemService(Context.WINDOW_SERVICE);
        (mAssistGestureHelper = new AssistGestureHelper(getApplicationContext())).setListener(this);
        mLaunchedFrom = getIntent().getStringExtra("launched_from");
        mIndicatorView = new AssistGestureIndicatorView(new ContextThemeWrapper(getApplicationContext(), getTheme()));
    }

    @Override
    public void onGestureProgress(final float n, final int n2) {
        mIndicatorView.onGestureProgress(n);
    }

    public void onPause() {
        super.onPause();
        mAssistGestureHelper.setListener(null);
        mAssistGestureHelper.unbindFromElmyraServiceProxy();
        clearIndicators();
        mWindowManager.removeView(mIndicatorView);
    }

    public void onResume() {
        super.onResume();
        boolean assistEnabled = Settings.Secure.getInt(getContentResolver(), "assist_gesture_enabled", 1) != 0;
        AssistGestureFeatureProvider assistGestureFeatureProvider = FeatureFactory.getFactory(this).getAssistGestureFeatureProvider();
        mWindowManager.addView(mIndicatorView, mIndicatorView.getLayoutParams(getWindow().getAttributes()));
        mAssistGestureHelper.bindToElmyraServiceProxy();
        mAssistGestureHelper.setListener(this);
        if (assistGestureFeatureProvider.isSupported(this) && assistEnabled) {
            return;
        }
        setResult(1);
        finishAndRemoveTask();
    }

    protected class HandleProgress
    {
        private boolean mErrorSqueezeBottomShown;
        private Handler mHandler;
        private int mLastStage;
        private boolean mShouldCheckForNoProgress;

        public HandleProgress(Handler mHandler) {
            mHandler = mHandler;
            mShouldCheckForNoProgress = true;
        }

        private boolean checkSqueezeNoProgress(int n) {
            return mLastStage == 1 && n == 0;
        }

        private boolean checkSqueezeTooLong(int n) {
            return mLastStage == 2 && n == 0;
        }

        public void onGestureDetected() {
            mLastStage = 0;
        }

        public void onGestureProgress(final float n, int mLastStage) {
            int n2 = 0;
            if (mLastStage != mLastStage) {
                if (mShouldCheckForNoProgress && checkSqueezeNoProgress(mLastStage)) {
                    n2 = 1;
                }
                else if (checkSqueezeTooLong(mLastStage)) {
                    n2 = 2;
                }
                mLastStage = mLastStage;
                if (n2 != 0) {
                    if ((mLastStage = n2) == 1) {
                        if (mErrorSqueezeBottomShown) {
                            n2 = 4;
                        }
                        mErrorSqueezeBottomShown = true;
                        mLastStage = n2;
                    }
                    mHandler.obtainMessage(2, mLastStage, 0).sendToTarget();
                }
            }
        }

        public void setShouldCheckForNoProgress(boolean mShouldCheckForNoProgress) {
            mShouldCheckForNoProgress = mShouldCheckForNoProgress;
        }
    }
}
