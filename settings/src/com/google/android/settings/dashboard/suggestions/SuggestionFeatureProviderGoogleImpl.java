package com.google.android.settings.dashboard.suggestions;

import android.content.ComponentName;
import android.content.Context;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;
import android.util.Log;

import com.android.settings.dashboard.suggestions.SuggestionFeatureProviderImpl;
import com.android.settings.overlay.FeatureFactory;

public class SuggestionFeatureProviderGoogleImpl extends SuggestionFeatureProviderImpl
{
    static final String SETTING_USB_MIGRATION_STATE = "usb_migration_state";
    static final int USB_MIGRATION_TRANSFER_FINISHED = 2;
    
    public SuggestionFeatureProviderGoogleImpl(final Context context) {
        super(context);
    }
    
    public ComponentName getSuggestionServiceComponent() {
        return new ComponentName("com.google.android.settings.intelligence", "com.google.android.settings.intelligence.modules.suggestions.SuggestionService");
    }

    @Override
    public boolean isSuggestionComplete(final Context context, final ComponentName componentName) {
        if (componentName.getClassName().equals("com.google.android.settings.gestures.AssistGestureSuggestion")) {
            final boolean supported = FeatureFactory.getFactory(context).getAssistGestureFeatureProvider().isSupported(context);
            final int int1 = Settings.Secure.getInt(context.getContentResolver(), "assist_gesture_setup_complete", 0);
            final boolean b = true;
            final boolean b2 = int1 != 0;
            final boolean b3 = Settings.Secure.getInt(context.getContentResolver(), "assist_gesture_enabled", 1) != 0;
            boolean b4 = b;
            if (supported) {
                b4 = b;
                if (!b2) {
                    b4 = (!b3 && b);
                }
            }
            return b4;
        }
        return super.isSuggestionComplete(context, componentName);
    }
}
