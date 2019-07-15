package com.google.android.settings.search;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.JobIntentService;
import com.android.settings.overlay.FeatureFactory;
import com.android.settings.search.DeviceIndexFeatureProvider;
import com.google.android.settings.search.AppIndexingUpdateServiceRunnable;

public class AppIndexingUpdateService extends JobIntentService {
    public static void enqueueWork(Context context) {
        enqueueWork(context, AppIndexingUpdateService.class, 42, new Intent());
    }
    
    @Override
    public void onHandleWork(Intent intent) {
        AsyncTask.execute(new AppIndexingUpdateServiceRunnable(this, FeatureFactory.getFactory(this).getDeviceIndexFeatureProvider()));
    }
}

