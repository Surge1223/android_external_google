package com.google.android.settings.widget;

import android.content.res.TypedArray;
import com.android.settings.R;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.util.AttributeSet;
import android.content.Context;
import android.widget.TextView;
import android.widget.ProgressBar;
import android.view.View;
import android.widget.FrameLayout;
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

public class AssistGestureTrainingProgressBar extends FrameLayout
{
    private View mDoneView;
    private ProgressBar mProgressBar;
    private int mState;
    private TextView mTextView;

    public AssistGestureTrainingProgressBar(final Context context) {
        this(context, null);
    }

    public AssistGestureTrainingProgressBar(final Context context, final AttributeSet set) {
        this(context, set, 0, 0x7f13000a);
    }

    public AssistGestureTrainingProgressBar(final Context context, final AttributeSet set, final int n, final int n2) {
        super(context, set);
        this.mState = 0;
        LayoutInflater.from(context).inflate(R.layout.assist_gesture_training_progress_bar, (ViewGroup)this, true);
        this.mTextView = (TextView)this.findViewById(R.id.label);
        this.mProgressBar = (ProgressBar)this.findViewById(R.id.progress);
        this.mDoneView = this.findViewById(R.id.done);
        this.refreshViews();
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(set, com.android.internal.R.styleable.ProgressBar, n, n2);
        this.mTextView.setText(obtainStyledAttributes.getText(0));
        obtainStyledAttributes.recycle();
    }

    private void refreshViews() {
        final TextView mTextView = this.mTextView;
        final int mState = this.mState;
        final int n = 8;
        int visibility;
        if (mState != 2) {
            visibility = 0;
        }
        else {
            visibility = 8;
        }
        mTextView.setVisibility(visibility);
        final ProgressBar mProgressBar = this.mProgressBar;
        int visibility2;
        if (this.mState == 1) {
            visibility2 = 0;
        }
        else {
            visibility2 = 8;
        }
        mProgressBar.setVisibility(visibility2);
        final View mDoneView = this.mDoneView;
        int visibility3 = n;
        if (this.mState == 2) {
            visibility3 = 0;
        }
        mDoneView.setVisibility(visibility3);
    }

    public void setState(final int mState) {
        this.mState = mState;
        this.refreshViews();
    }

    public void setText(final CharSequence text) {
        this.mTextView.setText(text);
    }
}

