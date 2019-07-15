package com.google.android.settings.gestures.assist.bubble;

import android.graphics.Color;
import java.util.concurrent.ThreadLocalRandom;
import android.graphics.Rect;
import java.util.Random;
import android.graphics.PointF;

public class Bubble
{
    private int mAlpha;
    private float mAmplitude;
    private int mBubbleState;
    private int mColor;
    private float mFrequency;
    private PointF mOriginalPoint;
    private int mOriginalSize;
    private PointF mPoint;
    private Random mRandom;
    private int mSize;
    private float mVelocityY;
    
    public Bubble(final Rect rect) {
        this.mOriginalPoint = new PointF();
        this.mPoint = new PointF();
        this.mRandom = new Random();
        this.mBubbleState = 0;
        this.mOriginalSize = ThreadLocalRandom.current().nextInt(40, 140);
        this.mSize = this.mOriginalSize;
        this.mOriginalPoint.x = rect.width() * (this.mRandom.nextFloat() * 0.6f + 0.2f);
        this.mOriginalPoint.y = rect.height() + this.mOriginalSize;
        this.mPoint = this.mOriginalPoint;
        this.mFrequency = 2.0f * this.mRandom.nextFloat();
        this.mAmplitude = 10.0f * this.mRandom.nextFloat();
        this.mVelocityY = 600.0f * (0.8f + this.mRandom.nextFloat() * 0.4f);
        this.mAlpha = (int)((0.6f + this.mRandom.nextFloat() * 0.2f) * 255.0f);
        this.mColor = Color.argb(this.mAlpha, this.mRandom.nextInt(255), this.mRandom.nextInt(255), this.mRandom.nextInt(255));
    }
    
    private void updateDying(final long n, final long n2) {
        this.mSize -= (int)(n2 * 0.1f);
        if (this.mSize < 0) {
            this.mSize = 0;
            this.mBubbleState = 2;
        }
    }
    
    public int getColor() {
        return this.mColor;
    }
    
    public int getOriginalSize() {
        return this.mOriginalSize;
    }
    
    public PointF getPoint() {
        return this.mPoint;
    }
    
    public int getSize() {
        return this.mSize;
    }
    
    public int getState() {
        return this.mBubbleState;
    }
    
    public boolean isBubbleDead() {
        return this.mBubbleState == 2;
    }
    
    public boolean isBubbleTouchingTop() {
        return this.mPoint.y - this.mSize <= 0.0f;
    }
    
    public void setSize(final int mSize) {
        this.mSize = mSize;
    }
    
    public void setState(final int mBubbleState) {
        this.mBubbleState = mBubbleState;
    }
    
    public void update(final long n, final long n2) {
        final float n3 = n;
        final float n4 = n2;
        final PointF mPoint = this.mPoint;
        mPoint.y -= this.mVelocityY * (0.001f * n4);
        this.mPoint.x = this.mOriginalPoint.x + this.mAmplitude * (float)Math.sin(6.283185307179586 * this.mFrequency * (n3 * 0.001f));
        if (this.mBubbleState == 1) {
            this.updateDying(n, n2);
        }
    }
}
