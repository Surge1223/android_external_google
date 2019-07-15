package com.google.android.settings.gestures.assist;

import android.widget.SeekBar;
import android.view.View.OnClickListener;
import android.widget.Button;
import com.android.settings.SetupWizardUtils;
import android.os.Bundle;
import android.view.ViewGroup;
import android.view.View;

public class AssistGestureTrainingFinishedActivity extends AssistGestureTrainingSliderBase
{
    private boolean mAccessibilityAnnounced;
    private View mAssistGestureCheck;
    private View mAssistGestureIllustration;
    private ViewGroup mLayout;
    
    private void fadeOutCheckAfterDelay() {
        this.mHandler.sendMessageDelayed(this.mHandler.obtainMessage(5, (Object)this.mAssistGestureCheck), 1000L);
    }
    
    public int getMetricsCategory() {
        return 993;
    }
    
    @Override
    protected void handleGestureDetected() {
        this.mErrorView.setVisibility(4);
        this.mAssistGestureCheck.animate().cancel();
        this.mAssistGestureCheck.setAlpha(1.0f);
        this.mAssistGestureCheck.setVisibility(0);
        if (!this.mAccessibilityAnnounced) {
            this.mLayout.announceForAccessibility((CharSequence)this.getApplicationContext().getResources().getString(2131886139));
            this.mAccessibilityAnnounced = true;
        }
        this.mHandler.removeMessages(4);
        this.mHandler.removeMessages(5);
        this.fadeOutCheckAfterDelay();
        this.fadeIndicators();
    }
    
    @Override
    public void onClick(final View view) {
        switch (view.getId()) {
            case 16908314: {
                if (this.flowTypeDeferredSetup() || this.flowTypeSettingsSuggestion() || this.flowTypeSetup()) {
                    this.setResult(-1);
                    this.mAssistGestureHelper.setListener(null);
                    this.finishAndRemoveTask();
                    break;
                }
                if (this.flowTypeAccidentalTrigger()) {
                    this.handleDoneAndLaunch();
                    break;
                }
                break;
            }
            case 16908313: {
                this.launchAssistGestureSettings();
                break;
            }
        }
    }
    
    @Override
    protected void onCreate(final Bundle bundle) {
        this.setTheme(SetupWizardUtils.getTheme(this.getIntent()));
        this.setContentView(2131558459);
        this.mLayout = (ViewGroup)this.findViewById(2131362327);
        super.onCreate(bundle);
        this.setShouldCheckForNoProgress(false);
        final Button button = (Button)this.findViewById(16908314);
        button.setOnClickListener((View.OnClickListener)this);
        if (!this.flowTypeDeferredSetup() && !this.flowTypeSetup()) {
            if (this.flowTypeSettingsSuggestion()) {
                button.setText(2131887493);
            }
            else if (this.flowTypeAccidentalTrigger()) {
                button.setText(2131886426);
            }
        }
        else {
            button.setText(2131888358);
        }
        final Button button2 = (Button)this.findViewById(16908313);
        button2.setOnClickListener((View.OnClickListener)this);
        if (this.flowTypeDeferredSetup() || this.flowTypeSetup()) {
            button2.setVisibility(4);
        }
        this.mAssistGestureCheck = this.findViewById(2131361890);
        this.mAssistGestureIllustration = this.findViewById(2131361891);
        this.fadeOutCheckAfterDelay();
    }
    
    @Override
    public void onProgressChanged(final SeekBar seekBar, final int n, final boolean b) {
        super.onProgressChanged(seekBar, n, b);
    }
    
    @Override
    public void onStartTrackingTouch(final SeekBar seekBar) {
        super.onStartTrackingTouch(seekBar);
        this.mHandler.removeMessages(4);
        this.mHandler.removeMessages(5);
        this.mHandler.obtainMessage(6, (Object)this.mAssistGestureCheck).sendToTarget();
        this.mHandler.obtainMessage(7, (Object)this.mAssistGestureIllustration).sendToTarget();
    }
    
    @Override
    protected void showMessage(final int n, final String s) {
        if (this.mAssistGestureCheck.getVisibility() == 4) {
            super.showMessage(n, s);
        }
    }
}
