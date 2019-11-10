package com.google.android.systemui.elmyra;

import android.content.Context;
import com.android.systemui.R;

public class SnapshotConfiguration {
    private final int mCompleteGestures;
    private final int mIncompleteGestures;
    private final int mSnapshotDelayAfterGesture;

    SnapshotConfiguration(Context context) {
        mCompleteGestures = context.getResources().getInteger(R.integer.elmyra_snapshot_logger_gesture_size);
        mIncompleteGestures = context.getResources().getInteger(R.integer.elmyra_snapshot_logger_incomplete_gesture_size);
        mSnapshotDelayAfterGesture = context.getResources().getInteger(R.integer.elmyra_snapshot_gesture_delay_ms);
    }

    public int getCompleteGestures() {
        return mCompleteGestures;
    }

    public int getIncompleteGestures() {
        return mIncompleteGestures;
    }

    int getSnapshotDelayAfterGesture() {
        return mSnapshotDelayAfterGesture;
    }
}
