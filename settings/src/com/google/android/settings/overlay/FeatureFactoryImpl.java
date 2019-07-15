package com.google.android.settings.overlay;

import android.util.Log;
import com.google.android.gsf.Gservices;
import com.google.android.settings.support.SupportFeatureProviderImpl;
import com.google.android.settings.dashboard.suggestions.SuggestionFeatureProviderGoogleImpl;
import com.google.android.settings.search.SearchFeatureProviderGoogleImpl;
import com.google.android.settings.fuelgauge.PowerUsageFeatureProviderGoogleImpl;
import com.google.android.settings.connecteddevice.dock.DockUpdaterFeatureProviderGoogleImpl;
import com.google.android.settings.dashboard.DashboardFeatureProviderGoogleImpl;
import com.google.android.settings.gestures.assist.AssistGestureFeatureProviderGoogleImpl;
import com.google.android.settings.applications.ApplicationFeatureProviderGoogleImpl;
import android.app.admin.DevicePolicyManager;
import android.app.AppGlobals;
import com.android.settingslib.wrapper.PackageManagerWrapper;
import android.content.Context;
import com.google.android.settings.accounts.AccountFeatureProviderGoogleImpl;
import com.android.settings.overlay.SurveyFeatureProvider;
import com.android.settings.overlay.SupportFeatureProvider;
import com.android.settings.dashboard.suggestions.SuggestionFeatureProvider;
import com.android.settings.search.SearchFeatureProvider;
import com.android.settings.fuelgauge.PowerUsageFeatureProvider;
import com.android.settings.overlay.DockUpdaterFeatureProvider;
import com.android.settings.search.DeviceIndexFeatureProvider;
import com.android.settings.dashboard.DashboardFeatureProvider;
import com.android.settings.gestures.AssistGestureFeatureProvider;
import com.android.settings.applications.ApplicationFeatureProvider;
import com.android.settings.accounts.AccountFeatureProvider;

public final class FeatureFactoryImpl extends com.android.settings.overlay.FeatureFactoryImpl
{
    private AccountFeatureProvider mAccountFeatureProvider;
    private ApplicationFeatureProvider mApplicationFeatureProvider;
    private AssistGestureFeatureProvider mAssistGestureFeatureProvider;
    private DashboardFeatureProvider mDashboardFeatureProvider;
//    private DeviceIndexFeatureProvider mDeviceIndexFeatureProvider;
    private DockUpdaterFeatureProvider mDockUpdaterFeatureProvider;
    private PowerUsageFeatureProvider mPowerUsageProvider;
    private SearchFeatureProvider mSearchFeatureProvider;
    private SuggestionFeatureProvider mSuggestionFeatureProvider;
    private SupportFeatureProvider mSupportProvider;
//    private SurveyFeatureProvider mSurveyFeatureProvider;
    
    @Override
    public AccountFeatureProvider getAccountFeatureProvider() {
        if (this.mAccountFeatureProvider == null) {
            this.mAccountFeatureProvider = new AccountFeatureProviderGoogleImpl();
        }
        return this.mAccountFeatureProvider;
    }
    
    @Override
    public ApplicationFeatureProvider getApplicationFeatureProvider(Context applicationContext) {
        if (this.mApplicationFeatureProvider == null) {
            applicationContext = applicationContext.getApplicationContext();
            this.mApplicationFeatureProvider = new ApplicationFeatureProviderGoogleImpl(applicationContext, new PackageManagerWrapper(applicationContext.getPackageManager()), AppGlobals.getPackageManager(), (DevicePolicyManager)applicationContext.getSystemService("device_policy"));
        }
        return this.mApplicationFeatureProvider;
    }
    
    @Override
    public AssistGestureFeatureProvider getAssistGestureFeatureProvider() {
        if (this.mAssistGestureFeatureProvider == null) {
            this.mAssistGestureFeatureProvider = new AssistGestureFeatureProviderGoogleImpl();
        }
        return this.mAssistGestureFeatureProvider;
    }
    
    @Override
    public DashboardFeatureProvider getDashboardFeatureProvider(final Context context) {
        if (this.mDashboardFeatureProvider == null) {
            this.mDashboardFeatureProvider = new DashboardFeatureProviderGoogleImpl(context.getApplicationContext());
        }
        return this.mDashboardFeatureProvider;
    }
/*
    @Override
    public DeviceIndexFeatureProvider getDeviceIndexFeatureProvider() {
        if (this.mDeviceIndexFeatureProvider == null) {
            this.mDeviceIndexFeatureProvider = new DeviceIndexFeatureProviderGoogleImpl();
        }
        return this.mDeviceIndexFeatureProvider;
    }
*/
    @Override
    public DockUpdaterFeatureProvider getDockUpdaterFeatureProvider() {
        if (this.mDockUpdaterFeatureProvider == null) {
            this.mDockUpdaterFeatureProvider = new DockUpdaterFeatureProviderGoogleImpl();
        }
        return this.mDockUpdaterFeatureProvider;
    }
    
    @Override
    public PowerUsageFeatureProvider getPowerUsageFeatureProvider(final Context context) {
        if (this.mPowerUsageProvider == null) {
            this.mPowerUsageProvider = new PowerUsageFeatureProviderGoogleImpl(context.getApplicationContext());
        }
        return this.mPowerUsageProvider;
    }
    
    @Override
    public SearchFeatureProvider getSearchFeatureProvider() {
        if (this.mSearchFeatureProvider == null) {
            this.mSearchFeatureProvider = new SearchFeatureProviderGoogleImpl();
        }
        return this.mSearchFeatureProvider;
    }
    
    @Override
    public SuggestionFeatureProvider getSuggestionFeatureProvider(final Context context) {
        if (this.mSuggestionFeatureProvider == null) {
            this.mSuggestionFeatureProvider = new SuggestionFeatureProviderGoogleImpl(context.getApplicationContext());
        }
        return this.mSuggestionFeatureProvider;
    }
    
    @Override
    public SupportFeatureProvider getSupportFeatureProvider(final Context context) {
        if (this.mSupportProvider == null) {
            this.mSupportProvider = new SupportFeatureProviderImpl(context.getApplicationContext());
        }
        return this.mSupportProvider;
    }
  /*  
    @Override
    public SurveyFeatureProvider getSurveyFeatureProvider(final Context context) {
        int boolean1 = 0;
        try {
            boolean1 = (Gservices.getBoolean(context.getContentResolver(), "settingsgoogle:surveys_enabled", false) ? 1 : 0);
        }
        catch (SecurityException ex) {
            Log.w("FeatureFactoryImpl", "Error reading survey feature enabled state", (Throwable)ex);
        }
        if (boolean1 != 0) {
            if (this.mSurveyFeatureProvider == null) {
                this.mSurveyFeatureProvider = new SurveyFeatureProviderImpl(context);
            }
            return this.mSurveyFeatureProvider;
        }
        return null;
    }
*/
}
