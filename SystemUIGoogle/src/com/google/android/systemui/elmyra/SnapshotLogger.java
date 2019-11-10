package com.google.android.systemui.elmyra;

import android.os.Binder;

import com.android.systemui.Dumpable;
import com.google.android.systemui.elmyra.proto.nano.ChassisProtos$SensorEvent;
import com.google.android.systemui.elmyra.proto.nano.SnapshotProtos$Snapshot;

import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class SnapshotLogger implements Dumpable {
    private final int mSnapshotCapacity;
    private List<Snapshot> mSnapshots;

    public SnapshotLogger(int i) {
        mSnapshotCapacity = i;
        mSnapshots = new ArrayList(i);
    }

    public void addSnapshot(SnapshotProtos$Snapshot snapshotProtos$Snapshot, long j) {
        if (mSnapshots.size() == mSnapshotCapacity) {
            mSnapshots.remove(0);
        }
        mSnapshots.add(new Snapshot(snapshotProtos$Snapshot, j));
    }

    public void didReceiveQuery() {
        if (mSnapshots.size() > 0) {
            List<Snapshot> list = mSnapshots;
            list.get(list.size() - 1).getSnapshot().header.feedback = 1;
        }
    }

    public List<Snapshot> getSnapshots() {
        return mSnapshots;
    }

    public void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        long clearCallingIdentity = Binder.clearCallingIdentity();
        try {
            dumpInternal(printWriter);
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
    }

    private void dumpInternal(PrintWriter printWriter) {
        printWriter.println("Dumping Elmyra Snapshots");
        for (int i = 0; i < mSnapshots.size(); i++) {
            SnapshotProtos$Snapshot snapshot = mSnapshots.get(i).getSnapshot();
            printWriter.println("SystemTime: " + mSnapshots.get(i).getTimestamp());
            printWriter.println("Snapshot: " + i);
            printWriter.print("header {");
            printWriter.print("  identifier: " + snapshot.header.identifier);
            printWriter.print("  gesture_type: " + snapshot.header.gestureType);
            printWriter.print("  feedback: " + snapshot.header.feedback);
            printWriter.print("}");
            for (int i2 = 0; i2 < snapshot.events.length; i2++) {
                printWriter.print("events {");
                if (snapshot.events[i2].hasGestureStage()) {
                    printWriter.print("  gesture_stage: " + snapshot.events[i2].getGestureStage());
                } else if (snapshot.events[i2].hasSensorEvent()) {
                    ChassisProtos$SensorEvent sensorEvent = snapshot.events[i2].getSensorEvent();
                    printWriter.print("  sensor_event {");
                    printWriter.print("    timestamp: " + sensorEvent.timestamp);
                    for (int i3 = 0; i3 < sensorEvent.values.length; i3++) {
                        printWriter.print("    values: " + sensorEvent.values[i3]);
                    }
                    printWriter.print("  }");
                }
                printWriter.print("}");
            }
            printWriter.println("sensitivity_setting: " + snapshot.sensitivitySetting);
            printWriter.println();
        }
        mSnapshots.clear();
        printWriter.println("Finished Dumping Elmyra Snapshots");
    }

    public class Snapshot {
        final SnapshotProtos$Snapshot mSnapshot;
        final long mTimestamp;

        Snapshot(SnapshotProtos$Snapshot snapshotProtos$Snapshot, long j) {
            mSnapshot = snapshotProtos$Snapshot;
            mTimestamp = j;
        }

        public SnapshotProtos$Snapshot getSnapshot() {
            return mSnapshot;
        }

        public long getTimestamp() {
            return mTimestamp;
        }
    }
}
