package com.google.android.systemui.elmyra;

import android.content.Context;
import com.google.android.systemui.elmyra.actions.Action;
import com.google.android.systemui.elmyra.actions.CameraAction;
import com.google.android.systemui.elmyra.actions.DismissTimer;
import com.google.android.systemui.elmyra.actions.LaunchOpa;
import com.google.android.systemui.elmyra.actions.SettingsAction;
import com.google.android.systemui.elmyra.actions.SetupWizardAction;
import com.google.android.systemui.elmyra.actions.SilenceCall;
import com.google.android.systemui.elmyra.actions.SnoozeAlarm;
import com.google.android.systemui.elmyra.actions.UnpinNotifications;
import com.google.android.systemui.elmyra.feedback.AssistInvocationEffect;
import com.google.android.systemui.elmyra.feedback.FeedbackEffect;
import com.google.android.systemui.elmyra.feedback.HapticClick;
import com.google.android.systemui.elmyra.feedback.NavUndimEffect;
import com.google.android.systemui.elmyra.feedback.SquishyNavigationButtons;
import com.google.android.systemui.elmyra.feedback.UserActivity;
import com.google.android.systemui.elmyra.gates.CameraVisibility;
import com.google.android.systemui.elmyra.gates.ChargingState;
import com.google.android.systemui.elmyra.gates.Gate;
import com.google.android.systemui.elmyra.gates.KeyguardDeferredSetup;
import com.google.android.systemui.elmyra.gates.KeyguardProximity;
import com.google.android.systemui.elmyra.gates.NavigationBarVisibility;
import com.google.android.systemui.elmyra.gates.PowerSaveState;
import com.google.android.systemui.elmyra.gates.SetupWizard;
import com.google.android.systemui.elmyra.gates.SystemKeyPress;
import com.google.android.systemui.elmyra.gates.TelephonyActivity;
import com.google.android.systemui.elmyra.gates.UsbState;
import com.google.android.systemui.elmyra.gates.VrMode;
import com.google.android.systemui.elmyra.gates.WakeMode;
import com.google.android.systemui.elmyra.sensors.CHREGestureSensor;
import com.google.android.systemui.elmyra.sensors.GestureSensor;
import com.google.android.systemui.elmyra.sensors.JNIGestureSensor;
import com.google.android.systemui.elmyra.sensors.config.GestureConfiguration;
import com.google.android.systemui.elmyra.sensors.config.ScreenStateAdjustment;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ServiceConfigurationGoogle implements ServiceConfiguration {
    private final List<Action> mActions = new ArrayList();
    private Context mContext;
    private final List<FeedbackEffect> mFeedbackEffects;
    private final List<Gate> mGates;
    private final GestureSensor mGestureSensor;

    public ServiceConfigurationGoogle(Context context) {
        mContext = context;
        AssistInvocationEffect assistInvocationEffect = new AssistInvocationEffect(context);
        LaunchOpa launchOpa = new LaunchOpa(context, Arrays.asList(new FeedbackEffect[]{assistInvocationEffect}));
        SettingsAction settingsAction = new SettingsAction(context, launchOpa);
        List asList = Arrays.asList(new Action[]{new DismissTimer(context), new SnoozeAlarm(context), new SilenceCall(context), settingsAction});
        CameraAction cameraAction = new CameraAction(context, Arrays.asList(new FeedbackEffect[]{assistInvocationEffect}));
        mActions.addAll(asList);
        mActions.add(new UnpinNotifications(context));
        mActions.add(cameraAction);
        mActions.add(new SetupWizardAction(context, settingsAction, launchOpa));
        mActions.add(launchOpa);
        mFeedbackEffects = new ArrayList();
        mFeedbackEffects.add(new HapticClick(context));
        mFeedbackEffects.add(new SquishyNavigationButtons(context));
        mFeedbackEffects.add(new NavUndimEffect());
        mFeedbackEffects.add(new UserActivity(context));
        mGates = new ArrayList();
        mGates.add(new WakeMode(context));
        mGates.add(new ChargingState(context));
        mGates.add(new UsbState(context));
        mGates.add(new KeyguardProximity(context));
        mGates.add(new SetupWizard(context, Arrays.asList(new Action[]{settingsAction})));
        mGates.add(new NavigationBarVisibility(context, asList));
        mGates.add(new SystemKeyPress(context));
        mGates.add(new TelephonyActivity(context));
        mGates.add(new VrMode(context));
        mGates.add(new KeyguardDeferredSetup(context, asList));
        mGates.add(new CameraVisibility(context, cameraAction, asList));
        mGates.add(new PowerSaveState(context));
        ArrayList arrayList = new ArrayList();
        arrayList.add(new ScreenStateAdjustment(context));
        GestureConfiguration gestureConfiguration = new GestureConfiguration(context, arrayList);
        if (JNIGestureSensor.isAvailable(context)) {
            mGestureSensor = new JNIGestureSensor(context, gestureConfiguration);
        } else {
            mGestureSensor = new CHREGestureSensor(context, gestureConfiguration, new SnapshotConfiguration(context));
        }
    }
    @Override
    public List<Action> getActions() {
        return mActions;
    }

    @Override
    public List<FeedbackEffect> getFeedbackEffects() {
        return mFeedbackEffects;
    }

    @Override
    public List<Gate> getGates() {
        return mGates;
    }

    @Override
    public GestureSensor getGestureSensor() {
        return mGestureSensor;
    }
}
