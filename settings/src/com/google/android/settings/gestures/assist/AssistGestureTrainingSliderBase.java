package com.google.android.settings.gestures.assist;

import android.content.Context;
import android.view.animation.AnimationUtils;
import android.os.Bundle;
import android.provider.Settings;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.Animator.AnimatorListener;
import android.animation.TimeInterpolator;
import android.view.View;
import android.os.Message;
import android.widget.SeekBar;
import android.os.Handler;
import android.view.animation.Interpolator;
import android.widget.TextView;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.view.View.OnClickListener;

public abstract class AssistGestureTrainingSliderBase extends AssistGestureTrainingBase implements View.OnClickListener, SeekBar.OnSeekBarChangeListener
{
    private int mCurrentProgress;
    protected TextView mErrorView;
    private Interpolator mFastOutLinearInInterpolator;
    private HandleProgress mHandleProgress;
    protected Handler mHandler;
    private int mLastProgress;
    private Interpolator mLinearOutSlowInInterpolator;
    private SeekBar mSeekBar;
    private boolean mSeekBarTrackingTouch;
    
    public AssistGestureTrainingSliderBase() {
        this.mHandler = new Handler() {
            public void handleMessage(final Message message) {
                switch (message.what) {
                    case 7: {
                        final View view = (View)message.obj;
                        view.setAlpha(1.0f);
                        view.setVisibility(0);
                        break;
                    }
                    case 6: {
                        ((View)message.obj).setVisibility(4);
                        break;
                    }
                    case 5: {
                        AssistGestureTrainingSliderBase.this.fadeOutView((View)message.obj);
                        break;
                    }
                    case 4: {
                        AssistGestureTrainingSliderBase.this.fadeInView((View)message.obj);
                        break;
                    }
                    case 3: {
                        AssistGestureTrainingSliderBase.this.clearMessage();
                        break;
                    }
                    case 2: {
                        AssistGestureTrainingSliderBase.this.showMessage(message.arg1, AssistGestureTrainingSliderBase.this.getErrorString(message.arg1));
                        break;
                    }
                    case 1: {
                        AssistGestureTrainingSliderBase.this.clearMessage();
                        AssistGestureTrainingSliderBase.this.handleGestureDetected();
                        break;
                    }
                }
            }
        };
    }
    
    private void clearMessage() {
        if (this.mErrorView.getVisibility() == 0) {
            this.mErrorView.animate().alpha(0.0f).translationY((float)this.getResources().getDimensionPixelSize(2131165304)).setDuration(100L).setInterpolator((TimeInterpolator)this.mFastOutLinearInInterpolator).withEndAction((Runnable)new Runnable() {
                @Override
                public void run() {
                    AssistGestureTrainingSliderBase.this.mErrorView.setVisibility(4);
                }
            }).start();
        }
    }
    
    private void fadeInView(final View view) {
        view.setAlpha(0.0f);
        view.setVisibility(0);
        view.animate().alpha(1.0f).setDuration(350L).setListener((Animator.AnimatorListener)null);
    }
    
    private void fadeOutView(final View view) {
        view.animate().alpha(0.0f).setDuration(350L).setListener((Animator.AnimatorListener)new AnimatorListenerAdapter() {
            public void onAnimationEnd(final Animator animator) {
                view.setVisibility(4);
            }
        });
    }
    
    private String getErrorString(final int n) {
        switch (n) {
            default: {
                return null;
            }
            case 4: {
                return this.getResources().getString(2131886447);
            }
            case 3: {
                return this.getResources().getString(2131886442);
            }
            case 2: {
                return this.getResources().getString(2131886446);
            }
            case 1: {
                return this.getResources().getString(2131886443);
            }
        }
    }
    
    private void updateSeekBar() {
        final int mLastProgress = (int)((1.0f - Settings.Secure.getFloat(this.getContentResolver(), "assist_gesture_sensitivity", 0.5f)) * this.mSeekBar.getMax());
        this.mLastProgress = mLastProgress;
        if (this.mSeekBar != null && mLastProgress != this.mSeekBar.getProgress()) {
            this.mSeekBar.setProgress(mLastProgress, false);
        }
    }
    
    private void updateSensitivity(final SeekBar seekBar) {
        Settings.Secure.putFloat(this.getContentResolver(), "assist_gesture_sensitivity", 1.0f - this.mCurrentProgress / seekBar.getMax());
        if (this.mCurrentProgress <= this.mLastProgress && this.mCurrentProgress / seekBar.getMax() < 0.35f) {
            this.mHandler.obtainMessage(2, 3, 0).sendToTarget();
        }
        else {
            this.mHandler.removeMessages(2);
            this.mHandler.obtainMessage(3).sendToTarget();
        }
        this.mLastProgress = this.mCurrentProgress;
    }
    
    protected abstract void handleGestureDetected();
    
    public abstract void onClick(final View p0);
    
    @Override
    protected void onCreate(final Bundle bundle) {
        super.onCreate(bundle);
        this.mErrorView = (TextView)this.findViewById(2131362127);
        this.mLinearOutSlowInInterpolator = AnimationUtils.loadInterpolator((Context)this, 17563662);
        this.mFastOutLinearInInterpolator = AnimationUtils.loadInterpolator((Context)this, 17563663);
        (this.mSeekBar = (SeekBar)this.findViewById(2131361889)).setOnSeekBarChangeListener((SeekBar.OnSeekBarChangeListener)this);
        this.mHandleProgress = new HandleProgress(this.mHandler);
    }
    
    public void onGestureDetected() {
        this.mHandler.removeMessages(2);
        this.mHandler.obtainMessage(1).sendToTarget();
        this.mHandleProgress.onGestureDetected();
    }
    
    @Override
    public void onGestureProgress(final float n, final int n2) {
        super.onGestureProgress(n, n2);
        this.mHandleProgress.onGestureProgress(n, n2);
    }
    
    public void onProgressChanged(final SeekBar seekBar, final int mCurrentProgress, final boolean b) {
        if (b) {
            this.mCurrentProgress = mCurrentProgress;
            if (!this.mSeekBarTrackingTouch) {
                this.updateSensitivity(seekBar);
            }
        }
    }
    
    @Override
    public void onResume() {
        super.onResume();
        this.updateSeekBar();
    }
    
    public void onStartTrackingTouch(final SeekBar seekBar) {
        this.mSeekBarTrackingTouch = true;
    }
    
    public void onStopTrackingTouch(final SeekBar seekBar) {
        this.updateSensitivity(seekBar);
        this.mSeekBarTrackingTouch = false;
    }
    
    protected void setShouldCheckForNoProgress(final boolean shouldCheckForNoProgress) {
        this.mHandleProgress.setShouldCheckForNoProgress(shouldCheckForNoProgress);
    }
    
    protected void showMessage(final int n, final String text) {
        this.mErrorView.setText((CharSequence)text);
        if (this.mErrorView.getVisibility() == 4) {
            this.mErrorView.setVisibility(0);
            this.mErrorView.setTranslationY((float)this.getResources().getDimensionPixelSize(2131165303));
            this.mErrorView.setAlpha(0.0f);
            this.mErrorView.animate().alpha(1.0f).translationY(0.0f).setDuration(200L).setInterpolator((TimeInterpolator)this.mLinearOutSlowInInterpolator).start();
        }
        else {
            this.mErrorView.animate().cancel();
            this.mErrorView.setAlpha(1.0f);
            this.mErrorView.setTranslationY(0.0f);
        }
        this.mHandler.removeMessages(3);
        if (n != 4) {
            this.mHandler.sendMessageDelayed(this.mHandler.obtainMessage(3), 5000L);
        }
    }
}
