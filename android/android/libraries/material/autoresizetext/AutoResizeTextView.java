package com.google.android.libraries.material.autoresizetext;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.RectF;
import android.os.Build;
import android.text.Layout;
import android.text.Layout.Alignment;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.SparseIntArray;
import android.util.TypedValue;
import android.widget.TextView;
import javax.annotation.Nullable;

public class AutoResizeTextView extends TextView
{
    private final RectF availableSpaceRect;
    private final DisplayMetrics displayMetrics;
    private float lineSpacingExtra;
    private float lineSpacingMultiplier;
    private int maxLines;
    private float maxTextSize;
    private int maxWidth;
    private float minTextSize;
    private int resizeStepUnit;
    private final TextPaint textPaint;
    private final SparseIntArray textSizesCache;

    public AutoResizeTextView(final Context context) {
        super(context, (AttributeSet)null, 0);
        this.displayMetrics = this.getResources().getDisplayMetrics();
        this.availableSpaceRect = new RectF();
        this.textSizesCache = new SparseIntArray();
        this.textPaint = new TextPaint();
        this.resizeStepUnit = 0;
        this.minTextSize = 16.0f;
        this.lineSpacingMultiplier = 1.0f;
        this.lineSpacingExtra = 0.0f;
        this.initialize(context, null, 0, 0);
    }

    public AutoResizeTextView(final Context context, final AttributeSet set) {
        super(context, set, 0);
        this.displayMetrics = this.getResources().getDisplayMetrics();
        this.availableSpaceRect = new RectF();
        this.textSizesCache = new SparseIntArray();
        this.textPaint = new TextPaint();
        this.resizeStepUnit = 0;
        this.minTextSize = 16.0f;
        this.lineSpacingMultiplier = 1.0f;
        this.lineSpacingExtra = 0.0f;
        this.initialize(context, set, 0, 0);
    }

    public AutoResizeTextView(final Context context, final AttributeSet set, final int n) {
        super(context, set, n);
        this.displayMetrics = this.getResources().getDisplayMetrics();
        this.availableSpaceRect = new RectF();
        this.textSizesCache = new SparseIntArray();
        this.textPaint = new TextPaint();
        this.resizeStepUnit = 0;
        this.minTextSize = 16.0f;
        this.lineSpacingMultiplier = 1.0f;
        this.lineSpacingExtra = 0.0f;
        this.initialize(context, set, n, 0);
    }

    public AutoResizeTextView(final Context context, final AttributeSet set, final int n, final int n2) {
        super(context, set, n, n2);
        this.displayMetrics = this.getResources().getDisplayMetrics();
        this.availableSpaceRect = new RectF();
        this.textSizesCache = new SparseIntArray();
        this.textPaint = new TextPaint();
        this.resizeStepUnit = 0;
        this.minTextSize = 16.0f;
        this.lineSpacingMultiplier = 1.0f;
        this.lineSpacingExtra = 0.0f;
        this.initialize(context, set, n, n2);
    }

    private void adjustTextSize() {
        final int maxWidth = this.getMeasuredWidth() - this.getPaddingLeft() - this.getPaddingRight();
        final int n = this.getMeasuredHeight() - this.getPaddingBottom() - this.getPaddingTop();
        if (maxWidth > 0 && n > 0) {
            this.maxWidth = maxWidth;
            this.availableSpaceRect.right = maxWidth;
            this.availableSpaceRect.bottom = n;
            super.setTextSize(this.resizeStepUnit, this.computeTextSize((int)Math.ceil(this.convertToResizeStepUnits(this.minTextSize)), (int)Math.floor(this.convertToResizeStepUnits(this.maxTextSize)), this.availableSpaceRect));
        }
    }

    private int binarySearchSizes(int i, int n, final RectF rectF) {
        final int n2 = i + 1;
        int n3 = n;
        n = i;
        int n4;
        for (i = n2; i <= n3; i = n4) {
            n = (i + n3) / 2;
            if (this.suggestedSizeFitsInSpace(TypedValue.applyDimension(this.resizeStepUnit, (float)n, this.displayMetrics), rectF)) {
                n4 = n + 1;
                n = i;
            }
            else {
                n3 = --n;
                n4 = i;
            }
        }
        return n;
    }

    private float computeTextSize(int hashCode, int binarySearchSizes, final RectF rectF) {
        final CharSequence text = this.getText();
        if (text != null && this.textSizesCache.get(text.hashCode()) != 0) {
            return this.textSizesCache.get(text.hashCode());
        }
        binarySearchSizes = this.binarySearchSizes(hashCode, binarySearchSizes, rectF);
        final SparseIntArray textSizesCache = this.textSizesCache;
        if (text == null) {
            hashCode = 0;
        }
        else {
            hashCode = text.hashCode();
        }
        textSizesCache.put(hashCode, binarySearchSizes);
        return binarySearchSizes;
    }

    private float convertToResizeStepUnits(final float n) {
        return n * (1.0f / TypedValue.applyDimension(this.resizeStepUnit, 1.0f, this.displayMetrics));
    }

    private void initialize(final Context context, final AttributeSet set, final int n, final int n2) {
        this.readAttrs(context.getTheme().obtainStyledAttributes(set, R.styleable.AutoResizeTextView, n, n2));
        this.textPaint.set(this.getPaint());
    }

    private void readAttrs(final TypedArray typedArray) {
        this.resizeStepUnit = typedArray.getInt(R.styleable.AutoResizeTextView_autoResizeText_resizeStepUnit, 0);
        this.minTextSize = (int)typedArray.getDimension(R.styleable.AutoResizeTextView_autoResizeText_minTextSize, 16.0f);
        this.maxTextSize = (int)this.getTextSize();
    }

    private boolean suggestedSizeFitsInSpace(final float textSize, final RectF rectF) {
        this.textPaint.setTextSize(textSize);
        final String string = this.getText().toString();
        final int maxLines = this.getMaxLines();
        boolean b = true;
        if (maxLines == 1) {
            if (this.textPaint.getFontSpacing() > rectF.bottom || this.textPaint.measureText(string) > rectF.right) {
                b = false;
            }
            return b;
        }
        final StaticLayout staticLayout = new StaticLayout((CharSequence)string, this.textPaint, this.maxWidth, Layout.Alignment.ALIGN_NORMAL, this.getLineSpacingMultiplier(), this.getLineSpacingExtra(), true);
        return (maxLines == -1 || staticLayout.getLineCount() <= maxLines) && staticLayout.getHeight() <= rectF.bottom;
    }

    public final float getLineSpacingExtra() {
        return super.getLineSpacingExtra();
    }

    public final float getLineSpacingMultiplier() {
        return super.getLineSpacingMultiplier();
    }

    @Override
    public final int getMaxLines() {
        return super.getMaxLines();
    }

    protected final void onMeasure(final int n, final int n2) {
        this.adjustTextSize();
        super.onMeasure(n, n2);
    }

    protected final void onSizeChanged(final int n, final int n2, final int n3, final int n4) {
        super.onSizeChanged(n, n2, n3, n4);
        if (n != n3 || n2 != n4) {
            this.textSizesCache.clear();
            this.adjustTextSize();
        }
    }

    protected final void onTextChanged(final CharSequence charSequence, final int n, final int n2, final int n3) {
        super.onTextChanged(charSequence, n, n2, n3);
        this.adjustTextSize();
    }

    public final void setLineSpacing(final float lineSpacingExtra, final float lineSpacingMultiplier) {
        super.setLineSpacing(lineSpacingExtra, lineSpacingMultiplier);
        this.lineSpacingMultiplier = lineSpacingMultiplier;
        this.lineSpacingExtra = lineSpacingExtra;
    }

    public final void setMaxLines(final int n) {
        super.setMaxLines(n);
        this.maxLines = n;
    }

    public final void setMinTextSize(final int n, float applyDimension) {
        applyDimension = TypedValue.applyDimension(n, applyDimension, this.displayMetrics);
        if (this.minTextSize != applyDimension) {
            this.minTextSize = applyDimension;
            this.textSizesCache.clear();
            this.requestLayout();
        }
    }

    public final void setResizeStepUnit(final int resizeStepUnit) {
        if (this.resizeStepUnit != resizeStepUnit) {
            this.resizeStepUnit = resizeStepUnit;
            this.requestLayout();
        }
    }

    public final void setTextSize(final int n, float applyDimension) {
        applyDimension = TypedValue.applyDimension(n, applyDimension, this.displayMetrics);
        if (this.maxTextSize != applyDimension) {
            this.maxTextSize = applyDimension;
            this.textSizesCache.clear();
            this.requestLayout();
        }
    }
}

