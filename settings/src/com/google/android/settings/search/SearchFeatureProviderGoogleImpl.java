package com.google.android.settings.search;

import com.google.android.settings.external.SignatureVerifier;
import android.content.Context;
import com.android.settings.search.SearchFeatureProviderImpl;

public class SearchFeatureProviderGoogleImpl extends SearchFeatureProviderImpl
{
    public String getSettingsIntelligencePkgName() {
        return "com.google.android.settings.intelligence";
    }

    @Override
    protected boolean isSignatureWhitelisted(final Context context, final String s) {
        return SignatureVerifier.isPackageWhitelisted(context, s);
    }
}
