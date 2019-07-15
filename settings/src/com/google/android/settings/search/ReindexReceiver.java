package com.google.android.settings.search;

import android.content.Intent;
import android.content.Context;
import android.content.BroadcastReceiver;

public class ReindexReceiver extends BroadcastReceiver
{
    public void onReceive(final Context context, final Intent intent) {
        if (intent != null && "com.google.firebase.appindexing.UPDATE_INDEX".equals(intent.getAction())) {
            AppIndexingUpdateService.enqueueWork(context);
        }
    }
}
