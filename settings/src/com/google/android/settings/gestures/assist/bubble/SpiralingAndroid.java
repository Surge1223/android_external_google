package com.google.android.settings.gestures.assist.bubble;

import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.graphics.PorterDuff.Mode;
import android.content.res.Resources.Theme;
import android.graphics.Rect;
import android.content.Context;
import java.util.Random;
import android.graphics.drawable.Drawable;

public class SpiralingAndroid
{
    private Drawable mAndroid;
    private float mCurrentRotation;
    private Random mRandom;
    private final float mRotationSpeed;
    private final float mVelocityY;
    
    public SpiralingAndroid(final Context context, final Rect rect) {
        this.mRandom = new Random();
        this.mVelocityY = 400.0f * (this.mRandom.nextFloat() * 0.8f + 0.8f);
        this.mRotationSpeed = 360.0f * (0.8f + this.mRandom.nextFloat() * 0.8f);
        final int n = rect.width() / 10 + this.mRandom.nextInt(rect.width() / 5);
        (this.mAndroid = context.getResources().getDrawable(2131230951, (Resources.Theme)null).getConstantState().newDrawable()).mutate();
        this.mAndroid.setColorFilter(this.mRandom.nextInt(), PorterDuff.Mode.SRC_ATOP);
        final int nextInt = this.mRandom.nextInt(rect.width() - n);
        final int n2 = -this.mRandom.nextInt(rect.height() / 2) - n;
        this.mAndroid.setBounds(new Rect(nextInt, n2, nextInt + n, n2 + n));
    }
    
    public Drawable getAndroid() {
        return this.mAndroid;
    }
    
    public float getCurrentRotation() {
        return this.mCurrentRotation;
    }
    
    public void update(final long n, final long n2) {
        final float n3 = n;
        final float n4 = 0.001f * n2;
        final float mVelocityY = this.mVelocityY;
        final Rect copyBounds = this.mAndroid.copyBounds();
        copyBounds.offset((int)0.0f, (int)(mVelocityY * n4));
        this.mAndroid.setBounds(copyBounds);
        this.mCurrentRotation += this.mRotationSpeed * n4;
    }
}
