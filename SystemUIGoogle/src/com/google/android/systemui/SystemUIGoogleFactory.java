// package com.google.android.systemui;

// import android.content.Context;
// import android.util.ArrayMap;
// import com.android.systemui.DependencyProvider;
// import com.android.systemui.SystemUIFactory;
// import com.android.systemui.statusbar.notification.NotificationEntryManager;
// import com.google.android.systemui.statusbar.NotificationEntryManagerGoogle;

// public class SystemUIGoogleFactory extends SystemUIFactory {

//     public void injectDependencies(ArrayMap<Object, DependencyProvider> providers,
//             Context context) {
//         super.injectDependencies(providers, context);
//         providers.put(NotificationEntryManager.class,
//                 () -> new NotificationEntryManagerGoogle(context));
//     }
// }