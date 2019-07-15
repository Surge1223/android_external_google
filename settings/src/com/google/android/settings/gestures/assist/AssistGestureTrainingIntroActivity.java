package com.google.android.settings.gestures.assist;

import android.widget.Button;
import android.os.Bundle;
import android.view.View;
import com.android.settings.SetupWizardUtils;
import android.content.Context;
import android.content.Intent;
import com.android.setupwizardlib.util.WizardManagerHelper;
import android.view.View.OnClickListener;

public class AssistGestureTrainingIntroActivity extends AssistGestureTrainingBase implements View.OnClickListener
{
    private static final String FROM_ACCIDENTAL_TRIGGER_CLASS;
    
    static {
        FROM_ACCIDENTAL_TRIGGER_CLASS = AssistGestureTrainingIntroActivity.class.getName();
    }
    
    private String getFlowType() {
        final Intent intent = this.getIntent();
        if (WizardManagerHelper.isSetupWizardIntent(intent)) {
            return "setup";
        }
        if (WizardManagerHelper.isDeferredSetupWizard(intent)) {
            return "deferred_setup";
        }
        if ("com.google.android.settings.gestures.AssistGestureSuggestion".contentEquals(intent.getComponent().getClassName())) {
            return "settings_suggestion";
        }
        if (AssistGestureTrainingIntroActivity.FROM_ACCIDENTAL_TRIGGER_CLASS.contentEquals(intent.getComponent().getClassName())) {
            return "accidental_trigger";
        }
        return null;
    }
    
    private void startEnrollingActivity() {
        final Intent intent = new Intent((Context)this, (Class)AssistGestureTrainingEnrollingActivity.class);
        intent.putExtra("launched_from", this.getFlowType());
        SetupWizardUtils.copySetupExtras(this.getIntent(), intent);
        this.startActivityForResult(intent, 1);
    }
    
    public int getMetricsCategory() {
        return 991;
    }
    
    protected void onActivityResult(final int n, final int n2, final Intent intent) {
        super.onActivityResult(n, n2, intent);
        if (n == 1 && n2 != 0) {
            this.setResult(n2, intent);
            this.finishAndRemoveTask();
        }
    }
    
    public void onClick(final View view) {
        switch (view.getId()) {
            case 16908314: {
                this.startEnrollingActivity();
                break;
            }
            case 16908313: {
                if ("accidental_trigger".contentEquals(this.getFlowType())) {
                    this.launchAssistGestureSettings();
                    break;
                }
                this.setResult(101);
                this.finishAndRemoveTask();
                break;
            }
        }
    }
    
    @Override
    protected void onCreate(final Bundle bundle) {
        this.setTheme(SetupWizardUtils.getTheme(this.getIntent()));
        super.onCreate(bundle);
        this.setContentView(2131558461);
        ((Button)this.findViewById(16908314)).setOnClickListener((View.OnClickListener)this);
        final Button button = (Button)this.findViewById(16908313);
        button.setOnClickListener((View.OnClickListener)this);
        if ("accidental_trigger".contentEquals(this.getFlowType())) {
            button.setText(2131886430);
        }
        else {
            button.setText(2131886427);
        }
    }
    
    public void onGestureDetected() {
        this.clearIndicators();
        this.startEnrollingActivity();
    }
    
    @Override
    public void onGestureProgress(final float n, final int n2) {
        super.onGestureProgress(n, n2);
    }
}
