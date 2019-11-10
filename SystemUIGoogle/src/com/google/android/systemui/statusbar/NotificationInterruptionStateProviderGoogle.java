package com.google.android.systemui.statusbar;

import android.content.Context;
import android.util.Log;
import com.android.systemui.statusbar.notification.NotificationInterruptionStateProvider;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.google.android.systemui.dreamliner.DockObserver;

public class NotificationInterruptionStateProviderGoogle extends NotificationInterruptionStateProvider {
    public NotificationInterruptionStateProviderGoogle(Context context) {
        super(context);
    }

    /* access modifiers changed from: protected */
    public boolean canAlertCommon(NotificationEntry notificationEntry) {
        if (!DockObserver.isDockingUiShowing()) {
            return super.canAlertCommon(notificationEntry);
        }
        if (!Log.isLoggable("NotificationInterruption", 3)) {
            return false;
        }
        Log.d("NotificationInterruption", "No alerting: the Docking UI is showing");
        return false;
    }
}
