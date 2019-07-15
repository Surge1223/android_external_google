package com.google.android.settings.gestures.assist;

import android.graphics.ColorFilter;
import android.graphics.Path;
import android.graphics.Canvas;
import android.animation.TimeAnimator.TimeListener;
import android.os.Message;
import android.os.Looper;
import android.animation.TimeAnimator;
import android.graphics.Paint;
import android.os.Handler;
import android.content.Context;
import android.graphics.drawable.Drawable;

class IndicatorDrawable extends Drawable
{
    private Context mContext;
    private final Handler mHandler;
    private Paint mPaint;
    private float mProgress;
    private boolean mReversed;
    private long mTime;
    private TimeAnimator mTimeAnimator;
    
    public IndicatorDrawable(final Context mContext, final boolean mReversed) {
        this.mPaint = new Paint();
        this.mHandler = new Handler(Looper.getMainLooper()) {
            public void handleMessage(final Message message) {
                switch (message.what) {
                    case 2: {
                        IndicatorDrawable.this.mTimeAnimator.end();
                        break;
                    }
                    case 1: {
                        IndicatorDrawable.this.mTimeAnimator.start();
                        break;
                    }
                }
            }
        };
        this.mContext = mContext;
        this.mReversed = mReversed;
        (this.mTimeAnimator = new TimeAnimator()).setTimeListener((TimeAnimator.TimeListener)new TimeAnimator.TimeListener() {
            public void onTimeUpdate(final TimeAnimator timeAnimator, final long n, final long n2) {
                IndicatorDrawable.this.mTime = n;
                if (n >= 150L) {
                    timeAnimator.end();
                }
                IndicatorDrawable.this.invalidateSelf();
            }
        });
    }
    
    public void draw(final Canvas canvas) {
        this.mPaint.setAntiAlias(true);
        this.mPaint.setColor(this.mContext.getResources().getColor(2131099674));
        this.mPaint.setAlpha(63);
        final int n = canvas.getHeight() / 2;
        final int height = canvas.getHeight();
        final Path path = new Path();
        if (this.mReversed) {
            path.moveTo((float)canvas.getWidth(), (float)n);
        }
        else {
            path.moveTo(0.0f, (float)n);
        }
        final float n2 = canvas.getWidth();
        final float mProgress = this.mProgress;
        float n4;
        if (this.mTimeAnimator.isRunning()) {
            final float n3 = 1.0f - this.mTime / 150.0f;
            this.mPaint.setAlpha((int)(63.0f * n3));
            n4 = canvas.getWidth() * n3;
        }
        else {
            n4 = n2 * mProgress;
        }
        if (this.mReversed) {
            path.cubicTo(canvas.getWidth() - n4, n + 150.0f, canvas.getWidth() - n4, height - 150.0f, (float)canvas.getWidth(), (float)height);
        }
        else {
            path.cubicTo(n4, n + 150.0f, n4, height - 150.0f, 0.0f, (float)height);
        }
        canvas.drawPath(path, this.mPaint);
    }
    
    public int getOpacity() {
        return 0;
    }
    
    public void onGestureDetected() {
        this.mProgress = 0.0f;
        this.mHandler.sendEmptyMessage(1);
    }
    
    public void onGestureProgress(final float mProgress) {
        this.mHandler.sendEmptyMessage(2);
        this.mProgress = mProgress;
        this.invalidateSelf();
    }
    
    public void setAlpha(final int n) {
    }
    
    public void setColorFilter(final ColorFilter colorFilter) {
    }
}
