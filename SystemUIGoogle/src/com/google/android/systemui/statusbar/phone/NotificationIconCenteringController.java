package com.google.android.systemui.statusbar.phone;

import android.os.Handler;
import com.android.internal.annotations.VisibleForTesting;
import com.android.systemui.plugins.SensorManagerPlugin;
import com.android.systemui.statusbar.notification.NotificationEntryManager;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.phone.NotificationIconAreaController;
import com.android.systemui.util.Assert;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.Objects;

public class NotificationIconCenteringController implements SensorManagerPlugin.SensorEventListener {
    private NotificationEntry mEntryCentered;
    private final NotificationEntryManager mEntryManager;
    @VisibleForTesting
    protected boolean mIsSkipGestureEnabled;
    @VisibleForTesting
    protected Handler mMainThreadHandler;
    @VisibleForTesting
    protected String mMusicPlayingPkg;
    private NotificationIconAreaController mNotificationIconAreaController;
    private boolean mRegistered;
    @VisibleForTesting
    protected final Runnable mResetCenteredIconRunnable;

    public void onSensorChanged(SensorManagerPlugin.SensorEvent sensorEvent) {
        this.mMainThreadHandler.post(new Runnable(sensorEvent) {
            private final /* synthetic */ SensorManagerPlugin.SensorEvent f$1;

            {
                this.f$1 = r2;
            }

            public final void run() {
                NotificationIconCenteringController.this.lambda$onSensorChanged$1$NotificationIconCenteringController(this.f$1);
            }
        });
    }

    public /* synthetic */ void lambda$onSensorChanged$1$NotificationIconCenteringController(SensorManagerPlugin.SensorEvent sensorEvent) {
        boolean z = false;
        if (sensorEvent.getValues()[0] == 1.0f) {
            z = true;
        }
        this.mIsSkipGestureEnabled = z;
        updateCenteredIcon();
    }

    /* access modifiers changed from: protected */
    @VisibleForTesting
    public void updateCenteredIcon() {
        if (isMusicPlaying() && isSkipGestureEnabled()) {
            for (NotificationEntry next : this.mEntryManager.getNotificationData().getActiveNotifications()) {
                if (next.isMediaNotification() && Objects.equals(next.notification.getPackageName(), this.mMusicPlayingPkg)) {
                    showIconCentered(next);
                    return;
                }
            }
        }
        showIconCentered((NotificationEntry) null);
    }

    private void showIconCentered(NotificationEntry notificationEntry) {
        Assert.isMainThread();
        this.mMainThreadHandler.removeCallbacks(this.mResetCenteredIconRunnable);
        if (notificationEntry == null) {
            this.mMainThreadHandler.postDelayed(this.mResetCenteredIconRunnable, 250);
            return;
        }
        this.mNotificationIconAreaController.showIconCentered(notificationEntry);
        this.mEntryCentered = notificationEntry;
    }

    private boolean isMusicPlaying() {
        return this.mMusicPlayingPkg != null;
    }

    private boolean isSkipGestureEnabled() {
        return this.mIsSkipGestureEnabled;
    }

    public void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        PrintWriter append = printWriter.append("NotifIconCenterContr").append(": ");
        PrintWriter append2 = append.append("\nisMusicPlaying: " + isMusicPlaying());
        PrintWriter append3 = append2.append("\nisSkipGestureEnabled: " + isSkipGestureEnabled());
        PrintWriter append4 = append3.append("\nmSkipStatusRegistered: " + this.mRegistered);
        append4.append("\nmEntryCentered: " + this.mEntryCentered + "\n");
    }
}
