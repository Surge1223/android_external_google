package com.google.android.settings.gestures.assist;

import android.animation.LayoutTransition;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.android.settings.R;
import com.android.settings.SetupWizardUtils;
import com.google.android.settings.gestures.assist.AssistGestureTrainingFinishedActivity;
import com.google.android.settings.gestures.assist.AssistGestureTrainingSliderBase;

public class AssistGestureTrainingEnrollingActivity extends AssistGestureTrainingSliderBase {
    private void startFinishedActivity() {
        Intent intent = new Intent(this, AssistGestureTrainingFinishedActivity.class);
        intent.putExtra("launched_from", getIntent().getStringExtra("launched_from"));
        intent.addFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
        SetupWizardUtils.copySetupExtras(getIntent(), intent);
        startActivity(intent);
    }

    @Override
    public int getMetricsCategory() {
        return 992;
    }

    @Override
    protected void handleGestureDetected() {
        mErrorView.setVisibility(View.INVISIBLE);
        Settings.Secure.putInt(getContentResolver(), "assist_gesture_setup_complete", 1);
        startFinishedActivity();
        finishAndRemoveTask();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.cancel_button) {
            setResult(101);
            finishAndRemoveTask();
        }
    }

    protected int getContentView() {
        return R.layout.assist_gesture_training_enrolling_activity;
    }

    @Override
    protected void onCreate(Bundle bundle) {
        setTheme(SetupWizardUtils.getTheme(getIntent()));
        setContentView(getContentView());
        super.onCreate(bundle);
        LinearLayout linearLayout = findViewById(R.id.content_container);
        LayoutTransition layoutTransition = new LayoutTransition();
        layoutTransition.setDuration(3, 100L);
        linearLayout.setLayoutTransition(layoutTransition);
        findViewById(R.id.cancel_button).setOnClickListener(this);
    }
}

