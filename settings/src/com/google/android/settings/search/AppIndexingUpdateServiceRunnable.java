package com.google.android.settings.search;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.JobIntentService;
import com.android.settings.overlay.FeatureFactory;
import com.android.settings.search.DeviceIndexFeatureProvider;
import com.google.android.settings.search.AppIndexingUpdateService;

class AppIndexingUpdateServiceRunnable implements Runnable {
    private AppIndexingUpdateService mAppIndexingUpdateService;
    private DeviceIndexFeatureProvider mDeviceIndexFeatureProvider;

    public AppIndexingUpdateServiceRunnable(AppIndexingUpdateService appIndexingUpdateService, DeviceIndexFeatureProvider deviceIndexFeatureProvider) {
        mAppIndexingUpdateService = appIndexingUpdateService;
        mDeviceIndexFeatureProvider = deviceIndexFeatureProvider;
    }
    
    public final void run() {
        mDeviceIndexFeatureProvider.updateIndex(mAppIndexingUpdateService, true);
    }
}

