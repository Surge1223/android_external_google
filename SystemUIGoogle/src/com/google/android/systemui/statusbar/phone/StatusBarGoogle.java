package com.google.android.systemui.statusbar.phone;

import android.widget.ImageView;
import com.android.systemui.C1737R$id;
import com.android.systemui.Dependency;
import com.android.systemui.dock.DockManager;
import com.android.systemui.statusbar.NotificationLockscreenUserManager;
import com.android.systemui.statusbar.phone.StatusBar;
import com.google.android.systemui.NotificationLockscreenUserManagerGoogle;
import com.google.android.systemui.dreamliner.DockIndicationController;
import com.google.android.systemui.dreamliner.DockObserver;
import com.google.android.systemui.smartspace.SmartSpaceController;
import java.io.FileDescriptor;
import java.io.PrintWriter;

public class StatusBarGoogle extends StatusBar {
    private NotificationIconCenteringController mNotifIconCenteringController;

    public void start() {
        super.start();
        ((NotificationLockscreenUserManagerGoogle) Dependency.get(NotificationLockscreenUserManager.class)).updateAodVisibilitySettings();
        DockObserver dockObserver = (DockObserver) Dependency.get(DockManager.class);
        dockObserver.setDreamlinerGear((ImageView) this.mStatusBarWindow.findViewById(C1737R$id.dreamliner_gear));
        dockObserver.setIndicationController(new DockIndicationController(this.mContext));
    }

    public void setLockscreenUser(int i) {
        super.setLockscreenUser(i);
        SmartSpaceController.get(this.mContext).reloadData();
    }

    public void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        super.dump(fileDescriptor, printWriter, strArr);
        SmartSpaceController.get(this.mContext).dump(fileDescriptor, printWriter, strArr);
        NotificationIconCenteringController notificationIconCenteringController = this.mNotifIconCenteringController;
        if (notificationIconCenteringController != null) {
            notificationIconCenteringController.dump(fileDescriptor, printWriter, strArr);
        }
    }
}
