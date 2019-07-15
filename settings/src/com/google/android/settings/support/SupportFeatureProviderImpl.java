package com.google.android.settings.support;

import android.accounts.AccountManager;
import com.google.android.setupwizard.deviceorigin.DeviceOrigin;
import com.android.settings.support.SupportPhone;
import com.android.settingslib.Utils;
import android.net.Uri;
import android.content.Intent;
import android.accounts.Account;
import android.app.Activity;
import android.text.TextUtils;
import java.util.ArrayList;
import android.util.Pair;
import java.util.List;
import android.content.Context;
import com.android.settings.overlay.SupportFeatureProvider;

public class SupportFeatureProviderImpl implements SupportFeatureProvider
{
    private final Context mContext;
    private SupportFlags mSupportFlags;
    private SupportOperationHoursManager mSupportOperationHoursManager;
    SupportPhoneDirectory mSupportPhoneDirectory;

    public SupportFeatureProviderImpl(final Context mContext) {
        this.mContext = mContext;
        this.mSupportFlags = new SupportFlags();
        this.refreshOperationRules();
    }
    /*
        private BaseHelpProductSpecificData getHelpPsd(final Context context) {
            return new BaseHelpProductSpecificData() {
                @Override
                public List<Pair<String, String>> getAsyncHelpPsd() {
                    final PsdBundle psdBundle = PsdValuesLoader.makePsdBundle(context, 0);
                    return zipStringArrays(psdBundle.getKeys(), psdBundle.getValues());
                }
    
                @Override
                public List<Pair<String, String>> getSyncHelpPsd() {
                                    /*if (context.getResources().getBoolean(17957018)) {
                        final ArrayList<Pair> list = (ArrayList<Pair>)new ArrayList<Pair<String, String>>();
                        list.add(Pair.create((Object)"genie-eng:app_pkg_name", (Object)"com.google.android.settings.gphone"));
                        return (List<Pair<String, String>>)list;
                        String s;
                        if (PsdValuesLoader.getDeviceAgeInDays(context.getContentResolver()) <= 30L) {
                            s = "1";
                        }
                        else {
                            s = "0";
                        }
                        list.add(Pair.create((Object)"noe_device_under_thirty", (Object)s));
                        list.add(Pair.create((Object)"genie-eng:app_pkg_name", (Object)"com.google.android.settings.gphone"));
                        return (List<Pair<String, String>>)list;
                    }
                }
            };
        }
    */
    static void maybeAddPhoneNumber( final String s) {
        if (!TextUtils.isEmpty((CharSequence)s)) {
        }
    }

    private static List<Pair<String, String>> zipStringArrays(final String[] array, final String[] array2) {
/*        final ArrayList<Pair> list = (ArrayList<Pair>)new ArrayList<Pair<String, String>>();
        for (int i = 0; i < Math.min(array.length, array2.length); ++i) {
            list.add(new Pair((Object)array[i], (Object)array2[i]));
        }
        return (List<Pair<String, String>>)list;
        */
        return null;
    }

    public String getCurrentCountryCodeIfHasConfig(final int n) {
        return this.mSupportOperationHoursManager.getCurrentCountryCodeIfHasConfig(n);
    }

    String getDeviceCountry() {
        return this.mSupportOperationHoursManager.getDeviceCountry();
    }

    Intent getGoogleHelpIntent(final Activity activity, final Account googleAccount) {
        final Uri parse = Uri.parse("https://support.google.com/");
        this.refreshOperationRules();
        final Context applicationContext = activity.getApplicationContext();
        final String currentCountryCodeIfHasConfig = this.getCurrentCountryCodeIfHasConfig(2);
        final SupportPhone supportPhones = this.getSupportPhones(currentCountryCodeIfHasConfig, true);
        String number = null;
        String number2;
        if (supportPhones != null) {
            number2 = supportPhones.number;
        }
        else {
            number2 = null;
        }
        final SupportPhone supportPhones2 = this.getSupportPhones(currentCountryCodeIfHasConfig, false);
        if (supportPhones2 != null) {
            number = supportPhones2.number;
        }
//        final GoogleHelp enableAccountPicker = GoogleHelp.newInstance("android_home").setFallbackSupportUri(parse).setGoogleAccount(googleAccount).setHelpPsd(this.getHelpPsd(applicationContext)).setThemeSettings(new ThemeSettings().setTheme(0).setPrimaryColor(Utils.getColorAttr(applicationContext, 16843829))).enableAccountPicker(true);
//        maybeAddPhoneNumber(enableAccountPicker, number2);
//        maybeAddPhoneNumber(enableAccountPicker, number);
    //    return enableAccountPicker.buildHelpIntent();
        return null;
    }
    @Override

    public String getNewDeviceIntroUrl(final Context context) {
        final String string = DeviceOrigin.getString(context, "source_device", "unknown");
        final int hashCode = string.hashCode();
        int n = 0;
        Label_0059: {
            if (hashCode != -861391249) {
                if (hashCode == 104461) {
                    if (string.equals("ios")) {
                        n = 1;
                        break Label_0059;
                    }
                }
            }
            else if (string.equals("android")) {
                n = 0;
                break Label_0059;
            }
            n = -1;
        }
        String s = null;
        switch (n) {
            default: {
                s = "https://g.co/pixel3/phonetips";
                break;
            }
            case 1: {
                s = "https://g.co/pixel3/phonetourios";
                break;
            }
            case 0: {
                final String string2 = DeviceOrigin.getString(context, "source_device_name", null);
                if (!TextUtils.isEmpty((CharSequence)string2) && string2.startsWith("Pixel")) {
                    s = "https://g.co/pixel3/phonetourpixel";
                    break;
                }
                s = "https://g.co/pixel3/phonetourandroid";
                break;
            }
        }
        return s;
    }

    public Account[] getSupportEligibleAccounts(final Context context) {
        return AccountManager.get(context).getAccountsByType("com.google");
    }

    public SupportPhone getSupportPhones(final String s, final boolean b) {
        return this.mSupportPhoneDirectory.getSupportPhones(s, b);
    }
    public void refreshOperationRules() {
        this.mSupportOperationHoursManager = new SupportOperationHoursManager(this.mContext, this.mSupportFlags);
        this.mSupportPhoneDirectory = new SupportPhoneDirectory(this.mContext, this.mSupportFlags);
    }
    @Override
    public void startSupportV2(final Activity activity) {
        if (activity != null) {
            final Account[] supportEligibleAccounts = this.getSupportEligibleAccounts((Context)activity);
            Account account = null;
            if (supportEligibleAccounts.length != 0) {
                account = supportEligibleAccounts[0];
            }
//            new GoogleHelpLauncher(activity).launch(this.getGoogleHelpIntent(activity, account));
        }
    }
}

