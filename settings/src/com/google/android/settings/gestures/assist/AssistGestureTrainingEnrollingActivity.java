package com.google.android.settings.gestures.assist;

import android.view.View.OnClickListener;
import android.widget.Button;
import android.animation.LayoutTransition;
import android.widget.LinearLayout;
import android.os.Bundle;
import android.view.View;
import android.provider.Settings;
import com.android.settings.SetupWizardUtils;
import android.content.Context;
import android.content.Intent;

public class AssistGestureTrainingEnrollingActivity extends AssistGestureTrainingSliderBase
{
    private void startFinishedActivity() {
        final Intent intent = new Intent((Context)this, (Class)AssistGestureTrainingFinishedActivity.class);
        intent.putExtra("launched_from", this.getIntent().getStringExtra("launched_from"));
        intent.addFlags(33554432);
        SetupWizardUtils.copySetupExtras(this.getIntent(), intent);
        this.startActivity(intent);
    }
    
    public int getMetricsCategory() {
        return 992;
    }
    
    @Override
    protected void handleGestureDetected() {
        this.clearIndicators();
        this.mErrorView.setVisibility(4);
        Settings.Secure.putInt(this.getContentResolver(), "assist_gesture_setup_complete", 1);
        this.startFinishedActivity();
        this.finishAndRemoveTask();
    }
    
    @Override
    public void onClick(final View view) {
        if (view.getId() == 2131361962) {
            this.setResult(101);
            this.finishAndRemoveTask();
        }
    }
    
    @Override
    protected void onCreate(final Bundle bundle) {
        this.setTheme(SetupWizardUtils.getTheme(this.getIntent()));
        this.setContentView(2131558457);
        super.onCreate(bundle);
        final LinearLayout linearLayout = (LinearLayout)this.findViewById(2131362011);
        final LayoutTransition layoutTransition = new LayoutTransition();
        layoutTransition.setDuration(3, 100L);
        linearLayout.setLayoutTransition(layoutTransition);
        ((Button)this.findViewById(2131361962)).setOnClickListener((View.OnClickListener)this);
    }
}
