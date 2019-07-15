package com.google.android.settings.gestures.assist;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.TimeInterpolator;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.widget.SeekBar;
import android.widget.TextView;

import com.android.settings.R;

import java.lang.ref.WeakReference;

public abstract class AssistGestureTrainingSliderBase extends AssistGestureTrainingBase
        implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {
    private int mCurrentProgress;
    private Interpolator mFastOutLinearInInterpolator;
    private HandleProgress mHandleProgress;
    protected Handler mHandler;
    private int mLastProgress;
    private Interpolator mLinearOutSlowInInterpolator;
    private SeekBar mSeekBar;
    private boolean mSeekBarTrackingTouch;
    protected static TextView mErrorView;
    private static Context mContext;


    public AssistGestureTrainingSliderBase() {
        new AssistGestureTrainingSliderBaseHandler(this);
    }

    private static String getErrorString(final int n) {
        switch (n) {
            default: {
                return null;
            }
            case 4: {
                return mContext .getResources().getString(R.string.assist_gesture_training_enrolling_error_may_cause_falsing);
            }
            case 3: {
                return mContext .getResources().getString(R.string.assist_gesture_training_enrolling_error_may_cause_falsing);
            }
            case 2: {
                return mContext .getResources().getString(R.string.assist_gesture_training_enrolling_error_squeeze_release_quickly);
            }
            case 1: {
                return mContext .getResources().getString(R.string.assist_gesture_training_enrolling_error_squeeze_bottom);
            }
        }
    }
    
    private void clearMessage() {
        if (mErrorView.getVisibility() == 0) {
            mErrorView
                    .animate().alpha(0.0f)
                    .translationY((float)getResources().getDimensionPixelSize(R.dimen.assist_gesture_error_text_disappear_distance))
                    .setDuration(100L).setInterpolator(mFastOutLinearInInterpolator)
                    .withEndAction(new AssistGestureRunnable())
                    .start();
        }
    }

    private void fadeInView(View view) {
        view.setAlpha(0.0f);
        view.setVisibility(View.VISIBLE);
        view.animate().alpha(1.0f).setDuration(350L).setListener(null);
    }

    private void fadeOutView(View view) {
        view.animate()
                .alpha(0.0f)
                .setDuration(350L)
                .setListener(
                        new AnimatorListenerAdapter() {
                            public void onAnimationEnd(Animator animator) {
                                view.setVisibility(View.INVISIBLE);
                            }
                        });
    }

    private void updateSeekBar() {
        int mLastProgress = (int)((1.0f - Settings.Secure.getFloat(getContentResolver(), "assist_gesture_sensitivity", 0.5f)) * mSeekBar.getMax());
        mLastProgress = mLastProgress;
        if (mSeekBar != null && mLastProgress != mSeekBar.getProgress()) {
            mSeekBar.setProgress(mLastProgress, false);
        }
    }

    private void updateSensitivity(final SeekBar seekBar) {
        Settings.Secure.putFloat(getContentResolver(), "assist_gesture_sensitivity", 1.0f - mCurrentProgress / seekBar.getMax());
        if (mCurrentProgress <= mLastProgress && mCurrentProgress / seekBar.getMax() < 0.35f) {
            mHandler.obtainMessage(2, 3, 0).sendToTarget();
        }
        else {
            mHandler.removeMessages(2);
            mHandler.obtainMessage(3).sendToTarget();
        }
        mLastProgress = mCurrentProgress;
    }

    @Override
    public abstract int getMetricsCategory();

    protected abstract void handleGestureDetected();



    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {}


    public abstract void onClick(View p0);


    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        mErrorView = findViewById(R.id.error_message);
        mLinearOutSlowInInterpolator =
                AnimationUtils.loadInterpolator(this, android.R.interpolator.linear_out_slow_in);
        mFastOutLinearInInterpolator =
                AnimationUtils.loadInterpolator(this, android.R.interpolator.fast_out_linear_in);
        (mSeekBar = findViewById(R.id.assist_gesture_sensitivity_seekbar))
                .setOnSeekBarChangeListener(this);
        Handler mHandler = new AssistGestureTrainingSliderBaseHandler(this);
        mHandleProgress = new HandleProgress(mHandler);
    }


    public void onGestureDetected() {
        mHandler.removeMessages(2);
        mHandler.obtainMessage(1).sendToTarget();
        mHandleProgress.onGestureDetected();
    }

    @Override
    public void onGestureProgress(final float n, final int n2) {
        super.onGestureProgress(n, n2);
        mHandleProgress.onGestureProgress(n, n2);
    }

    public void onProgressChanged(final SeekBar seekBar, int mCurrentProgress, final boolean b) {
        if (b) {
            mCurrentProgress = mCurrentProgress;
            if (!mSeekBarTrackingTouch) {
                updateSensitivity(seekBar);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        updateSeekBar();
    }

    public void onStartTrackingTouch(final SeekBar seekBar) {
        mSeekBarTrackingTouch = true;
    }

    public void onStopTrackingTouch(final SeekBar seekBar) {
        updateSensitivity(seekBar);
        mSeekBarTrackingTouch = false;
    }

    protected void setShouldCheckForNoProgress(final boolean shouldCheckForNoProgress) {
        mHandleProgress.setShouldCheckForNoProgress(shouldCheckForNoProgress);
    }

    protected void showMessage(final int n, final String text) {
        mErrorView.setText((CharSequence)text);
        if (mErrorView.getVisibility() == View.INVISIBLE) {
            mErrorView.setVisibility(View.VISIBLE);
            mErrorView.setTranslationY((float)getResources().getDimensionPixelSize(R.dimen.assist_gesture_error_text_appear_distance));
            mErrorView.setAlpha(0.0f);
            mErrorView.animate().alpha(1.0f).translationY(0.0f).setDuration(200L).setInterpolator((TimeInterpolator)mLinearOutSlowInInterpolator).start();
        }
        else {
            mErrorView.animate().cancel();
            mErrorView.setAlpha(1.0f);
            mErrorView.setTranslationY(0.0f);
        }
        mHandler.removeMessages(3);
        if (n != View.INVISIBLE) {
            mHandler.sendMessageDelayed(mHandler.obtainMessage(3), 5000L);
        }
    }

    class AssistGestureTrainingSliderBaseHandler extends Handler {
        private final WeakReference<AssistGestureTrainingSliderBase> weakActivity;

        AssistGestureTrainingSliderBaseHandler(AssistGestureTrainingSliderBase activity) {
            weakActivity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message message) {
            AssistGestureTrainingSliderBase activity = weakActivity.get();
            switch (message.what) {
                case 1:
                {
                    activity.clearMessage();
                    activity.handleGestureDetected();
                    break;
                }
                case 2:
                {
                    activity.showMessage(
                            message.arg1, AssistGestureTrainingSliderBase.getErrorString(message.arg1));
                    break;
                }
                case 3:
                {
                    activity.clearMessage();
                    break;
                }
                case 4:
                {
                    activity.fadeInView((View) message.obj);
                    break;
                }
                case 5:
                {
                    activity.fadeOutView((View) message.obj);
                    break;
                }
                case 6:
                {
                    ((View) message.obj).setVisibility(View.INVISIBLE);
                    break;
                }
                case 7:
                {
                    View view = (View) message.obj;
                    view.setAlpha(1.0f);
                    view.setVisibility(View.VISIBLE);
                    break;
                }
            }
        }
    }


    class AssistGestureRunnable implements Runnable {
        public void run() {
            AssistGestureTrainingSliderBase.mErrorView.setVisibility(View.INVISIBLE);
        }
    }
}

