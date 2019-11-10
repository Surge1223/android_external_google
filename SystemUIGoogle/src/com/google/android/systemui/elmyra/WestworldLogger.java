package com.google.android.systemui.elmyra;

import android.app.StatsManager;
import android.content.Context;
import android.os.IStatsPullerCallback;
import android.os.StatsLogEventWrapper;
import android.util.Log;

import com.google.android.systemui.elmyra.proto.nano.ChassisProtos$Chassis;
import com.google.android.systemui.elmyra.proto.nano.SnapshotProtos$Snapshot;
import com.google.android.systemui.elmyra.sensors.GestureSensor;
import com.google.android.systemui.elmyra.sensors.config.GestureConfiguration;

import java.util.concurrent.CountDownLatch;

import static android.util.StatsLogInternal.write;

public class WestworldLogger implements GestureSensor.Listener {
    private final IStatsPullerCallback mWestworldCallback = new IStatsPullerCallback.Stub() {
        public StatsLogEventWrapper[] pullData(int i, long j, long j2) {
            Log.d("Elmyra/Logger", "Receiving pull request from statsd.");
            return WestworldLogger.pull(i, j, j2);
        }
    };
    private ChassisProtos$Chassis mChassis = null;
    private CountDownLatch mCountDownLatch;
    private GestureConfiguration mGestureConfiguration;
    private Object mMutex;
    private SnapshotProtos$Snapshot mSnapshot;
    private SnapshotController mSnapshotController;

    public WestworldLogger(Context context, GestureConfiguration gestureConfiguration, SnapshotController snapshotController) {
        mGestureConfiguration = gestureConfiguration;
        mSnapshotController = snapshotController;
        mSnapshot = null;
        mMutex = new Object();
        registerWithWestworld(context);
    }

    public static android.os.StatsLogEventWrapper[] pull(int r13, long r14, long r16) {
        throw new UnsupportedOperationException("Method not decompiled: WestworldLogger.pull(int, long, long):android.os.StatsLogEventWrapper[]");
    }

    public void registerWithWestworld(Context context) {
        StatsManager statsManager = (StatsManager) context.getSystemService("stats");
        if (statsManager == null) {
            Log.d("Elmyra/Logger", "Failed to get StatsManager");
        }
        try {
            statsManager.setPullerCallback(150000, mWestworldCallback);
        } catch (StatsManager.StatsUnavailableException e) {
            Log.d("Elmyra/Logger", "Failed to register callback with StatsManager");
            e.printStackTrace();
        }
    }

    public void didReceiveChassis(ChassisProtos$Chassis chassisProtos$Chassis) {
        mChassis = chassisProtos$Chassis;
    }

    public void onGestureProgress(GestureSensor gestureSensor, float f, int i) {
        write(176, (int) (f * 100.0f));
        write(174, i);
    }

    public void onGestureDetected(GestureSensor gestureSensor, GestureSensor.DetectionProperties detectionProperties) {
        write(174, 3);
    }

    public void querySubmitted() {
        write(175, 2);
    }

    public void didReceiveSnapshot(SnapshotProtos$Snapshot snapshotProtos$Snapshot) {
        synchronized (mMutex) {
            mSnapshot = snapshotProtos$Snapshot;
            if (mCountDownLatch != null) {
                mCountDownLatch.countDown();
            }
        }
    }
}
