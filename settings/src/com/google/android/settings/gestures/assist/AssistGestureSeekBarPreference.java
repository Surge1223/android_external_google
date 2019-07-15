package com.google.android.settings.gestures.assist;

import android.support.v4.content.res.TypedArrayUtils;
import android.util.AttributeSet;
import android.content.Context;
import com.android.settings.widget.SeekBarPreference;
import com.android.settings.R;

public class AssistGestureSeekBarPreference extends SeekBarPreference
{
    public AssistGestureSeekBarPreference(final Context context, final AttributeSet set) {
        this(context, set, TypedArrayUtils.getAttr(context, R.attr.seekBarPreferenceStyle, R.attr.seekBarPreferenceStyle), 0);
    }

    public AssistGestureSeekBarPreference(final Context context, final AttributeSet set, final int resourceId, final int n) {
        super(context, set, resourceId, n);
        setLayoutResource(R.layout.preference_assist_gesture_slider);
    }
}
