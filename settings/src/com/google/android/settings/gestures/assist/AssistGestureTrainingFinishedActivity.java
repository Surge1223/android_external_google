package com.google.android.settings.gestures.assist;

import android.widget.SeekBar;
import android.widget.Button;
import com.android.settings.SetupWizardUtils;
import android.os.Bundle;
import android.view.ViewGroup;
import android.view.View;
import android.os.Handler;
import com.android.settings.R;

public class AssistGestureTrainingFinishedActivity extends AssistGestureTrainingSliderBase
{
    private boolean mAccessibilityAnnounced;
    private View mAssistGestureCheck;
    private View mAssistGestureIllustration;
    private ViewGroup mLayout;

    private void fadeOutCheckAfterDelay() {
        mHandler.sendMessageDelayed(mHandler.obtainMessage(5, (Object)mAssistGestureCheck), 1000L);
    }

    public int getMetricsCategory() {
        return 993;
    }

    @Override
    protected void handleGestureDetected() {
        mErrorView.setVisibility(View.INVISIBLE);
        mAssistGestureCheck.animate().cancel();
        mAssistGestureCheck.setAlpha(1.0f);
        mAssistGestureCheck.setVisibility(0);
        if (!mAccessibilityAnnounced) {
            mLayout.announceForAccessibility((CharSequence)getApplicationContext().getResources().getString(R.string.accessibility_assist_gesture_complete_or_keep_adjusting));
            mAccessibilityAnnounced = true;
        }
        mHandler.removeMessages(4);
        mHandler.removeMessages(5);
        fadeOutCheckAfterDelay();
        fadeIndicators();
    }

    @Override
    public void onClick(final View view) {
        switch (view.getId()) {
            case android.R.id.button2: {
                if (flowTypeDeferredSetup() || flowTypeSettingsSuggestion() || flowTypeSetup()) {
                    setResult(-1);
                    mAssistGestureHelper.setListener(null);
                    finishAndRemoveTask();
                    break;
                }
                if (flowTypeAccidentalTrigger()) {
                    handleDoneAndLaunch();
                    break;
                }
                break;
            }
            case android.R.id.button1: {
                launchAssistGestureSettings();
                break;
            }
        }
    }

    protected int getContentView() {
        return R.layout.assist_gesture_training_finished_activity;
    }


    @Override
    protected void onCreate(final Bundle bundle) {
        setTheme(SetupWizardUtils.getTheme(getIntent()));
        setContentView(getContentView());
        mLayout = findViewById(R.id.layout);
        super.onCreate(bundle);
        setShouldCheckForNoProgress(false);
        Button button1 = findViewById(android.R.id.button2);
        button1.setOnClickListener(this);
        if (flowTypeDeferredSetup()) {
            button1.setText(R.string.next_label);
        } else if (flowTypeSettingsSuggestion()) {
            button1.setText(R.string.done);
        } else if (flowTypeAccidentalTrigger()) {
            button1.setText(R.string.assist_gesture_enrollment_continue_to_assistant);
        }
        Button button2 = findViewById(android.R.id.button2);
        button2.setOnClickListener(this);
        if (flowTypeDeferredSetup() ||flowTypeSetup()) {
            button2.setVisibility(View.INVISIBLE);
        }
        mAssistGestureCheck = findViewById(R.id.assist_gesture_training_check);
        mAssistGestureIllustration = findViewById(R.id.assist_gesture_training_illustration);
        fadeOutCheckAfterDelay();
    }

    @Override
    public void onProgressChanged(final SeekBar seekBar, final int n, final boolean b) {
        super.onProgressChanged(seekBar, n, b);
    }

    @Override
    public void onStartTrackingTouch(final SeekBar seekBar) {
        super.onStartTrackingTouch(seekBar);
        mHandler.removeMessages(4);
        mHandler.removeMessages(5);
        mHandler.obtainMessage(6, (Object)mAssistGestureCheck).sendToTarget();
        mHandler.obtainMessage(7, (Object)mAssistGestureIllustration).sendToTarget();
    }

    @Override
    protected void showMessage(final int n, final String s) {
        if (mAssistGestureCheck.getVisibility() == View.INVISIBLE) {
            super.showMessage(n, s);
        }
    }
}

