package com.google.android.settings.applications;

import java.util.Set;
import android.app.admin.DevicePolicyManager;
import android.content.pm.IPackageManager;
import com.android.settingslib.wrapper.PackageManagerWrapper;
import android.content.Context;
import com.android.settings.applications.ApplicationFeatureProviderImpl;

public class ApplicationFeatureProviderGoogleImpl extends ApplicationFeatureProviderImpl
{
    public ApplicationFeatureProviderGoogleImpl(final Context context, final PackageManagerWrapper packageManagerWrapper, final IPackageManager packageManager, final DevicePolicyManager devicePolicyManager) {
        super(context, packageManagerWrapper, packageManager, devicePolicyManager);
    }
    
    @Override
    public Set<String> getKeepEnabledPackages() {
        final Set<String> keepEnabledPackages = super.getKeepEnabledPackages();
        keepEnabledPackages.add("com.google.android.inputmethod.latin");
        keepEnabledPackages.add("com.google.android.dialer");
        keepEnabledPackages.add("com.google.android.apps.wellbeing");
        return keepEnabledPackages;
    }
}
