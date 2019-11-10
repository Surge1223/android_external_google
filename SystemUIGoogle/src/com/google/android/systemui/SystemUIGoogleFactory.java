package com.google.android.systemui;

import android.app.AlarmManager;
import android.content.Context;
import com.android.internal.colorextraction.ColorExtractor;
import com.android.internal.util.function.TriConsumer;
import com.android.systemui.SystemUIFactory;
import com.android.systemui.assist.AssistManager;
import com.android.systemui.dock.DockManager;
import com.android.systemui.power.EnhancedEstimates;
import com.android.systemui.statusbar.NotificationLockscreenUserManager;
import com.android.systemui.statusbar.ScrimView;
import com.android.systemui.statusbar.notification.NotificationEntryManager;
import com.android.systemui.statusbar.notification.NotificationInterruptionStateProvider;
import com.android.systemui.statusbar.phone.DozeParameters;
import com.android.systemui.statusbar.phone.LockscreenWallpaper;
import com.android.systemui.statusbar.phone.ScrimController;
import com.android.systemui.statusbar.phone.ScrimState;
import com.android.systemui.statusbar.policy.DeviceProvisionedController;
import com.android.systemui.statusbar.policy.KeyguardMonitor;
import com.google.android.systemui.assist.AssistManagerGoogle;
import com.google.android.systemui.dreamliner.DockObserver;
import com.google.android.systemui.dreamliner.DreamlinerUtils;
import com.google.android.systemui.power.EnhancedEstimatesGoogleImpl;
import com.google.android.systemui.statusbar.NotificationEntryManagerGoogle;
import com.google.android.systemui.statusbar.NotificationInterruptionStateProviderGoogle;
import java.util.function.Consumer;

public class SystemUIGoogleFactory extends SystemUIFactory {
    public String provideLeakReportEmail() {
        return "buganizer-system+187317@google.com";
    }

    public EnhancedEstimates provideEnhancedEstimates(Context context) {
        return new EnhancedEstimatesGoogleImpl(context);
    }

    public AssistManager provideAssistManager(DeviceProvisionedController deviceProvisionedController, Context context) {
        return new AssistManagerGoogle(deviceProvisionedController, context);
    }

    public NotificationLockscreenUserManager provideNotificationLockscreenUserManager(Context context) {
        return new NotificationLockscreenUserManagerGoogle(context);
    }

    public NotificationEntryManager provideNotificationEntryManager(Context context) {
        return new NotificationEntryManagerGoogle(context);
    }

    public DockManager provideDockManager(Context context) {
        return new DockObserver(context, DreamlinerUtils.getInstance(context));
    }

    public NotificationInterruptionStateProvider provideNotificationInterruptionStateProvider(Context context) {
        return new NotificationInterruptionStateProviderGoogle(context);
    }

    public ScrimController createScrimController(ScrimView scrimView, ScrimView scrimView2, LockscreenWallpaper lockscreenWallpaper, TriConsumer<ScrimState, Float, ColorExtractor.GradientColors> triConsumer, Consumer<Integer> consumer, DozeParameters dozeParameters, AlarmManager alarmManager, KeyguardMonitor keyguardMonitor) {
        LiveWallpaperScrimController liveWallpaperScrimController = new LiveWallpaperScrimController(scrimView, scrimView2, lockscreenWallpaper, triConsumer, consumer, dozeParameters, alarmManager, keyguardMonitor);
        return liveWallpaperScrimController;
    }
}
