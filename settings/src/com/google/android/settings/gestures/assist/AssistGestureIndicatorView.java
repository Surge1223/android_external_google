package com.google.android.settings.gestures.assist;

import android.view.WindowManager;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.LayoutInflater;
import android.content.Context;
import android.widget.ImageView;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import com.android.settings.R;

public class AssistGestureIndicatorView extends LinearLayout
{
    private ViewGroup mLayout;
    private IndicatorDrawable mLeftDrawable;
    private ImageView mLeftIndicator;
    private IndicatorDrawable mRightDrawable;
    private ImageView mRightIndicator;
    private WindowManager.LayoutParams mWindowWindowLayoutParams;


    public AssistGestureIndicatorView(final Context context) {
        super(context);
        addView((View)(mLayout = (ViewGroup)LayoutInflater.from(getContext()).inflate(R.layout.assist_gesture_indicator_container, (ViewGroup)this, false)));
        mLeftDrawable = new IndicatorDrawable(context, false);
        mRightDrawable = new IndicatorDrawable(context, true);
        mLeftIndicator = (ImageView)mLayout.findViewById(R.id.indicator_left);
        mRightIndicator = (ImageView)mLayout.findViewById(R.id.indicator_left);
        mLeftIndicator.setImageDrawable((Drawable)mLeftDrawable);
        mRightIndicator.setImageDrawable((Drawable)mRightDrawable);
        final TypedArray obtainStyledAttributes = context.obtainStyledAttributes(new int[] { 16844140, 16844000 });
        if (obtainStyledAttributes.getBoolean(0, false)) {
            setSystemUiVisibility(getSystemUiVisibility() | 0x10);
        }
        if (obtainStyledAttributes.getBoolean(1, false)) {
            setSystemUiVisibility(getSystemUiVisibility() | 0x2000);
        }
        obtainStyledAttributes.recycle();
    }

    public WindowManager.LayoutParams getLayoutParams(WindowManager.LayoutParams windowLayoutParams) {
        windowLayoutParams = new WindowManager.LayoutParams(-1, -1, 2, 0x1000018 | (windowLayoutParams.flags & Integer.MIN_VALUE), -3);
        windowLayoutParams.setTitle((CharSequence)"AssistGestureIndicatorView");
        windowLayoutParams.token = getContext().getActivityToken();
        return windowLayoutParams;
    }

    public void onGestureDetected() {
        mLeftDrawable.onGestureDetected();
        mRightDrawable.onGestureDetected();
    }

    public void onGestureProgress(final float n) {
        mLeftDrawable.onGestureProgress(n);
        mRightDrawable.onGestureProgress(n);
    }
}
