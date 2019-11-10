package com.google.android.systemui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.UserHandle;
import android.provider.Settings;
// import com.android.internal.util.du.Utils;
import com.android.systemui.SysUiServiceProvider;
import com.android.systemui.VendorServices;
import com.android.systemui.R;
import com.android.systemui.Dumpable;
import com.android.systemui.statusbar.phone.StatusBar;
import com.google.android.systemui.dreamliner.DockObserver;
import com.google.android.systemui.dreamliner.DreamlinerContext;
import com.google.android.systemui.ambientmusic.AmbientIndicationContainer;
import com.google.android.systemui.ambientmusic.AmbientIndicationService;
import com.google.android.systemui.ambientmusic.AmbientIndicationContainer;
import com.google.android.systemui.ambientmusic.AmbientIndicationService;
import com.google.android.systemui.elmyra.ElmyraContext;
import com.google.android.systemui.elmyra.ElmyraService;
import com.google.android.systemui.elmyra.ServiceConfigurationGoogle;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;

public class GoogleServices extends VendorServices {
    private ArrayList<Object> mServices = new ArrayList();

    private void addService(Object obj) {
        if (obj != null) {
            this.mServices.add(obj);
        }
    }

    // Small broadcast receiver for helping with Application actions
    private final BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(action.equals(Intent.ACTION_PACKAGE_REMOVED)) {
                // Get packageName from Uri
                String packageName = intent.getData().getSchemeSpecificPart();
                // If the package is still installed
                if (/* Utils.isPackageInstalled(context, packageName) */ false) {
                    // it's an application update, we can skip the rest.
                    return;
                }
                // Get package names currently set as default
                // TODO: enable back when everything gets forward ported
                // String shortPackageName = Settings.Secure.getStringForUser(context.getContentResolver(),
                //         Settings.Secure.SHORT_SQUEEZE_CUSTOM_APP,
                //         UserHandle.USER_CURRENT);
                // String longPackageName = Settings.Secure.getStringForUser(context.getContentResolver(),
                //         Settings.Secure.LONG_SQUEEZE_CUSTOM_APP,
                //         UserHandle.USER_CURRENT);
                String shortPackageName = "";
                String longPackageName = "";
                // if the package name equals to some set value
                if(packageName.equals(shortPackageName)) {
                    // The short application action has to be reset
                    resetApplicationAction(/* isShortAction */ true);
                }
                if (packageName.equals(longPackageName)) {
                    // The long application action has to be reset
                    resetApplicationAction(/* isShortAction */ false);
                }
            }
        }
    };
    public void start() {
        StatusBar statusBar = SysUiServiceProvider.getComponent(mContext, StatusBar.class);
        AmbientIndicationContainer ambientIndicationContainer = statusBar.getStatusBarWindow().findViewById(R.id.ambient_indication_container);
        ambientIndicationContainer.initializeView(statusBar);
        addService(new AmbientIndicationService(mContext, ambientIndicationContainer));
        addService(new DisplayCutoutEmulationAdapter(mContext));
        if (new ElmyraContext(mContext).isAvailable()) {
            Context context = mContext;
            addService(new ElmyraService(context, new ServiceConfigurationGoogle(context)));
        }
        if (new DreamlinerContext(mContext).isAvailable()) {
            addService(new DockObserver(mContext));
        }
        // Intent for applications that get uninstalled
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_PACKAGE_REMOVED);
        filter.addDataScheme("package");
        // Register our BroadcastReceiver
        mContext.registerReceiver(mBroadcastReceiver, filter);
    }

    public void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        for (int i = 0; i < mServices.size(); i++) {
            if (mServices.get(i) instanceof Dumpable) {
                ((Dumpable) mServices.get(i)).dump(fileDescriptor, printWriter, strArr);
            }
        }
    }

    /* Helper that sets to default values, this gets called when a package
       used as action gets removed.*/
    private void resetApplicationAction(boolean isShortAction) {
        if (isShortAction) {
            // Remove stored values
            // TODO: enable back when everything gets forward ported
            // Settings.Secure.putIntForUser(mContext.getContentResolver(),
            //         Settings.Secure.SHORT_SQUEEZE_SELECTION, /* no action */ 0,
            //         UserHandle.USER_CURRENT);
            // Settings.Secure.putStringForUser(mContext.getContentResolver(),
            //         Settings.Secure.SHORT_SQUEEZE_CUSTOM_APP_FR_NAME, /* none */ "",
            //         UserHandle.USER_CURRENT);
        } else {
            // Remove stored values
            // TODO: enable back when everything gets forward ported
            // Settings.Secure.putIntForUser(mContext.getContentResolver(),
            //         Settings.Secure.LONG_SQUEEZE_SELECTION, /* no action */ 0,
            //         UserHandle.USER_CURRENT);
            // Settings.Secure.putStringForUser(mContext.getContentResolver(),
            //         Settings.Secure.LONG_SQUEEZE_CUSTOM_APP_FR_NAME, /* none */ "",
            //         UserHandle.USER_CURRENT);
        }
    }
}
