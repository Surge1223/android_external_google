package com.google.android.settings.survey;

import com.android.settings.overlay.SurveyFeatureProvider;
import com.android.settings.overlay.FeatureFactory;
import android.content.Intent;
import android.content.Context;
import android.app.Activity;
import android.content.BroadcastReceiver;

public class SurveyBroadcastReceiver extends BroadcastReceiver
{
    private Activity mActivity;
    
    public void onReceive(final Context context, final Intent intent) {
        final SurveyFeatureProvider surveyFeatureProvider = FeatureFactory.getFactory(context).getSurveyFeatureProvider(context);
        if (this.mActivity != null && "com.google.android.libraries.hats20.SURVEY_DOWNLOADED".equals(intent.getAction())) {
            SurveyFeatureProvider.unregisterReceiver(this.mActivity, this);
            if (surveyFeatureProvider != null) {
                surveyFeatureProvider.showSurveyIfAvailable(this.mActivity, intent.getStringExtra("SiteId"));
            }
        }
    }
    
    public void setActivity(final Activity mActivity) {
        this.mActivity = mActivity;
    }
}
