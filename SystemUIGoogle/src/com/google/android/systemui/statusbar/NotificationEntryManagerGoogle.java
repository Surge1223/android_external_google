package com.google.android.systemui.statusbar;

import android.app.WallpaperInfo;
import android.app.WallpaperManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import com.android.systemui.settings.CurrentUserTracker;
import com.android.systemui.statusbar.NotificationPresenter;
import com.android.systemui.statusbar.notification.NotificationEntryManager;
import com.android.systemui.statusbar.notification.stack.NotificationListContainer;
import com.android.systemui.statusbar.policy.HeadsUpManager;
import com.google.android.collect.Sets;
import java.util.HashSet;

public class NotificationEntryManagerGoogle extends NotificationEntryManager {
    private static final HashSet<String> NOTIFYABLE_PACKAGES = Sets.newHashSet(new String[]{"com.breel.wallpapers", "com.breel.wallpapers18", "com.google.pixel.livewallpaper"});
    private static final String[] NOTIFYABLE_WALLPAPERS = {"com.breel.wallpapers.imprint", "com.breel.wallpapers18.tactile", "com.breel.wallpapers18.delight", "com.breel.wallpapers18.miniman", "com.google.pixel.livewallpaper.imprint", "com.google.pixel.livewallpaper.tactile", "com.google.pixel.livewallpaper.delight", "com.google.pixel.livewallpaper.miniman"};
    private final Context mContext;
    private boolean mShouldBroadcastNotifications;
    private final CurrentUserTracker mUserTracker;
    private BroadcastReceiver mWallpaperChangedReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("android.intent.action.WALLPAPER_CHANGED")) {
                NotificationEntryManagerGoogle.this.checkNotificationBroadcastSupport();
            }
        }
    };
    private String mWallpaperPackage;

    public NotificationEntryManagerGoogle(Context context) {
        super(context);
        this.mContext = context;
        this.mUserTracker = new CurrentUserTracker(context) {
            public void onUserSwitched(int i) {
            }
        };
    }

    public void setUpWithPresenter(NotificationPresenter notificationPresenter, NotificationListContainer notificationListContainer, HeadsUpManager headsUpManager) {
        super.setUpWithPresenter(notificationPresenter, notificationListContainer, headsUpManager);
        this.mContext.registerReceiver(this.mWallpaperChangedReceiver, new IntentFilter("android.intent.action.WALLPAPER_CHANGED"));
        checkNotificationBroadcastSupport();
    }

    public void addNotification(StatusBarNotification statusBarNotification, NotificationListenerService.RankingMap rankingMap) {
        super.addNotification(statusBarNotification, rankingMap);
        boolean z = this.mUserTracker.getCurrentUserId() == 0;
        if (this.mShouldBroadcastNotifications && z) {
            Intent intent = new Intent();
            intent.setPackage(this.mWallpaperPackage);
            intent.setAction("com.breel.wallpapers.NOTIFICATION_RECEIVED");
            intent.putExtra("notification_color", statusBarNotification.getNotification().color);
            this.mContext.sendBroadcast(intent, "com.breel.wallpapers.notifications");
        }
    }

    /* access modifiers changed from: private */
    public void checkNotificationBroadcastSupport() {
        this.mShouldBroadcastNotifications = false;
        WallpaperManager wallpaperManager = (WallpaperManager) this.mContext.getSystemService(WallpaperManager.class);
        if (wallpaperManager != null) {
            WallpaperInfo wallpaperInfo = wallpaperManager.getWallpaperInfo();
            if (wallpaperInfo != null) {
                ComponentName component = wallpaperInfo.getComponent();
                String packageName = component.getPackageName();
                if (NOTIFYABLE_PACKAGES.contains(packageName)) {
                    this.mWallpaperPackage = packageName;
                    String className = component.getClassName();
                    for (String startsWith : NOTIFYABLE_WALLPAPERS) {
                        if (className.startsWith(startsWith)) {
                            this.mShouldBroadcastNotifications = true;
                            return;
                        }
                    }
                }
            }
        }
    }
}
