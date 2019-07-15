package com.google.android.settings.gestures.assist.bubble;

import android.graphics.ColorFilter;
import android.graphics.Color;
import android.graphics.Path.FillType;
import android.graphics.Path;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

public class AssistGesturePlayButtonDrawable extends Drawable
{
    private Rect mBounds;
    private PointF mCircleCenter;
    private float mCircleRadius;
    private Paint mPaint;
    
    public AssistGesturePlayButtonDrawable() {
        (this.mPaint = new Paint()).setAntiAlias(true);
        this.mCircleCenter = new PointF();
    }
    
    private double distance(final PointF pointF, final PointF pointF2) {
        return Math.sqrt(Math.pow(pointF2.x - pointF.x, 2.0) + Math.pow(pointF2.y - pointF.y, 2.0));
    }
    
    private void drawTriangle(final Canvas canvas, final float n, final float n2, final float n3, final Paint paint) {
        final float n4 = (float)Math.cos(0.5235987901687622) * n3;
        final float n5 = (float)Math.sin(0.5235987901687622) * n3;
        final PointF pointF = new PointF(n, n2 - n3);
        final PointF pointF2 = new PointF(n + n4, n2 + n5);
        final PointF pointF3 = new PointF(n - n4, n2 + n5);
        canvas.save();
        canvas.rotate(90.0f, n, n2);
        final Path path = new Path();
        path.setFillType(Path.FillType.EVEN_ODD);
        path.moveTo(pointF.x, pointF.y);
        path.lineTo(pointF2.x, pointF2.y);
        path.lineTo(pointF3.x, pointF3.y);
        path.lineTo(pointF.x, pointF.y);
        path.close();
        canvas.drawPath(path, paint);
        canvas.restore();
    }
    
    public void draw(final Canvas canvas) {
        canvas.save();
        if (this.mBounds == null) {
            return;
        }
        this.mCircleCenter.x = this.mBounds.width() / 2;
        this.mCircleCenter.y = this.mBounds.height() / 2;
        this.mCircleRadius = this.mBounds.width() / 6;
        this.mPaint.setColor(Color.rgb(90, 120, 160));
        canvas.drawCircle(this.mCircleCenter.x, this.mCircleCenter.y, this.mCircleRadius, this.mPaint);
        this.mPaint.setColor(-1);
        this.drawTriangle(canvas, this.mBounds.width() / 2, this.mBounds.height() / 2, this.mBounds.width() / 12, this.mPaint);
        canvas.restore();
    }
    
    public int getOpacity() {
        return -3;
    }
    
    public boolean hitTest(final float n, final float n2) {
        return this.distance(new PointF(n, n2), this.mCircleCenter) <= this.mCircleRadius;
    }
    
    public void onBoundsChange(final Rect mBounds) {
        this.mBounds = mBounds;
        this.invalidateSelf();
    }
    
    public void setAlpha(final int alpha) {
        this.mPaint.setAlpha(alpha);
    }
    
    public void setColorFilter(final ColorFilter colorFilter) {
        this.mPaint.setColorFilter(colorFilter);
    }
}
